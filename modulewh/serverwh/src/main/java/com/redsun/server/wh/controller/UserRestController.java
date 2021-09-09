
package com.redsun.server.wh.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redsun.server.wh.authorize.PermissionAuthorize;
import com.redsun.server.wh.common.Constant;
import com.redsun.server.wh.controller.common.ServerException;
import com.redsun.server.wh.model.User;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.service.PermissionService;
import com.redsun.server.wh.service.UserService;
import com.redsun.server.wh.util.CommonUtil;
import com.redsun.server.wh.util.RestAPIHelper;
import com.redsun.server.wh.util.SecurityUtil;

@RestController
@RequestMapping("/user")
public class UserRestController {

	public static final Logger logger = LoggerFactory.getLogger(UserRestController.class);

	@Autowired
	private Environment evn;
	
	@Autowired
	UserService userService; //Service which will do all data retrieval/manipulation work
	
	@Autowired
	PermissionService permissionService;
	
	@Autowired
	PermissionAuthorize permissionAuthorize;

	// -------------------Retrieve Permission For User Menu------------------------------------------

	@RequestMapping(value = "/getPermissionForUserMenu", method = RequestMethod.GET)
	public ResponseEntity<?> getPermissionForUserMenu() throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> result = permissionAuthorize.getPermissionForUserMenu(SecurityUtil.getIdUser());
		// return.
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}

	// -------------------Retrieve Permission By IdUser------------------------------------------

	@RequestMapping(value = "/getPermissionByIdUser", method = RequestMethod.GET)
	public ResponseEntity<?> getPermissionByIdUser() throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> result = permissionAuthorize.getPermissionByIdUser(SecurityUtil.getIdUser());
		// return.
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}

	// -------------------Retrieve Permission By Target------------------------------------------

	@RequestMapping(value = "/getPermissionByTarget/{target}", method = RequestMethod.GET)
	public ResponseEntity<?> getPersistencePermission(@PathVariable("target") String target) throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> result = new HashMap<String, Object>();
		Integer idUser = SecurityUtil.getIdUser();
		Map<String, Object> permission = permissionAuthorize.getPermissionByIdUserAndTarget(idUser, target);
		if(permission != null) {
			result.putAll(permission);
			result.put("id", idUser);
		}
		// return.
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}

	// -------------------Create a User With Login-------------------------------------------

	@PreAuthorize("@userAuthorize.canCreate(#user)")
	@RequestMapping(value = "/createWithLogin", method = RequestMethod.POST)
	public ResponseEntity<?> createWithLogin(@RequestBody User user, @RequestParam Map<String, Object> params) throws NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, KeyStoreException, KeyManagementException, UnrecoverableKeyException {
		// Call serverauth to create user.
	    String serverauthUrl = evn.getProperty("serverauth.url");
	    String token = params.get("token").toString();
	    Map<String, Object> data = new HashMap<String, Object>();
	    data.put("username", user.getUsername());
	    data.put("password", user.getPassword());
	    data.put("displayname", user.getDisplayname());
	    data.put("thumbnail", user.getThumbnail());
	    data.put("enabled", true);
	    ResponseEntity<Object> response = RestAPIHelper.exchangeSSLService(serverauthUrl + "/users/createWithEvent", HttpMethod.POST, token, null, data, null);
	    Map<String, Object> resultRemote = (Map<String, Object>) response.getBody();
	    if(resultRemote.get("error") != null) {
	    	// throw server exception.
		 	throw new ServerException(Constant.SERVERCODE_ERROR);
	    }
	    
	    try {
	    	User result = userService.create(user);
			// Call serverauth to confirm create user as SUCCESS.
			response = RestAPIHelper.exchangeSSLService(serverauthUrl + "/users/confirmStatus/" + resultRemote.get("id") + "/" + Constant.SERVICETRANSACTION_SUCCESS, HttpMethod.GET, token, null, null, null);
			// return.
			return new ResponseEntity<Integer>(result.getId(), HttpStatus.OK);
	    } catch(Exception ex) {
			// Call serverauth to confirm create user as ABORT.
			response = RestAPIHelper.exchangeSSLService(serverauthUrl + "/users/confirmStatus/" + resultRemote.get("id") + "/" + Constant.SERVICETRANSACTION_ABORT, HttpMethod.GET, token, null, null, null);
			// throw server exception.
		 	throw new ServerException(Constant.SERVERCODE_ERROR);
	    }
	    
	}

	// -------------------Create a User-------------------------------------------

	@PreAuthorize("@userAuthorize.canCreate(#user)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody User user) throws NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, KeyStoreException, KeyManagementException, UnrecoverableKeyException {
		User result = userService.create(user);
		// return.
		return new ResponseEntity<Integer>(result.getId(), HttpStatus.OK);
	}

	// -------------------Create a User With Event-------------------------------------------

	//@PreAuthorize("@userAuthorize.canCreate(#user)")
	@RequestMapping(value = "/createWithEvent", method = RequestMethod.POST)
	public ResponseEntity<?> createWithEvent(@RequestBody User user) throws InterruptedException, JsonParseException, JsonMappingException, IOException {
		Map<String, Object> syncObject = new HashMap<String, Object>();
		Integer iduser = SecurityUtil.getIdUser();
		syncObject.put("iduser", iduser);
		syncObject.put("id", -1);
		Thread thread = new Thread() {
			public void run() {
				try {
					userService.createWithEvent(user, syncObject);
				} catch (Exception e) {
					syncObject.put("error", e.getMessage());
					syncObject.notify();
					logger.error(e.getMessage());
				}
			}
		};
		thread.start();
		
		synchronized(syncObject) {
			syncObject.wait();
		}
		// return.
		return new ResponseEntity<Map<String, Object>>(syncObject, HttpStatus.OK);
	}

	// -------------------Update a Password With Lock------------------------------------------------

	@PreAuthorize("@userAuthorize.canUpdate(#user)")
	@RequestMapping(value = "/updatePasswordWithLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updatePasswordWithLock(@PathVariable("id") Integer id, @RequestBody User user, @RequestParam Map<String, Object> params) throws JsonParseException, JsonMappingException, IOException, KeyManagementException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException {
		// Call serverauth to create user.
	    String serverauthUrl = evn.getProperty("serverauth.url");
	    String token = params.get("token").toString();
	    Map<String, Object> data = new HashMap<String, Object>();
	    data.put("password", user.getPassword());
	    ResponseEntity<Object> response = RestAPIHelper.exchangeSSLService(serverauthUrl + "/users/updatePasswordWithEvent/" + user.getUsername(), HttpMethod.PUT, token, null, data, null);
	    Map<String, Object> resultRemote = (Map<String, Object>) response.getBody();
	    if(resultRemote.get("error") != null) {
	    	// throw server exception.
		 	throw new ServerException(Constant.SERVERCODE_ERROR);
	    }
	    
	    try {
	    	String password = (String) data.get("password");
	    	User result = userService.updatePasswordWithLock(id, password);
			// Call serverauth to confirm create user as SUCCESS.
			response = RestAPIHelper.exchangeSSLService(serverauthUrl + "/users/confirmStatus/" + resultRemote.get("id") + "/" + Constant.SERVICETRANSACTION_SUCCESS, HttpMethod.GET, token, null, null, null);
			// return.
			return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	    } catch(Exception ex) {
			// Call serverauth to confirm create user as ABORT.
			response = RestAPIHelper.exchangeSSLService(serverauthUrl + "/users/confirmStatus/" + resultRemote.get("id") + "/" + Constant.SERVICETRANSACTION_ABORT, HttpMethod.GET, token, null, null, null);
			// throw server exception.
		 	throw new ServerException(Constant.SERVERCODE_ERROR);
	    }
	}

	// -------------------Update a User------------------------------------------------

	@PreAuthorize("@userAuthorize.canUpdate(#user)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody User user) throws JsonParseException, JsonMappingException, IOException {
		User result = userService.update(id, user);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update a User With Lock------------------------------------------------

	@PreAuthorize("@userAuthorize.canUpdate(#user)")
	@RequestMapping(value = "/updateWithLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateWithLock(@PathVariable("id") Integer id, @RequestBody User user, @RequestParam Map<String, Object> params) throws JsonParseException, JsonMappingException, IOException, KeyManagementException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException {
		// Call serverauth to create user.
	    String serverauthUrl = evn.getProperty("serverauth.url");
	    String token = params.get("token").toString();
	    Map<String, Object> data = new HashMap<String, Object>();
	    data.put("id", user.getId());
	    data.put("username", user.getUsername());
	    //data.put("password", user.getPassword());
	    data.put("displayname", user.getDisplayname());
	    data.put("thumbnail", user.getThumbnail());
	    data.put("enabled", true);
	    ResponseEntity<Object> response = RestAPIHelper.exchangeSSLService(serverauthUrl + "/users/updateWithEvent/" + user.getUsername(), HttpMethod.PUT, token, null, data, null);
	    Map<String, Object> resultRemote = (Map<String, Object>) response.getBody();
	    if(resultRemote.get("error") != null) {
	    	// throw server exception.
		 	throw new ServerException(Constant.SERVERCODE_ERROR);
	    }
	    
	    try {
	    	//user.setPassword(CommonUtil.encrypt(user.getPassword()));
	    	User result = userService.updateWithLock(id, user);
			// Call serverauth to confirm create user as SUCCESS.
			response = RestAPIHelper.exchangeSSLService(serverauthUrl + "/users/confirmStatus/" + resultRemote.get("id") + "/" + Constant.SERVICETRANSACTION_SUCCESS, HttpMethod.GET, token, null, null, null);
			// return.
			return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	    } catch(Exception ex) {
			// Call serverauth to confirm create user as ABORT.
			response = RestAPIHelper.exchangeSSLService(serverauthUrl + "/users/confirmStatus/" + resultRemote.get("id") + "/" + Constant.SERVICETRANSACTION_ABORT, HttpMethod.GET, token, null, null, null);
			// throw server exception.
		 	throw new ServerException(Constant.SERVERCODE_ERROR);
	    }
	}

	// -------------------Update a User With Lock And Event------------------------------------------------

	//@PreAuthorize("@userAuthorize.canUpdate(#user)")
	@RequestMapping(value = "/updateWithLockAndEvent/{username}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateWithLockAndEvent(@PathVariable("username") String username, @RequestBody User user) throws InterruptedException, JsonParseException, JsonMappingException, IOException {
		Map<String, Object> syncObject = new HashMap<String, Object>();
		Integer iduser = SecurityUtil.getIdUser();
		syncObject.put("iduser", iduser);
		User userDb = userService.getByUsername(username);
		Integer id = userDb.getId();
		Thread thread = new Thread() {
			public void run() {
				try {
					userService.updateWithLockAndEvent(id, user, syncObject);
				} catch (Exception e) {
					syncObject.put("error", e.getMessage());
					syncObject.notify();
					logger.error(e.getMessage());
				}
			}
		};
		thread.start();
		
		synchronized(syncObject) {
			syncObject.wait();
		}
		// return.
		return new ResponseEntity<Map<String, Object>>(syncObject, HttpStatus.OK);
	}

	// -------------------Update For Delete a User------------------------------------------------

	@PreAuthorize("@userAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDelete/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDelete(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		User result = userService.updateForDelete(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete a User With Lock------------------------------------------------

	@PreAuthorize("@userAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDeleteWithLock/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDeleteWithLock(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		User result = userService.updateForDeleteWithLock(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete a User With Lock And Event------------------------------------------------

	//@PreAuthorize("@userAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDeleteWithLockAndEvent/{username}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDeleteWithLockAndEvent(@PathVariable("username") String username, @PathVariable("version") final Integer version) throws InterruptedException {
		Map<String, Object> syncObject = new HashMap<String, Object>();
		User userDb = userService.getByUsername(username);
		Integer id = userDb.getId();
		Thread thread = new Thread() {
			public void run() {
				try {
					userService.updateForDeleteWithLockAndEvent(id, version, syncObject);
				} catch (Exception e) {
					syncObject.put("error", e.getMessage());
					syncObject.notify();
					logger.error(e.getMessage());
				}
			}
		};
		thread.start();
		
		synchronized(syncObject) {
			syncObject.wait();
		}
		// return.
		return new ResponseEntity<Map<String, Object>>(syncObject, HttpStatus.OK);
	}

	// -------------------Delete a User-----------------------------------------

	@PreAuthorize("@userAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		userService.deleteById(id);
		// return.	
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	// -------------------Delete a User With Event-----------------------------------------

	@PreAuthorize("@userAuthorize.canDelete(#id)")
	@RequestMapping(value = "/deleteWithEvent/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteWithEvent(@PathVariable("id") Integer id) throws InterruptedException {
		Map<String, Object> syncObject = new HashMap<String, Object>();
		Thread thread = new Thread() {
			public void run() {
				try {
					userService.deleteWithEventById(id, syncObject);
				} catch (Exception e) {
					syncObject.put("error", e.getMessage());
					syncObject.notify();
					logger.error(e.getMessage());
				}
			}
		};
		thread.start();
		
		synchronized(syncObject) {
			syncObject.wait();
		}
		// return.
		return new ResponseEntity<Map<String, Object>>(syncObject, HttpStatus.OK);
	}

	// -------------------Confirm Transaction Service-----------------------------------------

	@RequestMapping(value = "/confirmStatus/{id}/{status}", method = RequestMethod.GET)
	public ResponseEntity<?> confirmStatus(@PathVariable("id") Integer id, @PathVariable("status") Integer status) throws JsonParseException, JsonMappingException, IOException {
		Boolean result = userService.confirmStatus(id, status);
		// return.
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}

	// -------------------Retrieve All Users---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<User>> listAll() {
		List<User> users = userService.listAll();
		// return.
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}

	// -------------------Retrieve Single User------------------------------------------

	@PreAuthorize("@userAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		User user = userService.getById(id);
		// return.
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}

	// -------------------Retrieve User Info By UserName------------------------------------------

	@RequestMapping(value = "/getUserInfoByUsername/{username}", method = RequestMethod.GET)
	public ResponseEntity<?> getUserInfoByUsername(@PathVariable("username") String username, HttpServletRequest request) throws JsonProcessingException {
		User user = userService.getByUsername(username);
		// User info.
		Map<String, Object> userinfo = new HashMap<String, Object>();
		userinfo.put("iduser", user.getId());
		userinfo.put("url", CommonUtil.getBaseUrl(request));
		// Encrypt.
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("iduser", user.getId());
		result.put("username", username);
		// Ecrypt userinfo.
		String info = CommonUtil.encrypt(objectMapper.writeValueAsString(userinfo));
		result.put("info", info);
		// return.
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Users With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		List<User> users = userService.listWithCritera(searchCriteria);
		// return.
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Users With Many Criteria------------------------------------------
	
	@PreAuthorize("@userAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		List<User> users = userService.listWithCriteras(searchCriterias);
		// return.
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}

	// -------------------Retrieve All Users By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<User>> listAllByPage(Pageable pageable) {
		Page<User> users = userService.listAllByPage(pageable);
		// return.
		return new ResponseEntity<Page<User>>(users, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Users With A Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		Page<User> users = userService.listWithCriteraByPage(searchCriteria, pageable);
		// return.
		return new ResponseEntity<Page<User>>(users, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Users With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@userAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		Page<User> users = userService.listWithCriterasByPage(searchCriterias, pageable);
		// return.
		return new ResponseEntity<Page<User>>(users, HttpStatus.OK);
	}
	
	// -------------------------Retrieve All User For Select--------------------------------------------------- 
	
	@RequestMapping(value = "/listAllForSelect", method = RequestMethod.GET)
	public ResponseEntity<List<Map<String, Object>>> listAllForSelect() {
		List<Map<String, Object>> usernames = userService.listAllForSelect();
		// return.
		return new ResponseEntity<List<Map<String, Object>>>(usernames, HttpStatus.OK);
	}

}

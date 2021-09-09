
package com.redsun.server.gateway.controller;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redsun.server.gateway.common.Constant;
import com.redsun.server.gateway.common.Role;
import com.redsun.server.gateway.controller.common.ServerException;
import com.redsun.server.gateway.model.UserRoles;
import com.redsun.server.gateway.model.Users;
import com.redsun.server.gateway.model.common.SearchCriteria;
import com.redsun.server.gateway.service.UserRolesService;
import com.redsun.server.gateway.service.UsersService;
import com.redsun.server.gateway.util.CommonUtil;
import com.redsun.server.gateway.util.RestAPIHelper;
import com.redsun.server.gateway.util.SecurityUtil;

@RestController
@RequestMapping("/users")
public class UsersRestController {

	public static final Logger logger = LoggerFactory.getLogger(UsersRestController.class);
	
	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	UsersService usersService;
	
	@Autowired
	UserRolesService userRolesService;

	// -------------------Create a Users-------------------------------------------
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Map<String, Object> data, HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException, KeyManagementException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException {
		// Get module info.
		List<Map<String, Object>> modules = SecurityUtil.getModulesInfo();
        // HttpHeaders.
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("userinfo", request.getHeader(Constant.USSER_INFO));
        headers.set("ConId", request.getHeader("ConId"));
		for (Map<String, Object> module : modules) {
			Map<String, Object> info = (Map<String, Object>) module.get("info");
			// Call module server to create user.
		    String serverUrl = (String) info.get("url");
	        final HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(data, headers);
	        // Call server.
	        ResponseEntity<?> response = RestAPIHelper.exchangeService(serverUrl + "/user/createWithEvent", HttpMethod.POST, headers, requestEntity, null);
	        Map<String, Object> resultRemote = (Map<String, Object>) response.getBody();
		    module.put("idresponse", resultRemote.get("id"));
		}
	    try {
	    	// Create users.
		    Users users = new Users();
		    Object value;
		    value = data.get("username");
		    if(value != null){
			    users.setUsername(value.toString());
		    }
		    value = data.get("password");
		    if(value != null){
			    users.setPassword(value.toString());
		    }
		    value = data.get("displayname");
		    if(value != null){
			    users.setDisplayname(value.toString());
		    }
		    value = data.get("email");
		    if(value != null){
			    users.setEmail(value.toString());
		    }
		    value = data.get("firstname");
		    if(value != null){
			    users.setFirstname(value.toString());
		    }
		    value = data.get("lastname");
		    if(value != null){
			    users.setLastname(value.toString());
		    }
		    value = data.get("thumbnail");
		    if(value != null){
			    users.setThumbnail(value.toString());
		    }
		    value = data.get("enabled");
		    if(value != null){
		    	users.setEnabled((Boolean)value);
		    }
		    users.setVersion(1);
	    	Users result = usersService.create(users);
			// Call module server to confirm create user as SUCCESS.
	    	for (Map<String, Object> module : modules) {
				Map<String, Object> info = (Map<String, Object>) module.get("info");
	    		String serverUrl = (String) info.get("url");
	    		Integer id = (Integer) module.get("idresponse");
		        final HttpEntity<?> requestEntity = new HttpEntity<>(null, headers);
		        // Call server.
		        ResponseEntity<?> response = RestAPIHelper.exchangeService(serverUrl + "/user/confirmStatus/" + id + "/" + Constant.SERVICETRANSACTION_SUCCESS, HttpMethod.GET, headers, requestEntity, null);
	    	}
			// return.
			return new ResponseEntity<Integer>(result.getId(), HttpStatus.OK);
	    } catch(Exception ex) {
			// Call module server to confirm create user as ABORT.
	    	for (Map<String, Object> module : modules) {
				Map<String, Object> info = (Map<String, Object>) module.get("info");
	    		String serverUrl = (String) info.get("url");
	    		Integer id = (Integer) module.get("idresponse");
		        final HttpEntity<?> requestEntity = new HttpEntity<>(null, headers);
		        // Call server.
		        ResponseEntity<?> response = RestAPIHelper.exchangeService(serverUrl + "/user/confirmStatus/" + id + "/" + Constant.SERVICETRANSACTION_ABORT, HttpMethod.GET, headers, requestEntity, null);
	    	}
			// throw server exception.
		 	throw new ServerException(Constant.SERVERCODE_ERROR);
	    }
	}

	// -------------------Create a Users With Event-------------------------------------------
	
	@RequestMapping(value = "/createWithEvent", method = RequestMethod.POST)
	public ResponseEntity<?> createWithEvent(@RequestBody Users users, UriComponentsBuilder ucBuilder) throws JsonParseException, JsonMappingException, IOException, InterruptedException {
		Map<String, Object> syncObject = new HashMap<String, Object>();		Thread thread = new Thread() {
			public void run() {
				try {
					usersService.createWithEvent(users, syncObject);
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

	// -------------------Update a Users------------------------------------------------

	@RequestMapping(value = "/update/{username}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("username") String username, @RequestBody Map<String, Object> data, HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException, KeyManagementException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException {
		// Get module info.
		List<Map<String, Object>> modules = SecurityUtil.getModulesInfo();
        // HttpHeaders.
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("userinfo", request.getHeader("userinfo"));
        headers.set("ConId", request.getHeader("ConId"));
		for (Map<String, Object> module : modules) {
			Map<String, Object> info = (Map<String, Object>) module.get("info");
			// Call module server to create user.
		    String serverUrl = (String) info.get("url");
	        final HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(data, headers);
	        // Call server.
	        ResponseEntity<?> response = RestAPIHelper.exchangeService(serverUrl + "/user/updateWithLockAndEvent/" + username, HttpMethod.PUT, headers, requestEntity, null);
	        module.put("httpStatus", response.getStatusCode());
	        Map<String, Object> resultRemote = (Map<String, Object>) response.getBody();
		    module.put("idresponse", resultRemote.get("id"));
		}
	    try {
	    	// Update users.
		    Users users = new Users();
		    Object value;
		    value = data.get("username");
		    if(value != null){
			    users.setUsername(value.toString());
		    }
		    value = data.get("password");
		    if(value != null){
			    users.setPassword(value.toString());
		    }
		    value = data.get("displayname");
		    if(value != null){
			    users.setDisplayname(value.toString());
		    }
		    value = data.get("email");
		    if(value != null){
			    users.setEmail(value.toString());
		    }
		    value = data.get("firstname");
		    if(value != null){
			    users.setFirstname(value.toString());
		    }
		    value = data.get("lastname");
		    if(value != null){
			    users.setLastname(value.toString());
		    }
		    value = data.get("thumbnail");
		    if(value != null){
			    users.setThumbnail(value.toString());
		    }
		    value = data.get("enabled");
		    if(value != null){
		    	users.setEnabled((Boolean)value);
		    }
		    value = data.get("version");
		    if(value != null){
			    users.setVersion((Integer)value);
		    }
	    	Users result = usersService.update(username, users);
			// Call module server to confirm create user as SUCCESS.
	    	for (Map<String, Object> module : modules) {
				Map<String, Object> info = (Map<String, Object>) module.get("info");
	    		String serverUrl = (String) info.get("url");
	    		Integer id = (Integer) module.get("idresponse");
		        final HttpEntity<?> requestEntity = new HttpEntity<>(null, headers);
		        // Call server.
		        ResponseEntity<?> response = RestAPIHelper.exchangeService(serverUrl + "/user/confirmStatus/" + id + "/" + Constant.SERVICETRANSACTION_SUCCESS, HttpMethod.GET, headers, requestEntity, null);
	    	}
			// return.
			return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	    } catch(Exception ex) {
			// Call module server to confirm create user as ABORT.
	    	for (Map<String, Object> module : modules) {
				Map<String, Object> info = (Map<String, Object>) module.get("info");
	    		String serverUrl = (String) info.get("url");
	    		Integer id = (Integer) module.get("idresponse");
		        final HttpEntity<?> requestEntity = new HttpEntity<>(null, headers);
		        // Call server.
		        ResponseEntity<?> response = RestAPIHelper.exchangeService(serverUrl + "/user/confirmStatus/" + id + "/" + Constant.SERVICETRANSACTION_ABORT, HttpMethod.GET, headers, requestEntity, null);
	    	}
			// throw server exception.
		 	throw new ServerException(Constant.SERVERCODE_ERROR);
	    }
	}

	// -------------------Update a Users With Event------------------------------------------------

	@RequestMapping(value = "/updateWithEvent/{username}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateWithEvent(@PathVariable("username") String username, @RequestBody Users users) throws JsonParseException, JsonMappingException, IOException, InterruptedException {
		Map<String, Object> syncObject = new HashMap<String, Object>();
		syncObject.put("username", username);
		Thread thread = new Thread() {
			public void run() {
				try {
					usersService.updateWithEvent(username, users, syncObject);
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

	// -------------------Update a Password------------------------------------------------

	@RequestMapping(value = "/updatePassword/{username}", method = RequestMethod.PUT)
	public ResponseEntity<?> updatePassword(@PathVariable("username") String username, @RequestBody Map<String, Object> data) throws JsonParseException, JsonMappingException, IOException {
		String password = "";
		Object value = data.get("password");
		if(value != null) {
			password = (String) value;
		}
		Users result = usersService.updatePassword(username, password);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update a Password With Event------------------------------------------------

	@RequestMapping(value = "/updatePasswordWithEvent/{username}", method = RequestMethod.PUT)
	public ResponseEntity<?> updatePasswordWithEvent(@PathVariable("username") String username, @RequestBody Map<String, Object> data) throws JsonParseException, JsonMappingException, IOException, InterruptedException {
		String password = (String) data.get("password");
		Map<String, Object> syncObject = new HashMap<String, Object>();
		syncObject.put("username", username);
		Thread thread = new Thread() {
			public void run() {
				try {
					usersService.updatePasswordWithEvent(username, password, syncObject);
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

	// -------------------Update For Delete------------------------------------------------

	@RequestMapping(value = "/updateForDelete/{username}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDelete(@PathVariable("username") String username, @PathVariable("version") final Integer version, HttpServletRequest request) throws JsonParseException, JsonMappingException, IOException, KeyManagementException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException {
		// Get module info.
				List<Map<String, Object>> modules = SecurityUtil.getModulesInfo();
		        // HttpHeaders.
		        final HttpHeaders headers = new HttpHeaders();
		        headers.setContentType(MediaType.APPLICATION_JSON);
		        headers.set("userinfo", request.getHeader("userinfo"));
		        headers.set("ConId", request.getHeader("ConId"));
				for (Map<String, Object> module : modules) {
					Map<String, Object> info = (Map<String, Object>) module.get("info");
					// Call module server to create user.
				    String serverUrl = (String) info.get("url");
				    Map<String, Object> data = new HashMap<String, Object>();
			        final HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(data, headers);
			        // Call server.
			        ResponseEntity<?> response = RestAPIHelper.exchangeService(serverUrl + "/user/updateForDeleteWithLockAndEvent/" + username + "/" + version, HttpMethod.PUT, headers, requestEntity, null);
			        module.put("httpStatus", response.getStatusCode());
			        Map<String, Object> resultRemote = (Map<String, Object>) response.getBody();
				    module.put("idresponse", resultRemote.get("id"));
				}
			    try {
			    	// Update users.
			    	Users result = usersService.updateForDelete(username);
					// Call module server to confirm create user as SUCCESS.
			    	for (Map<String, Object> module : modules) {
						Map<String, Object> info = (Map<String, Object>) module.get("info");
			    		String serverUrl = (String) info.get("url");
			    		Integer id = (Integer) module.get("idresponse");
				        final HttpEntity<?> requestEntity = new HttpEntity<>(null, headers);
				        // Call server.
				        ResponseEntity<?> response = RestAPIHelper.exchangeService(serverUrl + "/user/confirmStatus/" + id + "/" + Constant.SERVICETRANSACTION_SUCCESS, HttpMethod.GET, headers, requestEntity, null);
			    	}
					// return.
					return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
			    } catch(Exception ex) {
					// Call module server to confirm create user as ABORT.
			    	for (Map<String, Object> module : modules) {
						Map<String, Object> info = (Map<String, Object>) module.get("info");
			    		String serverUrl = (String) info.get("url");
			    		Integer id = (Integer) module.get("idresponse");
				        final HttpEntity<?> requestEntity = new HttpEntity<>(null, headers);
				        // Call server.
				        ResponseEntity<?> response = RestAPIHelper.exchangeService(serverUrl + "/user/confirmStatus/" + id + "/" + Constant.SERVICETRANSACTION_ABORT, HttpMethod.GET, headers, requestEntity, null);
			    	}
					// throw server exception.
				 	throw new ServerException(Constant.SERVERCODE_ERROR);
			    }
	}

	// -------------------Delete a Users-----------------------------------------

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		usersService.deleteById(id);
		
		// return.	
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	// -------------------Delete a Users With Event-----------------------------------------

	@RequestMapping(value = "/deleteWithEvent/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteWithEvent(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException, InterruptedException {
		Map<String, Object> syncObject = new HashMap<String, Object>();
		Thread thread = new Thread() {
			public void run() {
				try {
					usersService.deleteWithEventById(id, syncObject);
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
		Boolean result = usersService.confirmStatus(id, status);
		
		// return.
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}

	// -------------------Retrieve All Userss---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Users>> listAll() throws JsonParseException, JsonMappingException, IOException {
		List<Users> userss = usersService.listAll();
		// return.
		return new ResponseEntity<List<Users>>(userss, HttpStatus.OK);
	}

	// -------------------Retrieve Single Users------------------------------------------

	@PreAuthorize("@usersAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		Users users = usersService.getById(id);
		// return.
		return new ResponseEntity<Users>(users, HttpStatus.OK);
	}

	// -------------------Retrieve Single User Info------------------------------------------

	@PreAuthorize("@usersAuthorize.canRead(#id)")
	@RequestMapping(value = "/getUserInfoByUsername/{username}", method = RequestMethod.GET)
	public ResponseEntity<?> getUserInfoByUsername(@PathVariable("username") String username, HttpServletRequest request) throws JsonProcessingException {
		Users users = usersService.getByUsername(username);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("displayname", users.getDisplayname());
		result.put("thumbnail", users.getThumbnail());
		
		// User info.
		Map<String, Object> userinfo = new HashMap<String, Object>();
		userinfo.put("iduser", users.getId());
		userinfo.put("url", CommonUtil.getBaseUrl(request));
		// User roles.
		List<UserRoles> userRoles = userRolesService.listByUsername(username);
		List<Integer> idRoles = new ArrayList<Integer>();
		for (UserRoles userRole : userRoles) {
			idRoles.add(Role.valueOf(userRole.getRole()).getId());
		}
		userinfo.put("idroles", idRoles);
		
		// Ecrypt userinfo.
		String info = CommonUtil.encrypt(objectMapper.writeValueAsString(userinfo));
		result.put("info", info);
		
		// return.
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Users With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@userAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		Page<Users> users = usersService.listWithCriterasByPage(searchCriterias, pageable);
		// return.
		return new ResponseEntity<Page<Users>>(users, HttpStatus.OK);
	}

}

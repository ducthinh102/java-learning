
package com.redsun.server.wh.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.redsun.server.wh.authorize.PermissionAuthorize;
import com.redsun.server.wh.model.Permission;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.service.PermissionService;
import com.redsun.server.wh.util.SecurityUtil;

@RestController
@RequestMapping("/permission")
public class PermissionRestController {

	//public static final Logger logger = LoggerFactory.getLogger(PermissionRestController.class);

	@Autowired
	PermissionService permissionService; //Service which will do all data retrieval/manipulation work
	
	@Autowired
	PermissionAuthorize permissionAuthorize; //Cache permission.

	// -------------------Create a Permission-------------------------------------------

	@PreAuthorize("@permissionAuthorize.canCreate(#permission)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Permission permission) throws JsonParseException, JsonMappingException, IOException {
		Permission result = permissionService.create(permission);
		
		// Reload permission.
		permissionAuthorize.reloadPermissionRepo();
		
		// return.
		return new ResponseEntity<Integer>(result.getId(), HttpStatus.OK);
	}
	// -------------------Update lock a Permission------------------------------------------------

		@PreAuthorize("@permissionAuthorize.canUpdate(#permission)")
		@RequestMapping(value = "/updateLock/{id}", method = RequestMethod.PUT)
		public ResponseEntity<?> updateLock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
			Integer result = permissionService.updateLock(id);
			// return.
			return new ResponseEntity<Integer>(result, HttpStatus.OK);
		}

		// -------------------Update Unlock a Permission------------------------------------------------

		@PreAuthorize("@appconfigAuthorize.canUpdate(#permission)")
		@RequestMapping(value = "/updateUnlock/{id}", method = RequestMethod.PUT)
		public ResponseEntity<?> updateUnlock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
			Integer result = permissionService.updateUnlock(id);
			// return.
			return new ResponseEntity<Integer>(result, HttpStatus.OK);
		}
	// -------------------Update a Permission------------------------------------------------

	@PreAuthorize("@permissionAuthorize.canUpdate(#permission)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Permission permission) throws JsonParseException, JsonMappingException, IOException {
		Permission result = permissionService.update(id,permission);
		
		// Reload permission.
		permissionAuthorize.reloadPermissionRepo();
		
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}
	// -------------------Update a Permission With Lock------------------------------------------------

		@PreAuthorize("@permissionAuthorize.canUpdate(#permission)")
		@RequestMapping(value = "/updateWithLock/{id}", method = RequestMethod.PUT)
		public ResponseEntity<?> updateWithLock(@PathVariable("id") Integer id,@RequestBody Permission permission) throws JsonParseException, JsonMappingException, IOException {
			Permission result = permissionService.updateWithLock(id, permission);
			
			// Reload permission.
			permissionAuthorize.reloadPermissionRepo();
			
			// return.
			return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
		}

	// -------------------Delete a Permission-----------------------------------------

	@PreAuthorize("@permissionAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		permissionService.deleteById(id);
		
		// Reload permission.
		permissionAuthorize.reloadPermissionRepo();
		
		// return.	
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	// -------------------Retrieve All Permissions---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Permission>> listAll() throws JsonParseException, JsonMappingException, IOException {
		List<Permission> permissions = permissionService.listAll();
		// return.
		return new ResponseEntity<List<Permission>>(permissions, HttpStatus.OK);
	}

	// -------------------Retrieve All Permissions---------------------------------------------

	@RequestMapping(value = "/listAllByUserId/{userId}", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> listAllByUserId(@PathVariable("userId") Integer userId) throws JsonParseException, JsonMappingException, IOException {
		
		//String rules = "{\"admins\":[1,2],\"createbyids\":[{\"createbyids\":[1,2]}]}";
		
		Map<String, Object> permissions = permissionAuthorize.getPermissionByIdUser(userId);
		// return.
		return new ResponseEntity<Map<String, Object>>(permissions, HttpStatus.OK);
	}

	// -------------------Retrieve Single Permission------------------------------------------

	@PreAuthorize("@permissionAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		Permission permission = permissionService.getById(id);
		// return.
		return new ResponseEntity<Permission>(permission, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Permissions With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		List<Permission> permissions = permissionService.listWithCritera(searchCriteria);
		// return.
		return new ResponseEntity<List<Permission>>(permissions, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Permissions With Many Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		List<Permission> permissions = permissionService.listWithCriteras(searchCriterias);
		// return.
		return new ResponseEntity<List<Permission>>(permissions, HttpStatus.OK);
	}

	// -------------------Retrieve All Permissions By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Permission>> listAllByPage(Pageable pageable) {
		Page<Permission> permissions = permissionService.listAllByPage(pageable);
		// return.
		return new ResponseEntity<Page<Permission>>(permissions, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Permissions With A Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		Page<Permission> permissions = permissionService.listWithCriteraByPage(searchCriteria, pageable);
		// return.
		return new ResponseEntity<Page<Permission>>(permissions, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Permissions With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@permissionAuthorize.canRead(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		Page<Permission> permissions = permissionService.listWithCriterasByPage(searchCriterias, pageable);
		// return.
		return new ResponseEntity<Page<Permission>>(permissions, HttpStatus.OK);
	}

}

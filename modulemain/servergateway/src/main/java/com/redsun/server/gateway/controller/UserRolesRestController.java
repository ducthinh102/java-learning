
package com.redsun.server.gateway.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.redsun.server.gateway.model.UserRoles;
import com.redsun.server.gateway.model.common.SearchCriteria;
import com.redsun.server.gateway.service.UserRolesService;

@RestController
@RequestMapping("/userroles")
public class UserRolesRestController {

	//public static final Logger logger = LoggerFactory.getLogger(UserRolesRestController.class);
	
	@Autowired
	UserRolesService userRolesService; //Service which will do all data retrieval/manipulation work

	// -------------------Save List Of UserRoles-------------------------------------------

	@RequestMapping(value = "/saveWithUsername/{username}", method = RequestMethod.POST)
	public ResponseEntity<?> saveWithUsername(@PathVariable("username") String username, @RequestBody Map<String, Object> data) throws JsonParseException, JsonMappingException, IOException {
		List<Integer> idroles = (List<Integer>) data.get("idroles");
		List<UserRoles> result = userRolesService.saveWithUsername(username, idroles);
		// return.
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	// -------------------Create a UserRoles-------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody UserRoles userRoles) throws JsonParseException, JsonMappingException, IOException {
		UserRoles result = userRolesService.create(userRoles);
		// return.
		return new ResponseEntity<Integer>(result.getId(), HttpStatus.OK);
	}

	// -------------------Delete a UserRoles-----------------------------------------

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		userRolesService.deleteById(id);
		// return.	
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	// -------------------Retrieve All UserRoless---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<UserRoles>> listAll() {
		List<UserRoles> userRoles = userRolesService.listAll();
		// return.
		return new ResponseEntity<List<UserRoles>>(userRoles, HttpStatus.OK);
	}

	// -------------------Retrieve Single UserRoles------------------------------------------

	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		UserRoles userRoles = userRolesService.getById(id);
		// return.
		return new ResponseEntity<UserRoles>(userRoles, HttpStatus.OK);
	}

	// -------------------Retrieve List Of UserRoles------------------------------------------

	@RequestMapping(value = "/listByUsername/{username}", method = RequestMethod.GET)
	public ResponseEntity<?> listByUsername(@PathVariable("username") String username) {
		List<UserRoles> userRoles = userRolesService.listByUsername(username);
		// return.
		return new ResponseEntity<List<UserRoles>>(userRoles, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of UserRoless With Multiple Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		Page<UserRoles> userRoles = userRolesService.listWithCriterasByPage(searchCriterias, pageable);
		// return.
		return new ResponseEntity<Page<UserRoles>>(userRoles, HttpStatus.OK);
	}
	
	// -------------------Retrieve List All Of Roles------------------------------------------
	
	@RequestMapping(value = "/listAllRoles", method = RequestMethod.GET)
	public ResponseEntity<?> listAllRoles() {
		List<Map<String, Object>> userRoles = userRolesService.listAllRoles();
		// return.
		return new ResponseEntity<List<Map<String, Object>>>(userRoles, HttpStatus.OK);
	}
	
	// -------------------Retrieve List All Of Roles------------------------------------------
	
	@RequestMapping(value = "/listIdRolesByUsername/{username}", method = RequestMethod.GET)
	public ResponseEntity<?> listIdRolesByUsername(@PathVariable("username") String username) {
		List<Integer> idRoles = userRolesService.listIdRolesByUsername(username);
		// return.
		return new ResponseEntity<List<Integer>>(idRoles, HttpStatus.OK);
	}

}

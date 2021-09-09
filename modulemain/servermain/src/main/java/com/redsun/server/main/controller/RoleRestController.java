
package com.redsun.server.main.controller;

import java.io.IOException;
import java.util.List;

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
import com.redsun.server.main.model.Role;
import com.redsun.server.main.model.common.SearchCriteria;
import com.redsun.server.main.service.RoleService;

@RestController
@RequestMapping("/role")
public class RoleRestController {

	//public static final Logger logger = LoggerFactory.getLogger(RoleRestController.class);

	@Autowired
	RoleService roleService; //Service which will do all data retrieval/manipulation work

	// -------------------Create a Role-------------------------------------------

	@PreAuthorize("@roleAuthorize.canCreate(#role)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Role role) throws JsonParseException, JsonMappingException, IOException {
		Role result = roleService.create(role);
		// return.
		return new ResponseEntity<Integer>(result.getId(), HttpStatus.OK);
	}

	// -------------------Update lock a Role------------------------------------------------

	@PreAuthorize("@roleAuthorize.canUpdate(#role)")
	@RequestMapping(value = "/updateLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateLock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = roleService.updateLock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update Unlock a Role------------------------------------------------

	@PreAuthorize("@roleAuthorize.canUpdate(#role)")
	@RequestMapping(value = "/updateUnlock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUnlock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = roleService.updateUnlock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update a Role------------------------------------------------

	@PreAuthorize("@roleAuthorize.canUpdate(#role)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Role role) throws JsonParseException, JsonMappingException, IOException {
		Role result = roleService.update(id, role);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update a Role With Lock------------------------------------------------

	@PreAuthorize("@roleAuthorize.canUpdate(#role)")
	@RequestMapping(value = "/updateWithLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateWithLock(@PathVariable("id") Integer id, @RequestBody Role role) throws JsonParseException, JsonMappingException, IOException {
		Role result = roleService.updateWithLock(id, role);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete------------------------------------------------

	@PreAuthorize("@roleAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDelete/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDelete(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Role result = roleService.updateForDelete(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete With Lock------------------------------------------------

	@PreAuthorize("@roleAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDeleteWithLock/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDeleteWithLock(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Role result = roleService.updateForDeleteWithLock(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Delete a Role-----------------------------------------

	@PreAuthorize("@roleAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		roleService.deleteById(id);
		// return.	
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	// -------------------Retrieve All Roles---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Role>> listAll() {
		List<Role> roles = roleService.listAll();
		// return.
		return new ResponseEntity<List<Role>>(roles, HttpStatus.OK);
	}

	// -------------------Retrieve Single Role------------------------------------------

	@PreAuthorize("@roleAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		Role role = roleService.getById(id);
		// return.
		return new ResponseEntity<Role>(role, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Roles With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		List<Role> roles = roleService.listWithCritera(searchCriteria);
		// return.
		return new ResponseEntity<List<Role>>(roles, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Roles With Many Criteria------------------------------------------
	
	@PreAuthorize("@roleAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		List<Role> roles = roleService.listWithCriteras(searchCriterias);
		// return.
		return new ResponseEntity<List<Role>>(roles, HttpStatus.OK);
	}

	// -------------------Retrieve All Roles By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Role>> listAllByPage(Pageable pageable) {
		Page<Role> roles = roleService.listAllByPage(pageable);
		// return.
		return new ResponseEntity<Page<Role>>(roles, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Roles With A Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		Page<Role> roles = roleService.listWithCriteraByPage(searchCriteria, pageable);
		// return.
		return new ResponseEntity<Page<Role>>(roles, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Roles With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@roleAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		Page<Role> roles = roleService.listWithCriterasByPage(searchCriterias, pageable);
		// return.
		return new ResponseEntity<Page<Role>>(roles, HttpStatus.OK);
	}

}

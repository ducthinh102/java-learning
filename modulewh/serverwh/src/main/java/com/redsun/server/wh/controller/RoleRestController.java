
package com.redsun.server.wh.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.util.UriComponentsBuilder;

import com.redsun.server.wh.model.Role;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.service.RoleService;
import com.redsun.server.wh.util.CustomErrorType;

@RestController
@RequestMapping("/role")
public class RoleRestController {

	public static final Logger logger = LoggerFactory.getLogger(RoleRestController.class);

	@Autowired
	RoleService roleService; //Service which will do all data retrieval/manipulation work

	// -------------------Create a Role-------------------------------------------

	@PreAuthorize("@roleAuthorize.canCreate(#role)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Role role) {
		logger.info("Creating Role : {}", role);
		Role result = roleService.save(role);
		// return.
		return new ResponseEntity<Integer>(result.getId(), HttpStatus.CREATED);
	}

	// -------------------Update a Role------------------------------------------------

	@PreAuthorize("@roleAuthorize.canUpdate(#role)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Role role) {
		logger.info("Updating Role with id {}", id);
		Role result = roleService.save(role);
		// return.
		return new ResponseEntity<Role>(result, HttpStatus.OK);
	}

	// -------------------Delete a Role-----------------------------------------

	@PreAuthorize("@roleAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		logger.info("Fetching & Deleting Role with id {}", id);
		roleService.deleteById(id);
		// return.	
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	// -------------------Retrieve All Roles---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Role>> listAll() {
		List<Role> roles = roleService.listAll();
		if (roles.isEmpty()) {
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		// return.
		return new ResponseEntity<List<Role>>(roles, HttpStatus.OK);
	}

	// -------------------Retrieve Single Role------------------------------------------

	@PreAuthorize("@roleAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		logger.info("Fetching Role with id {}", id);
		Role role = roleService.getById(id);
		if (role == null) {
			logger.error("Role with id {} not found.", id);
			// return.
			return new ResponseEntity<CustomErrorType>(new CustomErrorType("Role with id " + id 
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		// return.
		return new ResponseEntity<Role>(role, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Roles With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		logger.info("Fetching list roles with criteria");
		List<Role> roles = roleService.listWithCritera(searchCriteria);
		if(roles.isEmpty()) {
			logger.error("List of roles is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<List<Role>>(roles, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Roles With Many Criteria------------------------------------------
	
	@PreAuthorize("@roleAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		logger.info("Fetching list roles with criteria");
		List<Role> roles = roleService.listWithCriteras(searchCriterias);
		if(roles.isEmpty()) {
			logger.error("List of roles is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<List<Role>>(roles, HttpStatus.OK);
	}

	// -------------------Retrieve All Roles By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Role>> listAllByPage(Pageable pageable) {
		Page<Role> roles = roleService.listAllByPage(pageable);
		if (!roles.hasContent()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		// return.
		return new ResponseEntity<Page<Role>>(roles, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Roles With A Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		logger.info("Fetching list roles with criteria");
		Page<Role> roles = roleService.listWithCriteraByPage(searchCriteria, pageable);
		if(!roles.hasContent()) {
			logger.error("List of roles is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<Page<Role>>(roles, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Roles With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@roleAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		logger.info("Fetching list roles with criteria");
		Page<Role> roles = roleService.listWithCriterasByPage(searchCriterias, pageable);
		if(!roles.hasContent()) {
			logger.error("List of roles is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<Page<Role>>(roles, HttpStatus.OK);
	}

}

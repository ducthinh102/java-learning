
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
import com.redsun.server.main.model.Userrole;
import com.redsun.server.main.model.common.SearchCriteria;
import com.redsun.server.main.service.UserroleService;

@RestController
@RequestMapping("/userrole")
public class UserroleRestController {

	//public static final Logger logger = LoggerFactory.getLogger(UserroleRestController.class);

	@Autowired
	UserroleService userroleService; //Service which will do all data retrieval/manipulation work

	// -------------------Create a Userrole-------------------------------------------

	@PreAuthorize("@userroleAuthorize.canCreate(#userrole)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Userrole userrole) throws JsonParseException, JsonMappingException, IOException {
		Userrole result = userroleService.create(userrole);
		// return.
		return new ResponseEntity<Integer>(result.getId(), HttpStatus.OK);
	}

	// -------------------Update lock a Userrole------------------------------------------------

	@PreAuthorize("@userroleAuthorize.canUpdate(#userrole)")
	@RequestMapping(value = "/updateLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateLock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = userroleService.updateLock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update Unlock a Userrole------------------------------------------------

	@PreAuthorize("@userroleAuthorize.canUpdate(#userrole)")
	@RequestMapping(value = "/updateUnlock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUnlock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = userroleService.updateUnlock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update a Userrole------------------------------------------------

	@PreAuthorize("@userroleAuthorize.canUpdate(#userrole)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Userrole userrole) throws JsonParseException, JsonMappingException, IOException {
		Userrole result = userroleService.update(id, userrole);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update a Userrole With Lock------------------------------------------------

	@PreAuthorize("@userroleAuthorize.canUpdate(#userrole)")
	@RequestMapping(value = "/updateWithLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateWithLock(@PathVariable("id") Integer id, @RequestBody Userrole userrole) throws JsonParseException, JsonMappingException, IOException {
		Userrole result = userroleService.updateWithLock(id, userrole);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete------------------------------------------------

	@PreAuthorize("@userroleAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDelete/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDelete(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Userrole result = userroleService.updateForDelete(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete With Lock------------------------------------------------

	@PreAuthorize("@userroleAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDeleteWithLock/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDeleteWithLock(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Userrole result = userroleService.updateForDeleteWithLock(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Delete a Userrole-----------------------------------------

	@PreAuthorize("@userroleAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		userroleService.deleteById(id);
		// return.	
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	// -------------------Retrieve All Userroles---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Userrole>> listAll() {
		List<Userrole> userroles = userroleService.listAll();
		// return.
		return new ResponseEntity<List<Userrole>>(userroles, HttpStatus.OK);
	}

	// -------------------Retrieve Single Userrole------------------------------------------

	@PreAuthorize("@userroleAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		Userrole userrole = userroleService.getById(id);
		// return.
		return new ResponseEntity<Userrole>(userrole, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Userroles With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		List<Userrole> userroles = userroleService.listWithCritera(searchCriteria);
		// return.
		return new ResponseEntity<List<Userrole>>(userroles, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Userroles With Many Criteria------------------------------------------
	
	@PreAuthorize("@userroleAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		List<Userrole> userroles = userroleService.listWithCriteras(searchCriterias);
		// return.
		return new ResponseEntity<List<Userrole>>(userroles, HttpStatus.OK);
	}

	// -------------------Retrieve All Userroles By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Userrole>> listAllByPage(Pageable pageable) {
		Page<Userrole> userroles = userroleService.listAllByPage(pageable);
		// return.
		return new ResponseEntity<Page<Userrole>>(userroles, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Userroles With A Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		Page<Userrole> userroles = userroleService.listWithCriteraByPage(searchCriteria, pageable);
		// return.
		return new ResponseEntity<Page<Userrole>>(userroles, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Userroles With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@userroleAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		Page<Userrole> userroles = userroleService.listWithCriterasByPage(searchCriterias, pageable);
		// return.
		return new ResponseEntity<Page<Userrole>>(userroles, HttpStatus.OK);
	}

}

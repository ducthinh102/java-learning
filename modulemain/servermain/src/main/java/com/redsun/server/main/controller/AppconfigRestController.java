
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
import com.redsun.server.main.model.Appconfig;
import com.redsun.server.main.model.common.SearchCriteria;
import com.redsun.server.main.service.AppconfigService;

@RestController
@RequestMapping("/appconfig")
public class AppconfigRestController {

	//public static final Logger logger = LoggerFactory.getLogger(AppconfigRestController.class);

	@Autowired
	AppconfigService appconfigService; //Service which will do all data retrieval/manipulation work

	// -------------------Create a Appconfig-------------------------------------------

	@PreAuthorize("@appconfigAuthorize.canCreate(#appconfig)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Appconfig appconfig) throws JsonParseException, JsonMappingException, IOException {
		Appconfig result = appconfigService.create(appconfig);
		// return.
		return new ResponseEntity<Integer>(result.getId(), HttpStatus.OK);
	}

	// -------------------Update lock a Appconfig------------------------------------------------

	@PreAuthorize("@appconfigAuthorize.canUpdate(#appconfig)")
	@RequestMapping(value = "/updateLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateLock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = appconfigService.updateLock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update Unlock a Appconfig------------------------------------------------

	@PreAuthorize("@appconfigAuthorize.canUpdate(#appconfig)")
	@RequestMapping(value = "/updateUnlock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUnlock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = appconfigService.updateUnlock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update a Appconfig------------------------------------------------

	@PreAuthorize("@appconfigAuthorize.canUpdate(#appconfig)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Appconfig appconfig) throws JsonParseException, JsonMappingException, IOException {
		Appconfig result = appconfigService.update(id, appconfig);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update a Appconfig With Lock------------------------------------------------

	@PreAuthorize("@appconfigAuthorize.canUpdate(#appconfig)")
	@RequestMapping(value = "/updateWithLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateWithLock(@PathVariable("id") Integer id, @RequestBody Appconfig appconfig) throws JsonParseException, JsonMappingException, IOException {
		Appconfig result = appconfigService.updateWithLock(id, appconfig);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete------------------------------------------------

	@PreAuthorize("@appconfigAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDelete/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDelete(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Appconfig result = appconfigService.updateForDelete(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete With Lock------------------------------------------------

	@PreAuthorize("@appconfigAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDeleteWithLock/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDeleteWithLock(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Appconfig result = appconfigService.updateForDeleteWithLock(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Delete a Appconfig-----------------------------------------

	@PreAuthorize("@appconfigAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		appconfigService.deleteById(id);
		// return.	
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	// -------------------Retrieve All Appconfigs---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Appconfig>> listAll() {
		List<Appconfig> appconfigs = appconfigService.listAll();
		// return.
		return new ResponseEntity<List<Appconfig>>(appconfigs, HttpStatus.OK);
	}

	// -------------------Retrieve Single Appconfig------------------------------------------

	@PreAuthorize("@appconfigAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		Appconfig appconfig = appconfigService.getById(id);
		// return.
		return new ResponseEntity<Appconfig>(appconfig, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Appconfigs With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		List<Appconfig> appconfigs = appconfigService.listWithCritera(searchCriteria);
		// return.
		return new ResponseEntity<List<Appconfig>>(appconfigs, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Appconfigs With Many Criteria------------------------------------------
	
	@PreAuthorize("@appconfigAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		List<Appconfig> appconfigs = appconfigService.listWithCriteras(searchCriterias);
		// return.
		return new ResponseEntity<List<Appconfig>>(appconfigs, HttpStatus.OK);
	}

	// -------------------Retrieve All Appconfigs By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Appconfig>> listAllByPage(Pageable pageable) {
		Page<Appconfig> appconfigs = appconfigService.listAllByPage(pageable);
		// return.
		return new ResponseEntity<Page<Appconfig>>(appconfigs, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Appconfigs With A Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		Page<Appconfig> appconfigs = appconfigService.listWithCriteraByPage(searchCriteria, pageable);
		// return.
		return new ResponseEntity<Page<Appconfig>>(appconfigs, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Appconfigs With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@appconfigAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		Page<Appconfig> appconfigs = appconfigService.listWithCriterasByPage(searchCriterias, pageable);
		// return.
		return new ResponseEntity<Page<Appconfig>>(appconfigs, HttpStatus.OK);
	}

}

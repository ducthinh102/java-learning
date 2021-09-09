
package com.redsun.server.wh.controller;

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
import com.redsun.server.wh.model.Materialquantity;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.service.MaterialquantityService;

@RestController
@RequestMapping("/materialquantity")
public class MaterialquantityRestController {

	//public static final Logger logger = LoggerFactory.getLogger(MaterialquantityRestController.class);

	@Autowired
	MaterialquantityService materialquantityService; //Service which will do all data retrieval/manipulation work

	// -------------------Create a Materialquantity-------------------------------------------

	@PreAuthorize("@materialquantityAuthorize.canCreate(#materialquantity)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Materialquantity materialquantity) throws JsonParseException, JsonMappingException, IOException {
		Materialquantity result = materialquantityService.create(materialquantity);
		// return.
		return new ResponseEntity<Integer>(result.getId(), HttpStatus.OK);
	}

	// -------------------Update lock a Materialquantity------------------------------------------------

	@PreAuthorize("@materialquantityAuthorize.canUpdate(#materialquantity)")
	@RequestMapping(value = "/updateLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateLock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = materialquantityService.updateLock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update Unlock a Materialquantity------------------------------------------------

	@PreAuthorize("@materialquantityAuthorize.canUpdate(#materialquantity)")
	@RequestMapping(value = "/updateUnlock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUnlock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = materialquantityService.updateUnlock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update a Materialquantity------------------------------------------------

	@PreAuthorize("@materialquantityAuthorize.canUpdate(#materialquantity)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Materialquantity materialquantity) throws JsonParseException, JsonMappingException, IOException {
		Materialquantity result = materialquantityService.update(id, materialquantity);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update a Materialquantity With Lock------------------------------------------------

	@PreAuthorize("@materialquantityAuthorize.canUpdate(#materialquantity)")
	@RequestMapping(value = "/updateWithLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateWithLock(@PathVariable("id") Integer id, @RequestBody Materialquantity materialquantity) throws JsonParseException, JsonMappingException, IOException {
		Materialquantity result = materialquantityService.updateWithLock(id, materialquantity);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete------------------------------------------------

	@PreAuthorize("@materialquantityAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDelete/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDelete(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Materialquantity result = materialquantityService.updateForDelete(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete With Lock------------------------------------------------

	@PreAuthorize("@materialquantityAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDeleteWithLock/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDeleteWithLock(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Materialquantity result = materialquantityService.updateForDeleteWithLock(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Delete a Materialquantity-----------------------------------------

	@PreAuthorize("@materialquantityAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		materialquantityService.deleteById(id);
		// return.	
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	// -------------------Retrieve All Materialquantitys---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Materialquantity>> listAll() {
		List<Materialquantity> materialquantitys = materialquantityService.listAll();
		// return.
		return new ResponseEntity<List<Materialquantity>>(materialquantitys, HttpStatus.OK);
	}

	// -------------------Retrieve Single Materialquantity------------------------------------------

	@PreAuthorize("@materialquantityAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		Materialquantity materialquantity = materialquantityService.getById(id);
		// return.
		return new ResponseEntity<Materialquantity>(materialquantity, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialquantitys With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		List<Materialquantity> materialquantitys = materialquantityService.listWithCritera(searchCriteria);
		// return.
		return new ResponseEntity<List<Materialquantity>>(materialquantitys, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialquantitys With Many Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		List<Materialquantity> materialquantitys = materialquantityService.listWithCriteras(searchCriterias);
		// return.
		return new ResponseEntity<List<Materialquantity>>(materialquantitys, HttpStatus.OK);
	}

	// -------------------Retrieve All Materialquantitys By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Materialquantity>> listAllByPage(Pageable pageable) {
		Page<Materialquantity> materialquantitys = materialquantityService.listAllByPage(pageable);
		// return.
		return new ResponseEntity<Page<Materialquantity>>(materialquantitys, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialquantitys With A Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		Page<Materialquantity> materialquantitys = materialquantityService.listWithCriteraByPage(searchCriteria, pageable);
		// return.
		return new ResponseEntity<Page<Materialquantity>>(materialquantitys, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialquantitys With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@materialquantityAuthorize.canRead(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		Page<Materialquantity> materialquantitys = materialquantityService.listWithCriterasByPage(searchCriterias, pageable);
		// return.
		return new ResponseEntity<Page<Materialquantity>>(materialquantitys, HttpStatus.OK);
	}

}

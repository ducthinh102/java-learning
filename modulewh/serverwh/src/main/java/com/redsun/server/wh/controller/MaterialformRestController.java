
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
import com.redsun.server.wh.model.Materialform;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.service.MaterialformService;

@RestController
@RequestMapping("/materialform")
public class MaterialformRestController {

	//public static final Logger logger = LoggerFactory.getLogger(MaterialformRestController.class);

	@Autowired
	MaterialformService materialformService; //Service which will do all data retrieval/manipulation work

	// -------------------Create a Materialform-------------------------------------------

	@PreAuthorize("@materialformAuthorize.canCreate(#materialform)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Materialform materialform) throws JsonParseException, JsonMappingException, IOException {
		Materialform result = materialformService.create(materialform);
		// return.
		return new ResponseEntity<Integer>(result.getId(), HttpStatus.OK);
	}

	// -------------------Update lock a Materialform------------------------------------------------

	@PreAuthorize("@materialformAuthorize.canUpdate(#materialform)")
	@RequestMapping(value = "/updateLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateLock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = materialformService.updateLock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update Unlock a Materialform------------------------------------------------

	@PreAuthorize("@materialformAuthorize.canUpdate(#materialform)")
	@RequestMapping(value = "/updateUnlock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUnlock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = materialformService.updateUnlock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update a Materialform------------------------------------------------

	@PreAuthorize("@materialformAuthorize.canUpdate(#materialform)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Materialform materialform) throws JsonParseException, JsonMappingException, IOException {
		Materialform result = materialformService.update(id, materialform);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update a Materialform With Lock------------------------------------------------

	@PreAuthorize("@materialformAuthorize.canUpdate(#materialform)")
	@RequestMapping(value = "/updateWithLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateWithLock(@PathVariable("id") Integer id, @RequestBody Materialform materialform) throws JsonParseException, JsonMappingException, IOException {
		Materialform result = materialformService.updateWithLock(id, materialform);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete------------------------------------------------

	@PreAuthorize("@materialformAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDelete/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDelete(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Materialform result = materialformService.updateForDelete(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete With Lock------------------------------------------------

	@PreAuthorize("@materialformAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDeleteWithLock/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDeleteWithLock(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Materialform result = materialformService.updateForDeleteWithLock(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Delete a Materialform-----------------------------------------

	@PreAuthorize("@materialformAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		materialformService.deleteById(id);
		// return.	
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	// -------------------Retrieve All Materialforms---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Materialform>> listAll() {
		List<Materialform> materialforms = materialformService.listAll();
		// return.
		return new ResponseEntity<List<Materialform>>(materialforms, HttpStatus.OK);
	}

	// -------------------Retrieve Single Materialform------------------------------------------

	@PreAuthorize("@materialformAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		Materialform materialform = materialformService.getById(id);
		// return.
		return new ResponseEntity<Materialform>(materialform, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialforms With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		List<Materialform> materialforms = materialformService.listWithCritera(searchCriteria);
		// return.
		return new ResponseEntity<List<Materialform>>(materialforms, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialforms With Many Criteria------------------------------------------
	
	@PreAuthorize("@materialformAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		List<Materialform> materialforms = materialformService.listWithCriteras(searchCriterias);
		// return.
		return new ResponseEntity<List<Materialform>>(materialforms, HttpStatus.OK);
	}

	// -------------------Retrieve All Materialforms By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Materialform>> listAllByPage(Pageable pageable) {
		Page<Materialform> materialforms = materialformService.listAllByPage(pageable);
		// return.
		return new ResponseEntity<Page<Materialform>>(materialforms, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialforms With A Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		Page<Materialform> materialforms = materialformService.listWithCriteraByPage(searchCriteria, pageable);
		// return.
		return new ResponseEntity<Page<Materialform>>(materialforms, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialforms With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@materialformAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		Page<Materialform> materialforms = materialformService.listWithCriterasByPage(searchCriterias, pageable);
		// return.
		return new ResponseEntity<Page<Materialform>>(materialforms, HttpStatus.OK);
	}
	// -------------------------Retrieve All User For Select--------------------------------------------------- 
		@RequestMapping(value = "/listAllForSelectByScope/{scope}", method = RequestMethod.GET)
		public ResponseEntity<List<Map<String, Object>>> listAllForSelectByScope(@PathVariable("scope")String scope) {
			List<Map<String, Object>> materialforms = materialformService.listAllForSelectByScope(scope);
			// return.
			return new ResponseEntity<List<Map<String, Object>>>(materialforms, HttpStatus.OK);
		}
}

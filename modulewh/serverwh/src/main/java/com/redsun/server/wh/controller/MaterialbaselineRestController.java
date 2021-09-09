
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
import com.redsun.server.wh.model.Materialbaseline;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.service.MaterialbaselineService;

@RestController
@RequestMapping("/materialbaseline")
public class MaterialbaselineRestController {

	//public static final Logger logger = LoggerFactory.getLogger(MaterialbaselineRestController.class);

	@Autowired
	MaterialbaselineService materialbaselineService; //Service which will do all data retrieval/manipulation work

	// -------------------Create a Materialbaseline-------------------------------------------

	@PreAuthorize("@materialbaselineAuthorize.canCreate(#materialbaseline)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Materialbaseline materialbaseline) throws JsonParseException, JsonMappingException, IOException {
		Materialbaseline result = materialbaselineService.create(materialbaseline);
		// return.
		return new ResponseEntity<Integer>(result.getId(), HttpStatus.OK);
	}

	// -------------------Update lock a Materialbaseline------------------------------------------------

	@PreAuthorize("@materialbaselineAuthorize.canUpdate(#materialbaseline)")
	@RequestMapping(value = "/updateLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateLock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = materialbaselineService.updateLock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update Unlock a Materialbaseline------------------------------------------------

	@PreAuthorize("@materialbaselineAuthorize.canUpdate(#materialbaseline)")
	@RequestMapping(value = "/updateUnlock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUnlock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = materialbaselineService.updateUnlock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update a Materialbaseline------------------------------------------------

	@PreAuthorize("@materialbaselineAuthorize.canUpdate(#materialbaseline)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Materialbaseline materialbaseline) throws JsonParseException, JsonMappingException, IOException {
		Materialbaseline result = materialbaselineService.update(id, materialbaseline);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update a Materialbaseline With Lock------------------------------------------------

	@PreAuthorize("@materialbaselineAuthorize.canUpdate(#materialbaseline)")
	@RequestMapping(value = "/updateWithLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateWithLock(@PathVariable("id") Integer id, @RequestBody Materialbaseline materialbaseline) throws JsonParseException, JsonMappingException, IOException {
		Materialbaseline result = materialbaselineService.updateWithLock(id, materialbaseline);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete------------------------------------------------

	@PreAuthorize("@materialbaselineAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDelete/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDelete(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Materialbaseline result = materialbaselineService.updateForDelete(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete With Lock------------------------------------------------

	@PreAuthorize("@materialbaselineAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDeleteWithLock/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDeleteWithLock(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Materialbaseline result = materialbaselineService.updateForDeleteWithLock(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Delete a Materialbaseline-----------------------------------------

	@PreAuthorize("@materialbaselineAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		materialbaselineService.deleteById(id);
		// return.	
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	// -------------------Retrieve All Materialbaselines---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Materialbaseline>> listAll() {
		List<Materialbaseline> materialbaselines = materialbaselineService.listAll();
		// return.
		return new ResponseEntity<List<Materialbaseline>>(materialbaselines, HttpStatus.OK);
	}

	// -------------------Retrieve Single Materialbaseline------------------------------------------

	@PreAuthorize("@materialbaselineAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		Materialbaseline materialbaseline = materialbaselineService.getById(id);
		// return.
		return new ResponseEntity<Materialbaseline>(materialbaseline, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialbaselines With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		List<Materialbaseline> materialbaselines = materialbaselineService.listWithCritera(searchCriteria);
		// return.
		return new ResponseEntity<List<Materialbaseline>>(materialbaselines, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialbaselines With Many Criteria------------------------------------------
	
	@PreAuthorize("@materialbaselineAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		List<Materialbaseline> materialbaselines = materialbaselineService.listWithCriteras(searchCriterias);
		// return.
		return new ResponseEntity<List<Materialbaseline>>(materialbaselines, HttpStatus.OK);
	}

	// -------------------Retrieve All Materialbaselines By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Materialbaseline>> listAllByPage(Pageable pageable) {
		Page<Materialbaseline> materialbaselines = materialbaselineService.listAllByPage(pageable);
		// return.
		return new ResponseEntity<Page<Materialbaseline>>(materialbaselines, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialbaselines With A Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		Page<Materialbaseline> materialbaselines = materialbaselineService.listWithCriteraByPage(searchCriteria, pageable);
		// return.
		return new ResponseEntity<Page<Materialbaseline>>(materialbaselines, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialbaselines With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@materialbaselineAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		Page<Map<String, Object>> materialbaselines = materialbaselineService.listWithCriterasByPage(searchCriterias, pageable);
		// return.
		return new ResponseEntity<Page<Map<String, Object>>>(materialbaselines, HttpStatus.OK);
	}

	// -------------------------Retrieve All User For Select--------------------------------------------------- 
			@RequestMapping(value = "/listAllForSelect", method = RequestMethod.GET)
			public ResponseEntity<List<Map<String, Object>>> listAllForSelect() {
				List<Map<String, Object>> materialbaselines = materialbaselineService.listAllForSelect();
				// return.
				return new ResponseEntity<List<Map<String, Object>>>(materialbaselines, HttpStatus.OK);
			}

}


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
import com.redsun.server.wh.model.Materialimport;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.service.MaterialimportService;

@RestController
@RequestMapping("/materialimport")
public class MaterialimportRestController {

	//public static final Logger logger = LoggerFactory.getLogger(MaterialimportRestController.class);

	@Autowired
	MaterialimportService materialimportService; //Service which will do all data retrieval/manipulation work

	// -------------------Create a Materialimport-------------------------------------------

	@PreAuthorize("@materialimportAuthorize.canCreate(#materialimport)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Materialimport materialimport) throws JsonParseException, JsonMappingException, IOException {
		Materialimport result = materialimportService.create(materialimport);
		// return.
		return new ResponseEntity<Integer>(result.getId(), HttpStatus.OK);
	}

	// -------------------Update lock a Materialimport------------------------------------------------

	@PreAuthorize("@materialimportAuthorize.canUpdate(#materialimport)")
	@RequestMapping(value = "/updateLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateLock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = materialimportService.updateLock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update Unlock a Materialimport------------------------------------------------

	@PreAuthorize("@materialimportAuthorize.canUpdate(#materialimport)")
	@RequestMapping(value = "/updateUnlock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUnlock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = materialimportService.updateUnlock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update a Materialimport------------------------------------------------

	@PreAuthorize("@materialimportAuthorize.canUpdate(#materialimport)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Materialimport materialimport) throws JsonParseException, JsonMappingException, IOException {
		Materialimport result = materialimportService.update(id, materialimport);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update a Materialimport With Lock------------------------------------------------

	@PreAuthorize("@materialimportAuthorize.canUpdate(#materialimport)")
	@RequestMapping(value = "/updateWithLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateWithLock(@PathVariable("id") Integer id, @RequestBody Materialimport materialimport) throws JsonParseException, JsonMappingException, IOException {
		Materialimport result = materialimportService.updateWithLock(id, materialimport);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete------------------------------------------------

	@PreAuthorize("@materialimportAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDelete/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDelete(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Materialimport result = materialimportService.updateForDelete(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete With Lock------------------------------------------------

	@PreAuthorize("@materialimportAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDeleteWithLock/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDeleteWithLock(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Materialimport result = materialimportService.updateForDeleteWithLock(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Delete a Materialimport-----------------------------------------

	@PreAuthorize("@materialimportAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		materialimportService.deleteById(id);
		// return.	
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	// -------------------Retrieve All Materialimports---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Materialimport>> listAll() {
		List<Materialimport> materialimports = materialimportService.listAll();
		// return.
		return new ResponseEntity<List<Materialimport>>(materialimports, HttpStatus.OK);
	}

	// -------------------Retrieve Single Materialimport------------------------------------------

	@PreAuthorize("@materialimportAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		Materialimport materialimport = materialimportService.getById(id);
		// return.
		return new ResponseEntity<Materialimport>(materialimport, HttpStatus.OK);
	}
	
	// -------------------Retrieve Single Materialimport------------------------------------------

		@PreAuthorize("@materialimportAuthorize.canRead(#id)")
		@RequestMapping(value = "/getByIdForView/{id}", method = RequestMethod.GET)
		public ResponseEntity<?> getByIdForView(@PathVariable("id") Integer id) {
			Map<String, Object> materialimport = materialimportService.getByIdForView(id);
			// return.
			return new ResponseEntity<Map<String, Object>>(materialimport, HttpStatus.OK);
		}
	
	// -------------------Retrieve List Of Materialimports With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		List<Materialimport> materialimports = materialimportService.listWithCritera(searchCriteria);
		// return.
		return new ResponseEntity<List<Materialimport>>(materialimports, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialimports With Many Criteria------------------------------------------
	
	@PreAuthorize("@materialimportAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		List<Materialimport> materialimports = materialimportService.listWithCriteras(searchCriterias);
		// return.
		return new ResponseEntity<List<Materialimport>>(materialimports, HttpStatus.OK);
	}

	// -------------------Retrieve All Materialimports By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Materialimport>> listAllByPage(Pageable pageable) {
		Page<Materialimport> materialimports = materialimportService.listAllByPage(pageable);
		// return.
		return new ResponseEntity<Page<Materialimport>>(materialimports, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialimports With A Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		Page<Materialimport> materialimports = materialimportService.listWithCriteraByPage(searchCriteria, pageable);
		// return.
		return new ResponseEntity<Page<Materialimport>>(materialimports, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialimports With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@materialimportAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		Page<Map<String, Object>> materialimports = materialimportService.listWithCriterasByPage(searchCriterias, pageable);
		// return.
		return new  ResponseEntity<Page<Map<String, Object>>>(materialimports, HttpStatus.OK);
	}
	
	// -------------------------Retrieve All User For Select--------------------------------------------------- 
		@RequestMapping(value = "/listAllForSelect", method = RequestMethod.GET)
		public ResponseEntity<List<Map<String, Object>>> listAllForSelect() {
			List<Map<String, Object>> materialimports = materialimportService.listAllForSelect();
			// return.
			return new ResponseEntity<List<Map<String, Object>>>(materialimports, HttpStatus.OK);
		}

}

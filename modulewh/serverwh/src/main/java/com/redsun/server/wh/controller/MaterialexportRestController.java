
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
import com.redsun.server.wh.model.Materialexport;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.service.MaterialexportService;


@RestController
@RequestMapping("/materialexport")
public class MaterialexportRestController {

	//public static final Logger logger = LoggerFactory.getLogger(MaterialexportRestController.class);

	@Autowired
	MaterialexportService materialexportService; //Service which will do all data retrieval/manipulation work

	// -------------------Create a Materialexport-------------------------------------------

	@PreAuthorize("@materialexportAuthorize.canCreate(#materialexport)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Materialexport materialexport) throws JsonParseException, JsonMappingException, IOException {
		Materialexport result = materialexportService.create(materialexport);
		// return.
		return new ResponseEntity<Integer>(result.getId(), HttpStatus.OK);
	}

	// -------------------Update lock a Materialexport------------------------------------------------

	@PreAuthorize("@materialexportAuthorize.canUpdate(#materialexport)")
	@RequestMapping(value = "/updateLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateLock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = materialexportService.updateLock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update Unlock a Materialexport------------------------------------------------

	@PreAuthorize("@materialexportAuthorize.canUpdate(#materialexport)")
	@RequestMapping(value = "/updateUnlock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUnlock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = materialexportService.updateUnlock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update a Materialexport------------------------------------------------

	@PreAuthorize("@materialexportAuthorize.canUpdate(#materialexport)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Materialexport materialexport) throws JsonParseException, JsonMappingException, IOException {
		Materialexport result = materialexportService.update(id, materialexport);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update a Materialexport With Lock------------------------------------------------

	@PreAuthorize("@materialexportAuthorize.canUpdate(#materialexport)")
	@RequestMapping(value = "/updateWithLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateWithLock(@PathVariable("id") Integer id, @RequestBody Materialexport materialexport) throws JsonParseException, JsonMappingException, IOException {
		Materialexport result = materialexportService.updateWithLock(id, materialexport);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete------------------------------------------------

	@PreAuthorize("@materialexportAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDelete/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDelete(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Materialexport result = materialexportService.updateForDelete(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete With Lock------------------------------------------------

	@PreAuthorize("@materialexportAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDeleteWithLock/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDeleteWithLock(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Materialexport result = materialexportService.updateForDeleteWithLock(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Delete a Materialexport-----------------------------------------

	@PreAuthorize("@materialexportAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		materialexportService.deleteById(id);
		// return.	
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	// -------------------Retrieve All Materialexports---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Materialexport>> listAll() {
		List<Materialexport> materialexports = materialexportService.listAll();
		// return.
		return new ResponseEntity<List<Materialexport>>(materialexports, HttpStatus.OK);
	}

	// -------------------Retrieve Single Materialexport------------------------------------------

	@PreAuthorize("@materialexportAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		Materialexport materialexport = materialexportService.getById(id);
		// return.
		return new ResponseEntity<Materialexport>(materialexport, HttpStatus.OK);
	}
	
	// -------------------Retrieve Single Materialexport------------------------------------------

		@PreAuthorize("@materialexportAuthorize.canRead(#id)")
		@RequestMapping(value = "/getByIdForView/{id}", method = RequestMethod.GET)
		public ResponseEntity<?> getByIdForView(@PathVariable("id") Integer id) {
			Map<String, Object> materialexport = materialexportService.getByIdForView(id);
			// return.
			return new ResponseEntity<Map<String, Object>>(materialexport, HttpStatus.OK);
		}
	
	// -------------------Retrieve List Of Materialexports With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		List<Materialexport> materialexports = materialexportService.listWithCritera(searchCriteria);
		// return.
		return new ResponseEntity<List<Materialexport>>(materialexports, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialexports With Many Criteria------------------------------------------
	
	@PreAuthorize("@materialexportAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		List<Materialexport> materialexports = materialexportService.listWithCriteras(searchCriterias);
		// return.
		return new ResponseEntity<List<Materialexport>>(materialexports, HttpStatus.OK);
	}

	// -------------------Retrieve All Materialexports By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Materialexport>> listAllByPage(Pageable pageable) {
		Page<Materialexport> materialexports = materialexportService.listAllByPage(pageable);
		// return.
		return new ResponseEntity<Page<Materialexport>>(materialexports, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialexports With A Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		Page<Materialexport> materialexports = materialexportService.listWithCriteraByPage(searchCriteria, pageable);
		// return.
		return new ResponseEntity<Page<Materialexport>>(materialexports, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialexports With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@materialexportAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		Page<Map<String, Object>> materialexports = materialexportService.listWithCriterasByPage(searchCriterias, pageable);
		// return.
		return new  ResponseEntity<Page<Map<String, Object>>>(materialexports, HttpStatus.OK);
	}
	
	// -------------------------Retrieve All User For Select--------------------------------------------------- 
		@RequestMapping(value = "/listAllForSelect", method = RequestMethod.GET)
		public ResponseEntity<List<Map<String, Object>>> listAllForSelect() {
			List<Map<String, Object>> materialexports = materialexportService.listAllForSelect();
			// return.
			return new ResponseEntity<List<Map<String, Object>>>(materialexports, HttpStatus.OK);
		}

}

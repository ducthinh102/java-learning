
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
import com.redsun.server.wh.model.Materialexportdetail;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.service.MaterialexportdetailService;
import com.redsun.server.wh.service.MaterialstoreService;

@RestController
@RequestMapping("/materialexportdetail")
public class MaterialexportdetailRestController {

	@Autowired
	MaterialexportdetailService materialexportdetailService; // Service which will do all data retrieval/manipulation
																// work

	@Autowired
	MaterialstoreService materialstoreService;

	// -------------------Create a
	// Materialexportdetail-------------------------------------------

	@PreAuthorize("@materialexportdetailAuthorize.canCreate(#materialexportdetail)")
	@RequestMapping(value = "/create/{store}", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Materialexportdetail materialexportdetail,
			@PathVariable("store") Integer idstore) throws JsonParseException, JsonMappingException, IOException {
		Materialexportdetail result = materialexportdetailService.create(materialexportdetail, idstore);
		// return
		return new ResponseEntity<Integer>(result.getId(), HttpStatus.OK);
	}

	// -------------------Update lock a
	// Materialexportdetail------------------------------------------------

	@PreAuthorize("@materialexportdetailAuthorize.canUpdate(#materialexportdetail)")
	@RequestMapping(value = "/updateLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateLock(@PathVariable("id") Integer id)
			throws JsonParseException, JsonMappingException, IOException {
		Integer result = materialexportdetailService.updateLock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update Unlock a
	// Materialexportdetail------------------------------------------------

	@PreAuthorize("@materialexportdetailAuthorize.canUpdate(#materialexportdetail)")
	@RequestMapping(value = "/updateUnlock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUnlock(@PathVariable("id") Integer id)
			throws JsonParseException, JsonMappingException, IOException {
		Integer result = materialexportdetailService.updateUnlock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update a
	// Materialexportdetail------------------------------------------------

	@PreAuthorize("@materialexportdetailAuthorize.canUpdate(#materialexportdetail)")
	@RequestMapping(value = "/update/{id}/{idStore}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id,
			@RequestBody Materialexportdetail materialexportdetail,@PathVariable("idStore")Integer idStore)
			throws JsonParseException, JsonMappingException, IOException {
		Materialexportdetail result = materialexportdetailService.update(id, materialexportdetail, idStore);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update a Materialexportdetail With
	// Lock------------------------------------------------

	@PreAuthorize("@materialexportdetailAuthorize.canUpdate(#materialexportdetail)")
	@RequestMapping(value = "/updateWithLock/{id}/{store}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateWithLock(@PathVariable("id") Integer id, @PathVariable("store") Integer idstore,
			@RequestBody Materialexportdetail materialexportdetail)
			throws JsonParseException, JsonMappingException, IOException {
		Materialexportdetail result = materialexportdetailService.updateWithLock(id, materialexportdetail, idstore);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For
	// Delete------------------------------------------------

	@PreAuthorize("@materialexportdetailAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDelete/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDelete(@PathVariable("id") Integer id, @PathVariable("version") Integer version)
			throws JsonParseException, JsonMappingException, IOException {
		Materialexportdetail result = materialexportdetailService.updateForDelete(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete With
	// Lock------------------------------------------------

	@PreAuthorize("@materialexportdetailAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDeleteWithLock/{id}/{version}/{store}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDeleteWithLock(@PathVariable("id") Integer id,
			@PathVariable("version") Integer version, @PathVariable("store") Integer idstore)
			throws JsonParseException, JsonMappingException, IOException {
		Materialexportdetail result = materialexportdetailService.updateForDeleteWithLock(id, version, idstore);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Delete a
	// Materialexportdetail-----------------------------------------

	@PreAuthorize("@materialexportdetailAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		materialexportdetailService.deleteById(id);
		// return.
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	// -------------------Retrieve All
	// Materialexportdetails---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Materialexportdetail>> listAll() {
		List<Materialexportdetail> materialexportdetails = materialexportdetailService.listAll();
		// return.
		return new ResponseEntity<List<Materialexportdetail>>(materialexportdetails, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// Materialexportdetail------------------------------------------

	@PreAuthorize("@materialexportdetailAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		Materialexportdetail materialexportdetail = materialexportdetailService.getById(id);
		// return.
		return new ResponseEntity<Materialexportdetail>(materialexportdetail, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// Materialexportdetail------------------------------------------

	@PreAuthorize("@materialexportdetailAuthorize.canRead(#id)")
	@RequestMapping(value = "/getByIdForView/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getByIdForView(@PathVariable("id") Integer id) {
		Map<String, Object> materialexportdetail = materialexportdetailService.getByIdForView(id);
		// return.
		return new ResponseEntity<Map<String, Object>>(materialexportdetail, HttpStatus.OK);
	}

	@PreAuthorize("@materialexportdetailAuthorize.canRead(#id)")
	@RequestMapping(value = "/getVerisonById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getVerisonById(@PathVariable("id") Integer id) {
		Materialexportdetail materialexportdetail = materialexportdetailService.getById(id);
		// return.
		return new ResponseEntity<Integer>(materialexportdetail.getVersion(), HttpStatus.OK);
	}

	// -------------------Retrieve List Of Materialexportdetails With A
	// Criteria------------------------------------------

	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		List<Materialexportdetail> materialexportdetails = materialexportdetailService.listWithCritera(searchCriteria);
		// return.
		return new ResponseEntity<List<Materialexportdetail>>(materialexportdetails, HttpStatus.OK);
	}

	// -------------------Retrieve List Of Materialexportdetails With Many
	// Criteria------------------------------------------

	@PreAuthorize("@materialexportdetailAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		List<Materialexportdetail> materialexportdetails = materialexportdetailService
				.listWithCriteras(searchCriterias);
		// return.
		return new ResponseEntity<List<Materialexportdetail>>(materialexportdetails, HttpStatus.OK);
	}

	// -------------------Retrieve All Materialexportdetails By
	// Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Materialexportdetail>> listAllByPage(Pageable pageable) {
		Page<Materialexportdetail> materialexportdetails = materialexportdetailService.listAllByPage(pageable);
		// return.
		return new ResponseEntity<Page<Materialexportdetail>>(materialexportdetails, HttpStatus.OK);
	}

	// -------------------Retrieve List Of Materialexportdetails With A Criteria By
	// Page------------------------------------------

	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		Page<Materialexportdetail> materialexportdetails = materialexportdetailService
				.listWithCriteraByPage(searchCriteria, pageable);
		// return.
		return new ResponseEntity<Page<Materialexportdetail>>(materialexportdetails, HttpStatus.OK);
	}

	// -------------------Retrieve List Of Materialexportdetails With Multiple
	// Criteria By Page------------------------------------------

	@PreAuthorize("@materialexportdetailAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias,
			Pageable pageable) {
		Page<Materialexportdetail> materialexportdetails = materialexportdetailService
				.listWithCriterasByPage(searchCriterias, pageable);
		// return.
		return new ResponseEntity<Page<Materialexportdetail>>(materialexportdetails, HttpStatus.OK);
	}

	// -------------------Retrieve List Of Materialexports With Multiple Criteria By
	// Page------------------------------------------

	@PreAuthorize("@materialexportdetailAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByIdmaterialexportAndPage/{idmaterialexport}", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByIdmaterialexportAndPage(
			@PathVariable("idmaterialexport") Integer idmaterialexport,
			@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		Page<Map<String, Object>> materialexports = materialexportdetailService
				.listWithCriteriasByIdmaterialexportAndPage(idmaterialexport, searchCriterias, pageable);
		// return.
		return new ResponseEntity<Page<Map<String, Object>>>(materialexports, HttpStatus.OK);
	}

	// -------------------Retrieve Multiple
	// Materialexportdetail------------------------------------------

	@PreAuthorize("@materialexportdetailAuthorize.canRead(#id)")
	@RequestMapping(value = "/getAllById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getAllById(@PathVariable("id") Integer id) {
		List<Map<String, Object>> materialexportdetails = materialexportdetailService.getAllById(id);
		// return.
		return new ResponseEntity<List<Map<String, Object>>>(materialexportdetails, HttpStatus.OK);
	}
	// -------------------Create Multiple
	// Materialexportdetail-------------------------------------------

	@PreAuthorize("@materialexportdetailAuthorize.canCreate(#materialexportdetail)")
	@RequestMapping(value = "/createWithSelectDetail/{store}", method = RequestMethod.POST)
	public ResponseEntity<?> createWithSelectDetail(@RequestBody List<Materialexportdetail> materialexportdetails,
			@PathVariable("store") Integer idstore) throws JsonParseException, JsonMappingException, IOException {
		for (Materialexportdetail materialexportdetail : materialexportdetails) {
			materialexportdetailService.create(materialexportdetail, idstore);
		}
		// return.
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	// -------------------Update Price of Materialformsdetail-------------------------------------------
				@PreAuthorize("@materialexportdetailAuthorize.canUpdate(#materialexportdetail)")
				@RequestMapping(value = "/updateQuantity/{idstore}", method = RequestMethod.POST)
				public ResponseEntity<?> updateQuantity(@RequestBody List<Materialexportdetail> materialexportdetails,@PathVariable("idstore")Integer idstore) throws JsonParseException, JsonMappingException, IOException {
					for (Materialexportdetail materialexportdetail : materialexportdetails) {
						materialexportdetailService.updateQuantity(materialexportdetail.getId(), materialexportdetail.getQuantity(),idstore);
					}
					// return.
					return new ResponseEntity<>(HttpStatus.OK);
				}
}

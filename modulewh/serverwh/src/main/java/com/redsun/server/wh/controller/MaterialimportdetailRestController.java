
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
import com.redsun.server.wh.model.Materialimportdetail;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.service.MaterialimportdetailService;
import com.redsun.server.wh.service.MaterialstoreService;

@RestController
@RequestMapping("/materialimportdetail")
public class MaterialimportdetailRestController {

	//public static final Logger logger = LoggerFactory.getLogger(MaterialimportdetailRestController.class);

	@Autowired
	MaterialimportdetailService materialimportdetailService; //Service which will do all data retrieval/manipulation work
	
	@Autowired
	MaterialstoreService materialstoreService;

	// -------------------Create a Materialimportdetail-------------------------------------------

	@PreAuthorize("@materialimportdetailAuthorize.canCreate(#materialimportdetail)")
	@RequestMapping(value = "/create/{store}", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Materialimportdetail materialimportdetail,@PathVariable("store")Integer idstore) throws JsonParseException, JsonMappingException, IOException {
			Materialimportdetail result = materialimportdetailService.create(materialimportdetail,idstore);
			// return
			return new ResponseEntity<Integer>(result.getId(), HttpStatus.OK);
	}

	// -------------------Update lock a Materialimportdetail------------------------------------------------

	@PreAuthorize("@materialimportdetailAuthorize.canUpdate(#materialimportdetail)")
	@RequestMapping(value = "/updateLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateLock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = materialimportdetailService.updateLock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update Unlock a Materialimportdetail------------------------------------------------

	@PreAuthorize("@materialimportdetailAuthorize.canUpdate(#materialimportdetail)")
	@RequestMapping(value = "/updateUnlock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUnlock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = materialimportdetailService.updateUnlock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update a Materialimportdetail------------------------------------------------

	@PreAuthorize("@materialimportdetailAuthorize.canUpdate(#materialimportdetail)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Materialimportdetail materialimportdetail) throws JsonParseException, JsonMappingException, IOException {
		Materialimportdetail result = materialimportdetailService.update(id, materialimportdetail);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update a Materialimportdetail With Lock------------------------------------------------

	@PreAuthorize("@materialimportdetailAuthorize.canUpdate(#materialimportdetail)")
	@RequestMapping(value = "/updateWithLock/{id}/{store}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateWithLock(@PathVariable("id") Integer id,@PathVariable("store") Integer idstore, @RequestBody Materialimportdetail materialimportdetail) throws JsonParseException, JsonMappingException, IOException {
		Materialimportdetail result = materialimportdetailService.updateWithLock(id, materialimportdetail, idstore);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete------------------------------------------------

	@PreAuthorize("@materialimportdetailAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDelete/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDelete(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Materialimportdetail result = materialimportdetailService.updateForDelete(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete With Lock------------------------------------------------

	@PreAuthorize("@materialimportdetailAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDeleteWithLock/{id}/{version}/{store}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDeleteWithLock(@PathVariable("id") Integer id, @PathVariable("version") Integer version,@PathVariable("store") Integer idstore) throws JsonParseException, JsonMappingException, IOException {
		Materialimportdetail result = materialimportdetailService.updateForDeleteWithLock(id, version,idstore);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Delete a Materialimportdetail-----------------------------------------

	@PreAuthorize("@materialimportdetailAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		materialimportdetailService.deleteById(id);
		// return.	
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	// -------------------Retrieve All Materialimportdetails---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Materialimportdetail>> listAll() {
		List<Materialimportdetail> materialimportdetails = materialimportdetailService.listAll();
		// return.
		return new ResponseEntity<List<Materialimportdetail>>(materialimportdetails, HttpStatus.OK);
	}

	// -------------------Retrieve Single Materialimportdetail------------------------------------------

	@PreAuthorize("@materialimportdetailAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		Materialimportdetail materialimportdetail = materialimportdetailService.getById(id);
		// return.
		return new ResponseEntity<Materialimportdetail>(materialimportdetail, HttpStatus.OK);
	}
	
	// -------------------Retrieve Single Materialimportdetail------------------------------------------

		@PreAuthorize("@materialimportdetailAuthorize.canRead(#id)")
		@RequestMapping(value = "/getByIdForView/{id}", method = RequestMethod.GET)
		public ResponseEntity<?> getByIdForView(@PathVariable("id") Integer id) {
			Map<String, Object> materialimportdetail = materialimportdetailService.getByIdForView(id);
			// return.
			return new ResponseEntity<Map<String, Object>>(materialimportdetail, HttpStatus.OK);
		}
	
	@PreAuthorize("@materialimportdetailAuthorize.canRead(#id)")
	@RequestMapping(value = "/getVerisonById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getVerisonById(@PathVariable("id") Integer id) {
		Materialimportdetail materialimportdetail = materialimportdetailService.getById(id);
		// return.
		return new ResponseEntity<Integer>(materialimportdetail.getVersion(), HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialimportdetails With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		List<Materialimportdetail> materialimportdetails = materialimportdetailService.listWithCritera(searchCriteria);
		// return.
		return new ResponseEntity<List<Materialimportdetail>>(materialimportdetails, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialimportdetails With Many Criteria------------------------------------------
	
	@PreAuthorize("@materialimportdetailAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		List<Materialimportdetail> materialimportdetails = materialimportdetailService.listWithCriteras(searchCriterias);
		// return.
		return new ResponseEntity<List<Materialimportdetail>>(materialimportdetails, HttpStatus.OK);
	}

	// -------------------Retrieve All Materialimportdetails By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Materialimportdetail>> listAllByPage(Pageable pageable) {
		Page<Materialimportdetail> materialimportdetails = materialimportdetailService.listAllByPage(pageable);
		// return.
		return new ResponseEntity<Page<Materialimportdetail>>(materialimportdetails, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialimportdetails With A Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		Page<Materialimportdetail> materialimportdetails = materialimportdetailService.listWithCriteraByPage(searchCriteria, pageable);
		// return.
		return new ResponseEntity<Page<Materialimportdetail>>(materialimportdetails, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialimportdetails With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@materialimportdetailAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		Page<Materialimportdetail> materialimportdetails = materialimportdetailService.listWithCriterasByPage(searchCriterias, pageable);
		// return.
		return new ResponseEntity<Page<Materialimportdetail>>(materialimportdetails, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialimports With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@materialimportdetailAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByIdmaterialimportAndPage/{idmaterialimport}", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByIdmaterialimportAndPage(@PathVariable("idmaterialimport") Integer idmaterialimport, @RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		Page<Map<String, Object>> materialimports = materialimportdetailService.listWithCriteriasByIdmaterialimportAndPage(idmaterialimport, searchCriterias, pageable);
		// return.
		return new ResponseEntity<Page<Map<String, Object>>>(materialimports, HttpStatus.OK);
	}
	
	// -------------------Retrieve Multiple Materialimportdetail------------------------------------------

			@PreAuthorize("@materialimportdetailAuthorize.canRead(#id)")
			@RequestMapping(value = "/getAllById/{id}", method = RequestMethod.GET)
			public ResponseEntity<?> getAllById(@PathVariable("id") Integer id) {
				List<Map<String, Object>> materialimportdetails = materialimportdetailService.getAllById(id);
				// return.
				return new ResponseEntity<List<Map<String, Object>>>(materialimportdetails, HttpStatus.OK);
			}
			// -------------------Create Multiple Materialimportdetail-------------------------------------------

			@PreAuthorize("@materialimportdetailAuthorize.canCreate(#materialimportdetail)")
			@RequestMapping(value = "/createWithSelectDetail/{store}", method = RequestMethod.POST)
			public ResponseEntity<?> createWithSelectDetail(@RequestBody List<Materialimportdetail> materialimportdetails,@PathVariable("store")Integer idstore) throws JsonParseException, JsonMappingException, IOException {
				for (Materialimportdetail materialimportdetail : materialimportdetails) {
					materialimportdetailService.create(materialimportdetail,idstore);
				}
				// return.
				return new ResponseEntity<>(HttpStatus.OK);
			}
			
			// -------------------Update Price of Materialformsdetail-------------------------------------------
			@PreAuthorize("@materialimportdetailAuthorize.canUpdate(#materialimportdetail)")
			@RequestMapping(value = "/updatePrice", method = RequestMethod.POST)
			public ResponseEntity<?> updatePrice(@RequestBody List<Materialimportdetail> materialimportdetails) throws JsonParseException, JsonMappingException, IOException {
				for (Materialimportdetail materialimportdetail : materialimportdetails) {
					materialimportdetailService.updatePrice(materialimportdetail.getId(), materialimportdetail.getPrice());
				}
				// return.
				return new ResponseEntity<>(HttpStatus.OK);
			}
				
			// -------------------Update Price of Materialformsdetail-------------------------------------------
			@PreAuthorize("@materialimportdetailAuthorize.canUpdate(#materialimportdetail)")
			@RequestMapping(value = "/updateQuantity/{idstore}", method = RequestMethod.POST)
			public ResponseEntity<?> updateQuantity(@RequestBody List<Materialimportdetail> materialimportdetails,@PathVariable("idstore")Integer idstore) throws JsonParseException, JsonMappingException, IOException {
				for (Materialimportdetail materialimportdetail : materialimportdetails) {
					materialimportdetailService.updateQuantity(materialimportdetail.getId(), materialimportdetail.getQuantity(),idstore);
				}
				// return.
				return new ResponseEntity<>(HttpStatus.OK);
			}
}

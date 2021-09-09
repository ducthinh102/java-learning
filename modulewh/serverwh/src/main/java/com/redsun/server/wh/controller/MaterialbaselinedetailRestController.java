
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
import com.redsun.server.wh.model.Materialbaselinedetail;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.service.MaterialbaselinedetailService;

@RestController
@RequestMapping("/materialbaselinedetail")
public class MaterialbaselinedetailRestController {

	//public static final Logger logger = LoggerFactory.getLogger(MaterialbaselinedetailRestController.class);

	@Autowired
	MaterialbaselinedetailService materialbaselinedetailService; //Service which will do all data retrieval/manipulation work

	// -------------------Create a Materialbaselinedetail-------------------------------------------

	@PreAuthorize("@materialbaselinedetailAuthorize.canCreate(#materialbaselinedetail)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Materialbaselinedetail materialbaselinedetail) throws JsonParseException, JsonMappingException, IOException {
		Materialbaselinedetail result = materialbaselinedetailService.create(materialbaselinedetail);
		// return.
		return new ResponseEntity<Integer>(result.getId(), HttpStatus.OK);
	}

	// -------------------Update lock a Materialbaselinedetail------------------------------------------------

	@PreAuthorize("@materialbaselinedetailAuthorize.canUpdate(#materialbaselinedetail)")
	@RequestMapping(value = "/updateLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateLock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = materialbaselinedetailService.updateLock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update Unlock a Materialbaselinedetail------------------------------------------------

	@PreAuthorize("@materialbaselinedetailAuthorize.canUpdate(#materialbaselinedetail)")
	@RequestMapping(value = "/updateUnlock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUnlock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = materialbaselinedetailService.updateUnlock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update a Materialbaselinedetail------------------------------------------------

	@PreAuthorize("@materialbaselinedetailAuthorize.canUpdate(#materialbaselinedetail)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Materialbaselinedetail materialbaselinedetail) throws JsonParseException, JsonMappingException, IOException {
		Materialbaselinedetail result = materialbaselinedetailService.update(id, materialbaselinedetail);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update a Materialbaselinedetail With Lock------------------------------------------------

	@PreAuthorize("@materialbaselinedetailAuthorize.canUpdate(#materialbaselinedetail)")
	@RequestMapping(value = "/updateWithLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateWithLock(@PathVariable("id") Integer id, @RequestBody Materialbaselinedetail materialbaselinedetail) throws JsonParseException, JsonMappingException, IOException {
		Materialbaselinedetail result = materialbaselinedetailService.updateWithLock(id, materialbaselinedetail);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete------------------------------------------------

	@PreAuthorize("@materialbaselinedetailAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDelete/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDelete(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Materialbaselinedetail result = materialbaselinedetailService.updateForDelete(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete With Lock------------------------------------------------

	@PreAuthorize("@materialbaselinedetailAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDeleteWithLock/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDeleteWithLock(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Materialbaselinedetail result = materialbaselinedetailService.updateForDeleteWithLock(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Delete a Materialbaselinedetail-----------------------------------------

	@PreAuthorize("@materialbaselinedetailAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		materialbaselinedetailService.deleteById(id);
		// return.	
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	// -------------------Retrieve All Materialbaselinedetails---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Materialbaselinedetail>> listAll() {
		List<Materialbaselinedetail> materialbaselinedetails = materialbaselinedetailService.listAll();
		// return.
		return new ResponseEntity<List<Materialbaselinedetail>>(materialbaselinedetails, HttpStatus.OK);
	}

	// -------------------Retrieve Single Materialbaselinedetail------------------------------------------

	@PreAuthorize("@materialbaselinedetailAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		Materialbaselinedetail materialbaselinedetail = materialbaselinedetailService.getById(id);
		// return.
		return new ResponseEntity<Materialbaselinedetail>(materialbaselinedetail, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialbaselinedetails With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		List<Materialbaselinedetail> materialbaselinedetails = materialbaselinedetailService.listWithCritera(searchCriteria);
		// return.
		return new ResponseEntity<List<Materialbaselinedetail>>(materialbaselinedetails, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialbaselinedetails With Many Criteria------------------------------------------
	
	@PreAuthorize("@materialbaselinedetailAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		List<Materialbaselinedetail> materialbaselinedetails = materialbaselinedetailService.listWithCriteras(searchCriterias);
		// return.
		return new ResponseEntity<List<Materialbaselinedetail>>(materialbaselinedetails, HttpStatus.OK);
	}

	// -------------------Retrieve All Materialbaselinedetails By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Materialbaselinedetail>> listAllByPage(Pageable pageable) {
		Page<Materialbaselinedetail> materialbaselinedetails = materialbaselinedetailService.listAllByPage(pageable);
		// return.
		return new ResponseEntity<Page<Materialbaselinedetail>>(materialbaselinedetails, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialbaselinedetails With A Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		Page<Materialbaselinedetail> materialbaselinedetails = materialbaselinedetailService.listWithCriteraByPage(searchCriteria, pageable);
		// return.
		return new ResponseEntity<Page<Materialbaselinedetail>>(materialbaselinedetails, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialbaselinedetails With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@materialbaselinedetailAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		Page<Materialbaselinedetail> materialbaselinedetails = materialbaselinedetailService.listWithCriterasByPage(searchCriterias, pageable);
		// return.
		return new ResponseEntity<Page<Materialbaselinedetail>>(materialbaselinedetails, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialbaselines With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@materialbaselinedetailAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByIdmaterialbaselineAndPage/{idmaterialbaseline}", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByIdmaterialbaselineAndPage(@PathVariable("idmaterialbaseline") Integer idmaterialbaseline, @RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		Page<Map<String, Object>> materialbaselines = materialbaselinedetailService.listWithCriteriasByIdmaterialbaselineAndPage(idmaterialbaseline, searchCriterias, pageable);
		// return.
		return new ResponseEntity<Page<Map<String, Object>>>(materialbaselines, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of SumAmount------------------------------------------
	
	@RequestMapping(value = "/sumAmount/{idmaterialbaseline}", method = RequestMethod.GET)
	public ResponseEntity<?> sumAmount(@PathVariable("idmaterialbaseline") Integer idmaterialbaseline){
		Double result = materialbaselinedetailService.sumAmount(idmaterialbaseline);
		// return.
		return new ResponseEntity<Double>(result, HttpStatus.OK);
	}

	// -------------------Retrieve Multiple Materialbaselines------------------------------------------

				@PreAuthorize("@materialbaselinedetailAuthorize.canRead(#id)")
				@RequestMapping(value = "/getAllById/{id}", method = RequestMethod.GET)
				public ResponseEntity<?> getAllById(@PathVariable("id") Integer id) {
					List<Map<String, Object>> materialbaselines = materialbaselinedetailService.getAllById(id);
					// return.
					return new ResponseEntity<List<Map<String, Object>>>(materialbaselines, HttpStatus.OK);
				}
				// -------------------Create Multiple Materialbaselines-------------------------------------------

				@PreAuthorize("@materialbaselinedetailAuthorize.canCreate(#materialbaselinedetail)")
				@RequestMapping(value = "/createWithSelectDetail/", method = RequestMethod.POST)
				public ResponseEntity<?> createWithSelectDetail(@RequestBody List<Materialbaselinedetail> materialbaselinedetails) throws JsonParseException, JsonMappingException, IOException {
					for (Materialbaselinedetail materialbaselinedetail : materialbaselinedetails) {
						materialbaselinedetailService.create(materialbaselinedetail);
					}
					// return.
					return new ResponseEntity<>(HttpStatus.OK);
				}
				// -------------------Update Price of Materialbaselines-------------------------------------------
				@PreAuthorize("@materialbaselinedetailAuthorize.canUpdate(#materialbaselinedetail)")
				@RequestMapping(value = "/updatePrice", method = RequestMethod.POST)
				public ResponseEntity<?> updatePrice(@RequestBody List<Materialbaselinedetail> materialbaselinedetails) throws JsonParseException, JsonMappingException, IOException {
					for (Materialbaselinedetail materialbaselinedetail : materialbaselinedetails) {
						materialbaselinedetailService.updatePrice(materialbaselinedetail.getId(), materialbaselinedetail.getPrice());
					}
					// return.
					return new ResponseEntity<>(HttpStatus.OK);
				}
					
				// -------------------Update Price of Materialbaselines-------------------------------------------
				@PreAuthorize("@materialbaselinedetailAuthorize.canUpdate(#materialbaselinedetail)")
				@RequestMapping(value = "/updateQuantity", method = RequestMethod.POST)
				public ResponseEntity<?> updateQuantity(@RequestBody List<Materialbaselinedetail> materialbaselinedetails) throws JsonParseException, JsonMappingException, IOException {
					for (Materialbaselinedetail materialbaselinedetail : materialbaselinedetails) {
						materialbaselinedetailService.updateQuantity(materialbaselinedetail.getId(), materialbaselinedetail.getQuantity());
					}
					// return.
					return new ResponseEntity<>(HttpStatus.OK);
				}
}

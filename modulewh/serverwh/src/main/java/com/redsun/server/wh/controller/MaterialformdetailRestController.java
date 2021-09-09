
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
import com.redsun.server.wh.model.Materialformdetail;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.service.MaterialformdetailService;

@RestController
@RequestMapping("/materialformdetail")
public class MaterialformdetailRestController {

	//public static final Logger logger = LoggerFactory.getLogger(MaterialformdetailRestController.class);

	@Autowired
	MaterialformdetailService materialformdetailService; //Service which will do all data retrieval/manipulation work

	// -------------------Create a Materialformdetail-------------------------------------------

	@PreAuthorize("@materialformdetailAuthorize.canCreate(#materialformdetail)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Materialformdetail materialformdetail) throws JsonParseException, JsonMappingException, IOException {
		Materialformdetail result = materialformdetailService.create(materialformdetail);
		// return.
		return new ResponseEntity<Integer>(result.getId(), HttpStatus.OK);
	}

	// -------------------Update lock a Materialformdetail------------------------------------------------

	@PreAuthorize("@materialformdetailAuthorize.canUpdate(#materialformdetail)")
	@RequestMapping(value = "/updateLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateLock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = materialformdetailService.updateLock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update Unlock a Materialformdetail------------------------------------------------

	@PreAuthorize("@materialformdetailAuthorize.canUpdate(#materialformdetail)")
	@RequestMapping(value = "/updateUnlock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUnlock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = materialformdetailService.updateUnlock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update a Materialformdetail------------------------------------------------

	@PreAuthorize("@materialformdetailAuthorize.canUpdate(#materialformdetail)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Materialformdetail materialformdetail) throws JsonParseException, JsonMappingException, IOException {
		Materialformdetail result = materialformdetailService.update(id, materialformdetail);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update a Materialformdetail With Lock------------------------------------------------

	@PreAuthorize("@materialformdetailAuthorize.canUpdate(#materialformdetail)")
	@RequestMapping(value = "/updateWithLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateWithLock(@PathVariable("id") Integer id, @RequestBody Materialformdetail materialformdetail) throws JsonParseException, JsonMappingException, IOException {
		Materialformdetail result = materialformdetailService.updateWithLock(id, materialformdetail);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete------------------------------------------------

	@PreAuthorize("@materialformdetailAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDelete/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDelete(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Materialformdetail result = materialformdetailService.updateForDelete(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete With Lock------------------------------------------------

	@PreAuthorize("@materialformdetailAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDeleteWithLock/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDeleteWithLock(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Materialformdetail result = materialformdetailService.updateForDeleteWithLock(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Delete a Materialformdetail-----------------------------------------

	@PreAuthorize("@materialformdetailAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		materialformdetailService.deleteById(id);
		// return.	
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	// -------------------Retrieve All Materialformdetails---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Materialformdetail>> listAll() {
		List<Materialformdetail> materialformdetails = materialformdetailService.listAll();
		// return.
		return new ResponseEntity<List<Materialformdetail>>(materialformdetails, HttpStatus.OK);
	}

	// -------------------Retrieve Single Materialformdetail------------------------------------------

	@PreAuthorize("@materialformdetailAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		Materialformdetail materialformdetail = materialformdetailService.getById(id);
		// return.
		return new ResponseEntity<Materialformdetail>(materialformdetail, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialformdetails With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		List<Materialformdetail> materialformdetails = materialformdetailService.listWithCritera(searchCriteria);
		// return.
		return new ResponseEntity<List<Materialformdetail>>(materialformdetails, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialformdetails With Many Criteria------------------------------------------
	
	@PreAuthorize("@materialformdetailAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		List<Materialformdetail> materialformdetails = materialformdetailService.listWithCriteras(searchCriterias);
		// return.
		return new ResponseEntity<List<Materialformdetail>>(materialformdetails, HttpStatus.OK);
	}

	// -------------------Retrieve All Materialformdetails By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Materialformdetail>> listAllByPage(Pageable pageable) {
		Page<Materialformdetail> materialformdetails = materialformdetailService.listAllByPage(pageable);
		// return.
		return new ResponseEntity<Page<Materialformdetail>>(materialformdetails, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialformdetails With A Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		Page<Materialformdetail> materialformdetails = materialformdetailService.listWithCriteraByPage(searchCriteria, pageable);
		// return.
		return new ResponseEntity<Page<Materialformdetail>>(materialformdetails, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialformdetails With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@materialformdetailAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		Page<Materialformdetail> materialformdetails = materialformdetailService.listWithCriterasByPage(searchCriterias, pageable);
		// return.
		return new ResponseEntity<Page<Materialformdetail>>(materialformdetails, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialforms With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@materialformdetailAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByIdmaterialformAndPage/{idmaterialform}", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByIdmaterialformAndPage(@PathVariable("idmaterialform") Integer idmaterialform, @RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		Page<Map<String, Object>> materialforms = materialformdetailService.listWithCriteriasByIdmaterialformAndPage(idmaterialform, searchCriterias, pageable);
		// return.
		return new ResponseEntity<Page<Map<String, Object>>>(materialforms, HttpStatus.OK);
	}

	// -------------------Retrieve List Of SumAmount------------------------------------------
	
	@RequestMapping(value = "/sumAmount/{idmaterialform}", method = RequestMethod.GET)
	public ResponseEntity<?> sumAmount(@PathVariable("idmaterialform") Integer idmaterialform){
		Double result = materialformdetailService.sumAmount(idmaterialform);
		// return.
		return new ResponseEntity<Double>(result, HttpStatus.OK);
	}
	
	// -------------------Retrieve Multiple Materialformsdetail------------------------------------------

	@PreAuthorize("@materialformdetailAuthorize.canRead(#id)")
	@RequestMapping(value = "/getAllById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getAllById(@PathVariable("id") Integer id) {
		List<Map<String, Object>> materialforms = materialformdetailService.getAllById(id);
		// return.
		return new ResponseEntity<List<Map<String, Object>>>(materialforms, HttpStatus.OK);
	}
	// -------------------Create Multiple Materialformsdetail-------------------------------------------

	@PreAuthorize("@materialformdetailAuthorize.canCreate(#materialformdetail)")
	@RequestMapping(value = "/createWithSelectDetail/", method = RequestMethod.POST)
	public ResponseEntity<?> createWithSelectDetail(@RequestBody List<Materialformdetail> materialformdetails) throws JsonParseException, JsonMappingException, IOException {
		for (Materialformdetail materialformdetail : materialformdetails) {
			materialformdetailService.create(materialformdetail);
		}
		// return.
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	// -------------------Update Price of Materialformsdetail-------------------------------------------
	@PreAuthorize("@materialformdetailAuthorize.canUpdate(#materialformdetail)")
	@RequestMapping(value = "/updatePrice", method = RequestMethod.POST)
	public ResponseEntity<?> updatePrice(@RequestBody List<Materialformdetail> materialformdetails) throws JsonParseException, JsonMappingException, IOException {
		for (Materialformdetail materialformdetail : materialformdetails) {
			materialformdetailService.updatePrice(materialformdetail.getId(), materialformdetail.getPrice());
		}
		// return.
		return new ResponseEntity<>(HttpStatus.OK);
	}
		
	// -------------------Update Price of Materialformsdetail-------------------------------------------
	@PreAuthorize("@materialformdetailAuthorize.canUpdate(#materialformdetail)")
	@RequestMapping(value = "/updateQuantity", method = RequestMethod.POST)
	public ResponseEntity<?> updateQuantity(@RequestBody List<Materialformdetail> materialformdetails) throws JsonParseException, JsonMappingException, IOException {
		for (Materialformdetail materialformdetail : materialformdetails) {
			materialformdetailService.updateQuantity(materialformdetail.getId(), materialformdetail.getQuantity());
		}
		// return.
		return new ResponseEntity<>(HttpStatus.OK);
	}

	// -------------------Update Ref a Materialformdetail------------------------------------------------

	@PreAuthorize("@materialformdetailAuthorize.canUpdate(#materialformdetail)")
	@RequestMapping(value = "/updateRef/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateRef(@PathVariable("id") Integer id, @RequestBody Materialformdetail materialformdetail) throws JsonParseException, JsonMappingException, IOException {
		Materialformdetail result = materialformdetailService.updateRef(id, materialformdetail);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update Ref a Materialformdetail With Lock------------------------------------------------

	@PreAuthorize("@materialformdetailAuthorize.canUpdate(#materialformdetail)")
	@RequestMapping(value = "/updateRefWithLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateRefWithLock(@PathVariable("id") Integer id, @RequestBody Materialformdetail materialformdetail) throws JsonParseException, JsonMappingException, IOException {
		Materialformdetail result = materialformdetailService.updateRefWithLock(id, materialformdetail);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}
	
}

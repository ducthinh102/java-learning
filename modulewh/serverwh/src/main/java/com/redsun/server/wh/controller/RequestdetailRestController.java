
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
import com.redsun.server.wh.model.Requestdetail;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.service.RequestdetailService;

@RestController
@RequestMapping("/requestdetail")
public class RequestdetailRestController {

	//public static final Logger logger = LoggerFactory.getLogger(RequestdetailRestController.class);

	@Autowired
	RequestdetailService requestdetailService; //Service which will do all data retrieval/manipulation work

	// -------------------Create a Requestdetail-------------------------------------------

	@PreAuthorize("@requestdetailAuthorize.canCreate(#requestdetail)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Requestdetail requestdetail) throws JsonParseException, JsonMappingException, IOException {
		Requestdetail result = requestdetailService.create(requestdetail);
		// return.
		return new ResponseEntity<Integer>(result.getId(), HttpStatus.OK);
	}

	// -------------------Update lock a Requestdetail------------------------------------------------

	@PreAuthorize("@requestdetailAuthorize.canUpdate(#requestdetail)")
	@RequestMapping(value = "/updateLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateLock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = requestdetailService.updateLock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update Unlock a Requestdetail------------------------------------------------

	@PreAuthorize("@requestdetailAuthorize.canUpdate(#requestdetail)")
	@RequestMapping(value = "/updateUnlock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUnlock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = requestdetailService.updateUnlock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update a Requestdetail------------------------------------------------

	@PreAuthorize("@requestdetailAuthorize.canUpdate(#requestdetail)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Requestdetail requestdetail) throws JsonParseException, JsonMappingException, IOException {
		Requestdetail result = requestdetailService.update(id, requestdetail);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update a Requestdetail With Lock------------------------------------------------

	@PreAuthorize("@requestdetailAuthorize.canUpdate(#requestdetail)")
	@RequestMapping(value = "/updateWithLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateWithLock(@PathVariable("id") Integer id, @RequestBody Requestdetail requestdetail) throws JsonParseException, JsonMappingException, IOException {
		Requestdetail result = requestdetailService.updateWithLock(id, requestdetail);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete------------------------------------------------

	@PreAuthorize("@requestdetailAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDelete/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDelete(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Requestdetail result = requestdetailService.updateForDelete(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete With Lock------------------------------------------------

	@PreAuthorize("@requestdetailAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDeleteWithLock/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDeleteWithLock(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Requestdetail result = requestdetailService.updateForDeleteWithLock(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Delete a Requestdetail-----------------------------------------

	@PreAuthorize("@requestdetailAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		requestdetailService.deleteById(id);
		// return.	
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	// -------------------Retrieve All Requestdetails---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Requestdetail>> listAll() {
		List<Requestdetail> requestdetails = requestdetailService.listAll();
		// return.
		return new ResponseEntity<List<Requestdetail>>(requestdetails, HttpStatus.OK);
	}

	// -------------------Retrieve Single Requestdetail------------------------------------------

	@PreAuthorize("@requestdetailAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		Requestdetail requestdetail = requestdetailService.getById(id);
		// return.
		return new ResponseEntity<Requestdetail>(requestdetail, HttpStatus.OK);
	}
	// -------------------Retrieve Single Material------------------------------------------

			@PreAuthorize("@requestdetailAuthorize.canRead(#id)")
			@RequestMapping(value = "/getByIdForView/{id}", method = RequestMethod.GET)
			public ResponseEntity<?> getByIdForView(@PathVariable("id") Integer id) {
				Map<String, Object> requestdetail = requestdetailService.getByIdForView(id);
				// return.
				return new ResponseEntity<Map<String, Object>>(requestdetail, HttpStatus.OK);
			}
	
	// -------------------Retrieve List Of Requestdetails With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		List<Requestdetail> requestdetails = requestdetailService.listWithCritera(searchCriteria);
		// return.
		return new ResponseEntity<List<Requestdetail>>(requestdetails, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Requestdetails With Many Criteria------------------------------------------
	
	@PreAuthorize("@requestdetailAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		List<Requestdetail> requestdetails = requestdetailService.listWithCriteras(searchCriterias);
		// return.
		return new ResponseEntity<List<Requestdetail>>(requestdetails, HttpStatus.OK);
	}

	// -------------------Retrieve All Requestdetails By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Requestdetail>> listAllByPage(Pageable pageable) {
		Page<Requestdetail> requestdetails = requestdetailService.listAllByPage(pageable);
		// return.
		return new ResponseEntity<Page<Requestdetail>>(requestdetails, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Requestdetails With A Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		Page<Requestdetail> requestdetails = requestdetailService.listWithCriteraByPage(searchCriteria, pageable);
		// return.
		return new ResponseEntity<Page<Requestdetail>>(requestdetails, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Requestdetails With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@requestdetailAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		Page<Requestdetail> requestdetails = requestdetailService.listWithCriterasByPage(searchCriterias, pageable);
		// return.
		return new ResponseEntity<Page<Requestdetail>>(requestdetails, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Requests With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@requestdetailAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByIdrequestAndPage/{idrequest}", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByIdrequestAndPage(@PathVariable("idrequest") Integer idrequest, @RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		Page<Map<String, Object>> requests = requestdetailService.listWithCriteriasByIdrequestAndPage(idrequest, searchCriterias, pageable);
		// return.
		return new ResponseEntity<Page<Map<String, Object>>>(requests, HttpStatus.OK);
	}
	
	// -------------------Retrieve Multiple Requestdetail------------------------------------------

	@PreAuthorize("@requestdetailAuthorize.canRead(#id)")
	@RequestMapping(value = "/getAllById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getAllById(@PathVariable("id") Integer id) {
		List<Map<String, Object>> requestdetails = requestdetailService.getAllById(id);
		// return.
		return new ResponseEntity<List<Map<String, Object>>>(requestdetails, HttpStatus.OK);
	}
	
	// -------------------Create Requestdetails-------------------------------------------

	@PreAuthorize("@requestdetailAuthorize.canCreate(#requestdetail)")
	@RequestMapping(value = "/createWithSelectDetail", method = RequestMethod.POST)
	public ResponseEntity<?> createWithSelectDetail(@RequestBody List<Requestdetail> requestdetails) throws JsonParseException, JsonMappingException, IOException {
		for (Requestdetail requestdetail : requestdetails) {
			requestdetailService.create(requestdetail);
		}
		// return.
		return new ResponseEntity<>(HttpStatus.OK);
	}
		// -------------------Update Price of Materialexportdetail-------------------------------------------
		@PreAuthorize("@requestdetailAuthorize.canUpdate(#requestdetail)")
		@RequestMapping(value = "/updateQuantity", method = RequestMethod.POST)
		public ResponseEntity<?> updateQuantity(@RequestBody List<Requestdetail> requestdetails) throws JsonParseException, JsonMappingException, IOException {
			for (Requestdetail requestdetail : requestdetails) {
				requestdetailService.updateQuantity(requestdetail.getId(), requestdetail.getQuantity());
			}
			// return.
			return new ResponseEntity<>(HttpStatus.OK);
		}
}

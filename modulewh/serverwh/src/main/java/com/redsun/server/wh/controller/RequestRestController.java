
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
import com.redsun.server.wh.model.Request;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.service.RequestService;

@RestController
@RequestMapping("/request")
public class RequestRestController {

	//public static final Logger logger = LoggerFactory.getLogger(RequestRestController.class);

	@Autowired
	RequestService requestService; //Service which will do all data retrieval/manipulation work

	// -------------------Create a Request-------------------------------------------

	@PreAuthorize("@requestAuthorize.canCreate(#request)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Request request) throws JsonParseException, JsonMappingException, IOException {
		Request result = requestService.create(request);
		// return.
		return new ResponseEntity<Integer>(result.getId(), HttpStatus.OK);
	}

	// -------------------Update lock a Request------------------------------------------------

	@PreAuthorize("@requestAuthorize.canUpdate(#request)")
	@RequestMapping(value = "/updateLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateLock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = requestService.updateLock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update Unlock a Request------------------------------------------------

	@PreAuthorize("@requestAuthorize.canUpdate(#request)")
	@RequestMapping(value = "/updateUnlock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUnlock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = requestService.updateUnlock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update a Request------------------------------------------------

	@PreAuthorize("@requestAuthorize.canUpdate(#request)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Request request) throws JsonParseException, JsonMappingException, IOException {
		Request result = requestService.update(id, request);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update a Request With Lock------------------------------------------------

	@PreAuthorize("@requestAuthorize.canUpdate(#request)")
	@RequestMapping(value = "/updateWithLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateWithLock(@PathVariable("id") Integer id, @RequestBody Request request) throws JsonParseException, JsonMappingException, IOException {
		Request result = requestService.updateWithLock(id, request);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete------------------------------------------------

	@PreAuthorize("@requestAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDelete/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDelete(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Request result = requestService.updateForDelete(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete With Lock------------------------------------------------

	@PreAuthorize("@requestAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDeleteWithLock/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDeleteWithLock(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Request result = requestService.updateForDeleteWithLock(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Delete a Request-----------------------------------------

	@PreAuthorize("@requestAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		requestService.deleteById(id);
		// return.	
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	// -------------------Retrieve All Requests---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Request>> listAll() {
		List<Request> requests = requestService.listAll();
		// return.
		return new ResponseEntity<List<Request>>(requests, HttpStatus.OK);
	}

	// -------------------Retrieve Single Request------------------------------------------

	@PreAuthorize("@requestAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		Request request = requestService.getById(id);
		// return.
		return new ResponseEntity<Request>(request, HttpStatus.OK);
	}
	
	// -------------------Retrieve Single Material------------------------------------------

		@PreAuthorize("@requestAuthorize.canRead(#id)")
		@RequestMapping(value = "/getByIdForView/{id}", method = RequestMethod.GET)
		public ResponseEntity<?> getByIdForView(@PathVariable("id") Integer id) {
			Map<String, Object> request = requestService.getByIdForView(id);
			// return.
			return new ResponseEntity<Map<String, Object>>(request, HttpStatus.OK);
		}
	
	// -------------------Retrieve List Of Requests With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		List<Request> requests = requestService.listWithCritera(searchCriteria);
		// return.
		return new ResponseEntity<List<Request>>(requests, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Requests With Many Criteria------------------------------------------
	
	@PreAuthorize("@requestAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		List<Request> requests = requestService.listWithCriteras(searchCriterias);
		// return.
		return new ResponseEntity<List<Request>>(requests, HttpStatus.OK);
	}

	// -------------------Retrieve All Requests By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Request>> listAllByPage(Pageable pageable) {
		Page<Request> requests = requestService.listAllByPage(pageable);
		// return.
		return new ResponseEntity<Page<Request>>(requests, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Requests With A Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		Page<Request> requests = requestService.listWithCriteraByPage(searchCriteria, pageable);
		// return.
		return new ResponseEntity<Page<Request>>(requests, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Requests With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@requestAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<Page<Map<String, Object>>> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) throws JsonParseException, JsonMappingException, IOException {
		Page<Map<String, Object>> requests = requestService.listWithCriterasByPage(searchCriterias, pageable);
		// return.
		return new ResponseEntity<Page<Map<String, Object>>>(requests, HttpStatus.OK);
	}
	
	// -------------------------Retrieve All User For Select--------------------------------------------------- 
	@RequestMapping(value = "/listAllForSelect", method = RequestMethod.GET)
	public ResponseEntity<List<Map<String, Object>>> listAllForSelect() {
		List<Map<String, Object>> requests = requestService.listAllForSelect();
		// return.
		return new ResponseEntity<List<Map<String, Object>>>(requests, HttpStatus.OK);
	}

}

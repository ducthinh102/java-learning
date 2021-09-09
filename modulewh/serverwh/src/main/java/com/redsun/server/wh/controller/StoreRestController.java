
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
import com.redsun.server.wh.model.Store;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.service.StoreService;

@RestController
@RequestMapping("/store")
public class StoreRestController {

	@Autowired
	StoreService storeService; //Service which will do all data retrieval/manipulation work

	// -------------------Create a Store-------------------------------------------

	@PreAuthorize("@storeAuthorize.canCreate(#store)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Store store) throws JsonParseException, JsonMappingException, IOException {
		Store result = storeService.create(store);
		// return.
		return new ResponseEntity<Integer>(result.getId(), HttpStatus.OK);
	}

	// -------------------Update lock a Store------------------------------------------------

	@PreAuthorize("@storeAuthorize.canUpdate(#store)")
	@RequestMapping(value = "/updateLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateLock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = storeService.updateLock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update Unlock a Store------------------------------------------------

	@PreAuthorize("@storeAuthorize.canUpdate(#store)")
	@RequestMapping(value = "/updateUnlock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUnlock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = storeService.updateUnlock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}
	
	// -------------------Update a Store With Lock------------------------------------------------

	@PreAuthorize("@storeAuthorize.canUpdate(#store)")
	@RequestMapping(value = "/updateWithLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateWithLock(@PathVariable("id") Integer id, @RequestBody Store store) throws JsonParseException, JsonMappingException, IOException {
		Store result = storeService.updateWithLock(id, store);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete------------------------------------------------

	@PreAuthorize("@storeAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDelete/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDelete(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Store result = storeService.updateForDelete(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete With Lock------------------------------------------------

	@PreAuthorize("@storeAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDeleteWithLock/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDeleteWithLock(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Store result = storeService.updateForDeleteWithLock(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}
	
	// -------------------Update a Store------------------------------------------------

	@PreAuthorize("@storeAuthorize.canUpdate(#store)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Store store) throws JsonParseException, JsonMappingException, IOException {
		Store result = storeService.update(id, store);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Delete a Store-----------------------------------------

	@PreAuthorize("@storeAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		storeService.deleteById(id);
		// return.	
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	// -------------------Retrieve All Stores---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Store>> listAll() {
		List<Store> stores = storeService.listAll();
		// return.
		return new ResponseEntity<List<Store>>(stores, HttpStatus.OK);
	}

	// -------------------Retrieve Single Store------------------------------------------

	@PreAuthorize("@storeAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		Store store = storeService.getById(id);
		// return.
		return new ResponseEntity<Store>(store, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Stores With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		List<Store> stores = storeService.listWithCritera(searchCriteria);
		// return.
		return new ResponseEntity<List<Store>>(stores, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Stores With Many Criteria------------------------------------------
	
	@PreAuthorize("@storeAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		List<Store> stores = storeService.listWithCriteras(searchCriterias);
		// return.
		return new ResponseEntity<List<Store>>(stores, HttpStatus.OK);
	}

	// -------------------Retrieve All Stores By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Store>> listAllByPage(Pageable pageable) {
		Page<Store> stores = storeService.listAllByPage(pageable);
		// return.
		return new ResponseEntity<Page<Store>>(stores, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Stores With A Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		Page<Store> stores = storeService.listWithCriteraByPage(searchCriteria, pageable);
		// return.
		return new ResponseEntity<Page<Store>>(stores, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Stores With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@storeAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		Page<Map<String, Object>> stores = storeService.listWithCriterasByPage(searchCriterias, pageable);
		// return.
		return new  ResponseEntity<Page<Map<String, Object>>>(stores, HttpStatus.OK);
	}
	
	// -------------------------Retrieve All User For Select--------------------------------------------------- 
		@RequestMapping(value = "/listAllForSelect", method = RequestMethod.GET)
		public ResponseEntity<List<Map<String, Object>>> listAllForSelect() {
			List<Map<String, Object>> stores = storeService.listAllForSelect();
			// return.
			return new ResponseEntity<List<Map<String, Object>>>(stores, HttpStatus.OK);
		}
}

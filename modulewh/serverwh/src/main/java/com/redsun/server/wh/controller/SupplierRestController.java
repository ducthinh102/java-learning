
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
import com.redsun.server.wh.model.Supplier;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.service.SupplierService;

@RestController
@RequestMapping("/supplier")
public class SupplierRestController {

	//public static final Logger logger = LoggerFactory.getLogger(SupplierRestController.class);
	
	@Autowired
	SupplierService supplierService; //Service which will do all data retrieval/manipulation work

	// -------------------Create a Supplier-------------------------------------------

	@PreAuthorize("@supplierAuthorize.canCreate(#supplier)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Supplier supplier) throws JsonParseException, JsonMappingException, IOException {
		Supplier result = supplierService.create(supplier);
		// return.
		return new ResponseEntity<Integer>(result.getId(), HttpStatus.OK);
	}

	// -------------------Update lock a Supplier------------------------------------------------

	@PreAuthorize("@supplierAuthorize.canUpdate(#supplier)")
	@RequestMapping(value = "/updateLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateLock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = supplierService.updateLock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update Unlock a Supplier------------------------------------------------

	@PreAuthorize("@supplierAuthorize.canUpdate(#supplier)")
	@RequestMapping(value = "/updateUnlock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUnlock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = supplierService.updateUnlock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update a Supplier------------------------------------------------

	@PreAuthorize("@supplierAuthorize.canUpdate(#supplier)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Supplier supplier) throws JsonParseException, JsonMappingException, IOException {
		Supplier result = supplierService.update(id, supplier);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update a Supplier With Lock------------------------------------------------

	@PreAuthorize("@supplierAuthorize.canUpdate(#supplier)")
	@RequestMapping(value = "/updateWithLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateWithLock(@PathVariable("id") Integer id, @RequestBody Supplier supplier) throws JsonParseException, JsonMappingException, IOException {
		Supplier result = supplierService.updateWithLock(id, supplier);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete------------------------------------------------

	@PreAuthorize("@supplierAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDelete/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDelete(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Supplier result = supplierService.updateForDelete(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete With Lock------------------------------------------------

	@PreAuthorize("@supplierAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDeleteWithLock/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDeleteWithLock(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Supplier result = supplierService.updateForDeleteWithLock(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Delete a Supplier-----------------------------------------

	@PreAuthorize("@supplierAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		supplierService.deleteById(id);
		// return.	
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	// -------------------Retrieve All Suppliers---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Supplier>> listAll() {
		List<Supplier> suppliers = supplierService.listAll();
		// return.
		return new ResponseEntity<List<Supplier>>(suppliers, HttpStatus.OK);
	}

	// -------------------Retrieve Single Supplier------------------------------------------

	@PreAuthorize("@supplierAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		Supplier supplier = supplierService.getById(id);
		// return.
		return new ResponseEntity<Supplier>(supplier, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Suppliers With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		List<Supplier> suppliers = supplierService.listWithCritera(searchCriteria);
		// return.
		return new ResponseEntity<List<Supplier>>(suppliers, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Suppliers With Many Criteria------------------------------------------
	
	@PreAuthorize("@supplierAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		List<Supplier> suppliers = supplierService.listWithCriteras(searchCriterias);
		// return.
		return new ResponseEntity<List<Supplier>>(suppliers, HttpStatus.OK);
	}

	// -------------------Retrieve All Suppliers By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Supplier>> listAllByPage(Pageable pageable) {
		Page<Supplier> suppliers = supplierService.listAllByPage(pageable);
		// return.
		return new ResponseEntity<Page<Supplier>>(suppliers, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Suppliers With A Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		Page<Supplier> suppliers = supplierService.listWithCriteraByPage(searchCriteria, pageable);
		// return.
		return new ResponseEntity<Page<Supplier>>(suppliers, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Suppliers With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@supplierAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		Page<Supplier> suppliers = supplierService.listWithCriterasByPage(searchCriterias, pageable);
		if (!suppliers.hasContent()) {
			// return.
			return new ResponseEntity<>(HttpStatus.OK);
		}
		// return.
		return new ResponseEntity<Page<Supplier>>(suppliers, HttpStatus.OK);
	}

	// -------------------------Retrieve All Supplier For Select--------------------------------------------------- 
	
		@RequestMapping(value = "/listAllForSelect", method = RequestMethod.GET)
		public ResponseEntity<List<Map<String, Object>>> listAllForSelect() {
			List<Map<String, Object>> suppliers = supplierService.listAllForSelect();
			// return.
			return new ResponseEntity<List<Map<String, Object>>>(suppliers, HttpStatus.OK);
		}
	
}

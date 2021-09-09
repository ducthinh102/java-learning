
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
import com.redsun.server.wh.model.Purchase;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.service.PurchaseService;

@RestController
@RequestMapping("/purchase")
public class PurchaseRestController {

	//public static final Logger logger = LoggerFactory.getLogger(PurchaseRestController.class);

	@Autowired
	PurchaseService purchaseService; //Service which will do all data retrieval/manipulation work

	// -------------------Create a Purchase-------------------------------------------

	@PreAuthorize("@purchaseAuthorize.canCreate(#purchase)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Purchase purchase) throws JsonParseException, JsonMappingException, IOException {
		Purchase result = purchaseService.create(purchase);
		// return.
		return new ResponseEntity<Integer>(result.getId(), HttpStatus.OK);
	}

	// -------------------Update lock a Purchase------------------------------------------------

	@PreAuthorize("@purchaseAuthorize.canUpdate(#purchase)")
	@RequestMapping(value = "/updateLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateLock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = purchaseService.updateLock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update Unlock a Purchase------------------------------------------------

	@PreAuthorize("@purchaseAuthorize.canUpdate(#purchase)")
	@RequestMapping(value = "/updateUnlock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUnlock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = purchaseService.updateUnlock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update a Purchase------------------------------------------------

	@PreAuthorize("@purchaseAuthorize.canUpdate(#purchase)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Purchase purchase) throws JsonParseException, JsonMappingException, IOException {
		Purchase result = purchaseService.update(id, purchase);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update a Purchase With Lock------------------------------------------------

	@PreAuthorize("@purchaseAuthorize.canUpdate(#purchase)")
	@RequestMapping(value = "/updateWithLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateWithLock(@PathVariable("id") Integer id, @RequestBody Purchase purchase) throws JsonParseException, JsonMappingException, IOException {
		Purchase result = purchaseService.updateWithLock(id, purchase);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete------------------------------------------------

	@PreAuthorize("@purchaseAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDelete/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDelete(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Purchase result = purchaseService.updateForDelete(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete With Lock------------------------------------------------

	@PreAuthorize("@purchaseAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDeleteWithLock/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDeleteWithLock(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Purchase result = purchaseService.updateForDeleteWithLock(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Delete a Purchase-----------------------------------------

	@PreAuthorize("@purchaseAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		purchaseService.deleteById(id);
		// return.	
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	// -------------------Retrieve All Purchases---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Purchase>> listAll() {
		List<Purchase> purchases = purchaseService.listAll();
		// return.
		return new ResponseEntity<List<Purchase>>(purchases, HttpStatus.OK);
	}

	// -------------------Retrieve Single Purchase------------------------------------------

	@PreAuthorize("@purchaseAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		Purchase purchase = purchaseService.getById(id);
		// return.
		return new ResponseEntity<Purchase>(purchase, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Purchases With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		List<Purchase> purchases = purchaseService.listWithCritera(searchCriteria);
		// return.
		return new ResponseEntity<List<Purchase>>(purchases, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Purchases With Many Criteria------------------------------------------
	
	@PreAuthorize("@purchaseAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		List<Purchase> purchases = purchaseService.listWithCriteras(searchCriterias);
		// return.
		return new ResponseEntity<List<Purchase>>(purchases, HttpStatus.OK);
	}

	// -------------------Retrieve All Purchases By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Purchase>> listAllByPage(Pageable pageable) {
		Page<Purchase> purchases = purchaseService.listAllByPage(pageable);
		// return.
		return new ResponseEntity<Page<Purchase>>(purchases, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Purchases With A Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		Page<Purchase> purchases = purchaseService.listWithCriteraByPage(searchCriteria, pageable);
		// return.
		return new ResponseEntity<Page<Purchase>>(purchases, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Purchases With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@purchaseAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		Page<Map<String, Object>> purchases = purchaseService.listWithCriterasByPage(searchCriterias, pageable);
		// return.
		return new ResponseEntity<Page<Map<String, Object>>>(purchases, HttpStatus.OK);
	}
	
	// -------------------------Retrieve All User For Select--------------------------------------------------- 
		@RequestMapping(value = "/listAllForSelect", method = RequestMethod.GET)
		public ResponseEntity<List<Map<String, Object>>> listAllForSelect() {
			List<Map<String, Object>> purchases = purchaseService.listAllForSelect();
			// return.
			return new ResponseEntity<List<Map<String, Object>>>(purchases, HttpStatus.OK);
		}

}

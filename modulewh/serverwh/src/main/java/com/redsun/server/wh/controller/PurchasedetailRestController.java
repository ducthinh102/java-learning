
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
import com.redsun.server.wh.model.Purchasedetail;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.service.PurchasedetailService;

@RestController
@RequestMapping("/purchasedetail")
public class PurchasedetailRestController {

	//public static final Logger logger = LoggerFactory.getLogger(PurchasedetailRestController.class);

	@Autowired
	PurchasedetailService purchasedetailService; //Service which will do all data retrieval/manipulation work

	// -------------------Create a Purchasedetail-------------------------------------------

	@PreAuthorize("@purchasedetailAuthorize.canCreate(#purchasedetail)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Purchasedetail purchasedetail) throws JsonParseException, JsonMappingException, IOException {
		/*Map<String, Object> purchasedetailQuantity = purchasedetailService.quantityMaterial(purchasedetail.getIdmaterial());
		if (purchasedetailQuantity.isEmpty()) {*/
			Purchasedetail result = purchasedetailService.create(purchasedetail);
		// return.
		return new ResponseEntity<Integer>(result.getId(), HttpStatus.OK);
		/*}
		else {
			Integer id = (Integer)purchasedetailQuantity.get("id");
			Integer version = (Integer)purchasedetailQuantity.get("version");
			purchasedetail.setId(id);
			purchasedetail.setVersion(version);
			purchasedetailService.updateWithLock(id, purchasedetail);
			// return when material exit in material
			return new ResponseEntity<Integer>(-1, HttpStatus.OK);
		}*/	
	}

	// -------------------Update lock a Purchasedetail------------------------------------------------

	@PreAuthorize("@purchasedetailAuthorize.canUpdate(#purchasedetail)")
	@RequestMapping(value = "/updateLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateLock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = purchasedetailService.updateLock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update Unlock a Purchasedetail------------------------------------------------

	@PreAuthorize("@purchasedetailAuthorize.canUpdate(#purchasedetail)")
	@RequestMapping(value = "/updateUnlock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUnlock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = purchasedetailService.updateUnlock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update a Purchasedetail------------------------------------------------

	@PreAuthorize("@purchasedetailAuthorize.canUpdate(#purchasedetail)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Purchasedetail purchasedetail) throws JsonParseException, JsonMappingException, IOException {
		Purchasedetail result = purchasedetailService.update(id, purchasedetail);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update a Purchasedetail With Lock------------------------------------------------

	@PreAuthorize("@purchasedetailAuthorize.canUpdate(#purchasedetail)")
	@RequestMapping(value = "/updateWithLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateWithLock(@PathVariable("id") Integer id, @RequestBody Purchasedetail purchasedetail) throws JsonParseException, JsonMappingException, IOException {
		Purchasedetail result = purchasedetailService.updateWithLock(id, purchasedetail);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete------------------------------------------------

	@PreAuthorize("@purchasedetailAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDelete/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDelete(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Purchasedetail result = purchasedetailService.updateForDelete(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete With Lock------------------------------------------------

	@PreAuthorize("@purchasedetailAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDeleteWithLock/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDeleteWithLock(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Purchasedetail result = purchasedetailService.updateForDeleteWithLock(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Delete a Purchasedetail-----------------------------------------

	@PreAuthorize("@purchasedetailAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		purchasedetailService.deleteById(id);
		// return.	
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	// -------------------Retrieve All Purchasedetails---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Purchasedetail>> listAll() {
		List<Purchasedetail> purchasedetails = purchasedetailService.listAll();
		// return.
		return new ResponseEntity<List<Purchasedetail>>(purchasedetails, HttpStatus.OK);
	}

	// -------------------Retrieve Single Purchasedetail------------------------------------------

	@PreAuthorize("@purchasedetailAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		Purchasedetail purchasedetail = purchasedetailService.getById(id);
		// return.
		return new ResponseEntity<Purchasedetail>(purchasedetail, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Purchasedetails With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		List<Purchasedetail> purchasedetails = purchasedetailService.listWithCritera(searchCriteria);
		// return.
		return new ResponseEntity<List<Purchasedetail>>(purchasedetails, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Purchasedetails With Many Criteria------------------------------------------
	
	@PreAuthorize("@purchasedetailAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		List<Purchasedetail> purchasedetails = purchasedetailService.listWithCriteras(searchCriterias);
		// return.
		return new ResponseEntity<List<Purchasedetail>>(purchasedetails, HttpStatus.OK);
	}

	// -------------------Retrieve All Purchasedetails By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Purchasedetail>> listAllByPage(Pageable pageable) {
		Page<Purchasedetail> purchasedetails = purchasedetailService.listAllByPage(pageable);
		// return.
		return new ResponseEntity<Page<Purchasedetail>>(purchasedetails, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Purchasedetails With A Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		Page<Purchasedetail> purchasedetails = purchasedetailService.listWithCriteraByPage(searchCriteria, pageable);
		// return.
		return new ResponseEntity<Page<Purchasedetail>>(purchasedetails, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Purchasedetails With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@purchasedetailAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		Page<Purchasedetail> purchasedetails = purchasedetailService.listWithCriterasByPage(searchCriterias, pageable);
		// return.
		return new ResponseEntity<Page<Purchasedetail>>(purchasedetails, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Purchases With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@purchasedetailAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByIdpurchaseAndPage/{idpurchase}", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByIdpurchaseAndPage(@PathVariable("idpurchase") Integer idpurchase, @RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		Page<Map<String, Object>> purchases = purchasedetailService.listWithCriteriasByIdpurchaseAndPage(idpurchase, searchCriterias, pageable);
		// return.
		return new ResponseEntity<Page<Map<String, Object>>>(purchases, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of SumAmount------------------------------------------
	
	@RequestMapping(value = "/sumAmount/{idpurchase}", method = RequestMethod.GET)
	public ResponseEntity<?> sumAmount(@PathVariable("idpurchase") Integer idpurchase){
		Double result = purchasedetailService.sumAmount(idpurchase);
		// return.
		return new ResponseEntity<Double>(result, HttpStatus.OK);
	}
	
	// -------------------Retrieve Multiple Purchasedetail------------------------------------------

	@PreAuthorize("@purchasedetailAuthorize.canRead(#id)")
	@RequestMapping(value = "/getAllById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getAllById(@PathVariable("id") Integer id) {
		List<Map<String, Object>> purchasedetails = purchasedetailService.getAllById(id);
		// return.
		return new ResponseEntity<List<Map<String, Object>>>(purchasedetails, HttpStatus.OK);
	}
	// -------------------Create Requestdetails-------------------------------------------

	@PreAuthorize("@purchasedetailAuthorize.canCreate(#purchasedetail)")
	@RequestMapping(value = "/createWithSelectDetail", method = RequestMethod.POST)
	public ResponseEntity<?> createWithSelectDetail(@RequestBody List<Purchasedetail> purchasedetails) throws JsonParseException, JsonMappingException, IOException {
		for (Purchasedetail purchasedetail : purchasedetails) {
			purchasedetailService.create(purchasedetail);
		}
		// return.
		return new ResponseEntity<>(HttpStatus.OK);
	}
	// -------------------Update Price of Materialexportdetail-------------------------------------------
	@PreAuthorize("@purchasedetailAuthorize.canUpdate(#purchasedetail)")
	@RequestMapping(value = "/updatePrice", method = RequestMethod.POST)
	public ResponseEntity<?> updatePrice(@RequestBody List<Purchasedetail> purchasedetails) throws JsonParseException, JsonMappingException, IOException {
		for (Purchasedetail purchasedetail : purchasedetails) {
			purchasedetailService.updatePrice(purchasedetail.getId(), purchasedetail.getPrice());
		}
		// return.
		return new ResponseEntity<>(HttpStatus.OK);
	}
		
	// -------------------Update Price of Materialexportdetail-------------------------------------------
	@PreAuthorize("@purchasedetailAuthorize.canUpdate(#purchasedetail)")
	@RequestMapping(value = "/updateQuantity", method = RequestMethod.POST)
	public ResponseEntity<?> updateQuantity(@RequestBody List<Purchasedetail> purchasedetails) throws JsonParseException, JsonMappingException, IOException {
		for (Purchasedetail purchasedetail : purchasedetails) {
			purchasedetailService.updateQuantity(purchasedetail.getId(), purchasedetail.getQuantity());
		}
		// return.
		return new ResponseEntity<>(HttpStatus.OK);
	}
}

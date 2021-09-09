
package com.redsun.server.wh.controller;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
import com.redsun.server.wh.model.Quotationdetail;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.service.QuotationdetailService;

@RestController
@RequestMapping("/quotationdetail")
public class QuotationdetailRestController {

	// public static final Logger logger =
	// LoggerFactory.getLogger(QuotationdetailRestController.class);
	
	@Autowired
	private Environment env;

	@Autowired
	QuotationdetailService quotationdetailService; // Service which will do all
													// data
													// retrieval/manipulation
													// work

	// -------------------Create a
	// Quotationdetail-------------------------------------------

	@PreAuthorize("@quotationdetailAuthorize.canCreate(#quotationdetail)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Quotationdetail quotationdetail)
			throws JsonParseException, JsonMappingException, IOException {
		Quotationdetail result = quotationdetailService.create(quotationdetail);
		// return.
		return new ResponseEntity<Integer>(result.getId(), HttpStatus.OK);
	}

	// -------------------Update lock a
	// Quotationdetail------------------------------------------------

	@PreAuthorize("@quotationdetailAuthorize.canUpdate(#quotationdetail)")
	@RequestMapping(value = "/updateLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateLock(@PathVariable("id") Integer id)
			throws JsonParseException, JsonMappingException, IOException {
		Integer result = quotationdetailService.updateLock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update Unlock a
	// Quotationdetail------------------------------------------------

	@PreAuthorize("@quotationdetailAuthorize.canUpdate(#quotationdetail)")
	@RequestMapping(value = "/updateUnlock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUnlock(@PathVariable("id") Integer id)
			throws JsonParseException, JsonMappingException, IOException {
		Integer result = quotationdetailService.updateUnlock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update a
	// Quotationdetail------------------------------------------------

	@PreAuthorize("@quotationdetailAuthorize.canUpdate(#quotationdetail)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Quotationdetail quotationdetail)
			throws JsonParseException, JsonMappingException, IOException {
		Quotationdetail result = quotationdetailService.update(id, quotationdetail);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update a Quotationdetail With
	// Lock------------------------------------------------

	@PreAuthorize("@quotationdetailAuthorize.canUpdate(#quotationdetail)")
	@RequestMapping(value = "/updateWithLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateWithLock(@PathVariable("id") Integer id,
			@RequestBody Quotationdetail quotationdetail) throws JsonParseException, JsonMappingException, IOException {
		Quotationdetail result = quotationdetailService.updateWithLock(id, quotationdetail);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For
	// Delete------------------------------------------------

	@PreAuthorize("@quotationdetailAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDelete/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDelete(@PathVariable("id") Integer id, @PathVariable("version") Integer version)
			throws JsonParseException, JsonMappingException, IOException {
		Quotationdetail result = quotationdetailService.updateForDelete(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete With
	// Lock------------------------------------------------

	@PreAuthorize("@quotationdetailAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDeleteWithLock/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDeleteWithLock(@PathVariable("id") Integer id,
			@PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Quotationdetail result = quotationdetailService.updateForDeleteWithLock(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Delete a
	// Quotationdetail-----------------------------------------

	@PreAuthorize("@quotationdetailAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		quotationdetailService.deleteById(id);
		// return.
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	// -------------------Retrieve All
	// Quotationdetails---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Quotationdetail>> listAll() {
		List<Quotationdetail> quotationdetails = quotationdetailService.listAll();
		// return.
		return new ResponseEntity<List<Quotationdetail>>(quotationdetails, HttpStatus.OK);
	}

	// -------------------Retrieve Single
	// Quotationdetail------------------------------------------

	@PreAuthorize("@quotationdetailAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		Quotationdetail quotationdetail = quotationdetailService.getById(id);
		// return.
		return new ResponseEntity<Quotationdetail>(quotationdetail, HttpStatus.OK);
	}

	// -------------------Retrieve Multiple
	// Quotationdetail------------------------------------------

	//@PreAuthorize("@quotationdetailAuthorize.canRead(#id)")
	@RequestMapping(value = "/getAllById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getAllById(@PathVariable("id") Integer id) {
		List<Map<String, Object>> quotationdetails = quotationdetailService.getAllById(id);
		// return.
		return new ResponseEntity<List<Map<String, Object>>>(quotationdetails, HttpStatus.OK);
	}

	// -------------------Retrieve Single Quotationdetail For
	// View------------------------------------------

	@PreAuthorize("@quotationdetailAuthorize.canRead(#id)")
	@RequestMapping(value = "/getByIdForView/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getByIdForView(@PathVariable("id") Integer id) {
		Map<String, Object> quotationdetail = quotationdetailService.getByIdForView(id);
		// return.
		return new ResponseEntity<Map<String, Object>>(quotationdetail, HttpStatus.OK);
	}

	// -------------------Retrieve List Of Quotationdetails With A
	// Criteria------------------------------------------

	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		List<Quotationdetail> quotationdetails = quotationdetailService.listWithCritera(searchCriteria);
		// return.
		return new ResponseEntity<List<Quotationdetail>>(quotationdetails, HttpStatus.OK);
	}

	// -------------------Retrieve List Of Quotationdetails With Many
	// Criteria------------------------------------------

	@PreAuthorize("@quotationdetailAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		List<Quotationdetail> quotationdetails = quotationdetailService.listWithCriteras(searchCriterias);
		// return.
		return new ResponseEntity<List<Quotationdetail>>(quotationdetails, HttpStatus.OK);
	}

	// -------------------Retrieve All Quotationdetails By
	// Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Quotationdetail>> listAllByPage(Pageable pageable) {
		Page<Quotationdetail> quotationdetails = quotationdetailService.listAllByPage(pageable);
		// return.
		return new ResponseEntity<Page<Quotationdetail>>(quotationdetails, HttpStatus.OK);
	}

	// -------------------Retrieve List Of Quotationdetails With A Criteria By
	// Page------------------------------------------

	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		Page<Quotationdetail> quotationdetails = quotationdetailService.listWithCriteraByPage(searchCriteria, pageable);
		// return.
		return new ResponseEntity<Page<Quotationdetail>>(quotationdetails, HttpStatus.OK);
	}

	// -------------------Retrieve List Of Quotationdetails With Multiple
	// Criteria By Page------------------------------------------

	@PreAuthorize("@quotationdetailAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias,
			Pageable pageable) {
		Page<Quotationdetail> quotationdetails = quotationdetailService.listWithCriterasByPage(searchCriterias,
				pageable);
		// return.
		return new ResponseEntity<Page<Quotationdetail>>(quotationdetails, HttpStatus.OK);
	}

	// -------------------Retrieve List Of Quotations With Multiple Criteria By
	// Page------------------------------------------

	@PreAuthorize("@quotationdetailAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByIdquotationAndPage/{idquotation}", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByIdquotationAndPage(@PathVariable("idquotation") Integer idquotation,
			@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		Page<Map<String, Object>> quotations = quotationdetailService.listWithCriteriasByIdquotationAndPage(idquotation,
				searchCriterias, pageable);
		// return.
		return new ResponseEntity<Page<Map<String, Object>>>(quotations, HttpStatus.OK);
	}

	// -------------------Create
	// Requestdetails-------------------------------------------

	@PreAuthorize("@quotationdetailAuthorize.canCreate(#quotationdetail)")
	@RequestMapping(value = "/createWithSelectDetail", method = RequestMethod.POST)
	public ResponseEntity<?> createWithSelectDetail(@RequestBody List<Quotationdetail> quotationdetails)
			throws JsonParseException, JsonMappingException, IOException {
		for (Quotationdetail quotationdetail : quotationdetails) {
			quotationdetailService.create(quotationdetail);
		}
		// return.
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/saveCsvFileByIdquotation/{idquotation}", method = RequestMethod.GET)
	public ResponseEntity<?> saveCsvFileByIdquotation(@PathVariable("idquotation") Integer idquotation) throws IOException, MessagingException {
		String filePath = "quotationdetail\\csvfile\\";
		filePath = URLDecoder.decode(filePath, "UTF-8");
	    final String absolutePath = (env.getProperty("filelocation") + filePath).replace('\\', File.separatorChar);
	    String fileName = idquotation + ".csv";

		quotationdetailService.saveCsvFileByIdquotation(idquotation, absolutePath, fileName);
		
		// return.
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@RequestMapping(value = "/saveCsvFileAndSendMailByIdquotation/{idquotation}", method = RequestMethod.GET)
	public ResponseEntity<?> saveCsvFileAndSendMailByIdquotation(@PathVariable("idquotation") Integer idquotation) throws IOException, MessagingException {
		String filePath = "quotationdetail\\csvfile\\";
		filePath = URLDecoder.decode(filePath, "UTF-8");
	    final String absolutePath = (env.getProperty("filelocation") + filePath).replace('\\', File.separatorChar);
	    String fileName = idquotation + ".csv";

		quotationdetailService.saveCsvFileAndSendMailByIdquotation(idquotation, absolutePath, fileName);
		
		// return.
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	// -------------------Update Price of Materialexportdetail-------------------------------------------
		@PreAuthorize("@quotationdetailAuthorize.canUpdate(#quotationdetail)")
		@RequestMapping(value = "/updatePrice", method = RequestMethod.POST)
		public ResponseEntity<?> updatePrice(@RequestBody List<Quotationdetail> quotationdetails) throws JsonParseException, JsonMappingException, IOException {
			for (Quotationdetail quotationdetail : quotationdetails) {
				quotationdetailService.updatePrice(quotationdetail.getId(), quotationdetail.getPrice());
			}
			// return.
			return new ResponseEntity<>(HttpStatus.OK);
		}
}

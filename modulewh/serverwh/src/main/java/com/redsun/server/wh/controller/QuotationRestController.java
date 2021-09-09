
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
import com.redsun.server.wh.model.Quotation;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.service.QuotationService;
import com.redsun.server.wh.service.QuotationdetailService;

@RestController
@RequestMapping("/quotation")
public class QuotationRestController {

	//public static final Logger logger = LoggerFactory.getLogger(QuotationRestController.class);
	
	@Autowired
	private Environment env;

	@Autowired
	QuotationService quotationService;
	
	@Autowired
	QuotationdetailService quotationdetailService;

	// -------------------Create a Quotation-------------------------------------------

	@PreAuthorize("@quotationAuthorize.canCreate(#quotation)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Quotation quotation) throws JsonParseException, JsonMappingException, IOException {
		Quotation result = quotationService.create(quotation);
		// return.
		return new ResponseEntity<Integer>(result.getId(), HttpStatus.OK);
	}

	// -------------------Update lock a Quotation------------------------------------------------

	@PreAuthorize("@quotationAuthorize.canUpdate(#quotation)")
	@RequestMapping(value = "/updateLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateLock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = quotationService.updateLock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update Unlock a Quotation------------------------------------------------

	@PreAuthorize("@quotationAuthorize.canUpdate(#quotation)")
	@RequestMapping(value = "/updateUnlock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUnlock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = quotationService.updateUnlock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update a Quotation------------------------------------------------

	@PreAuthorize("@quotationAuthorize.canUpdate(#quotation)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Quotation quotation) throws JsonParseException, JsonMappingException, IOException {
		Quotation result = quotationService.update(id, quotation);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update a Quotation With Lock------------------------------------------------

	@PreAuthorize("@quotationAuthorize.canUpdate(#quotation)")
	@RequestMapping(value = "/updateWithLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateWithLock(@PathVariable("id") Integer id, @RequestBody Quotation quotation) throws JsonParseException, JsonMappingException, IOException {
		Quotation result = quotationService.updateWithLock(id, quotation);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete------------------------------------------------

	@PreAuthorize("@quotationAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDelete/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDelete(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Quotation result = quotationService.updateForDelete(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete With Lock------------------------------------------------

	@PreAuthorize("@quotationAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDeleteWithLock/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDeleteWithLock(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Quotation result = quotationService.updateForDeleteWithLock(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Delete a Quotation-----------------------------------------

	@PreAuthorize("@quotationAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		quotationService.deleteById(id);
		// return.	
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	// -------------------Retrieve All Quotations---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Quotation>> listAll() {
		List<Quotation> quotations = quotationService.listAll();
		// return.
		return new ResponseEntity<List<Quotation>>(quotations, HttpStatus.OK);
	}

	// -------------------Retrieve Single Quotation------------------------------------------

	@PreAuthorize("@quotationAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		Quotation quotation = quotationService.getById(id);
		// return.
		return new ResponseEntity<Quotation>(quotation, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Quotations With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		List<Quotation> quotations = quotationService.listWithCritera(searchCriteria);
		// return.
		return new ResponseEntity<List<Quotation>>(quotations, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Quotations With Many Criteria------------------------------------------
	
	@PreAuthorize("@quotationAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		List<Quotation> quotations = quotationService.listWithCriteras(searchCriterias);
		// return.
		return new ResponseEntity<List<Quotation>>(quotations, HttpStatus.OK);
	}

	// -------------------Retrieve All Quotations By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Quotation>> listAllByPage(Pageable pageable) {
		Page<Quotation> quotations = quotationService.listAllByPage(pageable);
		// return.
		return new ResponseEntity<Page<Quotation>>(quotations, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Quotations With A Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		Page<Quotation> quotations = quotationService.listWithCriteraByPage(searchCriteria, pageable);
		// return.
		return new ResponseEntity<Page<Quotation>>(quotations, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Quotations With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@quotationAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		Page<Map<String,Object>> quotations = quotationService.listWithCriterasByPage(searchCriterias, pageable);
		// return.
		return new  ResponseEntity<Page<Map<String, Object>>>(quotations, HttpStatus.OK);
	}
	
	// -------------------------Retrieve All User For Select--------------------------------------------------- 
	@RequestMapping(value = "/listAllForSelect", method = RequestMethod.GET)
	public ResponseEntity<List<Map<String, Object>>> listAllForSelect() {
		List<Map<String, Object>> quotations = quotationService.listAllForSelect();
		// return.
		return new ResponseEntity<List<Map<String, Object>>>(quotations, HttpStatus.OK);
	}
	
	// -------------------getById For View------------------------------------------

	@PreAuthorize("@quotationAuthorize.canRead(#id)")
	@RequestMapping(value = "/getByIdForView/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getByIdForView(@PathVariable("id") Integer id) {
		Map<String, Object> quotation = quotationService.getByIdForView(id);
		// return.
		return new ResponseEntity<Map<String, Object>>(quotation, HttpStatus.OK);
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

}

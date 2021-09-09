
package com.redsun.server.main.controller;

import java.io.IOException;
import java.util.List;

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
import com.redsun.server.main.model.Attachment;
import com.redsun.server.main.model.common.SearchCriteria;
import com.redsun.server.main.service.AttachmentService;

@RestController
@RequestMapping("/attachment")
public class AttachmentRestController {

	//public static final Logger logger = LoggerFactory.getLogger(AttachmentRestController.class);

	@Autowired
	AttachmentService attachmentService; //Service which will do all data retrieval/manipulation work

	// -------------------Create a Attachment-------------------------------------------

	@PreAuthorize("@attachmentAuthorize.canCreate(#attachment)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Attachment attachment) throws JsonParseException, JsonMappingException, IOException {
		Attachment result = attachmentService.create(attachment);
		// return.
		return new ResponseEntity<Integer>(result.getId(), HttpStatus.OK);
	}

	// -------------------Update lock a Attachment------------------------------------------------

	@PreAuthorize("@attachmentAuthorize.canUpdate(#attachment)")
	@RequestMapping(value = "/updateLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateLock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = attachmentService.updateLock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update Unlock a Attachment------------------------------------------------

	@PreAuthorize("@attachmentAuthorize.canUpdate(#attachment)")
	@RequestMapping(value = "/updateUnlock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUnlock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = attachmentService.updateUnlock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update a Attachment------------------------------------------------

	@PreAuthorize("@attachmentAuthorize.canUpdate(#attachment)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Attachment attachment) throws JsonParseException, JsonMappingException, IOException {
		Attachment result = attachmentService.update(id, attachment);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update a Attachment With Lock------------------------------------------------

	@PreAuthorize("@attachmentAuthorize.canUpdate(#attachment)")
	@RequestMapping(value = "/updateWithLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateWithLock(@PathVariable("id") Integer id, @RequestBody Attachment attachment) throws JsonParseException, JsonMappingException, IOException {
		Attachment result = attachmentService.updateWithLock(id, attachment);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete------------------------------------------------

	@PreAuthorize("@attachmentAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDelete/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDelete(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Attachment result = attachmentService.updateForDelete(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete With Lock------------------------------------------------

	@PreAuthorize("@attachmentAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDeleteWithLock/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDeleteWithLock(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Attachment result = attachmentService.updateForDeleteWithLock(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Delete a Attachment-----------------------------------------

	@PreAuthorize("@attachmentAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		attachmentService.deleteById(id);
		// return.	
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	// -------------------Retrieve All Attachments---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Attachment>> listAll() {
		List<Attachment> attachments = attachmentService.listAll();
		// return.
		return new ResponseEntity<List<Attachment>>(attachments, HttpStatus.OK);
	}

	// -------------------Retrieve Single Attachment------------------------------------------

	@PreAuthorize("@attachmentAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		Attachment attachment = attachmentService.getById(id);
		// return.
		return new ResponseEntity<Attachment>(attachment, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Attachments With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		List<Attachment> attachments = attachmentService.listWithCritera(searchCriteria);
		// return.
		return new ResponseEntity<List<Attachment>>(attachments, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Attachments With Many Criteria------------------------------------------
	
	@PreAuthorize("@attachmentAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		List<Attachment> attachments = attachmentService.listWithCriteras(searchCriterias);
		// return.
		return new ResponseEntity<List<Attachment>>(attachments, HttpStatus.OK);
	}

	// -------------------Retrieve All Attachments By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Attachment>> listAllByPage(Pageable pageable) {
		Page<Attachment> attachments = attachmentService.listAllByPage(pageable);
		// return.
		return new ResponseEntity<Page<Attachment>>(attachments, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Attachments With A Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		Page<Attachment> attachments = attachmentService.listWithCriteraByPage(searchCriteria, pageable);
		// return.
		return new ResponseEntity<Page<Attachment>>(attachments, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Attachments With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@attachmentAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		Page<Attachment> attachments = attachmentService.listWithCriterasByPage(searchCriterias, pageable);
		// return.
		return new ResponseEntity<Page<Attachment>>(attachments, HttpStatus.OK);
	}

}

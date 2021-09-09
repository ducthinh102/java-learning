
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
import com.redsun.server.main.model.Comment;
import com.redsun.server.main.model.common.SearchCriteria;
import com.redsun.server.main.service.CommentService;

@RestController
@RequestMapping("/comment")
public class CommentRestController {

	//public static final Logger logger = LoggerFactory.getLogger(CommentRestController.class);

	@Autowired
	CommentService commentService; //Service which will do all data retrieval/manipulation work

	// -------------------Create a Comment-------------------------------------------

	@PreAuthorize("@commentAuthorize.canCreate(#comment)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Comment comment) throws JsonParseException, JsonMappingException, IOException {
		Comment result = commentService.create(comment);
		// return.
		return new ResponseEntity<Integer>(result.getId(), HttpStatus.OK);
	}

	// -------------------Update lock a Comment------------------------------------------------

	@PreAuthorize("@commentAuthorize.canUpdate(#comment)")
	@RequestMapping(value = "/updateLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateLock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = commentService.updateLock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update Unlock a Comment------------------------------------------------

	@PreAuthorize("@commentAuthorize.canUpdate(#comment)")
	@RequestMapping(value = "/updateUnlock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUnlock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = commentService.updateUnlock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update a Comment------------------------------------------------

	@PreAuthorize("@commentAuthorize.canUpdate(#comment)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Comment comment) throws JsonParseException, JsonMappingException, IOException {
		Comment result = commentService.update(id, comment);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update a Comment With Lock------------------------------------------------

	@PreAuthorize("@commentAuthorize.canUpdate(#comment)")
	@RequestMapping(value = "/updateWithLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateWithLock(@PathVariable("id") Integer id, @RequestBody Comment comment) throws JsonParseException, JsonMappingException, IOException {
		Comment result = commentService.updateWithLock(id, comment);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete------------------------------------------------

	@PreAuthorize("@commentAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDelete/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDelete(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Comment result = commentService.updateForDelete(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete With Lock------------------------------------------------

	@PreAuthorize("@commentAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDeleteWithLock/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDeleteWithLock(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Comment result = commentService.updateForDeleteWithLock(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Delete a Comment-----------------------------------------

	@PreAuthorize("@commentAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		commentService.deleteById(id);
		// return.	
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	// -------------------Retrieve All Comments---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Comment>> listAll() {
		List<Comment> comments = commentService.listAll();
		// return.
		return new ResponseEntity<List<Comment>>(comments, HttpStatus.OK);
	}

	// -------------------Retrieve Single Comment------------------------------------------

	@PreAuthorize("@commentAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		Comment comment = commentService.getById(id);
		// return.
		return new ResponseEntity<Comment>(comment, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Comments With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		List<Comment> comments = commentService.listWithCritera(searchCriteria);
		// return.
		return new ResponseEntity<List<Comment>>(comments, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Comments With Many Criteria------------------------------------------
	
	@PreAuthorize("@commentAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		List<Comment> comments = commentService.listWithCriteras(searchCriterias);
		// return.
		return new ResponseEntity<List<Comment>>(comments, HttpStatus.OK);
	}

	// -------------------Retrieve All Comments By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Comment>> listAllByPage(Pageable pageable) {
		Page<Comment> comments = commentService.listAllByPage(pageable);
		// return.
		return new ResponseEntity<Page<Comment>>(comments, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Comments With A Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		Page<Comment> comments = commentService.listWithCriteraByPage(searchCriteria, pageable);
		// return.
		return new ResponseEntity<Page<Comment>>(comments, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Comments With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@commentAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		Page<Comment> comments = commentService.listWithCriterasByPage(searchCriterias, pageable);
		// return.
		return new ResponseEntity<Page<Comment>>(comments, HttpStatus.OK);
	}

}

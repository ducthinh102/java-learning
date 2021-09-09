
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
import com.redsun.server.main.model.Notify;
import com.redsun.server.main.model.common.SearchCriteria;
import com.redsun.server.main.service.NotifyService;
import com.redsun.server.main.util.SecurityUtil;

@RestController
@RequestMapping("/notify")
public class NotifyRestController {

	//public static final Logger logger = LoggerFactory.getLogger(NotifyRestController.class);

	@Autowired
	NotifyService notifyService; //Service which will do all data retrieval/manipulation work

	// -------------------Create a Notify-------------------------------------------

	@PreAuthorize("@notifyAuthorize.canCreate(#notify)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Notify notify) throws JsonParseException, JsonMappingException, IOException {
		Notify result = notifyService.create(notify);
		// return.
		return new ResponseEntity<Integer>(result.getId(), HttpStatus.OK);
	}

	// -------------------Update lock a Notify------------------------------------------------

	@PreAuthorize("@notifyAuthorize.canUpdate(#notify)")
	@RequestMapping(value = "/updateLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateLock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = notifyService.updateLock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update Unlock a Notify------------------------------------------------

	@PreAuthorize("@notifyAuthorize.canUpdate(#notify)")
	@RequestMapping(value = "/updateUnlock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUnlock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = notifyService.updateUnlock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update a Notify------------------------------------------------

	@PreAuthorize("@notifyAuthorize.canUpdate(#notify)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Notify notify) throws JsonParseException, JsonMappingException, IOException {
		Notify result = notifyService.update(id, notify);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update a Notify With Lock------------------------------------------------

	@PreAuthorize("@notifyAuthorize.canUpdate(#notify)")
	@RequestMapping(value = "/updateWithLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateWithLock(@PathVariable("id") Integer id, @RequestBody Notify notify) throws JsonParseException, JsonMappingException, IOException {
		Notify result = notifyService.updateWithLock(id, notify);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete------------------------------------------------

	@PreAuthorize("@notifyAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDelete/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDelete(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Notify result = notifyService.updateForDelete(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete With Lock------------------------------------------------

	@PreAuthorize("@notifyAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDeleteWithLock/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDeleteWithLock(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Notify result = notifyService.updateForDeleteWithLock(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Delete a Notify-----------------------------------------

	@PreAuthorize("@notifyAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		notifyService.deleteById(id);
		// return.	
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	// -------------------Retrieve All Notifys---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Notify>> listAll() {
		List<Notify> notifys = notifyService.listAll();
		// return.
		return new ResponseEntity<List<Notify>>(notifys, HttpStatus.OK);
	}

	// -------------------Retrieve Single Notify------------------------------------------

	@PreAuthorize("@notifyAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		Notify notify = notifyService.getById(id);
		// return.
		return new ResponseEntity<Notify>(notify, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Notifys With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		List<Notify> notifys = notifyService.listWithCritera(searchCriteria);
		// return.
		return new ResponseEntity<List<Notify>>(notifys, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Notifys With Many Criteria------------------------------------------
	
	@PreAuthorize("@notifyAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		List<Notify> notifys = notifyService.listWithCriteras(searchCriterias);
		// return.
		return new ResponseEntity<List<Notify>>(notifys, HttpStatus.OK);
	}

	// -------------------Retrieve All Notifys By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Notify>> listAllByPage(Pageable pageable) {
		Page<Notify> notifys = notifyService.listAllByPage(pageable);
		// return.
		return new ResponseEntity<Page<Notify>>(notifys, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Notifys With A Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		Page<Notify> notifys = notifyService.listWithCriteraByPage(searchCriteria, pageable);
		// return.
		return new ResponseEntity<Page<Notify>>(notifys, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Notifys With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@notifyAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		Page<Notify> notifys = notifyService.listWithCriterasByPage(searchCriterias, pageable);
		// return.
		return new ResponseEntity<Page<Notify>>(notifys, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Notifys With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@notifyAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriterasByIdreceiverAndPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterasByIdreceiverAndPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) throws JsonParseException, JsonMappingException, IOException {
		Integer idreceiver = SecurityUtil.getIdUser();
		Page<Notify> notifys = notifyService.listWithCriterasByIdreceiverAndPage(idreceiver, searchCriterias, pageable);
		// return.
		return new ResponseEntity<Page<Notify>>(notifys, HttpStatus.OK);
	}

}

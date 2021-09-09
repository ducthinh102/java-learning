
package com.redsun.server.main.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.redsun.server.main.model.History;
import com.redsun.server.main.model.common.SearchCriteria;
import com.redsun.server.main.service.HistoryService;
import com.redsun.server.main.util.CustomErrorType;

@RestController
@RequestMapping("/history")
public class HistoryRestController {

	public static final Logger logger = LoggerFactory.getLogger(HistoryRestController.class);

	@Autowired
	HistoryService historyService; //Service which will do all data retrieval/manipulation work

	// -------------------Create a History-------------------------------------------

	@PreAuthorize("@historyAuthorize.canCreate(#history)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody History history) {
		logger.info("Creating History : {}", history);
		History result = historyService.save(history);
		// return.
		return new ResponseEntity<Long>(result.getId(), HttpStatus.CREATED);
	}

	// -------------------Update a History------------------------------------------------

	@PreAuthorize("@historyAuthorize.canUpdate(#history)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody History history) {
		logger.info("Updating History with id {}", id);
		History result = historyService.save(history);
		// return.
		return new ResponseEntity<History>(result, HttpStatus.OK);
	}

	// -------------------Delete a History-----------------------------------------

	@PreAuthorize("@historyAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		logger.info("Fetching & Deleting History with id {}", id);
		historyService.deleteById(id);
		// return.	
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	// -------------------Retrieve All Historys---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<History>> listAll() {
		List<History> historys = historyService.listAll();
		if (historys.isEmpty()) {
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		// return.
		return new ResponseEntity<List<History>>(historys, HttpStatus.OK);
	}

	// -------------------Retrieve Single History------------------------------------------

	@PreAuthorize("@historyAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		logger.info("Fetching History with id {}", id);
		History history = historyService.getById(id);
		if (history == null) {
			logger.error("History with id {} not found.", id);
			// return.
			return new ResponseEntity<CustomErrorType>(new CustomErrorType("History with id " + id 
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		// return.
		return new ResponseEntity<History>(history, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Historys With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		logger.info("Fetching list historys with criteria");
		List<History> historys = historyService.listWithCritera(searchCriteria);
		if(historys.isEmpty()) {
			logger.error("List of historys is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<List<History>>(historys, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Historys With Many Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		logger.info("Fetching list historys with criteria");
		List<History> historys = historyService.listWithCriteras(searchCriterias);
		if(historys.isEmpty()) {
			logger.error("List of historys is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<List<History>>(historys, HttpStatus.OK);
	}

	// -------------------Retrieve All Historys By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<History>> listAllByPage(Pageable pageable) {
		Page<History> historys = historyService.listAllByPage(pageable);
		if (!historys.hasContent()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		// return.
		return new ResponseEntity<Page<History>>(historys, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Historys With A Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		logger.info("Fetching list historys with criteria");
		Page<History> historys = historyService.listWithCriteraByPage(searchCriteria, pageable);
		if(!historys.hasContent()) {
			logger.error("List of historys is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<Page<History>>(historys, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Historys With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@historyAuthorize.canRead(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		logger.info("Fetching list historys with criteria");
		Page<History> historys = historyService.listWithCriterasByPage(searchCriterias, pageable);
		if(!historys.hasContent()) {
			logger.error("List of historys is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<Page<History>>(historys, HttpStatus.OK);
	}

}

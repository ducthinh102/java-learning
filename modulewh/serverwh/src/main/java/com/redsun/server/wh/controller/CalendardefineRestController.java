
package com.redsun.server.wh.controller;

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
import org.springframework.web.util.UriComponentsBuilder;

import com.redsun.server.wh.model.Calendardefine;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.service.CalendardefineService;
import com.redsun.server.wh.util.CustomErrorType;

@RestController
@RequestMapping("/calendardefine")
public class CalendardefineRestController {

	public static final Logger logger = LoggerFactory.getLogger(CalendardefineRestController.class);

	@Autowired
	CalendardefineService calendardefineService; //Service which will do all data retrieval/manipulation work

	// -------------------Create a Calendardefine-------------------------------------------

	@PreAuthorize("@calendardefineAuthorize.canCreate(#calendardefine)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Calendardefine calendardefine) {
		logger.info("Creating Calendardefine : {}", calendardefine);
		Calendardefine result = calendardefineService.save(calendardefine);
		// return.
		return new ResponseEntity<Integer>(result.getId(), HttpStatus.CREATED);
	}

	// -------------------Update a Calendardefine------------------------------------------------

	@PreAuthorize("@calendardefineAuthorize.canUpdate(#calendardefine)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Calendardefine calendardefine) {
		logger.info("Updating Calendardefine with id {}", id);
		Calendardefine result = calendardefineService.save(calendardefine);
		// return.
		return new ResponseEntity<Calendardefine>(result, HttpStatus.OK);
	}

	// -------------------Delete a Calendardefine-----------------------------------------

	@PreAuthorize("@calendardefineAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		logger.info("Fetching & Deleting Calendardefine with id {}", id);
		calendardefineService.deleteById(id);
		// return.	
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	// -------------------Retrieve All Calendardefines---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Calendardefine>> listAll() {
		List<Calendardefine> calendardefines = calendardefineService.listAll();
		if (calendardefines.isEmpty()) {
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		// return.
		return new ResponseEntity<List<Calendardefine>>(calendardefines, HttpStatus.OK);
	}

	// -------------------Retrieve Single Calendardefine------------------------------------------

	@PreAuthorize("@calendardefineAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		logger.info("Fetching Calendardefine with id {}", id);
		Calendardefine calendardefine = calendardefineService.getById(id);
		if (calendardefine == null) {
			logger.error("Calendardefine with id {} not found.", id);
			// return.
			return new ResponseEntity<CustomErrorType>(new CustomErrorType("Calendardefine with id " + id 
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		// return.
		return new ResponseEntity<Calendardefine>(calendardefine, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Calendardefines With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		logger.info("Fetching list calendardefines with criteria");
		List<Calendardefine> calendardefines = calendardefineService.listWithCritera(searchCriteria);
		if(calendardefines.isEmpty()) {
			logger.error("List of calendardefines is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<List<Calendardefine>>(calendardefines, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Calendardefines With Many Criteria------------------------------------------
	
	@PreAuthorize("@calendardefineAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		logger.info("Fetching list calendardefines with criteria");
		List<Calendardefine> calendardefines = calendardefineService.listWithCriteras(searchCriterias);
		if(calendardefines.isEmpty()) {
			logger.error("List of calendardefines is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<List<Calendardefine>>(calendardefines, HttpStatus.OK);
	}

	// -------------------Retrieve All Calendardefines By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Calendardefine>> listAllByPage(Pageable pageable) {
		Page<Calendardefine> calendardefines = calendardefineService.listAllByPage(pageable);
		if (!calendardefines.hasContent()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		// return.
		return new ResponseEntity<Page<Calendardefine>>(calendardefines, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Calendardefines With A Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		logger.info("Fetching list calendardefines with criteria");
		Page<Calendardefine> calendardefines = calendardefineService.listWithCriteraByPage(searchCriteria, pageable);
		if(!calendardefines.hasContent()) {
			logger.error("List of calendardefines is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<Page<Calendardefine>>(calendardefines, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Calendardefines With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@calendardefineAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		logger.info("Fetching list calendardefines with criteria");
		Page<Calendardefine> calendardefines = calendardefineService.listWithCriterasByPage(searchCriterias, pageable);
		if(!calendardefines.hasContent()) {
			logger.error("List of calendardefines is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<Page<Calendardefine>>(calendardefines, HttpStatus.OK);
	}

}

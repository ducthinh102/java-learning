
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

import com.redsun.server.wh.model.Calendar;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.service.CalendarService;
import com.redsun.server.wh.util.CustomErrorType;

@RestController
@RequestMapping("/calendar")
public class CalendarRestController {

	public static final Logger logger = LoggerFactory.getLogger(CalendarRestController.class);

	@Autowired
	CalendarService calendarService; //Service which will do all data retrieval/manipulation work

	// -------------------Create a Calendar-------------------------------------------

	@PreAuthorize("@calendarAuthorize.canCreate(#calendar)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Calendar calendar) {
		logger.info("Creating Calendar : {}", calendar);
		Calendar result = calendarService.save(calendar);
		// return.
		return new ResponseEntity<Integer>(result.getId(), HttpStatus.CREATED);
	}

	// -------------------Update a Calendar------------------------------------------------

	@PreAuthorize("@calendarAuthorize.canUpdate(#calendar)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Calendar calendar) {
		logger.info("Updating Calendar with id {}", id);
		Calendar result = calendarService.save(calendar);
		// return.
		return new ResponseEntity<Calendar>(result, HttpStatus.OK);
	}

	// -------------------Delete a Calendar-----------------------------------------

	@PreAuthorize("@calendarAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		logger.info("Fetching & Deleting Calendar with id {}", id);
		calendarService.deleteById(id);
		// return.	
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	// -------------------Retrieve All Calendars---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Calendar>> listAll() {
		List<Calendar> calendars = calendarService.listAll();
		if (calendars.isEmpty()) {
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		// return.
		return new ResponseEntity<List<Calendar>>(calendars, HttpStatus.OK);
	}

	// -------------------Retrieve Single Calendar------------------------------------------

	@PreAuthorize("@calendarAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		logger.info("Fetching Calendar with id {}", id);
		Calendar calendar = calendarService.getById(id);
		if (calendar == null) {
			logger.error("Calendar with id {} not found.", id);
			// return.
			return new ResponseEntity<CustomErrorType>(new CustomErrorType("Calendar with id " + id 
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		// return.
		return new ResponseEntity<Calendar>(calendar, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Calendars With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		logger.info("Fetching list calendars with criteria");
		List<Calendar> calendars = calendarService.listWithCritera(searchCriteria);
		if(calendars.isEmpty()) {
			logger.error("List of calendars is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<List<Calendar>>(calendars, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Calendars With Many Criteria------------------------------------------
	
	@PreAuthorize("@calendarAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		logger.info("Fetching list calendars with criteria");
		List<Calendar> calendars = calendarService.listWithCriteras(searchCriterias);
		if(calendars.isEmpty()) {
			logger.error("List of calendars is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<List<Calendar>>(calendars, HttpStatus.OK);
	}

	// -------------------Retrieve All Calendars By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Calendar>> listAllByPage(Pageable pageable) {
		Page<Calendar> calendars = calendarService.listAllByPage(pageable);
		if (!calendars.hasContent()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		// return.
		return new ResponseEntity<Page<Calendar>>(calendars, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Calendars With A Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		logger.info("Fetching list calendars with criteria");
		Page<Calendar> calendars = calendarService.listWithCriteraByPage(searchCriteria, pageable);
		if(!calendars.hasContent()) {
			logger.error("List of calendars is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<Page<Calendar>>(calendars, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Calendars With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@calendarAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		logger.info("Fetching list calendars with criteria");
		Page<Calendar> calendars = calendarService.listWithCriterasByPage(searchCriterias, pageable);
		if(!calendars.hasContent()) {
			logger.error("List of calendars is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<Page<Calendar>>(calendars, HttpStatus.OK);
	}

}

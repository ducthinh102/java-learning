
package com.redsun.server.main.controller;

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
import com.redsun.server.main.model.Calendar;
import com.redsun.server.main.model.common.SearchCriteria;
import com.redsun.server.main.service.CalendarService;

@RestController
@RequestMapping("/calendar")
public class CalendarRestController {

	//public static final Logger logger = LoggerFactory.getLogger(CalendarRestController.class);

	@Autowired
	CalendarService calendarService; //Service which will do all data retrieval/manipulation work

	// -------------------Create a Calendar-------------------------------------------

	@PreAuthorize("@calendarAuthorize.canCreate(#calendar)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Calendar calendar) throws JsonParseException, JsonMappingException, IOException {
		Calendar result = calendarService.create(calendar);
		// return.
		return new ResponseEntity<Integer>(result.getId(), HttpStatus.OK);
	}

	// -------------------Update lock a Calendar------------------------------------------------

	@PreAuthorize("@calendarAuthorize.canUpdate(#calendar)")
	@RequestMapping(value = "/updateLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateLock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = calendarService.updateLock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update Unlock a Calendar------------------------------------------------

	@PreAuthorize("@calendarAuthorize.canUpdate(#calendar)")
	@RequestMapping(value = "/updateUnlock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUnlock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = calendarService.updateUnlock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update a Calendar------------------------------------------------

	@PreAuthorize("@calendarAuthorize.canUpdate(#calendar)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Calendar calendar) throws JsonParseException, JsonMappingException, IOException {
		Calendar result = calendarService.update(id, calendar);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update a Calendar With Lock------------------------------------------------

	@PreAuthorize("@calendarAuthorize.canUpdate(#calendar)")
	@RequestMapping(value = "/updateWithLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateWithLock(@PathVariable("id") Integer id, @RequestBody Calendar calendar) throws JsonParseException, JsonMappingException, IOException {
		Calendar result = calendarService.updateWithLock(id, calendar);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete------------------------------------------------

	@PreAuthorize("@calendarAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDelete/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDelete(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Calendar result = calendarService.updateForDelete(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete With Lock------------------------------------------------

	@PreAuthorize("@calendarAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDeleteWithLock/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDeleteWithLock(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Calendar result = calendarService.updateForDeleteWithLock(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Delete a Calendar-----------------------------------------

	@PreAuthorize("@calendarAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		calendarService.deleteById(id);
		// return.	
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	// -------------------Retrieve All Calendars---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Calendar>> listAll() {
		List<Calendar> calendars = calendarService.listAll();
		// return.
		return new ResponseEntity<List<Calendar>>(calendars, HttpStatus.OK);
	}

	// -------------------Retrieve Single Calendar------------------------------------------

	@PreAuthorize("@calendarAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		Calendar calendar = calendarService.getById(id);
		// return.
		return new ResponseEntity<Calendar>(calendar, HttpStatus.OK);
	}
	
	// -------------------------Retrieve All Calendar For Select--------------------------------------------------- 
	
	@RequestMapping(value = "/listForSelect", method = RequestMethod.GET)
	public ResponseEntity<List<Map<String, Object>>> listForSelect() {
		List<Map<String, Object>> result = calendarService.listForSelect();
		// return.
		return new ResponseEntity<List<Map<String, Object>>>(result, HttpStatus.OK);
	}

	// -------------------Retrieve Single Calendar With Calendarextend By Id------------------------------------------

	@PreAuthorize("@calendarAuthorize.canRead(#id)")
	@RequestMapping(value = "/getWithCalendarextendById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getWithCalendarextendById(@PathVariable("id") Integer id) {
		Map<String, Object> result = calendarService.getWithCalendarextendById(id);
		// return.
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}

	// -------------------Retrieve Single Calendar With Calendarextend------------------------------------------

	//@PreAuthorize("@calendarAuthorize.canRead(#id)")
	@RequestMapping(value = "/getWithCalendarextend", method = RequestMethod.GET)
	public ResponseEntity<?> getWithCalendarextend() throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> result = calendarService.getWithCalendarextend();
		// return.
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Calendars With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		List<Calendar> calendars = calendarService.listWithCritera(searchCriteria);
		// return.
		return new ResponseEntity<List<Calendar>>(calendars, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Calendars With Many Criteria------------------------------------------
	
	@PreAuthorize("@calendarAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		List<Calendar> calendars = calendarService.listWithCriteras(searchCriterias);
		// return.
		return new ResponseEntity<List<Calendar>>(calendars, HttpStatus.OK);
	}

	// -------------------Retrieve All Calendars By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Calendar>> listAllByPage(Pageable pageable) {
		Page<Calendar> calendars = calendarService.listAllByPage(pageable);
		// return.
		return new ResponseEntity<Page<Calendar>>(calendars, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Calendars With A Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		Page<Calendar> calendars = calendarService.listWithCriteraByPage(searchCriteria, pageable);
		// return.
		return new ResponseEntity<Page<Calendar>>(calendars, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Calendars With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@calendarAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		Page<Calendar> calendars = calendarService.listWithCriterasByPage(searchCriterias, pageable);
		// return.
		return new ResponseEntity<Page<Calendar>>(calendars, HttpStatus.OK);
	}

}

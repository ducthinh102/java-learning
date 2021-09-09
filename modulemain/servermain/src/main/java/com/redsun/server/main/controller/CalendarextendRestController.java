
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
import com.redsun.server.main.model.Calendarextend;
import com.redsun.server.main.model.common.SearchCriteria;
import com.redsun.server.main.service.CalendarextendService;

@RestController
@RequestMapping("/calendarextend")
public class CalendarextendRestController {

	//public static final Logger logger = LoggerFactory.getLogger(CalendarextendRestController.class);

	@Autowired
	CalendarextendService calendarextendService; //Service which will do all data retrieval/manipulation work

	// -------------------Create a Calendarextend-------------------------------------------

	@PreAuthorize("@calendarextendAuthorize.canCreate(#calendarextend)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Calendarextend calendarextend) throws JsonParseException, JsonMappingException, IOException {
		Calendarextend result = calendarextendService.create(calendarextend);
		// return.
		return new ResponseEntity<Integer>(result.getId(), HttpStatus.OK);
	}

	// -------------------Update lock a Calendarextend------------------------------------------------

	@PreAuthorize("@calendarextendAuthorize.canUpdate(#calendarextend)")
	@RequestMapping(value = "/updateLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateLock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = calendarextendService.updateLock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update Unlock a Calendarextend------------------------------------------------

	@PreAuthorize("@calendarextendAuthorize.canUpdate(#calendarextend)")
	@RequestMapping(value = "/updateUnlock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUnlock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = calendarextendService.updateUnlock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update a Calendarextend------------------------------------------------

	@PreAuthorize("@calendarextendAuthorize.canUpdate(#calendarextend)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Calendarextend calendarextend) throws JsonParseException, JsonMappingException, IOException {
		Calendarextend result = calendarextendService.update(id, calendarextend);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update a Calendarextend With Lock------------------------------------------------

	@PreAuthorize("@calendarextendAuthorize.canUpdate(#calendarextend)")
	@RequestMapping(value = "/updateWithLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateWithLock(@PathVariable("id") Integer id, @RequestBody Calendarextend calendarextend) throws JsonParseException, JsonMappingException, IOException {
		Calendarextend result = calendarextendService.updateWithLock(id, calendarextend);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete------------------------------------------------

	@PreAuthorize("@calendarextendAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDelete/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDelete(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Calendarextend result = calendarextendService.updateForDelete(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete With Lock------------------------------------------------

	@PreAuthorize("@calendarextendAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDeleteWithLock/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDeleteWithLock(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Calendarextend result = calendarextendService.updateForDeleteWithLock(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Delete a Calendarextend-----------------------------------------

	@PreAuthorize("@calendarextendAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		calendarextendService.deleteById(id);
		// return.	
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	// -------------------Retrieve All Calendarextends---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Calendarextend>> listAll() {
		List<Calendarextend> calendarextends = calendarextendService.listAll();
		// return.
		return new ResponseEntity<List<Calendarextend>>(calendarextends, HttpStatus.OK);
	}

	// -------------------Retrieve Single Calendarextend------------------------------------------

	@PreAuthorize("@calendarextendAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		Calendarextend calendarextend = calendarextendService.getById(id);
		// return.
		return new ResponseEntity<Calendarextend>(calendarextend, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Calendarextends With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		List<Calendarextend> calendarextends = calendarextendService.listWithCritera(searchCriteria);
		// return.
		return new ResponseEntity<List<Calendarextend>>(calendarextends, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Calendarextends With Many Criteria------------------------------------------
	
	@PreAuthorize("@calendarextendAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		List<Calendarextend> calendarextends = calendarextendService.listWithCriteras(searchCriterias);
		// return.
		return new ResponseEntity<List<Calendarextend>>(calendarextends, HttpStatus.OK);
	}

	// -------------------Retrieve All Calendarextends By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Calendarextend>> listAllByPage(Pageable pageable) {
		Page<Calendarextend> calendarextends = calendarextendService.listAllByPage(pageable);
		// return.
		return new ResponseEntity<Page<Calendarextend>>(calendarextends, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Calendarextends With A Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		Page<Calendarextend> calendarextends = calendarextendService.listWithCriteraByPage(searchCriteria, pageable);
		// return.
		return new ResponseEntity<Page<Calendarextend>>(calendarextends, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Calendarextends With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@calendarextendAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		Page<Calendarextend> calendarextends = calendarextendService.listWithCriterasByPage(searchCriterias, pageable);
		// return.
		return new ResponseEntity<Page<Calendarextend>>(calendarextends, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Calendarextends With Multiple Criteria By Idcalendar And Page------------------------------------------
	
	@PreAuthorize("@calendarextendAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByIdcalendarAndPage/{idcalendar}", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByIdcalendarAndPage(@PathVariable("idcalendar") Integer idcalendar, @RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		Page<Map<String, Object>> calendarextends = calendarextendService.listWithCriteriasByIdcalendarAndPage(idcalendar, searchCriterias, pageable);
		// return.
		return new ResponseEntity<Page<Map<String, Object>>>(calendarextends, HttpStatus.OK);
	}
	
	// -------------------Retrieve List All Of Calendarextendse By Idcalendar------------------------------------------
	
	@PreAuthorize("@calendarextendAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listAllByIdcalendar/{idcalendar}", method = RequestMethod.POST)
	public ResponseEntity<?> listAllByIdcalendar(@PathVariable("idcalendar") Integer idcalendar) {
		List<Map<String, Object>> calendarextends = calendarextendService.listAllByIdcalendar(idcalendar);
		// return.
		return new ResponseEntity<List<Map<String, Object>>>(calendarextends, HttpStatus.OK);
	}

}

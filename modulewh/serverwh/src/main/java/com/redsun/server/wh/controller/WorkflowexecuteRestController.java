
package com.redsun.server.wh.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.redsun.server.wh.model.Workflowexecute;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.service.WorkflowexecuteService;
import com.redsun.server.wh.util.CustomErrorType;

@RestController
@RequestMapping("/workflowexecute")
public class WorkflowexecuteRestController {

	public static final Logger logger = LoggerFactory.getLogger(WorkflowexecuteRestController.class);

	@Autowired
	WorkflowexecuteService workflowexecuteService; //Service which will do all data retrieval/manipulation work

	// -------------------Create a Workflowexecute-------------------------------------------

	@PreAuthorize("@workflowexecuteAuthorize.canCreate(#workflowexecute)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Workflowexecute workflowexecute) {
		logger.info("Creating Workflowexecute : {}", workflowexecute);
		Workflowexecute result = workflowexecuteService.save(workflowexecute);
		// return.
		return new ResponseEntity<Integer>(result.getId(), HttpStatus.CREATED);
	}

	// -------------------Update a Workflowexecute------------------------------------------------

	@PreAuthorize("@workflowexecuteAuthorize.canUpdate(#workflowexecute)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Workflowexecute workflowexecute) {
		logger.info("Updating Workflowexecute with id {}", id);
		Workflowexecute result = workflowexecuteService.save(workflowexecute);
		// return.
		return new ResponseEntity<Workflowexecute>(result, HttpStatus.OK);
	}

	// -------------------Delete a Workflowexecute-----------------------------------------

	@PreAuthorize("@workflowexecuteAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		logger.info("Fetching & Deleting Workflowexecute with id {}", id);
		workflowexecuteService.deleteById(id);
		// return.	
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	// -------------------Retrieve All Workflowexecutes---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Workflowexecute>> listAll() {
		List<Workflowexecute> workflowexecutes = workflowexecuteService.listAll();
		if (workflowexecutes.isEmpty()) {
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		// return.
		return new ResponseEntity<List<Workflowexecute>>(workflowexecutes, HttpStatus.OK);
	}

	// -------------------Retrieve Single Workflowexecute------------------------------------------

	@PreAuthorize("@workflowexecuteAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		logger.info("Fetching Workflowexecute with id {}", id);
		Workflowexecute workflowexecute = workflowexecuteService.getById(id);
		if (workflowexecute == null) {
			logger.error("Workflowexecute with id {} not found.", id);
			// return.
			return new ResponseEntity<CustomErrorType>(new CustomErrorType("Workflowexecute with id " + id 
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		// return.
		return new ResponseEntity<Workflowexecute>(workflowexecute, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Workflowexecutes With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		logger.info("Fetching list workflowexecutes with criteria");
		List<Workflowexecute> workflowexecutes = workflowexecuteService.listWithCritera(searchCriteria);
		if(workflowexecutes.isEmpty()) {
			logger.error("List of workflowexecutes is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<List<Workflowexecute>>(workflowexecutes, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Workflowexecutes With Many Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		logger.info("Fetching list workflowexecutes with criteria");
		List<Workflowexecute> workflowexecutes = workflowexecuteService.listWithCriteras(searchCriterias);
		if(workflowexecutes.isEmpty()) {
			logger.error("List of workflowexecutes is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<List<Workflowexecute>>(workflowexecutes, HttpStatus.OK);
	}

	// -------------------Retrieve All Workflowexecutes By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Workflowexecute>> listAllByPage(Pageable pageable) {
		Page<Workflowexecute> workflowexecutes = workflowexecuteService.listAllByPage(pageable);
		if (!workflowexecutes.hasContent()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		// return.
		return new ResponseEntity<Page<Workflowexecute>>(workflowexecutes, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Workflowexecutes With A Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		logger.info("Fetching list workflowexecutes with criteria");
		Page<Workflowexecute> workflowexecutes = workflowexecuteService.listWithCriteraByPage(searchCriteria, pageable);
		if(!workflowexecutes.hasContent()) {
			logger.error("List of workflowexecutes is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<Page<Workflowexecute>>(workflowexecutes, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Workflowexecutes With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@workflowexecuteAuthorize.canRead(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		logger.info("Fetching list workflowexecutes with criteria");
		Page<Workflowexecute> workflowexecutes = workflowexecuteService.listWithCriterasByPage(searchCriterias, pageable);
		if(!workflowexecutes.hasContent()) {
			logger.error("List of workflowexecutes is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<Page<Workflowexecute>>(workflowexecutes, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Workflowexecutes With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@workflowexecuteAuthorize.canRead(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByIdworkflowAndIdtabAndPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByIdworkflowAndIdtabAndPage(@RequestBody List<SearchCriteria> searchCriterias, @RequestParam Map<String, String> params, Pageable pageable) throws JsonParseException, JsonMappingException, IOException {
		
		Integer idworkflow = (params.get("idworkflow")!=null) ? Integer.valueOf(params.get("idworkflow")) : -1;
		String idtab = params.get("idtab");
		
		Page<Map<String, Object>> workflowexecutes = workflowexecuteService.listWithCriteriasByIdworkflowAndIdtabAndPage(searchCriterias, idworkflow, idtab, pageable);
		return new ResponseEntity<Page<Map<String, Object>>>(workflowexecutes, HttpStatus.OK);
	
	}
	
	// -------------------Execute - Insert/ Update a Workflowexecute------------------------------------------------

	@RequestMapping(value = "/execute", method = RequestMethod.PUT)
	public ResponseEntity<Object> execute(@RequestParam Map<String, String> params) throws JsonParseException, JsonMappingException, NumberFormatException, IOException {
		Integer idworkflow = (params.get("idworkflow")!=null) ? Integer.valueOf(params.get("idworkflow")) : -1;
		Integer idref = (params.get("idref")!=null) ? Integer.valueOf(params.get("idref")) : -1;
		Integer command = (params.get("command")!=null) ? Integer.valueOf(params.get("command")) : -1;
		Object result = workflowexecuteService.execute(idworkflow, idref, command);
		if (result == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Object>(result, HttpStatus.OK);
	}
	
	// -------------------Assign - Insert/ Update a Workflowexecute------------------------------------------------

	@RequestMapping(value = "/assign", method = RequestMethod.PUT)
	public ResponseEntity<Object> assign(@RequestParam Map<String, String> params) throws JsonParseException, JsonMappingException, NumberFormatException, IOException {
		Integer idworkflow = (params.get("idworkflow")!=null) ? Integer.valueOf(params.get("idworkflow")) : -1;
		Integer idref = (params.get("idref")!=null) ? Integer.valueOf(params.get("idref")) : -1;
		Integer idassignee = (params.get("idassignee")!=null) ? Integer.valueOf(params.get("idassignee")) : -1;
		Object result = workflowexecuteService.assign(idworkflow, idref, idassignee);
		if (result == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Object>(result, HttpStatus.OK);
	}
	
	// -------------------Check buttons visibility------------------------------------------

	@RequestMapping(value = "/checkButtonVisibility/{idworkflow}/{idref}", method = RequestMethod.GET)
	public ResponseEntity<?> checkButtonVisibility(@PathVariable("idworkflow") Integer idworkflow, @PathVariable("idref") Integer idref) throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> result = workflowexecuteService.checkButtonVisibility(idworkflow, idref);
		// return.
		return new ResponseEntity<Map<String, Object>>(result, HttpStatus.OK);
	}
		
}

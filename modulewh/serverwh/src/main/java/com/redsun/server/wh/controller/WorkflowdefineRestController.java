
package com.redsun.server.wh.controller;

import java.io.IOException;
import java.util.HashMap;
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
import com.redsun.server.wh.model.Workflow;
import com.redsun.server.wh.model.Workflowdefine;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.WorkflowdefineRepository;
import com.redsun.server.wh.service.WorkflowService;
import com.redsun.server.wh.service.WorkflowdefineService;
import com.redsun.server.wh.util.CustomErrorType;

@RestController
@RequestMapping("/workflowdefine")
public class WorkflowdefineRestController {

	@Autowired
	WorkflowdefineService workflowdefineService; //Service which will do all data retrieval/manipulation work

	@Autowired
	WorkflowService workflowService;
	
	@Autowired
	WorkflowdefineRepository workflowdefineRepository;
	
	// -------------------Create a Workflowdefine-------------------------------------------

	@PreAuthorize("@workflowdefineAuthorize.canCreate(#workflowdefine)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Workflowdefine workflowdefine) throws JsonParseException, JsonMappingException, IOException {
		Workflowdefine result = workflowdefineService.create(workflowdefine);
		// return.
		if(result != null)
			return new ResponseEntity<Integer>(result.getId(), HttpStatus.CREATED);
		return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
	}

	// -------------------Update a Workflowdefine------------------------------------------------

	@PreAuthorize("@workflowdefineAuthorize.canUpdate(#workflowdefine)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Workflowdefine workflowdefine) throws JsonParseException, JsonMappingException, IOException {
		Workflowdefine result = workflowdefineService.updateWithLock(workflowdefine.getId(), workflowdefine);
		return new ResponseEntity<Workflowdefine>(result, HttpStatus.OK);
	}

	// -------------------Delete a Workflowdefine-----------------------------------------

	@PreAuthorize("@workflowdefineAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}/{version}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		workflowdefineService.updateForDeleteWithLock(id, version);
		// return.	
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	// -------------------Retrieve All Workflowdefines---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Workflowdefine>> listAll() {
		List<Workflowdefine> workflowdefines = workflowdefineService.listAll();
		if (workflowdefines.isEmpty()) {
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		// return.
		return new ResponseEntity<List<Workflowdefine>>(workflowdefines, HttpStatus.OK);
	}

	// -------------------Retrieve Single Workflowdefine------------------------------------------

	@PreAuthorize("@workflowdefineAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		Workflowdefine workflowdefine = workflowdefineService.getById(id);
		if (workflowdefine == null) {
			// return.
			return new ResponseEntity<CustomErrorType>(new CustomErrorType("Workflowdefine with id " + id 
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		
		Workflow workflow = workflowService.getById(workflowdefine.getIdworkflow());
		HashMap<String,Object> result = new HashMap<String,Object>();
		result.put("workflow", workflow);
		result.put("workflowdefine", workflowdefine);
		// return.
		return new ResponseEntity<HashMap<String,Object>>(result, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Workflowdefines With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		List<Workflowdefine> workflowdefines = workflowdefineService.listWithCritera(searchCriteria);
		if(workflowdefines.isEmpty()) {
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<List<Workflowdefine>>(workflowdefines, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Workflowdefines With Many Criteria------------------------------------------
	
	@PreAuthorize("@workflowdefineAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		List<Workflowdefine> workflowdefines = workflowdefineService.listWithCriteras(searchCriterias);
		if(workflowdefines.isEmpty()) {
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<List<Workflowdefine>>(workflowdefines, HttpStatus.OK);
	}

	// -------------------Retrieve All Workflowdefines By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Workflowdefine>> listAllByPage(Pageable pageable) {
		Page<Workflowdefine> workflowdefines = workflowdefineService.listAllByPage(pageable);
		if (!workflowdefines.hasContent()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		// return.
		return new ResponseEntity<Page<Workflowdefine>>(workflowdefines, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Workflowdefines With A Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		Page<Workflowdefine> workflowdefines = workflowdefineService.listWithCriteraByPage(searchCriteria, pageable);
		if(!workflowdefines.hasContent()) {
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<Page<Workflowdefine>>(workflowdefines, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Workflowdefines With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@workflowdefineAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		Page<Workflowdefine> workflowdefines = workflowdefineService.listWithCriterasByPage(searchCriterias, pageable);
		if(!workflowdefines.hasContent()) {
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<Page<Workflowdefine>>(workflowdefines, HttpStatus.OK);
	}
	
	// -------------------Check Workflow Step Is Duplicated------------------------------------------

	@RequestMapping(value = "/isStepExisted/{idworkflow}/{step}", method = RequestMethod.GET)
	public ResponseEntity<?> isStepExisted(@PathVariable("idworkflow") Integer idworkflow, @PathVariable("step") Integer step) {
		Boolean result = workflowdefineService.isStepExisted(idworkflow, step);
		return new ResponseEntity<Boolean>(result, HttpStatus.OK);
	}
	
}

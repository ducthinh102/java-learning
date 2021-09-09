
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
import com.redsun.server.wh.model.Workflow;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.service.WorkflowService;
import com.redsun.server.wh.util.CustomErrorType;

@RestController
@RequestMapping("/workflow")
public class WorkflowRestController {

	public static final Logger logger = LoggerFactory.getLogger(WorkflowRestController.class);

	@Autowired
	WorkflowService workflowService; //Service which will do all data retrieval/manipulation work

	// -------------------Create a Workflow-------------------------------------------

	@PreAuthorize("@workflowAuthorize.canCreate(#workflow)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Workflow workflow) {
		logger.info("Creating Workflow : {}", workflow);
		Workflow result = workflowService.save(workflow);
		// return.
		return new ResponseEntity<Integer>(result.getId(), HttpStatus.CREATED);
	}

	// -------------------Update a Workflow------------------------------------------------

	@PreAuthorize("@workflowAuthorize.canUpdate(#workflow)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Workflow workflow) {
		logger.info("Updating Workflow with id {}", id);
		Workflow result = workflowService.save(workflow);
		// return.
		return new ResponseEntity<Workflow>(result, HttpStatus.OK);
	}

	// -------------------Delete a Workflow-----------------------------------------

	@PreAuthorize("@workflowAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		logger.info("Fetching & Deleting Workflow with id {}", id);
		workflowService.deleteById(id);
		// return.	
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	// -------------------Retrieve All Workflows---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Workflow>> listAll() {
		List<Workflow> workflows = workflowService.listAll();
		if (workflows.isEmpty()) {
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		// return.
		return new ResponseEntity<List<Workflow>>(workflows, HttpStatus.OK);
	}

	// -------------------Retrieve Single Workflow------------------------------------------

	@PreAuthorize("@workflowAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		logger.info("Fetching Workflow with id {}", id);
		Workflow workflow = workflowService.getById(id);
		if (workflow == null) {
			logger.error("Workflow with id {} not found.", id);
			// return.
			return new ResponseEntity<CustomErrorType>(new CustomErrorType("Workflow with id " + id 
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		// return.
		return new ResponseEntity<Workflow>(workflow, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Workflows With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		logger.info("Fetching list workflows with criteria");
		List<Workflow> workflows = workflowService.listWithCritera(searchCriteria);
		if(workflows.isEmpty()) {
			logger.error("List of workflows is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<List<Workflow>>(workflows, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Workflows With Many Criteria------------------------------------------
	
	@PreAuthorize("@workflowAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		logger.info("Fetching list workflows with criteria");
		List<Workflow> workflows = workflowService.listWithCriteras(searchCriterias);
		if(workflows.isEmpty()) {
			logger.error("List of workflows is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<List<Workflow>>(workflows, HttpStatus.OK);
	}

	// -------------------Retrieve All Workflows By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Workflow>> listAllByPage(Pageable pageable) {
		Page<Workflow> workflows = workflowService.listAllByPage(pageable);
		if (!workflows.hasContent()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		// return.
		return new ResponseEntity<Page<Workflow>>(workflows, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Workflows With A Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		logger.info("Fetching list workflows with criteria");
		Page<Workflow> workflows = workflowService.listWithCriteraByPage(searchCriteria, pageable);
		if(!workflows.hasContent()) {
			logger.error("List of workflows is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<Page<Workflow>>(workflows, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Workflows With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@workflowAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		logger.info("Fetching list workflows with criteria");
		Page<Workflow> workflows = workflowService.listWithCriterasByPage(searchCriterias, pageable);
		if(!workflows.hasContent()) {
			logger.error("List of workflows is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<Page<Workflow>>(workflows, HttpStatus.OK);
	}

}

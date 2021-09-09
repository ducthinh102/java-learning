
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

import com.redsun.server.wh.model.Assignment;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.AssignmentRepository;
import com.redsun.server.wh.repository.SignRepository;
import com.redsun.server.wh.service.AssignmentService;
import com.redsun.server.wh.service.WorkflowexecuteService;
import com.redsun.server.wh.util.CustomErrorType;

@RestController
@RequestMapping("/assignment")
public class AssignmentRestController {

	public static final Logger logger = LoggerFactory.getLogger(AssignmentRestController.class);

	@Autowired
	AssignmentService assignmentService; //Service which will do all data retrieval/manipulation work

	@Autowired
	AssignmentRepository assignmentRepository;
	
	@Autowired
	SignRepository signRepository;
	
	@Autowired
	WorkflowexecuteService workflowexecuteService; //Service which will do all data retrieval/manipulation work

	// -------------------Create a Assignment-------------------------------------------

	//@Transactional
	@PreAuthorize("@assignmentAuthorize.canCreate(#assignment)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Assignment assignment) {
		logger.info("Creating Assignment : {}");
		Assignment result = assignmentService.save(assignment);
		return new ResponseEntity<Integer>(result.getId(), HttpStatus.CREATED);
	}

	// -------------------Update a Assignment------------------------------------------------

	@PreAuthorize("@assignmentAuthorize.canUpdate(#assignment)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Assignment assignment) {
		logger.info("Updating Assignment with id {}", id);
		Assignment result = assignmentService.save(assignment);
		// return.
		return new ResponseEntity<Assignment>(result, HttpStatus.OK);
	}

	// -------------------Delete a Assignment-----------------------------------------

	@PreAuthorize("@assignmentAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		logger.info("Fetching & Deleting Assignment with id {}", id);
		assignmentService.deleteById(id);
		// return.	
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	// -------------------Retrieve All Assignments---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Assignment>> listAll() {
		List<Assignment> assignments = assignmentService.listAll();
		if (assignments.isEmpty()) {
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		// return.
		return new ResponseEntity<List<Assignment>>(assignments, HttpStatus.OK);
	}

	// -------------------Retrieve Single Assignment------------------------------------------

	@PreAuthorize("@assignmentAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		logger.info("Fetching Assignment with id {}", id);
		Assignment assignment = assignmentService.getById(id);
		if (assignment == null) {
			logger.error("Assignment with id {} not found.", id);
			// return.
			return new ResponseEntity<CustomErrorType>(new CustomErrorType("Assignment with id " + id 
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		// return.
		return new ResponseEntity<Assignment>(assignment, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Assignments With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		logger.info("Fetching list assignments with criteria");
		List<Assignment> assignments = assignmentService.listWithCritera(searchCriteria);
		if(assignments.isEmpty()) {
			logger.error("List of assignments is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<List<Assignment>>(assignments, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Assignments With Many Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		logger.info("Fetching list assignments with criteria");
		List<Assignment> assignments = assignmentService.listWithCriteras(searchCriterias);
		if(assignments.isEmpty()) {
			logger.error("List of assignments is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<List<Assignment>>(assignments, HttpStatus.OK);
	}

	// -------------------Retrieve All Assignments By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Assignment>> listAllByPage(Pageable pageable) {
		Page<Assignment> assignments = assignmentService.listAllByPage(pageable);
		if (!assignments.hasContent()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		// return.
		return new ResponseEntity<Page<Assignment>>(assignments, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Assignments With A Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		logger.info("Fetching list assignments with criteria");
		Page<Assignment> assignments = assignmentService.listWithCriteraByPage(searchCriteria, pageable);
		if(!assignments.hasContent()) {
			logger.error("List of assignments is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<Page<Assignment>>(assignments, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Assignments With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@assignmentAuthorize.canRead(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		logger.info("Fetching list assignments with criteria");
		Page<Assignment> assignments = assignmentService.listWithCriterasByPage(searchCriterias, pageable);
		if(!assignments.hasContent()) {
			logger.error("List of assignments is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<Page<Assignment>>(assignments, HttpStatus.OK);
	}
	
}

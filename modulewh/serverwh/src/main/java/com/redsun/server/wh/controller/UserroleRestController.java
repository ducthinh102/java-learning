
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

import com.redsun.server.wh.model.Userrole;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.service.UserroleService;
import com.redsun.server.wh.util.CustomErrorType;

@RestController
@RequestMapping("/userrole")
public class UserroleRestController {

	public static final Logger logger = LoggerFactory.getLogger(UserroleRestController.class);

	@Autowired
	UserroleService userroleService; //Service which will do all data retrieval/manipulation work

	// -------------------Create a Userrole-------------------------------------------

	@PreAuthorize("@userroleAuthorize.canCreate(#userrole)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Userrole userrole) {
		logger.info("Creating Userrole : {}", userrole);
		Userrole result = userroleService.save(userrole);
		// return.
		return new ResponseEntity<Integer>(result.getId(), HttpStatus.CREATED);
	}

	// -------------------Update a Userrole------------------------------------------------

	@PreAuthorize("@userroleAuthorize.canUpdate(#userrole)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Userrole userrole) {
		logger.info("Updating Userrole with id {}", id);
		Userrole result = userroleService.save(userrole);
		// return.
		return new ResponseEntity<Userrole>(result, HttpStatus.OK);
	}

	// -------------------Delete a Userrole-----------------------------------------

	@PreAuthorize("@userroleAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		logger.info("Fetching & Deleting Userrole with id {}", id);
		userroleService.deleteById(id);
		// return.	
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	// -------------------Retrieve All Userroles---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Userrole>> listAll() {
		List<Userrole> userroles = userroleService.listAll();
		if (userroles.isEmpty()) {
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		// return.
		return new ResponseEntity<List<Userrole>>(userroles, HttpStatus.OK);
	}

	// -------------------Retrieve Single Userrole------------------------------------------

	@PreAuthorize("@userroleAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		logger.info("Fetching Userrole with id {}", id);
		Userrole userrole = userroleService.getById(id);
		if (userrole == null) {
			logger.error("Userrole with id {} not found.", id);
			// return.
			return new ResponseEntity<CustomErrorType>(new CustomErrorType("Userrole with id " + id 
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		// return.
		return new ResponseEntity<Userrole>(userrole, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Userroles With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		logger.info("Fetching list userroles with criteria");
		List<Userrole> userroles = userroleService.listWithCritera(searchCriteria);
		if(userroles.isEmpty()) {
			logger.error("List of userroles is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<List<Userrole>>(userroles, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Userroles With Many Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		logger.info("Fetching list userroles with criteria");
		List<Userrole> userroles = userroleService.listWithCriteras(searchCriterias);
		if(userroles.isEmpty()) {
			logger.error("List of userroles is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<List<Userrole>>(userroles, HttpStatus.OK);
	}

	// -------------------Retrieve All Userroles By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Userrole>> listAllByPage(Pageable pageable) {
		Page<Userrole> userroles = userroleService.listAllByPage(pageable);
		if (!userroles.hasContent()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		// return.
		return new ResponseEntity<Page<Userrole>>(userroles, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Userroles With A Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		logger.info("Fetching list userroles with criteria");
		Page<Userrole> userroles = userroleService.listWithCriteraByPage(searchCriteria, pageable);
		if(!userroles.hasContent()) {
			logger.error("List of userroles is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<Page<Userrole>>(userroles, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Userroles With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@userroleAuthorize.canRead(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		logger.info("Fetching list userroles with criteria");
		Page<Userrole> userroles = userroleService.listWithCriterasByPage(searchCriterias, pageable);
		if(!userroles.hasContent()) {
			logger.error("List of userroles is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<Page<Userrole>>(userroles, HttpStatus.OK);
	}

}

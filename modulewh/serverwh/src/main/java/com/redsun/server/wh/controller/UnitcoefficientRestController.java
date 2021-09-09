
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

import com.redsun.server.wh.model.Unitcoefficient;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.service.UnitcoefficientService;
import com.redsun.server.wh.util.CustomErrorType;

@RestController
@RequestMapping("/unitcoefficient")
public class UnitcoefficientRestController {

	public static final Logger logger = LoggerFactory.getLogger(UnitcoefficientRestController.class);

	@Autowired
	UnitcoefficientService unitcoefficientService; //Service which will do all data retrieval/manipulation work

	// -------------------Create a Unitcoefficient-------------------------------------------

	@PreAuthorize("@unitcoefficientAuthorize.canCreate(#unitcoefficient)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Unitcoefficient unitcoefficient) {
		logger.info("Creating Unitcoefficient : {}", unitcoefficient);
		Unitcoefficient result = unitcoefficientService.save(unitcoefficient);
		// return.
		return new ResponseEntity<Integer>(result.getId(), HttpStatus.CREATED);
	}

	// -------------------Update a Unitcoefficient------------------------------------------------

	@PreAuthorize("@unitcoefficientAuthorize.canUpdate(#unitcoefficient)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Unitcoefficient unitcoefficient) {
		logger.info("Updating Unitcoefficient with id {}", id);
		Unitcoefficient result = unitcoefficientService.save(unitcoefficient);
		// return.
		return new ResponseEntity<Unitcoefficient>(result, HttpStatus.OK);
	}

	// -------------------Delete a Unitcoefficient-----------------------------------------

	@PreAuthorize("@unitcoefficientAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		logger.info("Fetching & Deleting Unitcoefficient with id {}", id);
		unitcoefficientService.deleteById(id);
		// return.	
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	// -------------------Retrieve All Unitcoefficients---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Unitcoefficient>> listAll() {
		List<Unitcoefficient> unitcoefficients = unitcoefficientService.listAll();
		if (unitcoefficients.isEmpty()) {
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		// return.
		return new ResponseEntity<List<Unitcoefficient>>(unitcoefficients, HttpStatus.OK);
	}

	// -------------------Retrieve Single Unitcoefficient------------------------------------------

	@PreAuthorize("@unitcoefficientAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		logger.info("Fetching Unitcoefficient with id {}", id);
		Unitcoefficient unitcoefficient = unitcoefficientService.getById(id);
		if (unitcoefficient == null) {
			logger.error("Unitcoefficient with id {} not found.", id);
			// return.
			return new ResponseEntity<CustomErrorType>(new CustomErrorType("Unitcoefficient with id " + id 
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		// return.
		return new ResponseEntity<Unitcoefficient>(unitcoefficient, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Unitcoefficients With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		logger.info("Fetching list unitcoefficients with criteria");
		List<Unitcoefficient> unitcoefficients = unitcoefficientService.listWithCritera(searchCriteria);
		if(unitcoefficients.isEmpty()) {
			logger.error("List of unitcoefficients is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<List<Unitcoefficient>>(unitcoefficients, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Unitcoefficients With Many Criteria------------------------------------------
	
	@PreAuthorize("@unitcoefficientAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		logger.info("Fetching list unitcoefficients with criteria");
		List<Unitcoefficient> unitcoefficients = unitcoefficientService.listWithCriteras(searchCriterias);
		if(unitcoefficients.isEmpty()) {
			logger.error("List of unitcoefficients is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<List<Unitcoefficient>>(unitcoefficients, HttpStatus.OK);
	}

	// -------------------Retrieve All Unitcoefficients By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Unitcoefficient>> listAllByPage(Pageable pageable) {
		Page<Unitcoefficient> unitcoefficients = unitcoefficientService.listAllByPage(pageable);
		if (!unitcoefficients.hasContent()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		// return.
		return new ResponseEntity<Page<Unitcoefficient>>(unitcoefficients, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Unitcoefficients With A Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		logger.info("Fetching list unitcoefficients with criteria");
		Page<Unitcoefficient> unitcoefficients = unitcoefficientService.listWithCriteraByPage(searchCriteria, pageable);
		if(!unitcoefficients.hasContent()) {
			logger.error("List of unitcoefficients is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<Page<Unitcoefficient>>(unitcoefficients, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Unitcoefficients With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@unitcoefficientAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		logger.info("Fetching list unitcoefficients with criteria");
		Page<Unitcoefficient> unitcoefficients = unitcoefficientService.listWithCriterasByPage(searchCriterias, pageable);
		if(!unitcoefficients.hasContent()) {
			logger.error("List of unitcoefficients is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<Page<Unitcoefficient>>(unitcoefficients, HttpStatus.OK);
	}

}

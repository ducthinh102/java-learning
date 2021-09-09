
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

import com.redsun.server.wh.model.Materialamount;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.service.MaterialamountService;
import com.redsun.server.wh.util.CustomErrorType;

@RestController
@RequestMapping("/materialamount")
public class MaterialamountRestController {

	public static final Logger logger = LoggerFactory.getLogger(MaterialamountRestController.class);

	@Autowired
	MaterialamountService materialamountService; //Service which will do all data retrieval/manipulation work

	// -------------------Create a Materialamount-------------------------------------------

	@PreAuthorize("@materialamountAuthorize.canCreate(#materialamount)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Materialamount materialamount) {
		logger.info("Creating Materialamount : {}", materialamount);
		Materialamount result = materialamountService.save(materialamount);
		// return.
		return new ResponseEntity<Integer>(result.getId(), HttpStatus.CREATED);
	}

	// -------------------Update a Materialamount------------------------------------------------

	@PreAuthorize("@materialamountAuthorize.canUpdate(#materialamount)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Materialamount materialamount) {
		logger.info("Updating Materialamount with id {}", id);
		Materialamount result = materialamountService.save(materialamount);
		// return.
		return new ResponseEntity<Materialamount>(result, HttpStatus.OK);
	}

	// -------------------Delete a Materialamount-----------------------------------------

	@PreAuthorize("@materialamountAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		logger.info("Fetching & Deleting Materialamount with id {}", id);
		materialamountService.deleteById(id);
		// return.	
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	// -------------------Retrieve All Materialamounts---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Materialamount>> listAll() {
		List<Materialamount> materialamounts = materialamountService.listAll();
		if (materialamounts.isEmpty()) {
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		// return.
		return new ResponseEntity<List<Materialamount>>(materialamounts, HttpStatus.OK);
	}

	// -------------------Retrieve Single Materialamount------------------------------------------

	@PreAuthorize("@materialamountAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		logger.info("Fetching Materialamount with id {}", id);
		Materialamount materialamount = materialamountService.getById(id);
		if (materialamount == null) {
			logger.error("Materialamount with id {} not found.", id);
			// return.
			return new ResponseEntity<CustomErrorType>(new CustomErrorType("Materialamount with id " + id 
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		// return.
		return new ResponseEntity<Materialamount>(materialamount, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialamounts With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		logger.info("Fetching list materialamounts with criteria");
		List<Materialamount> materialamounts = materialamountService.listWithCritera(searchCriteria);
		if(materialamounts.isEmpty()) {
			logger.error("List of materialamounts is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<List<Materialamount>>(materialamounts, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialamounts With Many Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		logger.info("Fetching list materialamounts with criteria");
		List<Materialamount> materialamounts = materialamountService.listWithCriteras(searchCriterias);
		if(materialamounts.isEmpty()) {
			logger.error("List of materialamounts is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<List<Materialamount>>(materialamounts, HttpStatus.OK);
	}

	// -------------------Retrieve All Materialamounts By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Materialamount>> listAllByPage(Pageable pageable) {
		Page<Materialamount> materialamounts = materialamountService.listAllByPage(pageable);
		if (!materialamounts.hasContent()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		// return.
		return new ResponseEntity<Page<Materialamount>>(materialamounts, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialamounts With A Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		logger.info("Fetching list materialamounts with criteria");
		Page<Materialamount> materialamounts = materialamountService.listWithCriteraByPage(searchCriteria, pageable);
		if(!materialamounts.hasContent()) {
			logger.error("List of materialamounts is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<Page<Materialamount>>(materialamounts, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialamounts With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@materialamountAuthorize.canRead(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		logger.info("Fetching list materialamounts with criteria");
		Page<Materialamount> materialamounts = materialamountService.listWithCriterasByPage(searchCriterias, pageable);
		if(!materialamounts.hasContent()) {
			logger.error("List of materialamounts is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<Page<Materialamount>>(materialamounts, HttpStatus.OK);
	}

}

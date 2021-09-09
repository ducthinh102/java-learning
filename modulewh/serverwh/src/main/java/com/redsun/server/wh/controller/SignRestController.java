
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

import com.redsun.server.wh.model.Sign;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.service.SignService;
import com.redsun.server.wh.util.CustomErrorType;

@RestController
@RequestMapping("/sign")
public class SignRestController {

	public static final Logger logger = LoggerFactory.getLogger(SignRestController.class);

	@Autowired
	SignService signService; //Service which will do all data retrieval/manipulation work

	// -------------------Create a Sign-------------------------------------------

	@PreAuthorize("@signAuthorize.canCreate(#sign)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Sign sign) {
		logger.info("Creating Sign : {}", sign);
		Sign result = signService.save(sign);
		// return.
		return new ResponseEntity<Integer>(result.getId(), HttpStatus.CREATED);
	}

	// -------------------Update a Sign------------------------------------------------

	@PreAuthorize("@signAuthorize.canUpdate(#sign)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Sign sign) {
		logger.info("Updating Sign with id {}", id);
		Sign result = signService.save(sign);
		// return.
		return new ResponseEntity<Sign>(result, HttpStatus.OK);
	}

	// -------------------Delete a Sign-----------------------------------------

	@PreAuthorize("@signAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		logger.info("Fetching & Deleting Sign with id {}", id);
		signService.deleteById(id);
		// return.	
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	// -------------------Retrieve All Signs---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Sign>> listAll() {
		List<Sign> signs = signService.listAll();
		if (signs.isEmpty()) {
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		// return.
		return new ResponseEntity<List<Sign>>(signs, HttpStatus.OK);
	}

	// -------------------Retrieve Single Sign------------------------------------------

	@PreAuthorize("@signAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		logger.info("Fetching Sign with id {}", id);
		Sign sign = signService.getById(id);
		if (sign == null) {
			logger.error("Sign with id {} not found.", id);
			// return.
			return new ResponseEntity<CustomErrorType>(new CustomErrorType("Sign with id " + id 
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		// return.
		return new ResponseEntity<Sign>(sign, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Signs With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		logger.info("Fetching list signs with criteria");
		List<Sign> signs = signService.listWithCritera(searchCriteria);
		if(signs.isEmpty()) {
			logger.error("List of signs is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<List<Sign>>(signs, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Signs With Many Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		logger.info("Fetching list signs with criteria");
		List<Sign> signs = signService.listWithCriteras(searchCriterias);
		if(signs.isEmpty()) {
			logger.error("List of signs is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<List<Sign>>(signs, HttpStatus.OK);
	}

	// -------------------Retrieve All Signs By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Sign>> listAllByPage(Pageable pageable) {
		Page<Sign> signs = signService.listAllByPage(pageable);
		if (!signs.hasContent()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		// return.
		return new ResponseEntity<Page<Sign>>(signs, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Signs With A Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		logger.info("Fetching list signs with criteria");
		Page<Sign> signs = signService.listWithCriteraByPage(searchCriteria, pageable);
		if(!signs.hasContent()) {
			logger.error("List of signs is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<Page<Sign>>(signs, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Signs With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@signAuthorize.canRead(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		logger.info("Fetching list signs with criteria");
		Page<Sign> signs = signService.listWithCriterasByPage(searchCriterias, pageable);
		if(!signs.hasContent()) {
			logger.error("List of signs is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<Page<Sign>>(signs, HttpStatus.OK);
	}

}

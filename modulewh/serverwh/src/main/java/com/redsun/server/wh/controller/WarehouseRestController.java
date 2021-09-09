
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

import com.redsun.server.wh.model.Warehouse;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.service.WarehouseService;
import com.redsun.server.wh.util.CustomErrorType;

@RestController
@RequestMapping("/warehouse")
public class WarehouseRestController {

	public static final Logger logger = LoggerFactory.getLogger(WarehouseRestController.class);

	@Autowired
	WarehouseService warehouseService; //Service which will do all data retrieval/manipulation work

	// -------------------Create a Warehouse-------------------------------------------

	@PreAuthorize("@warehouseAuthorize.canCreate(#warehouse)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Warehouse warehouse, UriComponentsBuilder ucBuilder) {
		logger.info("Creating Warehouse : {}", warehouse);
		Warehouse result = warehouseService.save(warehouse);
		// return.
		return new ResponseEntity<Warehouse>(result, HttpStatus.CREATED);
	}

	// -------------------Update a Warehouse------------------------------------------------

	@PreAuthorize("@warehouseAuthorize.canUpdate(#warehouse)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Warehouse warehouse) {
		logger.info("Updating Warehouse with id {}", id);
		Warehouse result = warehouseService.save(warehouse);
		// return.
		return new ResponseEntity<Warehouse>(result, HttpStatus.OK);
	}

	// -------------------Delete a Warehouse-----------------------------------------

	@PreAuthorize("@warehouseAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		logger.info("Fetching & Deleting Warehouse with id {}", id);
		warehouseService.deleteById(id);
		// return.	
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}

	// -------------------Retrieve All Warehouse---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Warehouse>> listAll() {
		List<Warehouse> warehouses = warehouseService.listAll();
		if (warehouses.isEmpty()) {
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		// return.
		return new ResponseEntity<List<Warehouse>>(warehouses, HttpStatus.OK);
	}

	// -------------------Retrieve Single Warehouses------------------------------------------

	@PreAuthorize("@warehouseAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		logger.info("Fetching Warehouse with id {}", id);
		Warehouse warehouse = warehouseService.getById(id);
		if (warehouse == null) {
			logger.error("Warehouse with id {} not found.", id);
			// return.
			return new ResponseEntity<CustomErrorType>(new CustomErrorType("Warehouse with id " + id 
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		// return.
		return new ResponseEntity<Warehouse>(warehouse, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Warehouses With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		logger.info("Fetching list warehouses with criteria");
		List<Warehouse> warehouses = warehouseService.listWithCritera(searchCriteria);
		if(warehouses.isEmpty()) {
			logger.error("List of warehouses is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<List<Warehouse>>(warehouses, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Warehouses With Many Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		logger.info("Fetching list warehouses with criteria");
		List<Warehouse> warehouses = warehouseService.listWithCriteras(searchCriterias);
		if(warehouses.isEmpty()) {
			logger.error("List of warehouses is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<List<Warehouse>>(warehouses, HttpStatus.OK);
	}

	// -------------------Retrieve All Warehouses By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Warehouse>> listAllByPage(Pageable pageable) {
		Page<Warehouse> warehouses = warehouseService.listAllByPage(pageable);
		if (!warehouses.hasContent()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		// return.
		return new ResponseEntity<Page<Warehouse>>(warehouses, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Warehouses With A Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		logger.info("Fetching list warehouses with criteria");
		Page<Warehouse> warehouses = warehouseService.listWithCriteraByPage(searchCriteria, pageable);
		if(!warehouses.hasContent()) {
			logger.error("List of warehouses is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<Page<Warehouse>>(warehouses, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Warehouses With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@warehouseAuthorize.canRead(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		logger.info("Fetching list warehouses with criteria");
		Page<Warehouse> warehouses = warehouseService.listWithCriterasByPage(searchCriterias, pageable);
		if(!warehouses.hasContent()) {
			logger.error("List of warehouses is empty.");
			// return.
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		// return.
		return new ResponseEntity<Page<Warehouse>>(warehouses, HttpStatus.OK);
	}

}

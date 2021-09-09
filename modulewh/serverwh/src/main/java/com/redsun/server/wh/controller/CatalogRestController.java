package com.redsun.server.wh.controller;

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
import com.redsun.server.wh.model.Catalog;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.service.CatalogService;

@RestController
@RequestMapping("/catalog")
public class CatalogRestController {

	@Autowired
	CatalogService catalogService;

	// -------------------Create a Catalog-------------------------------------------

	@PreAuthorize("@catalogAuthorize.canCreate(#catalog)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Catalog catalog) throws JsonParseException, JsonMappingException, IOException {
		Catalog result = catalogService.create(catalog);
		// return.
		return new ResponseEntity<Integer>(result.getId(), HttpStatus.OK);

	}

	// -------------------Update lock a Catalog------------------------------------------------

		@PreAuthorize("@catalogAuthorize.canUpdate(#id)")
		@RequestMapping(value = "/updateLock/{id}", method = RequestMethod.PUT)
		public ResponseEntity<?> updateLock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
			Integer result = catalogService.updateLock(id);
			// return.
			return new ResponseEntity<Integer>(result, HttpStatus.OK);
		}

		// -------------------Update Unlock a Catalog------------------------------------------------

		@PreAuthorize("@catalogAuthorize.canUpdate(#id)")
		@RequestMapping(value = "/updateUnlock/{id}", method = RequestMethod.PUT)
		public ResponseEntity<?> updateUnlock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
			Integer result = catalogService.updateUnlock(id);
			// return.
			return new ResponseEntity<Integer>(result, HttpStatus.OK);
		}

		// -------------------Update a Catalog------------------------------------------------

		@PreAuthorize("@catalogAuthorize.canUpdate(#catalog)")
		@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
		public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Catalog catalog) throws JsonParseException, JsonMappingException, IOException {
			Catalog result = catalogService.update(id, catalog);
			// return.
			return new ResponseEntity<Integer>(result.getId(), HttpStatus.OK);
		}

		// -------------------Update a Catalog With Lock------------------------------------------------

		@PreAuthorize("@catalogAuthorize.canUpdate(#catalog)")
		@RequestMapping(value = "/updateWithLock/{id}", method = RequestMethod.PUT)
		public ResponseEntity<?> updateWithLock(@PathVariable("id") Integer id, @RequestBody  Catalog catalog) throws JsonParseException, JsonMappingException, IOException {
			Catalog result = catalogService.updateWithLock(id, catalog);
			// return.
			return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
		}
		// -------------------Update For Delete a Catalog------------------------------------------------

		@PreAuthorize("@catalogAuthorize.canUpdateForDelete(#id, #version)")
		@RequestMapping(value = "/updateForDelete/{id}/{version}", method = RequestMethod.PUT)
		public ResponseEntity<?> updateForDelete(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
			Catalog result = catalogService.updateForDelete(id, version);
			// return.
			return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
		}

		// -------------------Update For Delete a Catalog With Lock------------------------------------------------

		@PreAuthorize("@catalogAuthorize.canUpdateForDelete(#id, #version)")
		@RequestMapping(value = "/updateForDeleteWithLock/{id}/{version}", method = RequestMethod.PUT)
		public ResponseEntity<?> updateForDeleteWithLock(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
			Catalog result = catalogService.updateForDeleteWithLock(id, version);
			// return.
			return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
		}

	// -------------------Retrieve All Catalog---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Catalog>> listAll() {

		List<Catalog> catalog = catalogService.listAll();
		// return.
		return new ResponseEntity<List<Catalog>>(catalog, HttpStatus.OK);

	}

	// -------------------Retrieve Single Catalog------------------------------------------

	@PreAuthorize("@catalogAuthorize.canRead(#id,#scope)")
	@RequestMapping(value = "/getById/{id}/{scope}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id, @PathVariable("scope") String scope) {

		Catalog catalog = catalogService.getById(id);
		// return.
		return new ResponseEntity<Catalog>(catalog, HttpStatus.OK);

	}
	
	//@PreAuthorize("@catalogAuthorize.canRead(#id,#scope)")
	@RequestMapping(value = "/getList/{scope}", method = RequestMethod.GET)
	public ResponseEntity<List<Catalog>> getListByScope(@PathVariable("scope") String scope) {

		List<Catalog> catalog = catalogService.getListByScope(scope);
		// return.
		return new ResponseEntity<List<Catalog>>(catalog, HttpStatus.OK);

	}

	// -------------------Retrieve List Of Catalog With An Criteria------------------------------------------

	@RequestMapping(value = "/listWithCriteria/{scope}", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria,@PathVariable("scope")String scope) {

		List<Catalog> catalog = catalogService.listWithCritera(searchCriteria,scope);
		// return.
		return new ResponseEntity<List<Catalog>>(catalog, HttpStatus.OK);

	}

	// -------------------Retrieve List Of Catalog With Many Criteria------------------------------------------

	@PreAuthorize("@catalogAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriterias/{scope}", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias,@PathVariable("scope")String scope) {

		List<Catalog> catalog = catalogService.listWithCriteras(searchCriterias,scope);
		// return.
		return new ResponseEntity<List<Catalog>>(catalog, HttpStatus.OK);

	}

	// -------------------Retrieve All Catalog By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Catalog>> listAllByPage(Pageable pageable) {

		Page<Catalog> catalog = catalogService.listAllByPage(pageable);
		// return.
		return new ResponseEntity<Page<Catalog>>(catalog, HttpStatus.OK);

	}

	// -------------------Retrieve List Of Catalog With A Criteria By Page------------------------------------------

	@RequestMapping(value = "/listWithCriteriaByPage/{scope}", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable,@PathVariable("scope")String scope) {

		Page<Catalog> catalog = catalogService.listWithCriteraByPage(searchCriteria, pageable,scope);
		// return.
		return new ResponseEntity<Page<Catalog>>(catalog, HttpStatus.OK);

	}

	// -------------------Retrieve List Of Catalog With Multiple Criteria By Page------------------------------------------

	@PreAuthorize("@catalogAuthorize.canList(#searchCriterias, #scope)")
	@RequestMapping(value = "/listWithCriteriasByPage/{scope}", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias,
			Pageable pageable,@PathVariable("scope")String scope) {

		Page<Catalog> catalog = catalogService.listWithCriterasByPage(searchCriterias, pageable,scope);
		// return.
		return new ResponseEntity<Page<Catalog>>(catalog, HttpStatus.OK);

	}
	
	// -------------------------Retrieve All User For Select--------------------------------------------------- 
	@RequestMapping(value = "/listAllForSelect/{scope}", method = RequestMethod.GET)
	public ResponseEntity<List<Map<String, Object>>> listAllForSelect(@PathVariable("scope")String scope) {
		List<Map<String, Object>> catalogs = catalogService.listAllForSelectByScope(scope);
		// return.
		return new ResponseEntity<List<Map<String, Object>>>(catalogs, HttpStatus.OK);
	}
}

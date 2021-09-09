
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
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.redsun.server.wh.model.Material;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.service.MaterialService;

@RestController
@RequestMapping("/material")
public class MaterialRestController {

	public static final Logger logger = LoggerFactory.getLogger(MaterialRestController.class);

	@Autowired
	MaterialService materialService; //Service which will do all data retrieval/manipulation work

	// -------------------Create a Material-------------------------------------------

		@PreAuthorize("@materialAuthorize.canCreate(#material)")
		@RequestMapping(value = "/create", method = RequestMethod.POST)
		public ResponseEntity<?> create(@RequestBody Material material) throws JsonParseException, JsonMappingException, IOException {
			Material result = materialService.create(material);
			// return.
			return new ResponseEntity<Integer>(result.getId(), HttpStatus.OK);
		}

		// -------------------Update lock a Material------------------------------------------------

		@PreAuthorize("@materialAuthorize.canUpdate(#material)")
		@RequestMapping(value = "/updateLock/{id}", method = RequestMethod.PUT)
		public ResponseEntity<?> updateLock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
			Integer result = materialService.updateLock(id);
			// return.
			return new ResponseEntity<Integer>(result, HttpStatus.OK);
		}

		// -------------------Update Unlock a Material------------------------------------------------

		@PreAuthorize("@materialAuthorize.canUpdate(#material)")
		@RequestMapping(value = "/updateUnlock/{id}", method = RequestMethod.PUT)
		public ResponseEntity<?> updateUnlock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
			Integer result = materialService.updateUnlock(id);
			// return.
			return new ResponseEntity<Integer>(result, HttpStatus.OK);
		}

		// -------------------Update a Material------------------------------------------------

		@PreAuthorize("@materialAuthorize.canUpdate(#material)")
		@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
		public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Material material) throws Exception {
			Material result = materialService.update(id, material);
			// return.
			return new ResponseEntity<Integer>(result.getId(), HttpStatus.OK);
		}

		// -------------------Update a Material With Lock------------------------------------------------

		@PreAuthorize("@materialAuthorize.canUpdate(#material)")
		@RequestMapping(value = "/updateWithLock/{id}", method = RequestMethod.PUT)
		public ResponseEntity<?> updateWithLock(@PathVariable("id") Integer id, @RequestBody Material material) throws JsonParseException, JsonMappingException, IOException {
			Material result = materialService.updateWithLock(id, material);
			// return.
			return new ResponseEntity<Integer>(result.getId(), HttpStatus.OK);
		}
		
		// -------------------Update For Delete With Lock------------------------------------------------

		@PreAuthorize("@materialAuthorize.canUpdateForDelete(#id, #version)")
		@RequestMapping(value = "/updateForDeleteWithLock/{id}/{version}", method = RequestMethod.PUT)
		public ResponseEntity<?> updateForDeleteWithLock(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
			Material result = materialService.updateForDeleteWithLock(id, version);
			// return.
			return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
		}

		// -------------------Delete a Material-----------------------------------------

		@PreAuthorize("@materialAuthorize.canDelete(#id)")
		@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
		public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
			materialService.deleteById(id);
			// return.	
			return new ResponseEntity<Void>(HttpStatus.OK);
		}


	// -------------------Retrieve All Materials---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Material>> listAll() {
		List<Material> materials = materialService.listAll();
		// return.
		return new ResponseEntity<List<Material>>(materials, HttpStatus.OK);
	}

	// -------------------Retrieve Single Material------------------------------------------

	@PreAuthorize("@materialAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		logger.info("Fetching Material with id {}", id);
		Material material = materialService.getById(id);
		// return.
		return new ResponseEntity<Material>(material, HttpStatus.OK);
	}
	
	// -------------------Retrieve Single Material------------------------------------------

	@PreAuthorize("@materialAuthorize.canRead(#id)")
	@RequestMapping(value = "/getByIdForView/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getByIdForView(@PathVariable("id") Integer id) {
		Map<String, Object> material = materialService.getByIdForView(id);
		// return.
		return new ResponseEntity<Map<String, Object>>(material, HttpStatus.OK);
	}
	
	// -------------------Retrieve Single Material Sub------------------------------------------

		@PreAuthorize("@materialAuthorize.canRead(#id)")
		@RequestMapping(value = "/getByIdForViewSub/{id}", method = RequestMethod.GET)
		public ResponseEntity<?> getByIdForViewSub(@PathVariable("id") Integer id) {
			Map<String, Object> material = materialService.getByIdForViewSub(id);
			// return.
			return new ResponseEntity<Map<String, Object>>(material, HttpStatus.OK);
		}
	
	// -------------------Retrieve List Of Materials With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		logger.info("Fetching list materials with criteria");
		List<Material> materials = materialService.listWithCritera(searchCriteria);
		// return.
		return new ResponseEntity<List<Material>>(materials, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materials With Many Criteria------------------------------------------
	
	@PreAuthorize("@materialAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		logger.info("Fetching list materials with criteria");
		List<Material> materials = materialService.listWithCriteras(searchCriterias);
		// return.
		return new ResponseEntity<List<Material>>(materials, HttpStatus.OK);
	}

	// -------------------Retrieve All Materials By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Material>> listAllByPage(Pageable pageable) {
		Page<Material> materials = materialService.listAllByPage(pageable);
		// return.
		return new ResponseEntity<Page<Material>>(materials, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materials With A Criteria By Page------------------------------------------
	
	@PreAuthorize("@materialAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		logger.info("Fetching list materials with criteria");
		Page<Material> materials = materialService.listWithCriteraByPage(searchCriteria, pageable);
		// return.
		return new ResponseEntity<Page<Material>>(materials, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materials With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@materialAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		logger.info("Fetching list materials with criteria");
		Page<Material> materials = materialService.listWithCriterasByPage(searchCriterias, pageable);
		// return.
		return new ResponseEntity<Page<Material>>(materials, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materials With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@materialAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByIdrefAndReftypeAndPage/{idref}/{reftype}", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByIdrefAndReftypeAndPage(@PathVariable("idref") Integer idref, @PathVariable("reftype") String reftype, @RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		Page<Map<String, Object>> materials = materialService.listWithCriteriasByIdrefAndReftypeAndPage(idref, reftype, searchCriterias, pageable);
		// return.
		return new ResponseEntity<Page<Map<String, Object>>>(materials, HttpStatus.OK);
	}
	
	// -------------------------Retrieve All User For Select--------------------------------------------------- 
	
		@RequestMapping(value = "/loadAllMaterialConfirmed", method = RequestMethod.GET)
		public ResponseEntity<List<Map<String, Object>>> loadAllMaterialConfirmed() {
			List<Map<String, Object>> materials = materialService.loadAllMaterialConfirmed();
			// return.
			return new ResponseEntity<List<Map<String, Object>>>(materials, HttpStatus.OK);
		}
		
	// -------------------------Retrieve All User For Select--------------------------------------------------- 
		
	@RequestMapping(value = "/loadAllFromBaselineByIdstore/{idstore}", method = RequestMethod.GET)
	public ResponseEntity<List<Map<String, Object>>> loadAllFromBaselineByIdstore(@PathVariable("idstore") Integer idstore) {
		List<Map<String, Object>> materials = materialService.loadAllFromBaselineByIdstore(idstore);
		// return.
		return new ResponseEntity<List<Map<String, Object>>>(materials, HttpStatus.OK);
	}
	
		// -------------------------Retrieve All User For Select--------------------------------------------------- 
		
		@RequestMapping(value = "/listAllForSelect", method = RequestMethod.GET)
		public ResponseEntity<List<Map<String, Object>>> listAllForSelect() {
			List<Map<String, Object>> materials = materialService.listAllForSelect();
			// return.
			return new ResponseEntity<List<Map<String, Object>>>(materials, HttpStatus.OK);
		}
		

}


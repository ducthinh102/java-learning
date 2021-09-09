
package com.redsun.server.wh.controller;

import java.io.IOException;
import java.util.ArrayList;
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
import com.redsun.server.wh.model.Materialstore;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.service.MaterialstoreService;


@RestController
@RequestMapping("/materialstore")
public class MaterialstoreRestController {

	@Autowired
	MaterialstoreService materialstoreService; //Service which will do all data retrieval/manipulation work

	// -------------------Create a Materialstore-------------------------------------------

//	@PreAuthorize("@materialstoreAuthorize.canCreate(#materialstore)")
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<?> create(@RequestBody Materialstore materialstore) throws JsonParseException, JsonMappingException, IOException {
		Materialstore result = materialstoreService.create(materialstore);
		// return.
		return new ResponseEntity<Integer>(result.getId(), HttpStatus.OK);
	}

	// -------------------Update lock a Materialstore------------------------------------------------

	@PreAuthorize("@materialstoreAuthorize.canUpdate(#materialstore)")
	@RequestMapping(value = "/updateLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateLock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = materialstoreService.updateLock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}

	// -------------------Update Unlock a Materialstore------------------------------------------------

	@PreAuthorize("@materialstoreAuthorize.canUpdate(#materialstore)")
	@RequestMapping(value = "/updateUnlock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateUnlock(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer result = materialstoreService.updateUnlock(id);
		// return.
		return new ResponseEntity<Integer>(result, HttpStatus.OK);
	}
	
	// -------------------Update a Materialstore With Lock------------------------------------------------

	@PreAuthorize("@materialstoreAuthorize.canUpdate(#materialstore)")
	@RequestMapping(value = "/updateWithLock/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateWithLock(@PathVariable("id") Integer id, @RequestBody Materialstore materialstore) throws JsonParseException, JsonMappingException, IOException {
		Materialstore result = materialstoreService.updateWithLock(id, materialstore);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete------------------------------------------------

	@PreAuthorize("@materialstoreAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDelete/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDelete(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Materialstore result = materialstoreService.updateForDelete(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Update For Delete With Lock------------------------------------------------

	@PreAuthorize("@materialstoreAuthorize.canUpdateForDelete(#id, #version)")
	@RequestMapping(value = "/updateForDeleteWithLock/{id}/{version}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateForDeleteWithLock(@PathVariable("id") Integer id, @PathVariable("version") Integer version) throws JsonParseException, JsonMappingException, IOException {
		Materialstore result = materialstoreService.updateForDeleteWithLock(id, version);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}
	
	// -------------------Update a Materialstore------------------------------------------------

	@PreAuthorize("@materialstoreAuthorize.canUpdate(#materialstore)")
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Materialstore materialstore) throws JsonParseException, JsonMappingException, IOException {
		Materialstore result = materialstoreService.update(id, materialstore);
		// return.
		return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
	}

	// -------------------Delete a Materialstore-----------------------------------------

	@PreAuthorize("@materialstoreAuthorize.canDelete(#id)")
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Integer id) {
		materialstoreService.deleteById(id);
		// return.	
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	// -------------------Retrieve All Materialstores---------------------------------------------

	@RequestMapping(value = "/listAll", method = RequestMethod.GET)
	public ResponseEntity<List<Materialstore>> listAll() {
		List<Materialstore> materialstores = materialstoreService.listAll();
		// return.
		return new ResponseEntity<List<Materialstore>>(materialstores, HttpStatus.OK);
	}

	// -------------------Retrieve Single Materialstore------------------------------------------

	@PreAuthorize("@materialstoreAuthorize.canRead(#id)")
	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		Materialstore materialstore = materialstoreService.getById(id);
		// return.
		return new ResponseEntity<Materialstore>(materialstore, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialstores With A Criteria------------------------------------------
	
	@RequestMapping(value = "/listWithCriteria", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteria(@RequestBody SearchCriteria searchCriteria) {
		List<Materialstore> materialstores = materialstoreService.listWithCritera(searchCriteria);
		// return.
		return new ResponseEntity<List<Materialstore>>(materialstores, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialstores With Many Criteria------------------------------------------
	
	@PreAuthorize("@materialstoreAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriterias", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriterias(@RequestBody List<SearchCriteria> searchCriterias) {
		List<Materialstore> materialstores = materialstoreService.listWithCriteras(searchCriterias);
		// return.
		return new ResponseEntity<List<Materialstore>>(materialstores, HttpStatus.OK);
	}

	// -------------------Retrieve All Materialstores By Page---------------------------------------------

	@RequestMapping(value = "/listAllByPage", method = RequestMethod.GET)
	public ResponseEntity<Page<Materialstore>> listAllByPage(Pageable pageable) {
		Page<Materialstore> materialstores = materialstoreService.listAllByPage(pageable);
		// return.
		return new ResponseEntity<Page<Materialstore>>(materialstores, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialstores With A Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriaByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriaByPage(@RequestBody SearchCriteria searchCriteria, Pageable pageable) {
		Page<Materialstore> materialstores = materialstoreService.listWithCriteraByPage(searchCriteria, pageable);
		// return.
		return new ResponseEntity<Page<Materialstore>>(materialstores, HttpStatus.OK);
	}
	
	// -------------------Retrieve List Of Materialstores With Multiple Criteria By Page------------------------------------------
	
	@PreAuthorize("@materialstoreAuthorize.canList(#searchCriterias)")
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		Page<Materialstore> materialstores = materialstoreService.listWithCriterasByPage(searchCriterias, pageable);
		// return.
		return new ResponseEntity<Page<Materialstore>>(materialstores, HttpStatus.OK);
	}
	
	// -------------------Retrieve Single Materialstore------------------------------------------

		@RequestMapping(value = "/getQuantityByMaterial/{idstore}/{idmaterial}", method = RequestMethod.GET)
		public ResponseEntity<?> getQuantityByMaterial(@PathVariable("idstore") Integer idstore,@PathVariable("idmaterial")Integer idmaterial) {
			Map<String, Object> materialstore = materialstoreService.quantityMaterialByidStore(idstore, idmaterial);
			// return.
			return new ResponseEntity<Map<String, Object>>(materialstore, HttpStatus.OK);
		}
		
//		@PreAuthorize("@materialstoreAuthorize.canUpdate(#materialstore)")
		@RequestMapping(value = "/updateWithLockForImport/{id}", method = RequestMethod.PUT)
		public ResponseEntity<?> updateWithLockForImport(@PathVariable("id") Integer id, @RequestBody Materialstore materialstore) throws JsonParseException, JsonMappingException, IOException {
			Materialstore result = materialstoreService.updateWithLockForImport(id, materialstore);
			// return.
			return new ResponseEntity<Integer>(result.getVersion(), HttpStatus.OK);
		}
		
		
		// -------------------Retrieve List Of MaterialStores With Multiple Criteria By Page------------------------------------------
//		@PreAuthorize("@materialstoreAuthorize.canList(#searchCriterias)")
		@RequestMapping(value = "/listWithCriteriasByIdStoreimportAndPage/{idstore}", method = RequestMethod.POST)
		public ResponseEntity<?> listWithCriteriasByIdStoreAndPage(@PathVariable("idstore") Integer idstore, @RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
			Page<Map<String, Object>> materialstores = materialstoreService.listWithCriteriasByIdStoreAndPage(idstore, searchCriterias, pageable);
			// return.
			return new ResponseEntity<Page<Map<String, Object>>>(materialstores, HttpStatus.OK);
		}
		
		// -------------------Retrieve List Of MaterialStores With Multiple Criteria By Page------------------------------------------
//		@PreAuthorize("@materialstoreAuthorize.canList(#searchCriterias)")
		@RequestMapping(value = "/listWithCriteriasByIdMaterialimportAndPage/{idmaterial}", method = RequestMethod.POST)
		public ResponseEntity<?> listWithCriteriasByIdMaterialAndPage(@PathVariable("idmaterial") Integer idmaterial, @RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
			Page<Map<String, Object>> materialstores = materialstoreService.listWithCriteriasByIdMaterialAndPage(idmaterial, searchCriterias, pageable);
			// return.
			return new ResponseEntity<Page<Map<String, Object>>>(materialstores, HttpStatus.OK);
		}
		
		@RequestMapping(value = "/checkQuantityMaterial/{idstore}", method = RequestMethod.POST)
		public ResponseEntity<?> checkQuantityMaterial(@RequestBody List<Materialstore> materialstores, @PathVariable("idstore") Integer idstore ) {
			List<Map<String, Object>> quantitymaterials = new ArrayList<Map<String, Object>>();
			for (Materialstore materialstore : materialstores) {
				Map<String, Object> quantitymaterial = materialstoreService.checkQuantityMaterial(idstore, materialstore.getIdmaterial());
				if (quantitymaterial.size()!=0) {
					quantitymaterials.add(quantitymaterial);
				}
			}		
			// return.
			return new ResponseEntity<List<Map<String, Object>>>(quantitymaterials, HttpStatus.OK);
		}
		
}


package com.redsun.server.wh.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.redsun.server.wh.util.CustomErrorType;
import com.redsun.server.wh.util.SecurityUtil;

@RestController
@RequestMapping("/materialconfirm")
public class MaterialConfirmRestController {

	@Autowired
	MaterialService materialService; //Service which will do all data retrieval/manipulation work

	// -------------------Retrieve List Of Unconfirmed Materials With Multiple Criteria By Page------------------------------------------
	
	@RequestMapping(value = "/listWithCriteriasByPage", method = RequestMethod.POST)
	public ResponseEntity<?> listWithCriteriasByPage(@RequestBody List<SearchCriteria> searchCriterias, Pageable pageable) {
		Page<Material> unconfirmmaterials = materialService.listUnconfirmedWithCriterasByPage(searchCriterias, pageable);
		if(!unconfirmmaterials.hasContent()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<Page<Material>>(unconfirmmaterials, HttpStatus.OK);
	}

	// -------------------Update List Of Unconfirmed Materials---------------------------------------------

	@RequestMapping(value = "/updateUnconfirmedItems", method = RequestMethod.POST)
	public ResponseEntity<List<Material>> updateUnconfirmedItems(@RequestBody List<Integer> ids) throws JsonParseException, JsonMappingException, IOException {
		List<Material> confirmedMaterials = materialService.updateUnconfirmedItems(ids);
		if (confirmedMaterials.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Material>>(confirmedMaterials, HttpStatus.OK);
	}

	// -------------------Retrieve Single Unconfirmed Material------------------------------------------

	@RequestMapping(value = "/getById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getById(@PathVariable("id") Integer id) {
		Material unconfirmmaterial = materialService.getById(id);
		if (unconfirmmaterial == null) {
			return new ResponseEntity<CustomErrorType>(new CustomErrorType("Unconfirmed Material with id " + id 
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Material>(unconfirmmaterial, HttpStatus.OK);
	}
	
	// -------------------Retrieve Single Unconfirmed Material And Lock Item------------------------------------------

	@RequestMapping(value = "/getAndLockById/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getAndLockById(@PathVariable("id") Integer id) throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> userinfo = SecurityUtil.getUserInfo();
		Integer iduser = Integer.parseInt(userinfo.get("iduser").toString());
		Material unconfirmmaterial = materialService.getAndLockById(id, iduser);
		if (unconfirmmaterial == null) {
			return new ResponseEntity<CustomErrorType>(new CustomErrorType("Unconfirmed Material with id " + id 
					+ " not found"), HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<Material>(unconfirmmaterial, HttpStatus.OK);
	}

	// -------------------Get A Confirmed Material By Code------------------------------------------------

	@RequestMapping(value = "/getConfirmedItemByCode", method = RequestMethod.POST)
	public ResponseEntity<Material> getConfirmedItemByCode(@RequestBody String code) throws JsonParseException, JsonMappingException, IOException {
		Material material = materialService.getConfirmedItemByCode(code);
		if (material == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		return new ResponseEntity<Material>(material, HttpStatus.OK);
	}

	// -------------------Retrieve Confirmed Materials By Name------------------------------------------

	@RequestMapping(value = "/listConfirmedItemsByName", method = RequestMethod.POST)
	public ResponseEntity<?> listConfirmedMaterialsByName(@RequestBody String name) {
		List<Material> confirmedMaterials = materialService.findConfirmedItemsByName(name);
		return new ResponseEntity<List<Material>>(confirmedMaterials, HttpStatus.OK);
	}

	// -------------------Update An Unconfirmed Material------------------------------------------------

	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> update(@PathVariable("id") Integer id, @RequestBody Material unconfirmedItem) throws JsonParseException, JsonMappingException, IOException {
		Material result = materialService.updateUnconfirmedItem(unconfirmedItem);
		if(result == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		return new ResponseEntity<Material>(result, HttpStatus.OK);
	}
	
}

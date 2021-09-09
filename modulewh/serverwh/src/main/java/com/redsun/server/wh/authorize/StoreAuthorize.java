
package com.redsun.server.wh.authorize;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.redsun.server.wh.controller.StoreRestController;
import com.redsun.server.wh.model.Store;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.StoreRepository;
import com.redsun.server.wh.util.SecurityUtil;

@Component("storeAuthorize")
public class StoreAuthorize {

	public static final Logger logger = LoggerFactory.getLogger(StoreRestController.class);

	@Autowired
	PermissionAuthorize permissionAuthorize;
	
	@Autowired
	StoreRepository storeRepository;

	public boolean canCreate(Store store) throws JsonParseException, JsonMappingException, IOException {
		boolean result = true;
		Integer iduser = SecurityUtil.getIdUser();
		result = permissionAuthorize.isCreate("store", iduser);

		return result;
	}

	public boolean canRead(Integer id) throws JsonParseException, JsonMappingException, IOException {
		boolean result = true;
		Integer iduser = SecurityUtil.getIdUser();
		Map<String, Object> authorizeProperties = storeRepository.getAuthorizePropertiesById(id).get(0);
		Integer idcreate = (Integer)authorizeProperties.get("idcreate");
		Integer idowner = (Integer)authorizeProperties.get("idowner");
		result = permissionAuthorize.isRead("store", iduser, idcreate, idowner);

		return result;
	}
	
	public boolean canUpdate(Store store) throws JsonParseException, JsonMappingException, IOException {
		boolean result = true;
		Integer iduser = SecurityUtil.getIdUser();
		Map<String, Object> authorizeProperties = storeRepository.getAuthorizePropertiesById(store.getId()).get(0);
		Integer idcreate = (Integer)authorizeProperties.get("idcreate");
		Integer idowner = (Integer)authorizeProperties.get("idowner");
		result = permissionAuthorize.isUpdate("store", iduser, idcreate, idowner);

		return result;
	}

	public boolean canUpdateId(Integer id) throws JsonParseException, JsonMappingException, IOException {
		boolean result = true;
		Integer iduser = SecurityUtil.getIdUser();
		Map<String, Object> authorizeProperties = storeRepository.getAuthorizePropertiesById(id).get(0);
		Integer idcreate = (Integer)authorizeProperties.get("idcreate");
		Integer idowner = (Integer)authorizeProperties.get("idowner");
		result = permissionAuthorize.isUpdate("store", iduser, idcreate, idowner);

		return result;
	}

	public boolean canUpdateForDelete(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		return canUpdateId(id);
	}

	public boolean canDelete(Integer id) throws JsonParseException, JsonMappingException, IOException {
		boolean result = true;
		Integer iduser = SecurityUtil.getIdUser();
		Map<String, Object> authorizeProperties = storeRepository.getAuthorizePropertiesById(id).get(0);
		Integer idcreate = (Integer)authorizeProperties.get("idcreate");
		Integer idowner = (Integer)authorizeProperties.get("idowner");
		result = permissionAuthorize.isDelete("store", iduser, idcreate, idowner);

		return result;
	}

	public boolean canList(List<SearchCriteria> searchCriterias) throws JsonParseException, JsonMappingException, IOException {
		boolean result = true;
		result = permissionAuthorize.isList(searchCriterias, "store");

		return result;
	}

}

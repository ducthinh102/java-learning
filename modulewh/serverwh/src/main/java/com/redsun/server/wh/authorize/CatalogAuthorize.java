
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
import com.redsun.server.wh.controller.CatalogRestController;
import com.redsun.server.wh.model.Catalog;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.CatalogRepository;
import com.redsun.server.wh.util.SecurityUtil;

@Component("catalogAuthorize")
public class CatalogAuthorize {

	public static final Logger logger = LoggerFactory.getLogger(CatalogRestController.class);

	@Autowired
	PermissionAuthorize permissionAuthorize;
	
	@Autowired
	CatalogRepository catalogRepository;

	public boolean canCreate(Catalog catalog) throws JsonParseException, JsonMappingException, IOException {
		boolean result = true;
		Integer iduser = SecurityUtil.getIdUser();
		result = permissionAuthorize.isCreate(catalog.getScope(), iduser);

		return result;
	}

	public boolean canRead(Integer id, String scope) throws JsonParseException, JsonMappingException, IOException {
		boolean result = true;
		Integer iduser = SecurityUtil.getIdUser();
		Map<String, Object> authorizeProperties = catalogRepository.getAuthorizePropertiesById(id).get(0);
		Integer idcreate = (Integer)authorizeProperties.get("idcreate");
		Integer idowner = (Integer)authorizeProperties.get("idowner");
		result = permissionAuthorize.isRead(scope, iduser, idcreate, idowner);

		return result;
	}
	
	public boolean canUpdate(Catalog catalog) throws JsonParseException, JsonMappingException, IOException {
		boolean result = true;
		Integer iduser = SecurityUtil.getIdUser();
		Map<String, Object> authorizeProperties = catalogRepository.getAuthorizePropertiesById(catalog.getId()).get(0);
		Integer idcreate = (Integer)authorizeProperties.get("idcreate");
		Integer idowner = (Integer)authorizeProperties.get("idowner");
		result = permissionAuthorize.isUpdate(catalog.getScope(), iduser, idcreate, idowner);

		return result;
	}

	public boolean canUpdateId(Integer id) throws JsonParseException, JsonMappingException, IOException {
		Catalog catalog = catalogRepository.findOne(id);
		return canUpdate(catalog);
	}

	public boolean canUpdateForDelete(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		Catalog catalog = catalogRepository.findOne(id);
		return canUpdate(catalog);
	}

	public boolean canDelete(Integer id, String scope) throws JsonParseException, JsonMappingException, IOException {
		boolean result = true;
		Integer iduser = SecurityUtil.getIdUser();
		Map<String, Object> authorizeProperties = catalogRepository.getAuthorizePropertiesById(id).get(0);
		Integer idcreate = (Integer)authorizeProperties.get("idcreate");
		Integer idowner = (Integer)authorizeProperties.get("idowner");
		result = permissionAuthorize.isDelete(scope, iduser, idcreate, idowner);

		return result;
	}

	public boolean canList(List<SearchCriteria> searchCriterias, String scope) throws JsonParseException, JsonMappingException, IOException {
		boolean result = true;
		result = permissionAuthorize.isList(searchCriterias, scope);

		return result;
	}

}


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
import com.redsun.server.wh.controller.SupplierRestController;
import com.redsun.server.wh.model.Supplier;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.SupplierRepository;
import com.redsun.server.wh.util.SecurityUtil;

@Component("supplierAuthorize")
public class SupplierAuthorize {

	public static final Logger logger = LoggerFactory.getLogger(SupplierRestController.class);

	@Autowired
	PermissionAuthorize permissionAuthorize;
	
	@Autowired
	SupplierRepository supplierRepository;

	public boolean canCreate(Supplier supplier) throws JsonParseException, JsonMappingException, IOException {
		boolean result = true;
		Integer iduser = SecurityUtil.getIdUser();
		result = permissionAuthorize.isCreate("supplier", iduser);

		return result;
	}

	public boolean canRead(Integer id) throws JsonParseException, JsonMappingException, IOException {
		boolean result = true;
		Integer iduser = SecurityUtil.getIdUser();
		Map<String, Object> authorizeProperties = supplierRepository.getAuthorizePropertiesById(id).get(0);
		Integer idcreate = (Integer)authorizeProperties.get("idcreate");
		Integer idowner = (Integer)authorizeProperties.get("idowner");
		result = permissionAuthorize.isRead("supplier", iduser, idcreate, idowner);

		return result;
	}
	
	public boolean canUpdate(Supplier supplier) throws JsonParseException, JsonMappingException, IOException {
		boolean result = true;
		Integer iduser = SecurityUtil.getIdUser();
		Map<String, Object> authorizeProperties = supplierRepository.getAuthorizePropertiesById(supplier.getId()).get(0);
		Integer idcreate = (Integer)authorizeProperties.get("idcreate");
		Integer idowner = (Integer)authorizeProperties.get("idowner");
		result = permissionAuthorize.isUpdate("supplier", iduser, idcreate, idowner);

		return result;
	}

	public boolean canUpdateId(Integer id) throws JsonParseException, JsonMappingException, IOException {
		boolean result = true;
		Integer iduser = SecurityUtil.getIdUser();
		Map<String, Object> authorizeProperties = supplierRepository.getAuthorizePropertiesById(id).get(0);
		Integer idcreate = (Integer)authorizeProperties.get("idcreate");
		Integer idowner = (Integer)authorizeProperties.get("idowner");
		result = permissionAuthorize.isUpdate("supplier", iduser, idcreate, idowner);

		return result;
	}

	public boolean canUpdateForDelete(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		return canUpdateId(id);
	}

	public boolean canDelete(Integer id) throws JsonParseException, JsonMappingException, IOException {
		boolean result = true;
		Integer iduser = SecurityUtil.getIdUser();
		Map<String, Object> authorizeProperties = supplierRepository.getAuthorizePropertiesById(id).get(0);
		Integer idcreate = (Integer)authorizeProperties.get("idcreate");
		Integer idowner = (Integer)authorizeProperties.get("idowner");
		result = permissionAuthorize.isDelete("supplier", iduser, idcreate, idowner);

		return result;
	}

	public boolean canList(List<SearchCriteria> searchCriterias) throws JsonParseException, JsonMappingException, IOException {
		boolean result = true;
		result = permissionAuthorize.isList(searchCriterias, "supplier");

		return result;
	}

}


package com.redsun.server.wh.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.redsun.server.wh.authorize.PermissionAuthorize;
import com.redsun.server.wh.common.Constant;
import com.redsun.server.wh.controller.common.ServerException;
import com.redsun.server.wh.model.History;
import com.redsun.server.wh.model.Permission;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.HistoryRepository;
import com.redsun.server.wh.repository.PermissionRepository;
import com.redsun.server.wh.repository.specification.PermissionSpecification;
import com.redsun.server.wh.repository.specification.PermissionSpecificationsBuilder;
import com.redsun.server.wh.service.PermissionService;
import com.redsun.server.wh.util.SecurityUtil;

@Service("permission")
@Transactional
public class PermissionServiceImpl implements PermissionService {

	@Autowired
	PermissionAuthorize permissionAuthorize;

	public static final Object synchLock = new Object();

	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private PermissionRepository permissionRepository;

	@Autowired
	private PermissionSpecificationsBuilder permissionSpecificationsBuilder;

	@Override
	public Permission save(Permission permission) {
		return permissionRepository.save(permission);
	}

	@Override
	public Permission create(Permission permission) throws JsonParseException, JsonMappingException, IOException {
		Integer iduser = SecurityUtil.getIdUser();
		// Current date;
		Date currentDate = new Date();
		permission.setStatus(Constant.SERVERDB_STATUS_NEW);
		permission.setIdcreate(iduser);
		permission.setCreatedate(currentDate);
		permission.setIdowner(iduser);
		permission.setIdupdate(iduser);
		permission.setUpdatedate(currentDate);
		permission.setVersion(1);
		// Save.
		Permission result = permissionRepository.save(permission);
		return result;
	}

	@Override
	public Integer updateLock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Update lock.
		Integer result = permissionRepository.updateLock(id, iduser, Constant.SERVERDB_lOCKTIMEOUT);
		if (result == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		return result;
	}

	@Override
	public Integer updateUnlock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Update lock.
		Integer result = permissionRepository.updateUnlock(id);
		if (result == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		return result;
	}

	private Map<String, Object> updatePre(Map<String, Object> params)
			throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> result = new HashMap<String, Object>();
		// Get value from params.
		Integer iduser = (Integer) params.get("iduser");
		Integer id = (Integer) params.get("id");
		Integer version = (Integer) params.get("version");
		// Get from DB.
		Permission permissionDb = permissionRepository.findOne(id);
		if (permissionDb == null) { // not exist.
			throw new ServerException(Constant.SERVERCODE_NOTEXISTID);
		} else if (permissionDb.getIdlock() != iduser) { // locked.
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		} else if (permissionDb.getVersion() != version) { // version difference.
			throw new ServerException(Constant.SERVERCODE_VERSIONDIFFERENCE);
		} else {
			// Keep history data.
			String historyStr = permissionDb.toString();
			// Increase version.
			permissionDb.setVersion(permissionDb.getVersion() + 1);
			// return.
			result.put("permissionDb", permissionDb);
			result.put("historyStr", historyStr);
			return result;
		}
	}

	private Map<String, Object> updatePost(Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		// Get value from params.
		Integer id = (Integer) params.get("id");
		String historyStr = (String) params.get("historyStr");
		// Save history.
		History history = new History(id, "permission", historyStr);
		historyRepository.save(history);
		// return.
		return result;
	}

	@Override
	public Permission update(Integer id, Permission permission)
			throws JsonParseException, JsonMappingException, IOException {
		// Permission dbPermission = permissionRepository.findOne([Integer id]);
		// permissionRepository.save(dbPermission);
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", permission.getVersion());
		params.put("permission", permission);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Permission permissionDb = (Permission) resultPre.get("permissionDb");
		// Ignore properties.
		String[] ignoreProperties = new String[] { "status", "idcreate", "idowner", "idupdate", "iddelete", "idlock",
				"createdate", "updatedate", "deletedate", "lockdate", "version" };
		// Copy data.
		BeanUtils.copyProperties(permission, permissionDb, ignoreProperties);
		Date currentDate = new Date();
		permissionDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		permissionDb.setIdupdate(iduser);
		permissionDb.setUpdatedate(currentDate);
		permissionDb.setIdowner(iduser);
		// Save.
		permissionDb = permissionRepository.save(permissionDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return permissionDb;
	}

	@Override
	public Permission updateWithLock(Integer id, Permission permission)
			throws JsonParseException, JsonMappingException, IOException {
		Permission result = null;
		// Lock to update.
		updateLock(id);
		// Update.
		result = update(id, permission);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}

	@Override
	public void delete(Permission permission) {
		permissionRepository.delete(permission);
	}

	@Override
	public void deleteById(Integer id) {
		permissionRepository.delete(id);
	}

	@Override
	public Permission getById(Integer id) {
		return permissionRepository.findOne(id);
	}
	
	@Override
	public Permission getByTarget(String target) {
		List<Permission> permissions = permissionRepository.findByTarget(target);
		if(permissions == null || permissions.isEmpty()) {
			return null;
		}
		return permissions.get(0);
	}
	
	@Override
	public List<Permission> listAll() {
		return permissionRepository.findAll();
	}

	@Override
	public long countAll() {
		return permissionRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return permissionRepository.exists(id);
	}

	public List<Permission> listWithCritera(SearchCriteria searchCriteria) {
		Specification<Permission> permissionSpecification = new PermissionSpecification(searchCriteria);
		List<Permission> result = permissionRepository.findAll(permissionSpecification);
		return result;
	}

	public List<Permission> listWithCriteras(List<SearchCriteria> searchCriterias) {
		Specification<Permission> permissionSpecification = permissionSpecificationsBuilder.build(searchCriterias);
		List<Permission> result = permissionRepository.findAll(permissionSpecification);
		return result;
	}

	public Page<Permission> listAllByPage(Pageable pageable) {
		return permissionRepository.findAll(pageable);
	}

	public Page<Permission> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<Permission> permissionSpecification = new PermissionSpecification(searchCriteria);
		Page<Permission> result = permissionRepository.findAll(permissionSpecification, pageable);
		return result;
	}

	public Page<Permission> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Permission> permissionSpecification = permissionSpecificationsBuilder.build(searchCriterias);
		Page<Permission> result = permissionRepository.findAll(permissionSpecification, pageable);
		return result;
	}

}

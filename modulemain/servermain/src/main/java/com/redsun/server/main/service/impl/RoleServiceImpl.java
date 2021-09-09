

package com.redsun.server.main.service.impl;

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
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.redsun.server.main.common.Constant;
import com.redsun.server.main.controller.common.ServerException;
import com.redsun.server.main.model.Role;
import com.redsun.server.main.model.History;
import com.redsun.server.main.model.common.SearchCriteria;
import com.redsun.server.main.repository.RoleRepository;
import com.redsun.server.main.repository.HistoryRepository;
import com.redsun.server.main.repository.specification.RoleSpecification;
import com.redsun.server.main.repository.specification.RoleSpecificationsBuilder;
import com.redsun.server.main.service.RoleService;
import com.redsun.server.main.util.SecurityUtil;

@Service("role")
@Transactional
public class RoleServiceImpl implements RoleService {
	
	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private RoleSpecificationsBuilder roleSpecificationsBuilder;

	@Override
	public Role save(Role role) {
		return roleRepository.save(role);
	}

	@Override
	public Role create(Role role) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Current date;
		Date currentDate = new Date();
		role.setStatus(Constant.SERVERDB_STATUS_NEW);
		role.setIdcreate(iduser);
		role.setCreatedate(currentDate);
		role.setIdowner(iduser);
		role.setIdupdate(iduser);
		role.setUpdatedate(currentDate);
		role.setVersion(1);
		return roleRepository.save(role);
	}

	@Override
	public Integer updateLock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Update lock.
		Integer result = roleRepository.updateLock(id, iduser, Constant.SERVERDB_lOCKTIMEOUT);
		if(result == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		return result;		
	}
	
	@Override
	public Integer updateUnlock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Update lock.
		Integer result = roleRepository.updateUnlock(id);
		if(result == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		return result;
	}
	
	private Map<String, Object> updatePre(Map<String, Object> params) throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> result = new HashMap<String, Object>();
		// Get value from params.
		Integer iduser = (Integer) params.get("iduser");
		Integer id = (Integer) params.get("id");
		Integer version = (Integer) params.get("version");
		Role role = (Role) params.get("role");
		// Get from DB.
		Role roleDb = roleRepository.findOne(id);
		if(roleDb == null) { // not exist.
			throw new ServerException(Constant.SERVERCODE_NOTEXISTID);
		} else if(roleDb.getIdlock() != iduser) { // locked.
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		} else if (roleDb.getVersion() != version) { // version difference.
			throw new ServerException(Constant.SERVERCODE_VERSIONDIFFERENCE);
		} else {
			// Keep history data.
			String historyStr = roleDb.toString();
			// Increase version.
			roleDb.setVersion(roleDb.getVersion() + 1);
			// return.
			result.put("roleDb", roleDb);
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
		History history = new History(id, "role", historyStr);
		historyRepository.save(history);
		// return.
		return result;
	}
	
	@Override
	public Role update(Integer id, Role role) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", role.getVersion());
		params.put("role", role);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Role roleDb = (Role) resultPre.get("roleDb");
		// Ignore properties.
		String[] ignoreProperties = new String[] { "status", "idcreate", "idowner", "idupdate", "iddelete", "idlock", "createdate", "updatedate", "deletedate", "lockdate", "version" };
		// Copy data.
		BeanUtils.copyProperties(role, roleDb, ignoreProperties);
		Date currentDate = new Date();
		roleDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		roleDb.setIdupdate(iduser);
		roleDb.setUpdatedate(currentDate);
		roleDb.setIdowner(iduser);
		// Save.
		roleDb = roleRepository.save(roleDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return roleDb;
	}

	@Override
	public Role updateWithLock(Integer id, Role role) throws JsonParseException, JsonMappingException, IOException {
		Role result = null;
		// Lock to update.
		updateLock(id);
		// Update.
		result = update(id, role);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}
	
	@Override
	public Role updateForDelete(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", version);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Role roleDb = (Role) resultPre.get("roleDb");
		Date currentDate = new Date();
		roleDb.setStatus(Constant.SERVERDB_STATUS_DELETE);
		roleDb.setIddelete(iduser);
		roleDb.setDeletedate(currentDate);
		// Save.
		roleDb = roleRepository.save(roleDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return roleDb;
	}

	@Override
	public Role updateForDeleteWithLock(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		Role result = null;
		// Lock to update.
		updateLock(id);
		// Update data.
		result = updateForDelete(id, version);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}


	@Override
	public void delete(Role role) {
		roleRepository.delete(role);
	}

	@Override
	public void deleteById(Integer id) {
		roleRepository.delete(id);
	}

	@Override
	public Role getById(Integer id) {
		return roleRepository.findOne(id);
	}

	@Override
	public List<Role> listAll() {
		return roleRepository.findAll();
	}

	@Override
	public long countAll() {
		return roleRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return roleRepository.exists(id);
	}
	
	public List<Role> listWithCritera(SearchCriteria searchCriteria) {
		Specification<Role> roleSpecification = new RoleSpecification(searchCriteria);
		// Where status != delete.
		Specification<Role> notDeleteSpec = new RoleSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		roleSpecification = Specifications.where(roleSpecification).and(notDeleteSpec);
		// Execute.
        List<Role> result = roleRepository.findAll(roleSpecification);
        return result;
	}
	
	public List<Role> listWithCriteras(List<SearchCriteria> searchCriterias) {
        Specification<Role> roleSpecification = roleSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Role> notDeleteSpec = new RoleSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		roleSpecification = Specifications.where(roleSpecification).and(notDeleteSpec);
		// Execute.
        List<Role> result = roleRepository.findAll(roleSpecification);
        return result;
	}
	
	public Page<Role> listAllByPage(Pageable pageable) {
		return roleRepository.findAll(pageable);
	}
	
	public Page<Role> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<Role> roleSpecification = new RoleSpecification(searchCriteria);
		// Where status != delete.
		Specification<Role> notDeleteSpec = new RoleSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		roleSpecification = Specifications.where(roleSpecification).and(notDeleteSpec);
		// Execute.
		Page<Role> result = roleRepository.findAll(roleSpecification, pageable);
        return result;
	}
	
	public Page<Role> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Role> roleSpecification = roleSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Role> notDeleteSpec = new RoleSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		roleSpecification = Specifications.where(roleSpecification).and(notDeleteSpec);
		// Execute.
		Page<Role> result = roleRepository.findAll(roleSpecification, pageable);
        return result;
	}

}

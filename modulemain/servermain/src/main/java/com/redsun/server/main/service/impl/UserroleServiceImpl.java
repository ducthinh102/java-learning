

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
import com.redsun.server.main.model.Userrole;
import com.redsun.server.main.model.History;
import com.redsun.server.main.model.common.SearchCriteria;
import com.redsun.server.main.repository.UserroleRepository;
import com.redsun.server.main.repository.HistoryRepository;
import com.redsun.server.main.repository.specification.UserroleSpecification;
import com.redsun.server.main.repository.specification.UserroleSpecificationsBuilder;
import com.redsun.server.main.service.UserroleService;
import com.redsun.server.main.util.SecurityUtil;

@Service("userrole")
@Transactional
public class UserroleServiceImpl implements UserroleService {
	
	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private UserroleRepository userroleRepository;
	
	@Autowired
	private UserroleSpecificationsBuilder userroleSpecificationsBuilder;

	@Override
	public Userrole save(Userrole userrole) {
		return userroleRepository.save(userrole);
	}

	@Override
	public Userrole create(Userrole userrole) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Current date;
		Date currentDate = new Date();
		userrole.setStatus(Constant.SERVERDB_STATUS_NEW);
		userrole.setIdcreate(iduser);
		userrole.setCreatedate(currentDate);
		userrole.setIdowner(iduser);
		userrole.setIdupdate(iduser);
		userrole.setUpdatedate(currentDate);
		userrole.setVersion(1);
		return userroleRepository.save(userrole);
	}

	@Override
	public Integer updateLock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Update lock.
		Integer result = userroleRepository.updateLock(id, iduser, Constant.SERVERDB_lOCKTIMEOUT);
		if(result == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		return result;		
	}
	
	@Override
	public Integer updateUnlock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Update lock.
		Integer result = userroleRepository.updateUnlock(id);
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
		Userrole userrole = (Userrole) params.get("userrole");
		// Get from DB.
		Userrole userroleDb = userroleRepository.findOne(id);
		if(userroleDb == null) { // not exist.
			throw new ServerException(Constant.SERVERCODE_NOTEXISTID);
		} else if(userroleDb.getIdlock() != iduser) { // locked.
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		} else if (userroleDb.getVersion() != version) { // version difference.
			throw new ServerException(Constant.SERVERCODE_VERSIONDIFFERENCE);
		} else {
			// Keep history data.
			String historyStr = userroleDb.toString();
			// Increase version.
			userroleDb.setVersion(userroleDb.getVersion() + 1);
			// return.
			result.put("userroleDb", userroleDb);
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
		History history = new History(id, "userrole", historyStr);
		historyRepository.save(history);
		// return.
		return result;
	}
	
	@Override
	public Userrole update(Integer id, Userrole userrole) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", userrole.getVersion());
		params.put("userrole", userrole);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Userrole userroleDb = (Userrole) resultPre.get("userroleDb");
		// Ignore properties.
		String[] ignoreProperties = new String[] { "status", "idcreate", "idowner", "idupdate", "iddelete", "idlock", "createdate", "updatedate", "deletedate", "lockdate", "version" };
		// Copy data.
		BeanUtils.copyProperties(userrole, userroleDb, ignoreProperties);
		Date currentDate = new Date();
		userroleDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		userroleDb.setIdupdate(iduser);
		userroleDb.setUpdatedate(currentDate);
		userroleDb.setIdowner(iduser);
		// Save.
		userroleDb = userroleRepository.save(userroleDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return userroleDb;
	}

	@Override
	public Userrole updateWithLock(Integer id, Userrole userrole) throws JsonParseException, JsonMappingException, IOException {
		Userrole result = null;
		// Lock to update.
		updateLock(id);
		// Update.
		result = update(id, userrole);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}
	
	@Override
	public Userrole updateForDelete(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", version);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Userrole userroleDb = (Userrole) resultPre.get("userroleDb");
		Date currentDate = new Date();
		userroleDb.setStatus(Constant.SERVERDB_STATUS_DELETE);
		userroleDb.setIddelete(iduser);
		userroleDb.setDeletedate(currentDate);
		// Save.
		userroleDb = userroleRepository.save(userroleDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return userroleDb;
	}

	@Override
	public Userrole updateForDeleteWithLock(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		Userrole result = null;
		// Lock to update.
		updateLock(id);
		// Update data.
		result = updateForDelete(id, version);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}


	@Override
	public void delete(Userrole userrole) {
		userroleRepository.delete(userrole);
	}

	@Override
	public void deleteById(Integer id) {
		userroleRepository.delete(id);
	}

	@Override
	public Userrole getById(Integer id) {
		return userroleRepository.findOne(id);
	}

	@Override
	public List<Userrole> listAll() {
		return userroleRepository.findAll();
	}

	@Override
	public long countAll() {
		return userroleRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return userroleRepository.exists(id);
	}
	
	public List<Userrole> listWithCritera(SearchCriteria searchCriteria) {
		Specification<Userrole> userroleSpecification = new UserroleSpecification(searchCriteria);
		// Where status != delete.
		Specification<Userrole> notDeleteSpec = new UserroleSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		userroleSpecification = Specifications.where(userroleSpecification).and(notDeleteSpec);
		// Execute.
        List<Userrole> result = userroleRepository.findAll(userroleSpecification);
        return result;
	}
	
	public List<Userrole> listWithCriteras(List<SearchCriteria> searchCriterias) {
        Specification<Userrole> userroleSpecification = userroleSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Userrole> notDeleteSpec = new UserroleSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		userroleSpecification = Specifications.where(userroleSpecification).and(notDeleteSpec);
		// Execute.
        List<Userrole> result = userroleRepository.findAll(userroleSpecification);
        return result;
	}
	
	public Page<Userrole> listAllByPage(Pageable pageable) {
		return userroleRepository.findAll(pageable);
	}
	
	public Page<Userrole> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<Userrole> userroleSpecification = new UserroleSpecification(searchCriteria);
		// Where status != delete.
		Specification<Userrole> notDeleteSpec = new UserroleSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		userroleSpecification = Specifications.where(userroleSpecification).and(notDeleteSpec);
		// Execute.
		Page<Userrole> result = userroleRepository.findAll(userroleSpecification, pageable);
        return result;
	}
	
	public Page<Userrole> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Userrole> userroleSpecification = userroleSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Userrole> notDeleteSpec = new UserroleSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		userroleSpecification = Specifications.where(userroleSpecification).and(notDeleteSpec);
		// Execute.
		Page<Userrole> result = userroleRepository.findAll(userroleSpecification, pageable);
        return result;
	}

}

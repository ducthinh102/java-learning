

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
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.redsun.server.wh.common.Constant;
import com.redsun.server.wh.controller.common.ServerException;
import com.redsun.server.wh.model.Appconfig;
import com.redsun.server.wh.model.History;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.AppconfigRepository;
import com.redsun.server.wh.repository.HistoryRepository;
import com.redsun.server.wh.repository.specification.AppconfigSpecification;
import com.redsun.server.wh.repository.specification.AppconfigSpecificationsBuilder;
import com.redsun.server.wh.service.AppconfigService;
import com.redsun.server.wh.util.SecurityUtil;

@Service("appconfig")
@Transactional
public class AppconfigServiceImpl implements AppconfigService {

	public static final Object synchLock = new Object();

	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private AppconfigRepository appconfigRepository;
	
	@Autowired
	private AppconfigSpecificationsBuilder appconfigSpecificationsBuilder;

	@Override
	public Appconfig save(Appconfig appconfig) {
		return appconfigRepository.save(appconfig);
	}

	@Override
	public Appconfig create(Appconfig appconfig) throws JsonParseException, JsonMappingException, IOException {
		if (appconfigRepository.isExistScope(appconfig.getId(), appconfig.getScope())) { // exist scope.
			throw new ServerException(Constant.SERVERCODE_EXISTSCOPE);
		} else {
			// Get iduser.
			Integer iduser = SecurityUtil.getIdUser();
			// Current date;
			Date currentDate = new Date();
			appconfig.setStatus(Constant.SERVERDB_STATUS_NEW);
			appconfig.setIdcreate(iduser);
			appconfig.setCreatedate(currentDate);
			appconfig.setIdowner(iduser);
			appconfig.setIdupdate(iduser);
			appconfig.setUpdatedate(currentDate);
			appconfig.setVersion(1);
			return appconfigRepository.save(appconfig);
		}
	}

	@Override
	public Integer updateLock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Update lock.
		Integer result = appconfigRepository.updateLock(id, iduser, Constant.SERVERDB_lOCKTIMEOUT);
		if(result == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		return result;		
	}
	
	@Override
	public Integer updateUnlock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Update lock.
		Integer result = appconfigRepository.updateUnlock(id);
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
		Appconfig appconfig = (Appconfig) params.get("appconfig");
		// Get from DB.
		Appconfig appconfigDb = appconfigRepository.findOne(id);
		if(appconfigDb == null) { // not exist.
			throw new ServerException(Constant.SERVERCODE_NOTEXISTID);
		} else if(appconfigDb.getIdlock() != iduser) { // locked.
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		} else if (appconfigDb.getVersion() != version) { // version difference.
			throw new ServerException(Constant.SERVERCODE_VERSIONDIFFERENCE);
		} else if (appconfig != null && appconfigRepository.isExistScope(id, appconfig.getScope())) { // exist scope.
			throw new ServerException(Constant.SERVERCODE_EXISTSCOPE);
		} else {
			// Keep history data.
			String historyStr = appconfigDb.toString();
			// Increase version.
			appconfigDb.setVersion(appconfigDb.getVersion() + 1);
			// return.
			result.put("appconfigDb", appconfigDb);
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
		History history = new History(id, "appconfig", historyStr);
		historyRepository.save(history);
		// return.
		return result;
	}
	
	@Override
	public Appconfig update(Integer id, Appconfig appconfig) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", appconfig.getVersion());
		params.put("appconfig", appconfig);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Appconfig appconfigDb = (Appconfig) resultPre.get("appconfigDb");
		// Ignore properties.
		String[] ignoreProperties = new String[] { "status", "idcreate", "idowner", "idupdate", "iddelete", "idlock", "createdate", "updatedate", "deletedate", "lockdate", "version" };
		// Copy data.
		BeanUtils.copyProperties(appconfig, appconfigDb, ignoreProperties);
		Date currentDate = new Date();
		appconfigDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		appconfigDb.setIdupdate(iduser);
		appconfigDb.setUpdatedate(currentDate);
		appconfigDb.setIdowner(iduser);
		// Save.
		appconfigDb = appconfigRepository.save(appconfigDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return appconfigDb;
	}

	@Override
	public Appconfig updateWithLock(Integer id, Appconfig appconfig) throws JsonParseException, JsonMappingException, IOException {
		Appconfig result = null;
		// Lock to update.
		updateLock(id);
		// Update.
		result = update(id, appconfig);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}
	
	@Override
	public Appconfig updateForDelete(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", version);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Appconfig appconfigDb = (Appconfig) resultPre.get("appconfigDb");
		Date currentDate = new Date();
		appconfigDb.setStatus(Constant.SERVERDB_STATUS_DELETE);
		appconfigDb.setIddelete(iduser);
		appconfigDb.setDeletedate(currentDate);
		// Save.
		appconfigDb = appconfigRepository.save(appconfigDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return appconfigDb;
	}

	@Override
	public Appconfig updateForDeleteWithLock(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		Appconfig result = null;
		// Lock to update.
		updateLock(id);
		// Update data.
		result = updateForDelete(id, version);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}


	@Override
	public void delete(Appconfig appconfig) {
		appconfigRepository.delete(appconfig);
	}

	@Override
	public void deleteById(Integer id) {
		appconfigRepository.delete(id);
	}

	@Override
	public Appconfig getById(Integer id) {
		return appconfigRepository.findOne(id);
	}

	@Override
	public List<Appconfig> listAll() {
		return appconfigRepository.findAll();
	}

	@Override
	public long countAll() {
		return appconfigRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return appconfigRepository.exists(id);
	}
	
	public List<Appconfig> listWithCritera(SearchCriteria searchCriteria) {
		Specification<Appconfig> appconfigSpecification = new AppconfigSpecification(searchCriteria);
		// Where status != delete.
		Specification<Appconfig> notDeleteSpec = new AppconfigSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		appconfigSpecification = Specifications.where(appconfigSpecification).and(notDeleteSpec);
		// Execute.
        List<Appconfig> result = appconfigRepository.findAll(appconfigSpecification);
        return result;
	}
	
	public List<Appconfig> listWithCriteras(List<SearchCriteria> searchCriterias) {
        Specification<Appconfig> appconfigSpecification = appconfigSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Appconfig> notDeleteSpec = new AppconfigSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		appconfigSpecification = Specifications.where(appconfigSpecification).and(notDeleteSpec);
		// Execute.
        List<Appconfig> result = appconfigRepository.findAll(appconfigSpecification);
        return result;
	}
	
	public Page<Appconfig> listAllByPage(Pageable pageable) {
		return appconfigRepository.findAll(pageable);
	}
	
	public Page<Appconfig> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<Appconfig> appconfigSpecification = new AppconfigSpecification(searchCriteria);
		// Where status != delete.
		Specification<Appconfig> notDeleteSpec = new AppconfigSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		appconfigSpecification = Specifications.where(appconfigSpecification).and(notDeleteSpec);
		// Execute.
		Page<Appconfig> result = appconfigRepository.findAll(appconfigSpecification, pageable);
        return result;
	}
	
	public Page<Appconfig> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Appconfig> appconfigSpecification = appconfigSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Appconfig> notDeleteSpec = new AppconfigSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		appconfigSpecification = Specifications.where(appconfigSpecification).and(notDeleteSpec);
		// Execute.
		Page<Appconfig> result = appconfigRepository.findAll(appconfigSpecification, pageable);
        return result;
	}

}

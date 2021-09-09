

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
import com.redsun.server.wh.model.Materialform;
import com.redsun.server.wh.model.History;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.MaterialformRepository;
import com.redsun.server.wh.repository.HistoryRepository;
import com.redsun.server.wh.repository.specification.MaterialformSpecification;
import com.redsun.server.wh.repository.specification.MaterialformSpecificationsBuilder;
import com.redsun.server.wh.service.MaterialformService;
import com.redsun.server.wh.util.SecurityUtil;

@Service("materialform")
@Transactional
public class MaterialformServiceImpl implements MaterialformService {
	
	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private MaterialformRepository materialformRepository;
	
	@Autowired
	private MaterialformSpecificationsBuilder materialformSpecificationsBuilder;

	@Override
	public Materialform save(Materialform materialform) {
		return materialformRepository.save(materialform);
	}

	@Override
	public Materialform create(Materialform materialform) throws JsonParseException, JsonMappingException, IOException {
		if (materialformRepository.isExistCode(materialform.getId(), materialform.getCode(), materialform.getScope())) {
			throw new ServerException(Constant.SERVERCODE_EXISTCODE);
		} else {
			// Get iduser.
			Integer iduser = SecurityUtil.getIdUser();
			// Current date;
			Date currentDate = new Date();
			materialform.setStatus(Constant.SERVERDB_STATUS_NEW);
			if (materialform.getStatus() == null)
				materialform.setStatus(Constant.SERVERDB_STATUS_NEW);
			materialform.setIdcreate(iduser);
			materialform.setCreatedate(currentDate);
			materialform.setIdowner(iduser);
			materialform.setIdupdate(iduser);
			materialform.setUpdatedate(currentDate);
			materialform.setVersion(1);
			return materialformRepository.save(materialform);
		}
	}

	@Override
	public Integer updateLock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Update lock.
		Integer result = materialformRepository.updateLock(id, iduser, Constant.SERVERDB_lOCKTIMEOUT);
		if(result == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		return result;		
	}
	
	@Override
	public Integer updateUnlock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Update lock.
		Integer result = materialformRepository.updateUnlock(id);
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
		Materialform materialform = (Materialform) params.get("materialform");
		// Get from DB.
		Materialform materialformDb = materialformRepository.findOne(id);
		if(materialformDb == null) { // not exist.
			throw new ServerException(Constant.SERVERCODE_NOTEXISTID);
		} else if(materialformDb.getIdlock() != iduser) { // locked.
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		} else if (materialformDb.getVersion() != version) { // version difference.
			throw new ServerException(Constant.SERVERCODE_VERSIONDIFFERENCE);
		} else if (materialform!=null && materialformRepository.isExistCode(id, materialform.getCode(), materialform.getScope())) {
			throw new ServerException(Constant.SERVERCODE_EXISTCODE);
		} else {
			// Keep history data.
			String historyStr = materialformDb.toString();
			// Increase version.
			materialformDb.setVersion(materialformDb.getVersion() + 1);
			// return.
			result.put("materialformDb", materialformDb);
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
		History history = new History(id, "materialform", historyStr);
		historyRepository.save(history);
		// return.
		return result;
	}
	
	@Override
	public Materialform update(Integer id, Materialform materialform) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", materialform.getVersion());
		params.put("materialform", materialform);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Materialform materialformDb = (Materialform) resultPre.get("materialformDb");
		// Ignore properties.
		String[] ignoreProperties = new String[] { "status", "idcreate", "idowner", "idupdate", "iddelete", "idlock", "createdate", "updatedate", "deletedate", "lockdate", "version" };
		// Copy data.
		BeanUtils.copyProperties(materialform, materialformDb, ignoreProperties);
		Date currentDate = new Date();
		if (materialformDb.getStatus() == Constant.SERVERDB_STATUS_NEW) {
			materialformDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		}
		materialformDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		materialformDb.setIdupdate(iduser);
		materialformDb.setUpdatedate(currentDate);
		materialformDb.setIdowner(iduser);
		// Save.
		materialformDb = materialformRepository.save(materialformDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return materialformDb;
	}

	@Override
	public Materialform updateWithLock(Integer id, Materialform materialform) throws JsonParseException, JsonMappingException, IOException {
		Materialform result = null;
		// Lock to update.
		updateLock(id);
		// Update.
		result = update(id, materialform);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}

	@Override
	public Materialform updateStatusAndOwnerWithLock(Integer id, Materialform materialform) throws JsonParseException, JsonMappingException, IOException {
		// Lock to update.
		updateLock(id);
		
		// Update.
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", materialform.getVersion());
		params.put("materialform", materialform);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Materialform materialformDb = (Materialform) resultPre.get("materialformDb");
		// Ignore properties.
		String[] ignoreProperties = new String[] { "status", "idcreate", "idowner", "idupdate", "iddelete", "idlock", "createdate", "updatedate", "deletedate", "lockdate", "version" };
		// Copy data.
		BeanUtils.copyProperties(materialform, materialformDb, ignoreProperties);
		Date currentDate = new Date();
		materialformDb.setIdupdate(iduser);
		materialformDb.setUpdatedate(currentDate);
		// Save.
		materialformDb = materialformRepository.save(materialformDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		
		// Unlock of update.
		updateUnlock(id);
		return materialformDb;
	}
	
	@Override
	public Materialform updateForDelete(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", version);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Materialform materialformDb = (Materialform) resultPre.get("materialformDb");
		Date currentDate = new Date();
		materialformDb.setStatus(Constant.SERVERDB_STATUS_DELETE);
		materialformDb.setIddelete(iduser);
		materialformDb.setDeletedate(currentDate);
		// Save.
		materialformDb = materialformRepository.save(materialformDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return materialformDb;
	}

	@Override
	public Materialform updateForDeleteWithLock(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		Integer iduser = SecurityUtil.getIdUser();
		Materialform result = null;
		// Lock to update.
		updateLock(id);
		// Update data.
		result = updateForDelete(id, version);
		materialformRepository.updateForDeleteByIdMaterialform(iduser, id);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}


	@Override
	public void delete(Materialform materialform) {
		materialformRepository.delete(materialform);
	}

	@Override
	public void deleteById(Integer id) {
		materialformRepository.delete(id);
	}

	@Override
	public Materialform getById(Integer id) {
		return materialformRepository.findOne(id);
	}

	@Override
	public List<Materialform> listAll() {
		return materialformRepository.findAll();
	}

	@Override
	public long countAll() {
		return materialformRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return materialformRepository.exists(id);
	}
	
	public List<Materialform> listWithCritera(SearchCriteria searchCriteria) {
		Specification<Materialform> materialformSpecification = new MaterialformSpecification(searchCriteria);
		// Where status != delete.
		Specification<Materialform> notDeleteSpec = new MaterialformSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialformSpecification = Specifications.where(materialformSpecification).and(notDeleteSpec);
		// Execute.
        List<Materialform> result = materialformRepository.findAll(materialformSpecification);
        return result;
	}
	
	public List<Materialform> listWithCriteras(List<SearchCriteria> searchCriterias) {
        Specification<Materialform> materialformSpecification = materialformSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Materialform> notDeleteSpec = new MaterialformSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialformSpecification = Specifications.where(materialformSpecification).and(notDeleteSpec);
		// Execute.
        List<Materialform> result = materialformRepository.findAll(materialformSpecification);
        return result;
	}
	
	public Page<Materialform> listAllByPage(Pageable pageable) {
		return materialformRepository.findAll(pageable);
	}
	
	public Page<Materialform> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<Materialform> materialformSpecification = new MaterialformSpecification(searchCriteria);
		// Where status != delete.
		Specification<Materialform> notDeleteSpec = new MaterialformSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialformSpecification = Specifications.where(materialformSpecification).and(notDeleteSpec);
		// Execute.
		Page<Materialform> result = materialformRepository.findAll(materialformSpecification, pageable);
        return result;
	}
	
	public Page<Materialform> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Materialform> materialformSpecification = materialformSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		//Specification<Materialform> notDeleteSpec = new MaterialformSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		//materialformSpecification = Specifications.where(materialformSpecification).and(notDeleteSpec);
		// Execute.
		Page<Materialform> result = materialformRepository.findAll(materialformSpecification, pageable);
        return result;
	}

	@Override
	public List<Map<String, Object>> listAllForSelectByScope(String scope) {
		return materialformRepository.listForSelect(scope);
	}

}

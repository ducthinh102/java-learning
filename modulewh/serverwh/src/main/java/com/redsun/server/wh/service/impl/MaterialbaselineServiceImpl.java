

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
import com.redsun.server.wh.model.Materialbaseline;
import com.redsun.server.wh.model.History;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.MaterialbaselineRepository;
import com.redsun.server.wh.repository.HistoryRepository;
import com.redsun.server.wh.repository.specification.MaterialbaselineSpecification;
import com.redsun.server.wh.repository.specification.MaterialbaselineSpecificationsBuilder;
import com.redsun.server.wh.service.MaterialbaselineService;
import com.redsun.server.wh.util.SecurityUtil;

@Service("materialbaseline")
@Transactional
public class MaterialbaselineServiceImpl implements MaterialbaselineService {
	
	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private MaterialbaselineRepository materialbaselineRepository;
	
	@Autowired
	private MaterialbaselineSpecificationsBuilder materialbaselineSpecificationsBuilder;

	@Override
	public Materialbaseline save(Materialbaseline materialbaseline) {
		return materialbaselineRepository.save(materialbaseline);
	}

	@Override
	public Materialbaseline create(Materialbaseline materialbaseline) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Current date;
		Date currentDate = new Date();
		materialbaseline.setStatus(Constant.SERVERDB_STATUS_NEW);
		if (materialbaselineRepository.isExistCode(materialbaseline.getId(),materialbaseline.getCode())) {
			throw new ServerException(Constant.SERVERCODE_EXISTCODE);
		}
		if (materialbaseline.getStatus() == null)
			materialbaseline.setStatus(Constant.SERVERDB_STATUS_NEW);
		materialbaseline.setTotalamount(0.0);
		materialbaseline.setIdcreate(iduser);
		materialbaseline.setCreatedate(currentDate);
		materialbaseline.setIdowner(iduser);
		materialbaseline.setIdupdate(iduser);
		materialbaseline.setUpdatedate(currentDate);
		materialbaseline.setVersion(1);
		return materialbaselineRepository.save(materialbaseline);
	}

	@Override
	public Integer updateLock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Update lock.
		Integer result = materialbaselineRepository.updateLock(id, iduser, Constant.SERVERDB_lOCKTIMEOUT);
		if(result == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		return result;		
	}
	
	@Override
	public Integer updateUnlock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Update lock.
		Integer result = materialbaselineRepository.updateUnlock(id);
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
		Materialbaseline materialbaseline = (Materialbaseline) params.get("materialbaseline");
		// Get from DB.
		Materialbaseline materialbaselineDb = materialbaselineRepository.findOne(id);
		if(materialbaselineDb == null) { // not exist.
			throw new ServerException(Constant.SERVERCODE_NOTEXISTID);
		} else if(materialbaselineDb.getIdlock() != iduser) { // locked.
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		} else if (materialbaselineDb.getVersion() != version) { // version difference.
			throw new ServerException(Constant.SERVERCODE_VERSIONDIFFERENCE);
		} else if (materialbaseline != null && materialbaselineRepository.isExistCode(id, materialbaseline.getCode())) { // check material exit in material import																								// difference.
			throw new ServerException(Constant.SERVERCODE_EXISTCODE);
		} else {
			// Keep history data.
			String historyStr = materialbaselineDb.toString();
			// Increase version.
			materialbaselineDb.setVersion(materialbaselineDb.getVersion() + 1);
			// return.
			result.put("materialbaselineDb", materialbaselineDb);
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
		History history = new History(id, "materialbaseline", historyStr);
		historyRepository.save(history);
		// return.
		return result;
	}
	
	@Override
	public Materialbaseline update(Integer id, Materialbaseline materialbaseline) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", materialbaseline.getVersion());
		params.put("materialbaseline", materialbaseline);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Materialbaseline materialbaselineDb = (Materialbaseline) resultPre.get("materialbaselineDb");
		// Ignore properties.
		String[] ignoreProperties = new String[] { "status", "idcreate", "idowner", "idupdate", "iddelete", "idlock", "createdate", "updatedate", "deletedate", "lockdate", "version" };
		// Copy data.
		BeanUtils.copyProperties(materialbaseline, materialbaselineDb, ignoreProperties);
		Date currentDate = new Date();
		materialbaselineDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		materialbaselineDb.setIdupdate(iduser);
		materialbaselineDb.setUpdatedate(currentDate);
		materialbaselineDb.setIdowner(iduser);
		// Save.
		materialbaselineDb = materialbaselineRepository.save(materialbaselineDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return materialbaselineDb;
	}

	@Override
	public Materialbaseline updateWithLock(Integer id, Materialbaseline materialbaseline) throws JsonParseException, JsonMappingException, IOException {
		Materialbaseline result = null;
		// Lock to update.
		updateLock(id);
		// Update.
		result = update(id, materialbaseline);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}

	@Override
	public Materialbaseline updateStatusAndOwnerWithLock(Integer id, Materialbaseline materialbaseline) throws JsonParseException, JsonMappingException, IOException {
		// Lock to update.
		updateLock(id);
		
		// Update.
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", materialbaseline.getVersion());
		params.put("materialbaseline", materialbaseline);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Materialbaseline materialbaselineDb = (Materialbaseline) resultPre.get("materialbaselineDb");
		// Ignore properties.
		String[] ignoreProperties = new String[] { "status", "idcreate", "idowner", "idupdate", "iddelete", "idlock", "createdate", "updatedate", "deletedate", "lockdate", "version" };
		// Copy data.
		BeanUtils.copyProperties(materialbaseline, materialbaselineDb, ignoreProperties);
		Date currentDate = new Date();
		materialbaselineDb.setIdupdate(iduser);
		materialbaselineDb.setUpdatedate(currentDate);
		// Save.
		materialbaselineDb = materialbaselineRepository.save(materialbaselineDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		
		// Unlock of update.
		updateUnlock(id);
		return materialbaselineDb;
	}
	
	@Override
	public Materialbaseline updateForDelete(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", version);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Materialbaseline materialbaselineDb = (Materialbaseline) resultPre.get("materialbaselineDb");
		Date currentDate = new Date();
		materialbaselineDb.setStatus(Constant.SERVERDB_STATUS_DELETE);
		materialbaselineDb.setIddelete(iduser);
		materialbaselineDb.setDeletedate(currentDate);
		// Save.
		materialbaselineDb = materialbaselineRepository.save(materialbaselineDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return materialbaselineDb;
	}

	@Override
	public Materialbaseline updateForDeleteWithLock(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		Materialbaseline result = null;
		Integer iduser = SecurityUtil.getIdUser();
		// Lock to update.
		updateLock(id);
		// Update data.
		result = updateForDelete(id, version);
		materialbaselineRepository.updateForDeleteByIdMaterialbaseline(iduser, id);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}


	@Override
	public void delete(Materialbaseline materialbaseline) {
		materialbaselineRepository.delete(materialbaseline);
	}

	@Override
	public void deleteById(Integer id) {
		materialbaselineRepository.delete(id);
	}

	@Override
	public Materialbaseline getById(Integer id) {
		return materialbaselineRepository.findOne(id);
	}

	@Override
	public List<Materialbaseline> listAll() {
		return materialbaselineRepository.findAll();
	}

	@Override
	public long countAll() {
		return materialbaselineRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return materialbaselineRepository.exists(id);
	}
	
	
	public List<Materialbaseline> listWithCritera(SearchCriteria searchCriteria) {
		Specification<Materialbaseline> materialbaselineSpecification = new MaterialbaselineSpecification(searchCriteria);
		// Where status != delete.
		Specification<Materialbaseline> notDeleteSpec = new MaterialbaselineSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialbaselineSpecification = Specifications.where(materialbaselineSpecification).and(notDeleteSpec);
		// Execute.
        List<Materialbaseline> result = materialbaselineRepository.findAll(materialbaselineSpecification);
        return result;
	}
	
	public List<Materialbaseline> listWithCriteras(List<SearchCriteria> searchCriterias) {
        Specification<Materialbaseline> materialbaselineSpecification = materialbaselineSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Materialbaseline> notDeleteSpec = new MaterialbaselineSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialbaselineSpecification = Specifications.where(materialbaselineSpecification).and(notDeleteSpec);
		// Execute.
        List<Materialbaseline> result = materialbaselineRepository.findAll(materialbaselineSpecification);
        return result;
	}
	
	public Page<Materialbaseline> listAllByPage(Pageable pageable) {
		return materialbaselineRepository.findAll(pageable);
	}
	
	public Page<Materialbaseline> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<Materialbaseline> materialbaselineSpecification = new MaterialbaselineSpecification(searchCriteria);
		// Where status != delete.
		Specification<Materialbaseline> notDeleteSpec = new MaterialbaselineSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialbaselineSpecification = Specifications.where(materialbaselineSpecification).and(notDeleteSpec);
		// Execute.
		Page<Materialbaseline> result = materialbaselineRepository.findAll(materialbaselineSpecification, pageable);
        return result;
	}
	
	public Page<Map<String, Object>> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Materialbaseline> materialbaselineSpecification = materialbaselineSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		//Specification<Materialbaseline> notDeleteSpec = new MaterialbaselineSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		//materialbaselineSpecification = Specifications.where(materialbaselineSpecification).and(notDeleteSpec);
		// Execute.
		Page<Materialbaseline> materialbaselines = materialbaselineRepository.findAll(materialbaselineSpecification, pageable);
        Page<Map<String, Object>> result = materialbaselines.map(this::convertToMap);
        return result;
	}
	
	private Map<String, Object> convertToMap(final Materialbaseline materialbaseline) {
	    final Map<String, Object> result = new HashMap<String, Object>();
	    result.put("id", materialbaseline.getId());
	    result.put("name", materialbaseline.getName());
	    result.put("storename", materialbaseline.getStore().getName());
	    result.put("code", materialbaseline.getCode());
	    result.put("baselinedate", materialbaseline.getBaselinedate());
	    result.put("totalamount", materialbaseline.getTotalamount());
	    result.put("status", materialbaseline.getStatus());
	    result.put("version", materialbaseline.getVersion());
	    return result;
	}
	
	@Override
	public List<Map<String, Object>> listAllForSelect() {
		return materialbaselineRepository.listForSelect();
	}
	
}

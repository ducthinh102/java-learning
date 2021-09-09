

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
import com.redsun.server.wh.model.Materialquantity;
import com.redsun.server.wh.model.History;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.MaterialquantityRepository;
import com.redsun.server.wh.repository.HistoryRepository;
import com.redsun.server.wh.repository.specification.MaterialquantitySpecification;
import com.redsun.server.wh.repository.specification.MaterialquantitySpecificationsBuilder;
import com.redsun.server.wh.service.MaterialquantityService;
import com.redsun.server.wh.util.SecurityUtil;

@Service("materialquantity")
@Transactional
public class MaterialquantityServiceImpl implements MaterialquantityService {
	
	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private MaterialquantityRepository materialquantityRepository;
	
	@Autowired
	private MaterialquantitySpecificationsBuilder materialquantitySpecificationsBuilder;

	@Override
	public Materialquantity save(Materialquantity materialquantity) {
		return materialquantityRepository.save(materialquantity);
	}

	@Override
	public Materialquantity create(Materialquantity materialquantity) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Current date;
		Date currentDate = new Date();
		materialquantity.setStatus(Constant.SERVERDB_STATUS_NEW);
		materialquantity.setIdcreate(iduser);
		materialquantity.setCreatedate(currentDate);
		materialquantity.setIdowner(iduser);
		materialquantity.setIdupdate(iduser);
		materialquantity.setUpdatedate(currentDate);
		materialquantity.setVersion(1);
		return materialquantityRepository.save(materialquantity);
	}

	@Override
	public Integer updateLock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Update lock.
		Integer result = materialquantityRepository.updateLock(id, iduser, Constant.SERVERDB_lOCKTIMEOUT);
		if(result == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		return result;		
	}
	
	@Override
	public Integer updateUnlock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Update lock.
		Integer result = materialquantityRepository.updateUnlock(id);
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
		//Materialquantity materialquantity = (Materialquantity) params.get("materialquantity");
		// Get from DB.
		Materialquantity materialquantityDb = materialquantityRepository.findOne(id);
		if(materialquantityDb == null) { // not exist.
			throw new ServerException(Constant.SERVERCODE_NOTEXISTID);
		} else if(materialquantityDb.getIdlock() != iduser) { // locked.
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		} else if (materialquantityDb.getVersion() != version) { // version difference.
			throw new ServerException(Constant.SERVERCODE_VERSIONDIFFERENCE);
		} else {
			// Keep history data.
			String historyStr = materialquantityDb.toString();
			// Increase version.
			materialquantityDb.setVersion(materialquantityDb.getVersion() + 1);
			// return.
			result.put("materialquantityDb", materialquantityDb);
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
		History history = new History(id, "materialquantity", historyStr);
		historyRepository.save(history);
		// return.
		return result;
	}
	
	@Override
	public Materialquantity update(Integer id, Materialquantity materialquantity) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", materialquantity.getVersion());
		params.put("materialquantity", materialquantity);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Materialquantity materialquantityDb = (Materialquantity) resultPre.get("materialquantityDb");
		// Ignore properties.
		String[] ignoreProperties = new String[] { "status", "idcreate", "idowner", "idupdate", "iddelete", "idlock", "createdate", "updatedate", "deletedate", "lockdate", "version" };
		// Copy data.
		BeanUtils.copyProperties(materialquantity, materialquantityDb, ignoreProperties);
		Date currentDate = new Date();
		materialquantityDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		materialquantityDb.setIdupdate(iduser);
		materialquantityDb.setUpdatedate(currentDate);
		materialquantityDb.setIdowner(iduser);
		// Save.
		materialquantityDb = materialquantityRepository.save(materialquantityDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return materialquantityDb;
	}

	@Override
	public Materialquantity updateWithLock(Integer id, Materialquantity materialquantity) throws JsonParseException, JsonMappingException, IOException {
		Materialquantity result = null;
		// Lock to update.
		updateLock(id);
		// Update.
		result = update(id, materialquantity);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}
	
	@Override
	public Materialquantity updateForDelete(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", version);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Materialquantity materialquantityDb = (Materialquantity) resultPre.get("materialquantityDb");
		Date currentDate = new Date();
		materialquantityDb.setStatus(Constant.SERVERDB_STATUS_DELETE);
		materialquantityDb.setIddelete(iduser);
		materialquantityDb.setDeletedate(currentDate);
		// Save.
		materialquantityDb = materialquantityRepository.save(materialquantityDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return materialquantityDb;
	}

	@Override
	public Materialquantity updateForDeleteWithLock(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		Materialquantity result = null;
		// Lock to update.
		updateLock(id);
		// Update data.
		result = updateForDelete(id, version);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}


	@Override
	public void delete(Materialquantity materialquantity) {
		materialquantityRepository.delete(materialquantity);
	}

	@Override
	public void deleteById(Integer id) {
		materialquantityRepository.delete(id);
	}

	@Override
	public Materialquantity getById(Integer id) {
		return materialquantityRepository.findOne(id);
	}

	@Override
	public List<Materialquantity> listAll() {
		return materialquantityRepository.findAll();
	}

	@Override
	public long countAll() {
		return materialquantityRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return materialquantityRepository.exists(id);
	}
	
	public List<Materialquantity> listWithCritera(SearchCriteria searchCriteria) {
		Specification<Materialquantity> materialquantitySpecification = new MaterialquantitySpecification(searchCriteria);
		// Where status != delete.
		Specification<Materialquantity> notDeleteSpec = new MaterialquantitySpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialquantitySpecification = Specifications.where(materialquantitySpecification).and(notDeleteSpec);
		// Execute.
        List<Materialquantity> result = materialquantityRepository.findAll(materialquantitySpecification);
        return result;
	}
	
	public List<Materialquantity> listWithCriteras(List<SearchCriteria> searchCriterias) {
        Specification<Materialquantity> materialquantitySpecification = materialquantitySpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Materialquantity> notDeleteSpec = new MaterialquantitySpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialquantitySpecification = Specifications.where(materialquantitySpecification).and(notDeleteSpec);
		// Execute.
        List<Materialquantity> result = materialquantityRepository.findAll(materialquantitySpecification);
        return result;
	}
	
	public Page<Materialquantity> listAllByPage(Pageable pageable) {
		return materialquantityRepository.findAll(pageable);
	}
	
	public Page<Materialquantity> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<Materialquantity> materialquantitySpecification = new MaterialquantitySpecification(searchCriteria);
		// Where status != delete.
		Specification<Materialquantity> notDeleteSpec = new MaterialquantitySpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialquantitySpecification = Specifications.where(materialquantitySpecification).and(notDeleteSpec);
		// Execute.
		Page<Materialquantity> result = materialquantityRepository.findAll(materialquantitySpecification, pageable);
        return result;
	}
	
	public Page<Materialquantity> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Materialquantity> materialquantitySpecification = materialquantitySpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Materialquantity> notDeleteSpec = new MaterialquantitySpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialquantitySpecification = Specifications.where(materialquantitySpecification).and(notDeleteSpec);
		// Execute.
		Page<Materialquantity> result = materialquantityRepository.findAll(materialquantitySpecification, pageable);
        return result;
	}

}

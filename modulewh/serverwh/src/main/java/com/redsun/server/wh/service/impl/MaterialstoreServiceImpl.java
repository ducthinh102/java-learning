
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
import com.redsun.server.wh.model.History;
import com.redsun.server.wh.model.Materialstore;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.HistoryRepository;
import com.redsun.server.wh.repository.MaterialstoreRepository;
import com.redsun.server.wh.repository.specification.MaterialstoreSpecification;
import com.redsun.server.wh.repository.specification.MaterialstoreSpecificationsBuilder;
import com.redsun.server.wh.service.MaterialstoreService;
import com.redsun.server.wh.util.SecurityUtil;

@Service("materialstore")
@Transactional
public class MaterialstoreServiceImpl implements MaterialstoreService {

	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private MaterialstoreRepository materialstoreRepository;

	@Autowired
	private MaterialstoreSpecificationsBuilder materialstoreSpecificationsBuilder;

	@Override
	public Materialstore save(Materialstore materialstore) {
		return materialstoreRepository.save(materialstore);
	}

	@Override
	public Materialstore create(Materialstore materialstore)
			throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Current date;
		Date currentDate = new Date();
		materialstore.setStatus(Constant.SERVERDB_STATUS_NEW);
		materialstore.setIdcreate(iduser);
		materialstore.setCreatedate(currentDate);
		materialstore.setIdowner(iduser);
		materialstore.setVersion(1);
		return materialstoreRepository.save(materialstore);
	}

	@Override
	public Integer updateLock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Update lock.
		Integer result = materialstoreRepository.updateLock(id, iduser, Constant.SERVERDB_lOCKTIMEOUT);
		if (result == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		return result;
	}

	@Override
	public Integer updateUnlock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Update lock.
		Integer result = materialstoreRepository.updateUnlock(id);
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
		Materialstore materialstoreDb = materialstoreRepository.findOne(id);
		if (materialstoreDb == null) {
			throw new ServerException(Constant.SERVERCODE_NOTEXISTID);
		} else if (materialstoreDb.getIdlock() != iduser) { // locked.
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		} else if (materialstoreDb.getVersion() != version) {
			throw new ServerException(Constant.SERVERCODE_VERSIONDIFFERENCE);
		} else {
			// Keep history data.
			String historyStr = materialstoreDb.toString();
			// Increase version.
			materialstoreDb.setVersion(materialstoreDb.getVersion() + 1);
			// return.
			result.put("materialstoreDb", materialstoreDb);
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
		History history = new History(id, "materialstore", historyStr);
		historyRepository.save(history);
		// return.
		return result;
	}

	@Override
	public Materialstore updateWithLock(Integer id, Materialstore materialstore)
			throws JsonParseException, JsonMappingException, IOException {
		Materialstore result = null;
		// Lock to update.
		updateLock(id);
		// Update.
		result = update(id, materialstore);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}
	
	@Override
	public Materialstore updateWithLockForImport(Integer id, Materialstore materialstore)
			throws JsonParseException, JsonMappingException, IOException {
		Materialstore result = null;
		
		// Lock to update.
		updateLock(id);
		// Update.
		Materialstore materialstoreDb = materialstoreRepository.findOne(id);
		materialstore.setQuantity(materialstoreDb.getQuantity()+materialstore.getQuantity());
		result = update(id, materialstore);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}

	@Override
	public Materialstore updateForDelete(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", version);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Materialstore materialstoreDb = (Materialstore) resultPre.get("materialstoreDb");
		Date currentDate = new Date();
		materialstoreDb.setStatus(Constant.SERVERDB_STATUS_DELETE);
		materialstoreDb.setIddelete(iduser);
		materialstoreDb.setDeletedate(currentDate);
		// Save.
		materialstoreDb = materialstoreRepository.save(materialstoreDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return materialstoreDb;
	}

	@Override
	public Materialstore updateForDeleteWithLock(Integer id, Integer version)
			throws JsonParseException, JsonMappingException, IOException {
		Materialstore result = null;
		// Lock to update.
		updateLock(id);
		// Update data.
		result = updateForDelete(id, version);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}

	@Override
	public Materialstore update(Integer id, Materialstore materialstore) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", materialstore.getVersion());
		params.put("materialstore", materialstore);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Materialstore materialstoreDb = (Materialstore) resultPre.get("materialstoreDb");
		// Ignore properties.
		String[] ignoreProperties = new String[] { "status", "idcreate", "idowner", "idupdate", "iddelete", "idlock",
				"createdate", "updatedate", "deletedate", "lockdate", "version" };
		// Copy data.
		BeanUtils.copyProperties(materialstore, materialstoreDb, ignoreProperties);
		Date currentDate = new Date();
		materialstoreDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		materialstoreDb.setIdupdate(iduser);
		materialstoreDb.setUpdatedate(currentDate);
		materialstoreDb.setIdowner(iduser);
		// Save.
		materialstoreDb = materialstoreRepository.save(materialstoreDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return materialstoreDb;
	}

	@Override
	public void delete(Materialstore materialstore) {
		materialstoreRepository.delete(materialstore);
	}

	@Override
	public void deleteById(Integer id) {
		materialstoreRepository.delete(id);
	}

	@Override
	public Materialstore getById(Integer id) {
		return materialstoreRepository.findOne(id);
	}

	@Override
	public List<Materialstore> listAll() {
		return materialstoreRepository.findAll();
	}

	@Override
	public long countAll() {
		return materialstoreRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return materialstoreRepository.exists(id);
	}

	@Override
	public List<Materialstore> listWithCritera(SearchCriteria searchCriteria) {
		Specification<Materialstore> materialstoreSpecification = new MaterialstoreSpecification(searchCriteria);
		// Where status != delete.
		Specification<Materialstore> notDeleteSpec = new MaterialstoreSpecification(
				new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialstoreSpecification = Specifications.where(materialstoreSpecification).and(notDeleteSpec);
		// Execute.
		List<Materialstore> result = materialstoreRepository.findAll(materialstoreSpecification);
		return result;
	}

	@Override
	public List<Materialstore> listWithCriteras(List<SearchCriteria> searchCriterias) {
		Specification<Materialstore> materialstoreSpecification = materialstoreSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Materialstore> notDeleteSpec = new MaterialstoreSpecification(
				new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialstoreSpecification = Specifications.where(materialstoreSpecification).and(notDeleteSpec);
		// Execute.
		List<Materialstore> result = materialstoreRepository.findAll(materialstoreSpecification);
		return result;
	}

	public Page<Materialstore> listAllByPage(Pageable pageable) {
		return materialstoreRepository.findAll(pageable);
	}

	public Page<Materialstore> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<Materialstore> materialstoreSpecification = new MaterialstoreSpecification(searchCriteria);
		// Where status != delete.
		Specification<Materialstore> notDeleteSpec = new MaterialstoreSpecification(
				new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialstoreSpecification = Specifications.where(materialstoreSpecification).and(notDeleteSpec);
		// Execute.
		Page<Materialstore> result = materialstoreRepository.findAll(materialstoreSpecification, pageable);
		return result;
	}

	public Page<Materialstore> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Materialstore> materialstoreSpecification = materialstoreSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Materialstore> notDeleteSpec = new MaterialstoreSpecification(
				new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialstoreSpecification = Specifications.where(materialstoreSpecification).and(notDeleteSpec);
		// Execute.
		Page<Materialstore> result = materialstoreRepository.findAll(materialstoreSpecification, pageable);
		return result;
	}

	@Override
	public Map<String, Object> quantityMaterialByidStore(Integer idstore, Integer idmaterial) {
		return materialstoreRepository.quantityMaterial(idstore, idmaterial);
	}
	
	@Override
	public Integer storeQuantity(Integer idstore) {
		return materialstoreRepository.storequantity(idstore);
		
	}
	
	public Page<Map<String, Object>> listWithCriteriasByIdStoreAndPage(Integer idstore,List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Materialstore> materialstoreSpecification = materialstoreSpecificationsBuilder.build(searchCriterias);
		// Where idmaterialimport.
		Specification<Materialstore> idstoreSpec = new MaterialstoreSpecification(new SearchCriteria("idstore", "=", idstore));
		// Where status != delete.
		Specification<Materialstore> notDeleteSpec = new MaterialstoreSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialstoreSpecification = Specifications.where(materialstoreSpecification).and(idstoreSpec).and(notDeleteSpec);
		// Execute.
		Page<Materialstore> materialstores = materialstoreRepository.findAll(materialstoreSpecification,pageable);
		Page<Map<String, Object>> result = materialstores.map(this::convertToMap);
		return result;
	}
	
	private Map<String, Object> convertToMap(final Materialstore materialstore) {
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("id", materialstore.getId());
		result.put("materialname", materialstore.getMaterial().getName());
		result.put("quantity", materialstore.getQuantity());
		result.put("note", materialstore.getNote());
		return result;
	}
	
	public Page<Map<String, Object>> listWithCriteriasByIdMaterialAndPage(Integer idmaterial,List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Materialstore> materialstoreSpecification = materialstoreSpecificationsBuilder.build(searchCriterias);
		// Where idmaterialimport.
		Specification<Materialstore> idmaterialSpec = new MaterialstoreSpecification(new SearchCriteria("idmaterial", "=", idmaterial));
		// Where status != delete.
		Specification<Materialstore> notDeleteSpec = new MaterialstoreSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialstoreSpecification = Specifications.where(materialstoreSpecification).and(idmaterialSpec).and(notDeleteSpec);
		// Execute.
		Page<Materialstore> materialstores = materialstoreRepository.findAll(materialstoreSpecification,pageable);
		Page<Map<String, Object>> result = materialstores.map(this::convertToMapMaterial);
		return result;
	}
	
	private Map<String, Object> convertToMapMaterial(final Materialstore materialstore) {
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("id", materialstore.getId());
		result.put("storename", materialstore.getStore().getName());
		result.put("quantity", materialstore.getQuantity());
		result.put("note", materialstore.getNote());
		return result;
	}

	@Override
	public Map<String, Object> checkQuantityMaterial(Integer idstore, Integer idmaterial) {
		return materialstoreRepository.checkQuantityMaterial(idstore, idmaterial);
	}

}

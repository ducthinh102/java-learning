
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
import com.redsun.server.wh.model.Store;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.HistoryRepository;
import com.redsun.server.wh.repository.MaterialstoreRepository;
import com.redsun.server.wh.repository.StoreRepository;
import com.redsun.server.wh.repository.specification.StoreSpecification;
import com.redsun.server.wh.repository.specification.StoreSpecificationsBuilder;
import com.redsun.server.wh.service.StoreService;
import com.redsun.server.wh.util.SecurityUtil;


@Service("store")
@Transactional
public class StoreServiceImpl implements StoreService {

	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private StoreRepository storeRepository;
	
	@Autowired
	private MaterialstoreRepository materialstoreRepository;

	@Autowired
	private StoreSpecificationsBuilder storeSpecificationsBuilder;

	@Override
	public Store save(Store store) {
		return storeRepository.save(store);
	}

	@Override
	public Store create(Store store) throws JsonParseException, JsonMappingException, IOException {
		if (storeRepository.isExistCode(store.getId(), store.getCode())
		&& storeRepository.isExistName(store.getId(), store.getName())		
		) {
			throw new ServerException(Constant.SERVERCODE_EXISTALL);
		} else {
			if (storeRepository.isExistCode(store.getId(), store.getCode())) {
				throw new ServerException(Constant.SERVERCODE_EXISTCODE);
			}
			else if (storeRepository.isExistName(store.getId(), store.getName())) {
				throw new ServerException(Constant.SERVERCODE_EXISTNAME);
			} else {
				// Get iduser.
				Integer iduser = SecurityUtil.getIdUser();
				// Current date;
				Date currentDate = new Date();
				store.setIdparent(1);
				store.setIdcreate(iduser);
				store.setCreatedate(currentDate);
				store.setIdowner(iduser);
				store.setVersion(1);
				store.setStatus(1);
				return storeRepository.save(store);
			}
		}
	}

	@Override
	public Integer updateLock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Update lock.
		Integer result = storeRepository.updateLock(id, iduser, Constant.SERVERDB_lOCKTIMEOUT);
		if (result == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		return result;
	}

	@Override
	public Integer updateUnlock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Update lock.
		Integer result = storeRepository.updateUnlock(id);
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
		Store store = (Store) params.get("store");
		// Get from DB.
		Store storeDb = storeRepository.findOne(id);
		if (storeDb == null) {
			throw new ServerException(Constant.SERVERCODE_NOTEXISTID);
		} else if (storeDb.getIdlock() != iduser) { // locked.
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		} else if (storeDb.getVersion() != version) {
			throw new ServerException(Constant.SERVERCODE_VERSIONDIFFERENCE);
		} else if (store != null && storeRepository.isExistCode(id, store.getCode())) {
			throw new ServerException(Constant.SERVERCODE_EXISTCODE);
		} else if (store != null && storeRepository.isExistName(id, store.getName())) {
			throw new ServerException(Constant.SERVERCODE_EXISTNAME);
		} else if (store != null && storeRepository.isExistCode(id, store.getCode())
								 && storeRepository.isExistName(id, store.getName())) { 
			throw new ServerException(Constant.SERVERCODE_EXISTALL);
		} else {
			// Keep history data.
			String historyStr = storeDb.toString();
			// Increase version.
			storeDb.setVersion(storeDb.getVersion() + 1);
			// return.
			result.put("storeDb", storeDb);
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
		History history = new History(id, "store", historyStr);
		historyRepository.save(history);
		// return.
		return result;
	}

	@Override
	public Store updateWithLock(Integer id, Store store)
			throws JsonParseException, JsonMappingException, IOException {
		Store result = null;
		// Lock to update.
		updateLock(id);
		// Update.
		result = update(id, store);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}

	@Override
	public Store updateForDelete(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", version);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Store storeDb = (Store) resultPre.get("storeDb");
		Date currentDate = new Date();
		storeDb.setStatus(Constant.SERVERDB_STATUS_DELETE);
		storeDb.setIddelete(iduser);
		storeDb.setDeletedate(currentDate);
		// Save.
		storeDb = storeRepository.save(storeDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return storeDb;
	}

	@Override
	public Store updateForDeleteWithLock(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		Store result = null;
		// Lock to update.
		updateLock(id);
		// Update data.
		result = updateForDelete(id, version);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}

	@Override
	public Store update(Integer id, Store store) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", store.getVersion());
		params.put("store", store);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Store storeDb = (Store) resultPre.get("storeDb");
		// Ignore properties.
		String[] ignoreProperties = new String[] { "status", "idcreate", "idowner", "idupdate", "iddelete",
				"idlock", "createdate", "updatedate", "deletedate", "lockdate", "version" };
		// Copy data.
		BeanUtils.copyProperties(store, storeDb, ignoreProperties);
		Date currentDate = new Date();
		storeDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		storeDb.setIdupdate(iduser);
		storeDb.setUpdatedate(currentDate);
		storeDb.setIdowner(iduser);
		storeDb.setIdparent(1);
		storeDb.setVersion(storeDb.getVersion() + 1);
		// Save.
		storeDb = storeRepository.save(storeDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return storeDb;
	}

	@Override
	public void delete(Store store) {
		storeRepository.delete(store);
	}

	@Override
	public void deleteById(Integer id) {
		storeRepository.delete(id);
	}

	@Override
	public Store getById(Integer id) {
		return storeRepository.findOne(id);
	}

	@Override
	public List<Store> listAll() {
		return storeRepository.findAll();
	}

	@Override
	public long countAll() {
		return storeRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return storeRepository.exists(id);
	}
	
	@Override
	public List<Store> listWithCritera(SearchCriteria searchCriteria) {
		Specification<Store> storeSpecification = new StoreSpecification(searchCriteria);
		// Where status != delete.
		Specification<Store> notDeleteSpec = new StoreSpecification(
				new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		storeSpecification = Specifications.where(storeSpecification).and(notDeleteSpec);
		// Execute.
		List<Store> result = storeRepository.findAll(storeSpecification);
		return result;
	}

	@Override
	public List<Store> listWithCriteras(List<SearchCriteria> searchCriterias) {
		Specification<Store> storeSpecification = storeSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Store> notDeleteSpec = new StoreSpecification(
				new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		storeSpecification = Specifications.where(storeSpecification).and(notDeleteSpec);
		// Execute.
		List<Store> result = storeRepository.findAll(storeSpecification);
		return result;
	}

	public Page<Store> listAllByPage(Pageable pageable) {
		return storeRepository.findAll(pageable);
	}

	public Page<Store> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<Store> storeSpecification = new StoreSpecification(searchCriteria);
		// Where status != delete.
		Specification<Store> notDeleteSpec = new StoreSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		storeSpecification = Specifications.where(storeSpecification).and(notDeleteSpec);
		// Execute.
		Page<Store> result = storeRepository.findAll(storeSpecification, pageable);
		return result;
	}

	public Page<Map<String, Object>> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Store> storeSpecification = storeSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Store> notDeleteSpec = new StoreSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		storeSpecification = Specifications.where(storeSpecification).and(notDeleteSpec);
		// Execute.
		Page<Store> store = storeRepository.findAll(storeSpecification, pageable);
		Page<Map<String, Object>> result = store.map(this::convertToMap);
		return result;
	}

	private Map<String, Object> convertToMap(final Store store) {
	    final Map<String, Object> result = new HashMap<String, Object>();
	    result.put("id", store.getId());
	    result.put("total", materialstoreRepository.storequantity(store.getId()));
	    result.put("code", store.getCode());
	    result.put("name", store.getName());
	    result.put("version", store.getVersion());
	    return result;
	}
	
	@Override
	public List<Map<String, Object>> listAllForSelect() {
		List<Map<String, Object>> result = storeRepository.listForSelect();
		// return.
		return result;
	}

}

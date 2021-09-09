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
import com.redsun.server.wh.model.Catalog;
import com.redsun.server.wh.model.History;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.CatalogRepository;
import com.redsun.server.wh.repository.HistoryRepository;
import com.redsun.server.wh.repository.specification.CatalogSpecification;
import com.redsun.server.wh.repository.specification.CatalogSpecificationsBuilder;
import com.redsun.server.wh.service.CatalogService;
import com.redsun.server.wh.util.SecurityUtil;

@Service("catalog")
@Transactional
public class CatalogServiceImpl implements CatalogService {

	public static final Object synchLock = new Object();

	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private CatalogRepository catalogRepository;

	@Autowired
	private CatalogSpecificationsBuilder catalogSpecificationsBuilder;

	@Override
	public Catalog save(Catalog catalog) {
		return catalogRepository.save(catalog);
	}

	@Override
	public Catalog create(Catalog catalog) throws JsonParseException, JsonMappingException, IOException {
		if (catalogRepository.isExistCode(catalog.getId(), catalog.getCode(), catalog.getScope())
				&& catalogRepository.isExistName(catalog.getId(), catalog.getName(), catalog.getScope())) {
			throw new ServerException(Constant.SERVERCODE_EXISTALL);
		} else {
			if (catalogRepository.isExistCode(catalog.getId(), catalog.getCode(), catalog.getScope())) {
				throw new ServerException(Constant.SERVERCODE_EXISTCODE);
			} else if (catalogRepository.isExistName(catalog.getId(), catalog.getName(), catalog.getScope())) {
				throw new ServerException(Constant.SERVERCODE_EXISTNAME);
			} else {
				// Get iduser.
				Integer iduser = SecurityUtil.getIdUser();
				// Current date;
				Date currentDate = new Date();
				catalog.setIdcreate(iduser);
				catalog.setCreatedate(currentDate);
				catalog.setIdowner(iduser);
				catalog.setVersion(1);
				catalog.setStatus(1);
				return catalogRepository.save(catalog);
			}
		}
	}

	@Override
	public Integer updateLock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Update lock.
		Integer result = catalogRepository.updateLock(id, iduser, Constant.SERVERDB_lOCKTIMEOUT);
		if (result == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		return result;
	}

	@Override
	public Integer updateUnlock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Update lock.
		Integer result = catalogRepository.updateUnlock(id);
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
		Catalog catalog = (Catalog) params.get("catalog");
		// Get from DB.
		Catalog catalogDb = catalogRepository.findOne(id);
		if (catalogDb == null) { // not exist.
			throw new ServerException(Constant.SERVERCODE_NOTEXISTID);
		} else if (catalogDb.getIdlock() != iduser) { // locked.
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		} else if (catalogDb.getVersion() != version) { // version difference.
			throw new ServerException(Constant.SERVERCODE_VERSIONDIFFERENCE);
		} else if (catalog != null && catalogRepository.isExistCode(id, catalog.getCode(), catalog.getScope())
				&& catalogRepository.isExistName(id, catalog.getName(), catalog.getScope())) { // exist scope.
			throw new ServerException(Constant.SERVERCODE_EXISTALL);
		} else if (catalog != null && catalogRepository.isExistCode(id, catalog.getCode(), catalog.getScope())) { // exist
																													// code.
			throw new ServerException(Constant.SERVERCODE_EXISTCODE);
		} else if (catalog != null && catalogRepository.isExistName(id, catalog.getName(), catalog.getScope())) { // exist
																													// name.
			throw new ServerException(Constant.SERVERCODE_EXISTNAME);
		} else {
			// Keep history data.
			String historyStr = catalogDb.toString();
			// Increase version.
			catalogDb.setVersion(catalogDb.getVersion() + 1);
			// return.
			result.put("catalogDb", catalogDb);
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
		History history = new History(id, "catalog", historyStr);
		historyRepository.save(history);
		// return.
		return result;
	}

	@Override
	public Catalog update(Integer id, Catalog catalog) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", catalog.getVersion());
		params.put("catalog", catalog);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Catalog catalogDb = (Catalog) resultPre.get("catalogDb");
		// Ignore properties.
		String[] ignoreProperties = new String[] { "status", "idcreate", "idowner", "idupdate", "iddelete", "idlock",
				"createdate", "updatedate", "deletedate", "lockdate" };
		// Copy data.
		BeanUtils.copyProperties(catalog, catalogDb, ignoreProperties);
		Date currentDate = new Date();
		catalogDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		catalogDb.setIdupdate(iduser);
		catalogDb.setUpdatedate(currentDate);
		catalogDb.setIdowner(iduser);
		catalogDb.setVersion(catalogDb.getVersion() + 1);
		// Save.
		catalogDb = catalogRepository.save(catalogDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return catalogDb;
	}

	@Override
	public Catalog updateWithLock(Integer id, Catalog catalog)
			throws JsonParseException, JsonMappingException, IOException {
		Catalog result = null;
		// Lock to update.
		updateLock(id);
		// Update.
		result = update(id, catalog);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}
	@Override
	public void updateForDeleteByIdParent(Integer id) throws JsonParseException, JsonMappingException, IOException {
		Integer iduser = SecurityUtil.getIdUser();
		catalogRepository.updateForDeleteByIdParentSys(iduser, id);
		List<Catalog> listCatalog = catalogRepository.findByIdParent(id);
		if (listCatalog.size()!=0) {
			for (Catalog catalog : listCatalog) {
				updateForDeleteByIdParent(catalog.getId());
			}	
		}
	}
	
	@Override
	public Catalog updateForDelete(Integer id, Integer version)
			throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", version);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Catalog catalogDb = (Catalog) resultPre.get("catalogDb");
		Date currentDate = new Date();
		catalogDb.setStatus(Constant.SERVERDB_STATUS_DELETE);
		catalogDb.setIddelete(iduser);
		catalogDb.setDeletedate(currentDate);
		// Save.
		catalogDb = catalogRepository.save(catalogDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return catalogDb;
	}

	@Override
	public Catalog updateForDeleteWithLock(Integer id, Integer version)
			throws JsonParseException, JsonMappingException, IOException {
		Catalog result = null;
		// Lock to update.
		updateLock(id);
		// Update.
		result = updateForDelete(id, version);
		// Update children.
		updateForDeleteByIdParent(id);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}

	@Override
	public void delete(Catalog catalog) {
		catalogRepository.delete(catalog);
	}

	@Override
	public Catalog getById(Integer id) {
		return catalogRepository.findOne(id);
	}

	@Override
	public List<Catalog> listAll() {
		return catalogRepository.findAll();
	}

	@Override
	public long countAll() {
		return catalogRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return catalogRepository.exists(id);
	}

	@Override
	public List<Catalog> listWithCritera(SearchCriteria searchCriteria,String scope) {
		
		Specification<Catalog> catalogSpecification = new CatalogSpecification(searchCriteria);
		// Where status != delete.
		Specification<Catalog> notDeleteSpec = new CatalogSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		Specification<Catalog> scopeSpec = new CatalogSpecification(new SearchCriteria("scope", "like", scope));
		catalogSpecification = Specifications.where(catalogSpecification).and(notDeleteSpec).and(scopeSpec);
		// Execute.
		List<Catalog> result = catalogRepository.findAll(catalogSpecification);
		return result;

	}

	@Override
	public List<Catalog> listWithCriteras(List<SearchCriteria> searchCriterias,String scope) {

		Specification<Catalog> catalogSpecification = catalogSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Catalog> notDeleteSpec = new CatalogSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		Specification<Catalog> scopeSpec = new CatalogSpecification(new SearchCriteria("scope", "like", scope));
		catalogSpecification = Specifications.where(catalogSpecification).and(notDeleteSpec).and(scopeSpec);
		// Execute.
		List<Catalog> result = catalogRepository.findAll(catalogSpecification);
		return result;

	}

	@Override
	public Page<Catalog> listAllByPage(Pageable pageable) {
		return catalogRepository.findAll(pageable);
	}

	@Override
	public Page<Catalog> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable,String scope) {

		Specification<Catalog> catalogSpecification = new CatalogSpecification(searchCriteria);
		Specification<Catalog> notDeleteSpec = new CatalogSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		Specification<Catalog> scopeSpec = new CatalogSpecification(new SearchCriteria("scope", "like", scope));
		catalogSpecification = Specifications.where(catalogSpecification).and(notDeleteSpec).and(scopeSpec);
		Page<Catalog> result = catalogRepository.findAll(catalogSpecification, pageable);
		return result;

	}

	@Override
	public Page<Catalog> listWithCriterasByPage(List<SearchCriteria> searchCriteria, Pageable pageable,String scope) {

		Specification<Catalog> catalogSpecification = catalogSpecificationsBuilder.build(searchCriteria);
		Specification<Catalog> notDeleteSpec = new CatalogSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		Specification<Catalog> scopeSpec = new CatalogSpecification(new SearchCriteria("scope", "like", scope));
		catalogSpecification = Specifications.where(catalogSpecification).and(notDeleteSpec).and(scopeSpec);
		Page<Catalog> result = catalogRepository.findAll(catalogSpecification, pageable);
		return result;

	}

	@Override
	public List<Catalog> getListByScope(String scope) {
		return catalogRepository.findByScopeAndStatusLessThanEqual(scope, 2);
	}
	
	@Override
	public List<Map<String, Object>> listAllForSelectByScope(String scope) {
		List<Map<String, Object>> result = catalogRepository.listForSelectByScope(scope);
		// return.
		return result;
	}
}


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
import com.redsun.server.wh.model.Supplier;
import com.redsun.server.wh.model.History;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.SupplierRepository;
import com.redsun.server.wh.repository.HistoryRepository;
import com.redsun.server.wh.repository.specification.SupplierSpecification;
import com.redsun.server.wh.repository.specification.SupplierSpecificationsBuilder;
import com.redsun.server.wh.service.SupplierService;
import com.redsun.server.wh.util.SecurityUtil;

@Service("supplier")
@Transactional
public class SupplierServiceImpl implements SupplierService {

	public static final Object synchLock = new Object();

	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private SupplierRepository supplierRepository;

	@Autowired
	private SupplierSpecificationsBuilder supplierSpecificationsBuilder;

	@Override
	public Supplier save(Supplier supplier) {
		return supplierRepository.save(supplier);
	}

	@Override
	public Supplier create(Supplier supplier) throws JsonParseException, JsonMappingException, IOException {
		if (supplierRepository.isExistCode(supplier.getId(), supplier.getCode()) && supplierRepository.isExistName(supplier.getId(), supplier.getName())) { // exis
			throw new ServerException(Constant.SERVERCODE_EXISTALL);
		} else {
			if (supplierRepository.isExistCode(supplier.getId(), supplier.getCode())) {
				throw new ServerException(Constant.SERVERCODE_EXISTCODE);
			} else if (supplierRepository.isExistName(supplier.getId(), supplier.getName())) {
				throw new ServerException(Constant.SERVERCODE_EXISTNAME);
			} else {

				// Get iduser.
				Integer iduser = SecurityUtil.getIdUser();
				// Current date;
				Date currentDate = new Date();
				supplier.setStatus(Constant.SERVERDB_STATUS_NEW);
				supplier.setIdcreate(iduser);
				supplier.setCreatedate(currentDate);
				supplier.setIdowner(iduser);
				supplier.setIdupdate(iduser);
				supplier.setUpdatedate(currentDate);
				supplier.setVersion(1);
				return supplierRepository.save(supplier);
			}
		}
	}

	@Override
	public Integer updateLock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Update lock.
		Integer result = supplierRepository.updateLock(id, iduser, Constant.SERVERDB_lOCKTIMEOUT);
		if (result == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		return result;
	}

	@Override
	public Integer updateUnlock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Update lock.
		Integer result = supplierRepository.updateUnlock(id);
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
		Supplier supplier = (Supplier) params.get("supplier");
		// Get from DB.
		Supplier supplierDb = supplierRepository.findOne(id);
		if (supplierDb == null) { // not exist.
			throw new ServerException(Constant.SERVERCODE_NOTEXISTID);
		} else if (supplierDb.getIdlock() != iduser) { // locked.
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		} else if (supplierDb.getVersion() != version) { // version difference.
			throw new ServerException(Constant.SERVERCODE_VERSIONDIFFERENCE);
		} else if (supplier != null && supplierRepository.isExistCode(id, supplier.getCode())) { // exist
																									// code.
			throw new ServerException(Constant.SERVERCODE_EXISTCODE);
		} else if (supplier != null && supplierRepository.isExistName(id, supplier.getName())) { // exist
																									// name.
			throw new ServerException(Constant.SERVERCODE_EXISTNAME);
		} else {
			// Keep history data.
			String historyStr = supplierDb.toString();
			// Increase version.
			supplierDb.setVersion(supplierDb.getVersion() + 1);
			// return.
			result.put("supplierDb", supplierDb);
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
		History history = new History(id, "supplier", historyStr);
		historyRepository.save(history);
		// return.
		return result;
	}

	@Override
	public Supplier update(Integer id, Supplier supplier) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", supplier.getVersion());
		params.put("supplier", supplier);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Supplier supplierDb = (Supplier) resultPre.get("supplierDb");
		// Ignore properties.
		String[] ignoreProperties = new String[] { "status", "idcreate", "idowner", "idupdate", "iddelete", "idlock",
				"createdate", "updatedate", "deletedate", "lockdate", "version" };
		// Copy data.
		BeanUtils.copyProperties(supplier, supplierDb, ignoreProperties);
		Date currentDate = new Date();
		supplierDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		supplierDb.setIdupdate(iduser);
		supplierDb.setUpdatedate(currentDate);
		supplierDb.setIdowner(iduser);
		// Save.
		supplierDb = supplierRepository.save(supplierDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return supplierDb;
	}

	@Override
	public Supplier updateWithLock(Integer id, Supplier supplier)
			throws JsonParseException, JsonMappingException, IOException {
		Supplier result = null;
		// Lock to update.
		updateLock(id);
		// Update.
		result = update(id, supplier);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}

	@Override
	public Supplier updateForDelete(Integer id, Integer version)
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
		Supplier supplierDb = (Supplier) resultPre.get("supplierDb");
		Date currentDate = new Date();
		supplierDb.setStatus(Constant.SERVERDB_STATUS_DELETE);
		supplierDb.setIddelete(iduser);
		supplierDb.setDeletedate(currentDate);
		// Save.
		supplierDb = supplierRepository.save(supplierDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return supplierDb;
	}

	@Override
	public Supplier updateForDeleteWithLock(Integer id, Integer version)
			throws JsonParseException, JsonMappingException, IOException {
		Supplier result = null;
		// Lock to update.
		updateLock(id);
		// Update data.
		result = updateForDelete(id, version);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}

	@Override
	public void delete(Supplier supplier) {
		supplierRepository.delete(supplier);
	}

	@Override
	public void deleteById(Integer id) {
		supplierRepository.delete(id);
	}

	@Override
	public Supplier getById(Integer id) {
		return supplierRepository.findOne(id);
	}

	@Override
	public List<Supplier> listAll() {
		return supplierRepository.findAll();
	}

	@Override
	public long countAll() {
		return supplierRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return supplierRepository.exists(id);
	}

	public List<Supplier> listWithCritera(SearchCriteria searchCriteria) {
		Specification<Supplier> supplierSpecification = new SupplierSpecification(searchCriteria);
		// Where status != delete.
		Specification<Supplier> notDeleteSpec = new SupplierSpecification(
				new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		supplierSpecification = Specifications.where(supplierSpecification).and(notDeleteSpec);
		// Execute.
		List<Supplier> result = supplierRepository.findAll(supplierSpecification);
		return result;
	}

	public List<Supplier> listWithCriteras(List<SearchCriteria> searchCriterias) {
		Specification<Supplier> supplierSpecification = supplierSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Supplier> notDeleteSpec = new SupplierSpecification(
				new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		supplierSpecification = Specifications.where(supplierSpecification).and(notDeleteSpec);
		// Execute.
		List<Supplier> result = supplierRepository.findAll(supplierSpecification);
		return result;
	}

	public Page<Supplier> listAllByPage(Pageable pageable) {
		return supplierRepository.findAll(pageable);
	}

	public Page<Supplier> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<Supplier> supplierSpecification = new SupplierSpecification(searchCriteria);
		// Where status != delete.
		Specification<Supplier> notDeleteSpec = new SupplierSpecification(
				new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		supplierSpecification = Specifications.where(supplierSpecification).and(notDeleteSpec);
		// Execute.
		Page<Supplier> result = supplierRepository.findAll(supplierSpecification, pageable);
		return result;
	}

	public Page<Supplier> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Supplier> supplierSpecification = supplierSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Supplier> notDeleteSpec = new SupplierSpecification(
				new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		supplierSpecification = Specifications.where(supplierSpecification).and(notDeleteSpec);
		// Execute.
		Page<Supplier> result = supplierRepository.findAll(supplierSpecification, pageable);
		return result;
	}

	public List<Map<String, Object>> listAllForSelect() {
		List<Map<String, Object>> result = supplierRepository.listAllForSelect();
        return result;
	}

}

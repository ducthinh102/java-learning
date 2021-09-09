
package com.redsun.server.wh.service.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
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
import com.redsun.server.wh.model.Materialexport;
import com.redsun.server.wh.model.Materialexportdetail;
import com.redsun.server.wh.model.Materialstore;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.HistoryRepository;
import com.redsun.server.wh.repository.MaterialexportRepository;
import com.redsun.server.wh.repository.MaterialexportdetailRepository;
import com.redsun.server.wh.repository.MaterialstoreRepository;
import com.redsun.server.wh.repository.specification.MaterialexportSpecification;
import com.redsun.server.wh.repository.specification.MaterialexportSpecificationsBuilder;
import com.redsun.server.wh.service.MaterialexportService;
import com.redsun.server.wh.service.MaterialstoreService;
import com.redsun.server.wh.util.SecurityUtil;


@Service("materialexport")
@Transactional
public class MaterialexportServiceImpl implements MaterialexportService {

	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private MaterialstoreService materialStoreService;

	@Autowired
	private MaterialexportRepository materialexportRepository;

	@Autowired
	private MaterialexportdetailRepository materialexportdetailRepository;

	@Autowired
	private MaterialstoreRepository materialstoreRepository;

	@Autowired
	private MaterialexportSpecificationsBuilder materialexportSpecificationsBuilder;

	@Override
	public Materialexport save(Materialexport materialexport) {
		return materialexportRepository.save(materialexport);
	}

	@Override
	public Materialexport create(Materialexport materialexport)
			throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Current date;
		if (materialexportRepository.isExistCode(materialexport.getId(), materialexport.getCode())) {
			throw new ServerException(Constant.SERVERCODE_EXISTCODE);
		} else {
			Date currentDate = new Date();
			materialexport.setStatus(Constant.SERVERDB_STATUS_NEW);
			materialexport.setIdcreate(iduser);
			materialexport.setCreatedate(currentDate);
			materialexport.setIdowner(iduser);
			materialexport.setIdupdate(iduser);
			materialexport.setUpdatedate(currentDate);
			materialexport.setVersion(1);
			return materialexportRepository.save(materialexport);
		}
	}

	@Override
	public Integer updateLock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Update lock.
		Integer result = materialexportRepository.updateLock(id, iduser, Constant.SERVERDB_lOCKTIMEOUT);
		if (result == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		return result;
	}

	@Override
	public Integer updateUnlock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Update lock.
		Integer result = materialexportRepository.updateUnlock(id);
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
		Materialexport materialexport = (Materialexport) params.get("materialexport");
		// Get from DB.
		Materialexport materialexportDb = materialexportRepository.findOne(id);
		if (materialexportDb == null) { // not exist.
			throw new ServerException(Constant.SERVERCODE_NOTEXISTID);
		}
		else if (materialexportDb.getIdlock() != iduser) { // locked.
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		} 
		else if (materialexportDb.getVersion() != version) { // version difference.
			throw new ServerException(Constant.SERVERCODE_VERSIONDIFFERENCE);
		} 
		else if (materialexport != null && materialexportRepository.isExistCode(id, materialexport.getCode())) { // version
			throw new ServerException(Constant.SERVERCODE_EXISTCODE);
		}
		else {
			// Keep history data.
			String historyStr = materialexportDb.toString();
			// Increase version.
			materialexportDb.setVersion(materialexportDb.getVersion() + 1);
			// return.
			result.put("materialexportDb", materialexportDb);
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
		History history = new History(id, "materialexport", historyStr);
		historyRepository.save(history);
		// return.
		return result;
	}

	@Override
	public Materialexport update(Integer id, Materialexport materialexport)
			throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", materialexport.getVersion());
		params.put("materialexport", materialexport);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Materialexport materialexportDb = (Materialexport) resultPre.get("materialexportDb");
		// Ignore properties.
		String[] ignoreProperties = new String[] { "status", "idcreate", "idowner", "idupdate", "iddelete", "idlock",
				"createdate", "updatedate", "deletedate", "lockdate", "version" };
		// Copy data.
		BeanUtils.copyProperties(materialexport, materialexportDb, ignoreProperties);
		Date currentDate = new Date();
		materialexportDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		materialexportDb.setIdupdate(iduser);
		materialexportDb.setUpdatedate(currentDate);
		materialexportDb.setIdowner(iduser);
		// Save.
		materialexportDb = materialexportRepository.save(materialexportDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return materialexportDb;
	}

	@Override
	public Materialexport updateWithLock(Integer id, Materialexport materialexport)
			throws JsonParseException, JsonMappingException, IOException {
		Materialexport result = null;
		// Lock to update.
		updateLock(id);
		// Update.
		Materialexport materialexportDb = materialexportRepository.findOne(id);
		if (materialexportDb.getIdstore() != materialexport.getIdstore()) {
			List<Materialexportdetail> materialexportdetails = materialexportdetailRepository.listMaterialByIdMaterialExport(id);
			for (Materialexportdetail materialexportdetail : materialexportdetails) {
				Materialstore materialStore = materialstoreRepository.getMaterial(materialexport.getIdstore(), materialexportdetail.getIdmaterial());
				if (materialStore != null) {
					materialstoreRepository.subMaterial(materialexport.getIdstore(), materialexportdetail.getIdmaterial(), materialexportdetail.getQuantity());
					materialstoreRepository.returnMaterial(materialexportDb.getIdstore(), materialexportdetail.getIdmaterial(), materialexportdetail.getQuantity());
				}
				else {
					materialstoreRepository.returnMaterial(materialexportDb.getIdstore(), materialexportdetail.getIdmaterial(), materialexportdetail.getQuantity());
					Materialstore materialStoreN = new Materialstore();
					materialStoreN.setIdstore(materialexport.getIdstore());
					materialStoreN.setIdmaterial(materialexportdetail.getIdmaterial());
					materialStoreN.setQuantity(materialexportdetail.getQuantity());
					materialStoreService.create(materialStoreN);	
				}
			}
		}
		result = update(id, materialexport);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}

	@Override
	public Materialexport updateStatusAndOwnerWithLock(Integer id, Materialexport materialexport)
			throws JsonParseException, JsonMappingException, IOException {
		// Lock to update.
		updateLock(id);
		// Update.
		Materialexport materialexportDb = materialexportRepository.findOne(id);
		if (materialexportDb.getIdstore() != materialexport.getIdstore()) {
			List<Materialexportdetail> materialexportdetails = materialexportdetailRepository.listMaterialByIdMaterialExport(id);
			for (Materialexportdetail materialexportdetail : materialexportdetails) {
				Materialstore materialStore = materialstoreRepository.getMaterial(materialexport.getIdstore(), materialexportdetail.getIdmaterial());
				if (materialStore != null) {
					materialstoreRepository.subMaterial(materialexport.getIdstore(), materialexportdetail.getIdmaterial(), materialexportdetail.getQuantity());
					materialstoreRepository.returnMaterial(materialexportDb.getIdstore(), materialexportdetail.getIdmaterial(), materialexportdetail.getQuantity());
				}
				else {
					materialstoreRepository.returnMaterial(materialexportDb.getIdstore(), materialexportdetail.getIdmaterial(), materialexportdetail.getQuantity());
					Materialstore materialStoreN = new Materialstore();
					materialStoreN.setIdstore(materialexport.getIdstore());
					materialStoreN.setIdmaterial(materialexportdetail.getIdmaterial());
					materialStoreN.setQuantity(materialexportdetail.getQuantity());
					materialStoreService.create(materialStoreN);	
				}
			}
		}
		
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", materialexport.getVersion());
		params.put("materialexport", materialexport);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Materialexport materialexportDb1 = (Materialexport) resultPre.get("materialexportDb");
		// Ignore properties.
		String[] ignoreProperties = new String[] { "status", "idcreate", "idowner", "idupdate", "iddelete", "idlock",
				"createdate", "updatedate", "deletedate", "lockdate", "version" };
		// Copy data.
		BeanUtils.copyProperties(materialexport, materialexportDb1, ignoreProperties);
		Date currentDate = new Date();
		materialexportDb1.setIdupdate(iduser);
		materialexportDb1.setUpdatedate(currentDate);
		// Save.
		materialexportDb1 = materialexportRepository.save(materialexportDb1);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		
		// Unlock of update.
		updateUnlock(id);
		return materialexportDb1;
	}

	@Override
	public Materialexport updateForDelete(Integer id, Integer version)
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
		Materialexport materialexportDb = (Materialexport) resultPre.get("materialexportDb");
		Date currentDate = new Date();
		materialexportDb.setStatus(Constant.SERVERDB_STATUS_DELETE);
		materialexportDb.setIddelete(iduser);
		materialexportDb.setDeletedate(currentDate);
		// Save.
		materialexportDb = materialexportRepository.save(materialexportDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return materialexportDb;
	}

	@Override
	public Materialexport updateForDeleteWithLock(Integer id, Integer version)
			throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();

		Materialexport result = null;
		// Lock to update.
		updateLock(id);
		// Update data.
		Materialexport materialexportDb = materialexportRepository.findOne(id);
		result = updateForDelete(id, version);
		List<Materialexportdetail> materialexportdetails = materialexportdetailRepository
				.listMaterialByIdMaterialExport(id);
		for (Materialexportdetail materialexportdetail : materialexportdetails) {
			materialstoreRepository.returnMaterial(materialexportDb.getIdstore(), materialexportdetail.getIdmaterial(),
					materialexportdetail.getQuantity());
		}
		materialexportRepository.updateForDeleteByIdMaterial(iduser, id);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}

	@Override
	public void delete(Materialexport materialexport) {
		materialexportRepository.delete(materialexport);
	}

	@Override
	public void deleteById(Integer id) {
		materialexportRepository.delete(id);
	}

	@Override
	public Materialexport getById(Integer id) {
		return materialexportRepository.findOne(id);
	}

	@Override
	public List<Materialexport> listAll() {
		return materialexportRepository.findAll();
	}

	@Override
	public long countAll() {
		return materialexportRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return materialexportRepository.exists(id);
	}

	public List<Materialexport> listWithCritera(SearchCriteria searchCriteria) {
		Specification<Materialexport> materialexportSpecification = new MaterialexportSpecification(searchCriteria);
		// Where status != delete.
		Specification<Materialexport> notDeleteSpec = new MaterialexportSpecification(
				new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialexportSpecification = Specifications.where(materialexportSpecification).and(notDeleteSpec);
		// Execute.
		List<Materialexport> result = materialexportRepository.findAll(materialexportSpecification);
		return result;
	}

	public List<Materialexport> listWithCriteras(List<SearchCriteria> searchCriterias) {
		Specification<Materialexport> materialexportSpecification = materialexportSpecificationsBuilder
				.build(searchCriterias);
		// Where status != delete.
		Specification<Materialexport> notDeleteSpec = new MaterialexportSpecification(
				new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialexportSpecification = Specifications.where(materialexportSpecification).and(notDeleteSpec);
		// Execute.
		List<Materialexport> result = materialexportRepository.findAll(materialexportSpecification);
		return result;
	}

	public Page<Materialexport> listAllByPage(Pageable pageable) {
		return materialexportRepository.findAll(pageable);
	}

	public Page<Materialexport> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<Materialexport> materialexportSpecification = new MaterialexportSpecification(searchCriteria);
		// Where status != delete.
		Specification<Materialexport> notDeleteSpec = new MaterialexportSpecification(
				new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialexportSpecification = Specifications.where(materialexportSpecification).and(notDeleteSpec);
		// Execute.
		Page<Materialexport> result = materialexportRepository.findAll(materialexportSpecification, pageable);
		return result;
	}

	public Page<Map<String, Object>> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Materialexport> materialexportSpecification = materialexportSpecificationsBuilder
				.build(searchCriterias);
		// Where status != delete.
		// Specification<Materialexport> notDeleteSpec = new
		// MaterialexportSpecification(new SearchCriteria("status", "!=",
		// Constant.SERVERDB_STATUS_DELETE));
		// materialexportSpecification =
		// Specifications.where(materialexportSpecification).and(notDeleteSpec);
		// Execute.
		Page<Materialexport> materialexport = materialexportRepository.findAll(materialexportSpecification, pageable);
		Page<Map<String, Object>> result = materialexport.map(this::convertToMap);
		return result;
	}

	private Map<String, Object> convertToMap(final Materialexport materialexport) {
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("id", materialexport.getId());
		result.put("storename", materialexport.getStore().getName());
		result.put("code", materialexport.getCode());
		result.put("exportdate", materialexport.getExportdate());
		result.put("note", materialexport.getNote());
		result.put("status", materialexport.getStatus());
		result.put("version", materialexport.getVersion());
		return result;
	}

	@Override
	public List<Map<String, Object>> listAllForSelect() {
		return materialexportRepository.listForSelect();
	}

	@Override
	public Map<String, Object> getByIdForView(Integer id) {
		Materialexport materialexport = materialexportRepository.getByIdForView(id);
		Map<String, Object> result = convertToMapForView(materialexport);
		return result;
	}

	private Map<String, Object> convertToMapForView(Materialexport materialexport) {
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("id", materialexport.getId());
		result.put("storeName", materialexport.getStore().getName());
		result.put("code", materialexport.getCode());
		result.put("exportdate", dateformat.format(materialexport.getExportdate()));
		result.put("exporter", materialexport.getUser().getFirstname() + " " + materialexport.getUser().getLastname()
				+ " (" + materialexport.getUser().getUsername() + ")");
		result.put("note", materialexport.getNote());
		return result;
	}

	@Override
	public Integer totalExportQuantity(Integer idstore, Integer idmaterial) {
		return materialexportRepository.totalExportQuantity(idstore, idmaterial);
	}
}

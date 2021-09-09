

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
import com.redsun.server.wh.model.Materialimport;
import com.redsun.server.wh.model.Materialimportdetail;
import com.redsun.server.wh.model.Materialstore;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.HistoryRepository;
import com.redsun.server.wh.repository.MaterialexportRepository;
import com.redsun.server.wh.repository.MaterialimportRepository;
import com.redsun.server.wh.repository.MaterialimportdetailRepository;
import com.redsun.server.wh.repository.MaterialstoreRepository;
import com.redsun.server.wh.repository.specification.MaterialimportSpecification;
import com.redsun.server.wh.repository.specification.MaterialimportSpecificationsBuilder;
import com.redsun.server.wh.service.MaterialimportService;
import com.redsun.server.wh.service.MaterialstoreService;
import com.redsun.server.wh.util.SecurityUtil;

@Service("materialimport")
@Transactional
public class MaterialimportServiceImpl implements MaterialimportService {
	
	@Autowired
	private HistoryRepository historyRepository;
	
	@Autowired
	private MaterialstoreService materialStoreService;

	@Autowired
	private MaterialimportRepository materialimportRepository;
	
	@Autowired
	private MaterialimportdetailRepository materialimportdetailRepository;
	
	@Autowired
	private MaterialstoreRepository materialstoreRepository;
	
	@Autowired
	private MaterialimportSpecificationsBuilder materialimportSpecificationsBuilder;

	@Override
	public Materialimport save(Materialimport materialimport) {
		return materialimportRepository.save(materialimport);
	}

	@Override
	public Materialimport create(Materialimport materialimport) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Current date;
		if (materialimportRepository.isExistCode(materialimport.getId(),materialimport.getCode())) {
			throw new ServerException(Constant.SERVERCODE_EXISTCODE);
		}
		else {
			Date currentDate = new Date();
			materialimport.setStatus(Constant.SERVERDB_STATUS_NEW);
			materialimport.setIdcreate(iduser);
			materialimport.setCreatedate(currentDate);
			materialimport.setIdowner(iduser);
			materialimport.setIdupdate(iduser);
			materialimport.setUpdatedate(currentDate);
			materialimport.setVersion(1);
			return materialimportRepository.save(materialimport);
		}
	}

	@Override
	public Integer updateLock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Update lock.
		Integer result = materialimportRepository.updateLock(id, iduser, Constant.SERVERDB_lOCKTIMEOUT);
		if(result == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		return result;		
	}
	
	@Override
	public Integer updateUnlock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Update lock.
		Integer result = materialimportRepository.updateUnlock(id);
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
		Materialimport materialimport = (Materialimport) params.get("materialimport");
		// Get from DB.
		Materialimport materialimportDb = materialimportRepository.findOne(id);
		if(materialimportDb == null) { // not exist.
			throw new ServerException(Constant.SERVERCODE_NOTEXISTID);
		} else if(materialimportDb.getIdlock() != iduser) { // locked.
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		} else if (materialimportDb.getVersion() != version) { // version difference.
			throw new ServerException(Constant.SERVERCODE_VERSIONDIFFERENCE);
		} else if (materialimport!=null && materialimportRepository.isExistCode(id, materialimport.getCode())) { // version difference.
			throw new ServerException(Constant.SERVERCODE_EXISTCODE);
		} else {
			// Keep history data.
			String historyStr = materialimportDb.toString();
			// Increase version.
			materialimportDb.setVersion(materialimportDb.getVersion() + 1);
			// return.
			result.put("materialimportDb", materialimportDb);
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
		History history = new History(id, "materialimport", historyStr);
		historyRepository.save(history);
		// return.
		return result;
	}
	
	@Override
	public Materialimport update(Integer id, Materialimport materialimport) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", materialimport.getVersion());
		params.put("materialimport", materialimport);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Materialimport materialimportDb = (Materialimport) resultPre.get("materialimportDb");
		// Ignore properties.
		String[] ignoreProperties = new String[] { "status", "idcreate", "idowner", "idupdate", "iddelete", "idlock", "createdate", "updatedate", "deletedate", "lockdate", "version" };
		// Copy data.
		BeanUtils.copyProperties(materialimport, materialimportDb, ignoreProperties);
		Date currentDate = new Date();
		if (materialimportDb.getStatus() == Constant.SERVERDB_STATUS_NEW) {
			materialimportDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		}
		materialimportDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		materialimportDb.setIdupdate(iduser);
		materialimportDb.setUpdatedate(currentDate);
		materialimportDb.setIdowner(iduser);
		// Save.
		materialimportDb = materialimportRepository.save(materialimportDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return materialimportDb;
	}

	@Override
	public Materialimport updateWithLock(Integer id, Materialimport materialimport) throws JsonParseException, JsonMappingException, IOException {
		Materialimport result = null;
		// Lock to update.
		updateLock(id);
		// Update.
		Materialimport materialimportDb = materialimportRepository.findOne(id);
		if (materialimportDb.getIdstore()!=materialimport.getIdstore()) {
			List<Materialimportdetail> materialimportdetails = materialimportdetailRepository.listMaterialByIdMaterialImport(id);
			for (Materialimportdetail materialimportdetail : materialimportdetails) {
				Materialstore materialStore = materialstoreRepository.getMaterial(materialimport.getIdstore(), materialimportdetail.getIdmaterial());
				if (materialStore!=null) {
					materialstoreRepository.addMaterial(materialimport.getIdstore(), materialimportdetail.getIdmaterial(), materialimportdetail.getQuantity());
					materialstoreRepository.removeMaterial(materialimportDb.getIdstore(), materialimportdetail.getIdmaterial(), materialimportdetail.getQuantity());
				}
				else {
					materialstoreRepository.removeMaterial(materialimportDb.getIdstore(), materialimportdetail.getIdmaterial(), materialimportdetail.getQuantity());
					Materialstore materialStoreN = new Materialstore();
					materialStoreN.setIdstore(materialimport.getIdstore());
					materialStoreN.setIdmaterial(materialimportdetail.getIdmaterial());
					materialStoreN.setQuantity(materialimportdetail.getQuantity());
					materialStoreService.create(materialStoreN);	
				}
			}
		}
		result = update(id, materialimport);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}
	

	@Override
	public Materialimport updateStatusAndOwnerWithLock(Integer id, Materialimport materialimport) throws JsonParseException, JsonMappingException, IOException {
		// Lock to update.
		updateLock(id);
		// Update.
		Materialimport materialimportDb = materialimportRepository.findOne(id);
		if (materialimportDb.getIdstore()!=materialimport.getIdstore()) {
			List<Materialimportdetail> materialimportdetails = materialimportdetailRepository.listMaterialByIdMaterialImport(id);
			for (Materialimportdetail materialimportdetail : materialimportdetails) {
				Materialstore materialStore = materialstoreRepository.getMaterial(materialimport.getIdstore(), materialimportdetail.getIdmaterial());
				if (materialStore!=null) {
					materialstoreRepository.addMaterial(materialimport.getIdstore(), materialimportdetail.getIdmaterial(), materialimportdetail.getQuantity());
					materialstoreRepository.removeMaterial(materialimportDb.getIdstore(), materialimportdetail.getIdmaterial(), materialimportdetail.getQuantity());
				}
				else {
					materialstoreRepository.removeMaterial(materialimportDb.getIdstore(), materialimportdetail.getIdmaterial(), materialimportdetail.getQuantity());
					Materialstore materialStoreN = new Materialstore();
					materialStoreN.setIdstore(materialimport.getIdstore());
					materialStoreN.setIdmaterial(materialimportdetail.getIdmaterial());
					materialStoreN.setQuantity(materialimportdetail.getQuantity());
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
		params.put("version", materialimport.getVersion());
		params.put("materialimport", materialimport);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Materialimport materialimportDb1 = (Materialimport) resultPre.get("materialimportDb");
		// Ignore properties.
		String[] ignoreProperties = new String[] { "status", "idcreate", "idowner", "idupdate", "iddelete", "idlock", "createdate", "updatedate", "deletedate", "lockdate", "version" };
		// Copy data.
		BeanUtils.copyProperties(materialimport, materialimportDb1, ignoreProperties);
		Date currentDate = new Date();
		materialimportDb1.setIdupdate(iduser);
		materialimportDb1.setUpdatedate(currentDate);
		// Save.
		materialimportDb1 = materialimportRepository.save(materialimportDb1);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		
		// Unlock of update.
		updateUnlock(id);
		return materialimportDb1;
	}
	
	@Override
	public Materialimport updateForDelete(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", version);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Materialimport materialimportDb = (Materialimport) resultPre.get("materialimportDb");
		Date currentDate = new Date();
		materialimportDb.setStatus(Constant.SERVERDB_STATUS_DELETE);
		materialimportDb.setIddelete(iduser);
		materialimportDb.setDeletedate(currentDate);
		// Save.
		materialimportDb = materialimportRepository.save(materialimportDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return materialimportDb;
	}

	@Override
	public Materialimport updateForDeleteWithLock(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		
		Materialimport result = null;
		// Lock to update.
		updateLock(id);
		// Update data.
		Materialimport materialimportDb = materialimportRepository.findOne(id);
		result = updateForDelete(id, version);
		List<Materialimportdetail> materialimportdetails = materialimportdetailRepository.listMaterialByIdMaterialImport(id);
		for (Materialimportdetail materialimportdetail : materialimportdetails) {
			materialstoreRepository.removeMaterial(materialimportDb.getIdstore(), materialimportdetail.getIdmaterial(), materialimportdetail.getQuantity());
		}
		materialimportRepository.updateForDeleteByIdMaterial(iduser, id);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}


	@Override
	public void delete(Materialimport materialimport) {
		materialimportRepository.delete(materialimport);
	}

	@Override
	public void deleteById(Integer id) {
		materialimportRepository.delete(id);
	}

	@Override
	public Materialimport getById(Integer id) {
		return materialimportRepository.findOne(id);
	}

	@Override
	public List<Materialimport> listAll() {
		return materialimportRepository.findAll();
	}

	@Override
	public long countAll() {
		return materialimportRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return materialimportRepository.exists(id);
	}
	
	public List<Materialimport> listWithCritera(SearchCriteria searchCriteria) {
		Specification<Materialimport> materialimportSpecification = new MaterialimportSpecification(searchCriteria);
		// Where status != delete.
		Specification<Materialimport> notDeleteSpec = new MaterialimportSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialimportSpecification = Specifications.where(materialimportSpecification).and(notDeleteSpec);
		// Execute.
        List<Materialimport> result = materialimportRepository.findAll(materialimportSpecification);
        return result;
	}
	
	public List<Materialimport> listWithCriteras(List<SearchCriteria> searchCriterias) {
        Specification<Materialimport> materialimportSpecification = materialimportSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Materialimport> notDeleteSpec = new MaterialimportSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialimportSpecification = Specifications.where(materialimportSpecification).and(notDeleteSpec);
		// Execute.
        List<Materialimport> result = materialimportRepository.findAll(materialimportSpecification);
        return result;
	}
	
	public Page<Materialimport> listAllByPage(Pageable pageable) {
		return materialimportRepository.findAll(pageable);
	}
	
	public Page<Materialimport> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<Materialimport> materialimportSpecification = new MaterialimportSpecification(searchCriteria);
		// Where status != delete.
		Specification<Materialimport> notDeleteSpec = new MaterialimportSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialimportSpecification = Specifications.where(materialimportSpecification).and(notDeleteSpec);
		// Execute.
		Page<Materialimport> result = materialimportRepository.findAll(materialimportSpecification, pageable);
        return result;
	}
	
	public Page<Map<String, Object>> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Materialimport> materialimportSpecification = materialimportSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		//Specification<Materialimport> notDeleteSpec = new MaterialimportSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		//materialimportSpecification = Specifications.where(materialimportSpecification).and(notDeleteSpec);
		// Execute.
		Page<Materialimport> materialimport = materialimportRepository.findAll(materialimportSpecification, pageable);
		Page<Map<String, Object>> result = materialimport.map(this::convertToMap);
        return result;
	}
	
	private Map<String, Object> convertToMap(final Materialimport materialimport) {
	    final Map<String, Object> result = new HashMap<String, Object>();
	    result.put("id", materialimport.getId());
	    result.put("storename", materialimport.getStore().getName());
	    result.put("code", materialimport.getCode());
	    result.put("importdate", materialimport.getImportdate());
	    result.put("note", materialimport.getNote());
	    result.put("status", materialimport.getStatus());
	    result.put("version", materialimport.getVersion());
	    return result;
	}

	@Override
	public List<Map<String, Object>> listAllForSelect() {
		return materialimportRepository.listForSelect();
	}

	@Override
	public Map<String, Object> getByIdForView(Integer id) {
		Materialimport materialimport = materialimportRepository.getByIdForView(id);
		Map<String, Object> result = convertToMapForView(materialimport);
		return result;
	}

	private Map<String, Object> convertToMapForView(Materialimport materialimport) {
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
	    Map<String, Object> result = new HashMap<String, Object>();
	    result.put("id", materialimport.getId());
	    result.put("storeName", materialimport.getStore().getName());
	    result.put("code", materialimport.getCode());
	    result.put("importdate", dateformat.format(materialimport.getImportdate()));
	    result.put("importer", materialimport.getUser().getFirstname() +" "+ materialimport.getUser().getLastname()+" ("+materialimport.getUser().getUsername()+")");
	    result.put("note", materialimport.getNote());   
	    return result;
	}

	@Override
	public Integer totalImportQuantity(Integer idstore, Integer idmaterial) {
		return materialimportRepository.totalImportQuantity(idstore, idmaterial);
	}	
}

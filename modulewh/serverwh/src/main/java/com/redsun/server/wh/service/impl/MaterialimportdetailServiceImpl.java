
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
import com.redsun.server.wh.model.Materialimportdetail;
import com.redsun.server.wh.model.Materialstore;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.HistoryRepository;
import com.redsun.server.wh.repository.MaterialexportRepository;
import com.redsun.server.wh.repository.MaterialimportRepository;
import com.redsun.server.wh.repository.MaterialimportdetailRepository;
import com.redsun.server.wh.repository.MaterialstoreRepository;
import com.redsun.server.wh.repository.specification.MaterialimportdetailSpecification;
import com.redsun.server.wh.repository.specification.MaterialimportdetailSpecificationsBuilder;
import com.redsun.server.wh.service.MaterialimportdetailService;
import com.redsun.server.wh.service.MaterialstoreService;
import com.redsun.server.wh.util.SecurityUtil;

@Service("materialimportdetail")
@Transactional
public class MaterialimportdetailServiceImpl implements MaterialimportdetailService {

	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private MaterialstoreRepository materialstoreRepository;

	@Autowired
	private MaterialexportRepository materialexportRepository;
	
	@Autowired
	private MaterialimportRepository materialimportRepository;
	
	@Autowired
	private MaterialstoreService materialStoreService;

	@Autowired
	private MaterialimportdetailRepository materialimportdetailRepository;

	@Autowired
	private MaterialimportdetailSpecificationsBuilder materialimportdetailSpecificationsBuilder;

	@Override
	public Materialimportdetail save(Materialimportdetail materialimportdetail) {
		return materialimportdetailRepository.save(materialimportdetail);
	}

	@Override
	public Materialimportdetail create(Materialimportdetail materialimportdetail, Integer idstore) throws JsonParseException, JsonMappingException, IOException {
		if (materialimportdetailRepository.checkExitsMaterial(materialimportdetail.getId(),materialimportdetail.getIdmaterialimport(),materialimportdetail.getIdmaterial())) {
			throw new ServerException(Constant.SERVERCODE_EXITMATERIAL);
		} else {
			Map<String, Object> materialstore = materialStoreService.quantityMaterialByidStore(idstore,materialimportdetail.getIdmaterial());
			//Check quantity in store
			if (materialstore.isEmpty()) {
				Materialstore materialstoreForCreate = new Materialstore();
				materialstoreForCreate.setIdstore(idstore);
				materialstoreForCreate.setIdmaterial(materialimportdetail.getIdmaterial());
				materialstoreForCreate.setQuantity(materialimportdetail.getQuantity());
				materialStoreService.create(materialstoreForCreate);
			}
			else {
				materialstoreRepository.addMaterial(idstore, materialimportdetail.getIdmaterial(), materialimportdetail.getQuantity());
			}
			
			// Get iduser.
			Integer iduser = SecurityUtil.getIdUser();
			// Current date;
			Date currentDate = new Date();
			materialimportdetail.setStatus(Constant.SERVERDB_STATUS_NEW);
			materialimportdetail.setIdcreate(iduser);
			materialimportdetail.setCreatedate(currentDate);
			materialimportdetail.setIdowner(iduser);
			materialimportdetail.setIdupdate(iduser);
			materialimportdetail.setUpdatedate(currentDate);
			materialimportdetail.setVersion(1);
			//return
			return materialimportdetailRepository.save(materialimportdetail);
		}
	}

	@Override
	public Integer updateLock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Update lock.
		Integer result = materialimportdetailRepository.updateLock(id, iduser, Constant.SERVERDB_lOCKTIMEOUT);
		if (result == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		return result;
	}

	@Override
	public Integer updateUnlock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Update lock.
		Integer result = materialimportdetailRepository.updateUnlock(id);
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
		Materialimportdetail materialimportdetail = (Materialimportdetail) params.get("materialimportdetail");
		// Get from DB.
		Materialimportdetail materialimportdetailDb = materialimportdetailRepository.findOne(id);
		if (materialimportdetailDb == null) { // not exist.
			throw new ServerException(Constant.SERVERCODE_NOTEXISTID);
		} else if (materialimportdetailDb.getIdlock() != iduser) { // locked.
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		} else if (materialimportdetailDb.getVersion() != version) { // version difference.
			throw new ServerException(Constant.SERVERCODE_VERSIONDIFFERENCE);
		} else if (materialimportdetail != null && materialimportdetailRepository.checkExitsMaterial(id,materialimportdetail.getIdmaterialimport(), materialimportdetail.getIdmaterial())) { // check material exit in material import																								// difference.
				throw new ServerException(Constant.SERVERCODE_EXITMATERIAL);
		} else {
			// Keep history data.
			String historyStr = materialimportdetailDb.toString();
			// Increase version.
			materialimportdetailDb.setVersion(materialimportdetailDb.getVersion() + 1);
			// return.
			result.put("materialimportdetailDb", materialimportdetailDb);
			result.put("historyStr", historyStr);
		}
		
		return result;
	}

	private Map<String, Object> updatePost(Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		// Get value from params.
		Integer id = (Integer) params.get("id");
		String historyStr = (String) params.get("historyStr");
		// Save history.
		History history = new History(id, "materialimportdetail", historyStr);
		historyRepository.save(history);
		// return.
		return result;
	}

	@Override
	public Materialimportdetail update(Integer id, Materialimportdetail materialimportdetail)throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", materialimportdetail.getVersion());
		params.put("materialimportdetail", materialimportdetail);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Materialimportdetail materialimportdetailDb = (Materialimportdetail) resultPre.get("materialimportdetailDb");
		// Ignore properties.
		String[] ignoreProperties = new String[] { "status", "idcreate", "idowner", "idupdate", "iddelete", "idlock",
				"createdate", "updatedate", "deletedate", "lockdate", "version" };
		// Copy data.
		BeanUtils.copyProperties(materialimportdetail, materialimportdetailDb, ignoreProperties);
		Date currentDate = new Date();
		if (materialimportdetailDb.getStatus() == Constant.SERVERDB_STATUS_NEW) {
			materialimportdetailDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		}
		materialimportdetailDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		materialimportdetailDb.setIdupdate(iduser);
		materialimportdetailDb.setUpdatedate(currentDate);
		materialimportdetailDb.setIdowner(iduser);
		// Save.
		materialimportdetailDb = materialimportdetailRepository.save(materialimportdetailDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return materialimportdetailDb;
	}

	@Override
	public Materialimportdetail updateWithLock(Integer id, Materialimportdetail materialimportdetail, Integer idstore)throws JsonParseException, JsonMappingException, IOException {
		Materialimportdetail result = null;
		// Lock to update.
		updateLock(id);
		// Update store
		Materialimportdetail materialimportdetailDb = materialimportdetailRepository.findOne(id);
		Integer totalImportQuantity = materialimportRepository.totalImportQuantity(idstore, materialimportdetail.getIdmaterial());
		Integer totalExportQuantity = materialexportRepository.totalExportQuantity(idstore, materialimportdetail.getIdmaterial());
		if ((totalImportQuantity-materialimportdetailDb.getQuantity()+materialimportdetail.getQuantity()-totalExportQuantity)<0) {
			throw new ServerException(Constant.SERVERCODE_OUT_OF_STOCK);
		}
		if (materialimportdetailDb.getQuantity()!=materialimportdetail.getQuantity()) { // Update store when quantity update
			materialstoreRepository.addMaterial(idstore, materialimportdetail.getIdmaterial(),(materialimportdetail.getQuantity()-materialimportdetailDb.getQuantity()));
		}
		if (materialimportdetailDb.getIdmaterial() != materialimportdetail.getIdmaterial()) { // Update store when material update
			Materialstore materialStore = materialstoreRepository.getMaterial(idstore,materialimportdetail.getIdmaterial()); // Check quantity in store
			if (materialStore != null) {
				materialstoreRepository.addMaterial(idstore, materialimportdetail.getIdmaterial(),materialimportdetail.getQuantity());
				materialstoreRepository.removeMaterial(idstore, materialimportdetailDb.getIdmaterial(),materialimportdetail.getQuantity());
			} else {
				Materialstore materialStoreN = new Materialstore();
				materialStoreN.setId(-1);
				materialStoreN.setIdstore(idstore);
				materialStoreN.setIdmaterial(materialimportdetail.getIdmaterial());
				materialStoreN.setQuantity(materialimportdetail.getQuantity());
				materialStoreService.create(materialStoreN);
				materialstoreRepository.removeMaterial(idstore, materialimportdetailDb.getIdmaterial(),materialimportdetailDb.getQuantity());
			}
		}
		// Update material detail
		result = update(id, materialimportdetail);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}

	

	@Override
	public Materialimportdetail updateForDelete(Integer id, Integer version)
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
		Materialimportdetail materialimportdetailDb = (Materialimportdetail) resultPre.get("materialimportdetailDb");
		Date currentDate = new Date();
		materialimportdetailDb.setStatus(Constant.SERVERDB_STATUS_DELETE);
		materialimportdetailDb.setIddelete(iduser);
		materialimportdetailDb.setDeletedate(currentDate);
		// Save.
		materialimportdetailDb = materialimportdetailRepository.save(materialimportdetailDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return materialimportdetailDb;
	}

	@Override
	public Materialimportdetail updateForDeleteWithLock(Integer id, Integer version, Integer idstore)
			throws JsonParseException, JsonMappingException, IOException {
		Materialimportdetail result = null;
		// Lock to update.
		updateLock(id);
		Materialimportdetail materialimportdetailDb = materialimportdetailRepository.findOne(id);
		materialstoreRepository.removeMaterial(idstore, materialimportdetailDb.getIdmaterial(),materialimportdetailDb.getQuantity());
		// Update data.
		result = updateForDelete(id, version);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}

	@Override
	public void delete(Materialimportdetail materialimportdetail) {
		materialimportdetailRepository.delete(materialimportdetail);
	}

	@Override
	public void deleteById(Integer id) {
		materialimportdetailRepository.delete(id);
	}

	@Override
	public Materialimportdetail getById(Integer id) {
		return materialimportdetailRepository.findOne(id);
	}

	@Override
	public List<Materialimportdetail> listAll() {
		return materialimportdetailRepository.findAll();
	}

	@Override
	public long countAll() {
		return materialimportdetailRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return materialimportdetailRepository.exists(id);
	}

	public List<Materialimportdetail> listWithCritera(SearchCriteria searchCriteria) {
		Specification<Materialimportdetail> materialimportdetailSpecification = new MaterialimportdetailSpecification(
				searchCriteria);
		// Where status != delete.
		Specification<Materialimportdetail> notDeleteSpec = new MaterialimportdetailSpecification(
				new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialimportdetailSpecification = Specifications.where(materialimportdetailSpecification).and(notDeleteSpec);
		// Execute.
		List<Materialimportdetail> result = materialimportdetailRepository.findAll(materialimportdetailSpecification);
		return result;
	}

	public List<Materialimportdetail> listWithCriteras(List<SearchCriteria> searchCriterias) {
		Specification<Materialimportdetail> materialimportdetailSpecification = materialimportdetailSpecificationsBuilder
				.build(searchCriterias);
		// Where status != delete.
		Specification<Materialimportdetail> notDeleteSpec = new MaterialimportdetailSpecification(
				new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialimportdetailSpecification = Specifications.where(materialimportdetailSpecification).and(notDeleteSpec);
		// Execute.
		List<Materialimportdetail> result = materialimportdetailRepository.findAll(materialimportdetailSpecification);
		return result;
	}

	public Page<Materialimportdetail> listAllByPage(Pageable pageable) {
		return materialimportdetailRepository.findAll(pageable);
	}

	public Page<Materialimportdetail> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<Materialimportdetail> materialimportdetailSpecification = new MaterialimportdetailSpecification(searchCriteria);
		// Where status != delete.
		Specification<Materialimportdetail> notDeleteSpec = new MaterialimportdetailSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialimportdetailSpecification = Specifications.where(materialimportdetailSpecification).and(notDeleteSpec);
		// Execute.
		Page<Materialimportdetail> result = materialimportdetailRepository.findAll(materialimportdetailSpecification,pageable);
		return result;
	}

	public Page<Materialimportdetail> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Materialimportdetail> materialimportdetailSpecification = materialimportdetailSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Materialimportdetail> notDeleteSpec = new MaterialimportdetailSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialimportdetailSpecification = Specifications.where(materialimportdetailSpecification).and(notDeleteSpec);
		// Execute.
		Page<Materialimportdetail> result = materialimportdetailRepository.findAll(materialimportdetailSpecification,pageable);
		return result;
	}

	public Page<Map<String, Object>> listWithCriteriasByIdmaterialimportAndPage(Integer idmaterialimport,List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Materialimportdetail> requestSpecification = materialimportdetailSpecificationsBuilder.build(searchCriterias);
		// Where idmaterialimport.
		Specification<Materialimportdetail> idmaterialimportSpec = new MaterialimportdetailSpecification(new SearchCriteria("idmaterialimport", "=", idmaterialimport));
		// Where status != delete.
		Specification<Materialimportdetail> notDeleteSpec = new MaterialimportdetailSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		requestSpecification = Specifications.where(requestSpecification).and(idmaterialimportSpec).and(notDeleteSpec);
		// Execute.
		Page<Materialimportdetail> materialimportdetails = materialimportdetailRepository.findAll(requestSpecification,pageable);
		Page<Map<String, Object>> result = materialimportdetails.map(this::convertToMap);
		return result;
	}

	private Map<String, Object> convertToMap(final Materialimportdetail materialimportdetail) {
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("id", materialimportdetail.getId());
		result.put("quantity", materialimportdetail.getQuantity());
		result.put("idmaterialimport", materialimportdetail.getIdmaterialimport());
		result.put("idmaterial", materialimportdetail.getIdmaterial());
		result.put("amount", materialimportdetail.getAmount());
		result.put("materialname", materialimportdetail.getMaterial().getName());
		result.put("materialthumbnail", materialimportdetail.getMaterial().getThumbnail());
		result.put("version", materialimportdetail.getVersion());
		return result;
	}

	@Override
	public Map<String, Object> quantityMaterialByIdMaterialimport(Integer idmaterialimport, Integer idmaterial) {
		return materialimportdetailRepository.quantityMaterial(idmaterialimport, idmaterial);
	}

	@Override
	public List<Map<String, Object>> getAllById(Integer id) {
		return materialimportdetailRepository.getAllById(id);
	}

	@Override
	public Map<String, Object> getByIdForView(Integer id) {
		Materialimportdetail materialimportdetail= materialimportdetailRepository.getByIdForView(id);
		Map<String, Object> result = convertToMapForView(materialimportdetail);
		return result;
	}

	private Map<String, Object> convertToMapForView(Materialimportdetail materialimportdetail) {
	    Map<String, Object> result = new HashMap<String, Object>();
	    result.put("id", materialimportdetail.getId());
	    result.put("materialName", materialimportdetail.getMaterial().getName());
	    result.put("price", materialimportdetail.getPrice());
	    result.put("quantity", materialimportdetail.getQuantity());
	    result.put("unitName", materialimportdetail.getCatalog().getName());
	    result.put("amount", materialimportdetail.getAmount());
	    result.put("note", materialimportdetail.getNote());
	    return result;
	}

	@Override
	public void updatePrice(Integer id, Double price) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		materialimportdetailRepository.updatePriceById(id, price,iduser);
	}

	@Override
	public void updateQuantity(Integer id, Integer quantity, Integer idstore)
			throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		Materialimportdetail materialimportdetailDb = materialimportdetailRepository.findOne(id);
		Materialstore materialStore = materialstoreRepository.getMaterial(idstore,materialimportdetailDb.getIdmaterial()); // Check quantity in store
		
		if (materialStore != null) {
			Integer quantityInput = quantity -  materialimportdetailDb.getQuantity();
			materialstoreRepository.addMaterial(idstore, materialimportdetailDb.getIdmaterial(),quantityInput);
		} 
		materialimportdetailRepository.updateQuantityById(id, quantity, iduser);
		
	}
	
}

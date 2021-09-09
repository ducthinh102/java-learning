
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
import com.redsun.server.wh.model.Materialexportdetail;
import com.redsun.server.wh.model.Materialstore;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.HistoryRepository;
import com.redsun.server.wh.repository.MaterialexportdetailRepository;
import com.redsun.server.wh.repository.MaterialstoreRepository;
import com.redsun.server.wh.repository.specification.MaterialexportdetailSpecification;
import com.redsun.server.wh.repository.specification.MaterialexportdetailSpecificationsBuilder;
import com.redsun.server.wh.service.MaterialexportdetailService;
import com.redsun.server.wh.service.MaterialstoreService;
import com.redsun.server.wh.util.SecurityUtil;

@Service("materialexportdetail")
@Transactional
public class MaterialexportdetailServiceImpl implements MaterialexportdetailService {

	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private MaterialstoreRepository materialstoreRepository;

	@Autowired
	private MaterialstoreService materialStoreService;

	@Autowired
	private MaterialexportdetailRepository materialexportdetailRepository;

	@Autowired
	private MaterialexportdetailSpecificationsBuilder materialexportdetailSpecificationsBuilder;

	@Override
	public Materialexportdetail save(Materialexportdetail materialexportdetail) {
		return materialexportdetailRepository.save(materialexportdetail);
	}

	@Override
	public Materialexportdetail create(Materialexportdetail materialexportdetail, Integer idstore)
			throws JsonParseException, JsonMappingException, IOException {
		if (materialexportdetailRepository.checkExitsMaterial(materialexportdetail.getId(),
				materialexportdetail.getIdmaterialexport(), materialexportdetail.getIdmaterial())) {
			throw new ServerException(Constant.SERVERCODE_EXITMATERIAL);
		} else {
			Map<String, Object> materialstore = materialStoreService.quantityMaterialByidStore(idstore,
					materialexportdetail.getIdmaterial());
			// Check quantity in store
			if (materialstore.isEmpty()) {
				throw new ServerException(Constant.SERVERCODE_OUT_OF_STOCK);
			} else {
				Integer quantity = (Integer) materialstore.get("quantity");
				if (quantity - materialexportdetail.getQuantity() < 0) {
					throw new ServerException(Constant.SERVERCODE_OUT_OF_STOCK);
				} else {
					materialstoreRepository.subMaterial(idstore, materialexportdetail.getIdmaterial(),
							materialexportdetail.getQuantity());
				}
			}

			// Get iduser.
			Integer iduser = SecurityUtil.getIdUser();
			// Current date;
			Date currentDate = new Date();
			materialexportdetail.setStatus(Constant.SERVERDB_STATUS_NEW);
			materialexportdetail.setIdcreate(iduser);
			materialexportdetail.setCreatedate(currentDate);
			materialexportdetail.setIdowner(iduser);
			materialexportdetail.setIdupdate(iduser);
			materialexportdetail.setUpdatedate(currentDate);
			materialexportdetail.setVersion(1);
			// return
			return materialexportdetailRepository.save(materialexportdetail);
		}
	}

	@Override
	public Integer updateLock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Update lock.
		Integer result = materialexportdetailRepository.updateLock(id, iduser, Constant.SERVERDB_lOCKTIMEOUT);
		if (result == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		return result;
	}

	@Override
	public Integer updateUnlock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Update lock.
		Integer result = materialexportdetailRepository.updateUnlock(id);
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
		Integer quantityStore = (Integer)params.get("quantitystore");
		Materialexportdetail materialexportdetail = (Materialexportdetail) params.get("materialexportdetail");
		// Get from DB.
		Materialexportdetail materialexportdetailDb = materialexportdetailRepository.findOne(id);
		if (materialexportdetailDb == null) { // not exist.
			throw new ServerException(Constant.SERVERCODE_NOTEXISTID);
		} else if (materialexportdetailDb.getIdlock() != iduser) { // locked.
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		} else if (materialexportdetailDb.getVersion() != version) { // version difference.
			throw new ServerException(Constant.SERVERCODE_VERSIONDIFFERENCE);
		} else if (materialexportdetail != null && materialexportdetailRepository.checkExitsMaterial(id,
				materialexportdetail.getIdmaterialexport(), materialexportdetail.getIdmaterial())) {
			throw new ServerException(Constant.SERVERCODE_EXITMATERIAL);
		} else if (materialexportdetail != null &&quantityStore + materialexportdetailDb.getQuantity() - materialexportdetail.getQuantity() < 0) {
			throw new ServerException(Constant.SERVERCODE_OUT_OF_STOCK);
		} else {
			// Keep history data.
			String historyStr = materialexportdetailDb.toString();
			// Increase version.
			materialexportdetailDb.setVersion(materialexportdetailDb.getVersion() + 1);
			// return.
			result.put("materialexportdetailDb", materialexportdetailDb);
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
		History history = new History(id, "materialexportdetail", historyStr);
		historyRepository.save(history);
		// return.
		return result;
	}

	@Override
	public Materialexportdetail update(Integer id, Materialexportdetail materialexportdetail,Integer quantityStore)
			throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", materialexportdetail.getVersion());
		params.put("materialexportdetail", materialexportdetail);
		params.put("quantitystore", quantityStore);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Materialexportdetail materialexportdetailDb = (Materialexportdetail) resultPre.get("materialexportdetailDb");
		// Ignore properties.
		String[] ignoreProperties = new String[] { "status", "idcreate", "idowner", "idupdate", "iddelete", "idlock",
				"createdate", "updatedate", "deletedate", "lockdate", "version" };
		// Copy data.
		BeanUtils.copyProperties(materialexportdetail, materialexportdetailDb, ignoreProperties);
		Date currentDate = new Date();
		materialexportdetailDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		materialexportdetailDb.setIdupdate(iduser);
		materialexportdetailDb.setUpdatedate(currentDate);
		materialexportdetailDb.setIdowner(iduser);
		// Save.
		materialexportdetailDb = materialexportdetailRepository.save(materialexportdetailDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return materialexportdetailDb;
	}

	@Override
	public Materialexportdetail updateWithLock(Integer id, Materialexportdetail materialexportdetail, Integer idstore)
			throws JsonParseException, JsonMappingException, IOException {
		Materialexportdetail result = null;
		// Lock to update.
		updateLock(id);

		// Update store
		Materialexportdetail materialexportdetailDb = materialexportdetailRepository.findOne(id);
		Map<String, Object> materialstore = materialStoreService.quantityMaterialByidStore(idstore,
				materialexportdetail.getIdmaterial());
		Integer quantity = (Integer) materialstore.get("quantity");

		if (quantity + materialexportdetailDb.getQuantity() - materialexportdetail.getQuantity() < 0) {
			throw new ServerException(Constant.SERVERCODE_OUT_OF_STOCK);
		} else {
			if (materialexportdetailDb.getQuantity() != materialexportdetail.getQuantity()) {
				materialstoreRepository.subMaterial(idstore, materialexportdetail.getIdmaterial(),
						(materialexportdetail.getQuantity() - materialexportdetailDb.getQuantity()));
			}
			if (materialexportdetailDb.getIdmaterial() != materialexportdetail.getIdmaterial()) {
				Materialstore materialStore = materialstoreRepository.getMaterial(idstore,
						materialexportdetail.getIdmaterial()); // Check quantity in store
				if (materialStore != null) {
					materialstoreRepository.subMaterial(idstore, materialexportdetail.getIdmaterial(),
							materialexportdetail.getQuantity());
					materialstoreRepository.returnMaterial(idstore, materialexportdetailDb.getIdmaterial(),
							materialexportdetail.getQuantity());
				} else {
					Materialstore materialStoreN = new Materialstore();
					materialStoreN.setId(-1);
					materialStoreN.setIdstore(idstore);
					materialStoreN.setIdmaterial(materialexportdetail.getIdmaterial());
					materialStoreN.setQuantity(materialexportdetail.getQuantity());
					materialStoreService.create(materialStoreN);
					materialstoreRepository.returnMaterial(idstore, materialexportdetailDb.getIdmaterial(),
							materialexportdetailDb.getQuantity());
				}
			}
		}

		// Update material detail
		result = update(id, materialexportdetail,quantity);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}

	@Override
	public Materialexportdetail updateForDelete(Integer id, Integer version)
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
		Materialexportdetail materialexportdetailDb = (Materialexportdetail) resultPre.get("materialexportdetailDb");
		Date currentDate = new Date();
		materialexportdetailDb.setStatus(Constant.SERVERDB_STATUS_DELETE);
		materialexportdetailDb.setIddelete(iduser);
		materialexportdetailDb.setDeletedate(currentDate);
		// Save.
		materialexportdetailDb = materialexportdetailRepository.save(materialexportdetailDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return materialexportdetailDb;
	}

	@Override
	public Materialexportdetail updateForDeleteWithLock(Integer id, Integer version, Integer idstore)
			throws JsonParseException, JsonMappingException, IOException {
		Materialexportdetail result = null;
		// Lock to update.
		updateLock(id);
		Materialexportdetail materialexportdetailDb = materialexportdetailRepository.findOne(id);
		materialstoreRepository.returnMaterial(idstore, materialexportdetailDb.getIdmaterial(),
				materialexportdetailDb.getQuantity());
		// Update data.
		result = updateForDelete(id, version);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}

	@Override
	public void delete(Materialexportdetail materialexportdetail) {
		materialexportdetailRepository.delete(materialexportdetail);
	}

	@Override
	public void deleteById(Integer id) {
		materialexportdetailRepository.delete(id);
	}

	@Override
	public Materialexportdetail getById(Integer id) {
		return materialexportdetailRepository.findOne(id);
	}

	@Override
	public List<Materialexportdetail> listAll() {
		return materialexportdetailRepository.findAll();
	}

	@Override
	public long countAll() {
		return materialexportdetailRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return materialexportdetailRepository.exists(id);
	}

	public List<Materialexportdetail> listWithCritera(SearchCriteria searchCriteria) {
		Specification<Materialexportdetail> materialexportdetailSpecification = new MaterialexportdetailSpecification(
				searchCriteria);
		// Where status != delete.
		Specification<Materialexportdetail> notDeleteSpec = new MaterialexportdetailSpecification(
				new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialexportdetailSpecification = Specifications.where(materialexportdetailSpecification).and(notDeleteSpec);
		// Execute.
		List<Materialexportdetail> result = materialexportdetailRepository.findAll(materialexportdetailSpecification);
		return result;
	}

	public List<Materialexportdetail> listWithCriteras(List<SearchCriteria> searchCriterias) {
		Specification<Materialexportdetail> materialexportdetailSpecification = materialexportdetailSpecificationsBuilder
				.build(searchCriterias);
		// Where status != delete.
		Specification<Materialexportdetail> notDeleteSpec = new MaterialexportdetailSpecification(
				new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialexportdetailSpecification = Specifications.where(materialexportdetailSpecification).and(notDeleteSpec);
		// Execute.
		List<Materialexportdetail> result = materialexportdetailRepository.findAll(materialexportdetailSpecification);
		return result;
	}

	public Page<Materialexportdetail> listAllByPage(Pageable pageable) {
		return materialexportdetailRepository.findAll(pageable);
	}

	public Page<Materialexportdetail> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<Materialexportdetail> materialexportdetailSpecification = new MaterialexportdetailSpecification(
				searchCriteria);
		// Where status != delete.
		Specification<Materialexportdetail> notDeleteSpec = new MaterialexportdetailSpecification(
				new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialexportdetailSpecification = Specifications.where(materialexportdetailSpecification).and(notDeleteSpec);
		// Execute.
		Page<Materialexportdetail> result = materialexportdetailRepository.findAll(materialexportdetailSpecification,
				pageable);
		return result;
	}

	public Page<Materialexportdetail> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Materialexportdetail> materialexportdetailSpecification = materialexportdetailSpecificationsBuilder
				.build(searchCriterias);
		// Where status != delete.
		Specification<Materialexportdetail> notDeleteSpec = new MaterialexportdetailSpecification(
				new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialexportdetailSpecification = Specifications.where(materialexportdetailSpecification).and(notDeleteSpec);
		// Execute.
		Page<Materialexportdetail> result = materialexportdetailRepository.findAll(materialexportdetailSpecification,
				pageable);
		return result;
	}

	public Page<Map<String, Object>> listWithCriteriasByIdmaterialexportAndPage(Integer idmaterialexport,
			List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Materialexportdetail> requestSpecification = materialexportdetailSpecificationsBuilder
				.build(searchCriterias);
		// Where idmaterialexport.
		Specification<Materialexportdetail> idmaterialexportSpec = new MaterialexportdetailSpecification(
				new SearchCriteria("idmaterialexport", "=", idmaterialexport));
		// Where status != delete.
		Specification<Materialexportdetail> notDeleteSpec = new MaterialexportdetailSpecification(
				new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		requestSpecification = Specifications.where(requestSpecification).and(idmaterialexportSpec).and(notDeleteSpec);
		// Execute.
		Page<Materialexportdetail> materialexportdetails = materialexportdetailRepository.findAll(requestSpecification,
				pageable);
		Page<Map<String, Object>> result = materialexportdetails.map(this::convertToMap);
		return result;
	}

	private Map<String, Object> convertToMap(final Materialexportdetail materialexportdetail) {
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("id", materialexportdetail.getId());
		result.put("quantity", materialexportdetail.getQuantity());
		result.put("idmaterialexport", materialexportdetail.getIdmaterialexport());
		result.put("idmaterial", materialexportdetail.getIdmaterial());
		result.put("amount", materialexportdetail.getAmount());
		result.put("materialname", materialexportdetail.getMaterial().getName());
		result.put("materialthumbnail", materialexportdetail.getMaterial().getThumbnail());
		result.put("version", materialexportdetail.getVersion());
		return result;
	}

	@Override
	public Map<String, Object> quantityMaterialByIdMaterialexport(Integer idmaterialexport, Integer idmaterial) {
		return materialexportdetailRepository.quantityMaterial(idmaterialexport, idmaterial);
	}

	@Override
	public List<Map<String, Object>> getAllById(Integer id) {
		return materialexportdetailRepository.getAllById(id);
	}

	@Override
	public Map<String, Object> getByIdForView(Integer id) {
		Materialexportdetail materialexportdetail = materialexportdetailRepository.getByIdForView(id);
		Map<String, Object> result = convertToMapForView(materialexportdetail);
		return result;
	}

	private Map<String, Object> convertToMapForView(Materialexportdetail materialexportdetail) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("id", materialexportdetail.getId());
		result.put("materialName", materialexportdetail.getMaterial().getName());
		result.put("price", materialexportdetail.getPrice());
		result.put("quantity", materialexportdetail.getQuantity());
		result.put("unitName", materialexportdetail.getCatalog().getName());
		result.put("amount", materialexportdetail.getAmount());
		result.put("note", materialexportdetail.getNote());
		return result;
	}

	@Override
	public void updateQuantity(Integer id, Integer quantity, Integer idstore)
			throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		Materialexportdetail materialexportdetailDb = materialexportdetailRepository.findOne(id);
		Materialstore materialStore = materialstoreRepository.getMaterial(idstore,materialexportdetailDb.getIdmaterial()); // Check quantity in store	
		if (materialStore != null) {
			Integer quantityInput = quantity -  materialexportdetailDb.getQuantity();
			materialstoreRepository.subMaterial(idstore, materialexportdetailDb.getIdmaterial(),quantityInput);
		} 
		materialexportdetailRepository.updateQuantityById(id, quantity, iduser);
	}

}



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
import com.redsun.server.wh.model.Materialformdetail;
import com.redsun.server.wh.model.Purchasedetail;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.HistoryRepository;
import com.redsun.server.wh.repository.MaterialformRepository;
import com.redsun.server.wh.repository.MaterialformdetailRepository;
import com.redsun.server.wh.repository.specification.MaterialformdetailSpecification;
import com.redsun.server.wh.repository.specification.MaterialformdetailSpecificationsBuilder;
import com.redsun.server.wh.service.MaterialformdetailService;
import com.redsun.server.wh.util.SecurityUtil;

@Service("materialformdetail")
@Transactional
public class MaterialformdetailServiceImpl implements MaterialformdetailService {
	
	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private MaterialformdetailRepository materialformdetailRepository;
	
	@Autowired
	private MaterialformRepository materialformRepository;
	
	@Autowired
	private MaterialformdetailSpecificationsBuilder materialformdetailSpecificationsBuilder;

	@Override
	public Materialformdetail save(Materialformdetail materialformdetail) {
		return materialformdetailRepository.save(materialformdetail);
	}

	@Override
	public Materialformdetail create(Materialformdetail materialformdetail) throws JsonParseException, JsonMappingException, IOException {
		if (materialformdetailRepository.checkExitsMaterialInMaterialformForCreate(materialformdetail.getIdmaterialform(), materialformdetail.getIdmaterial())) {
			throw new ServerException(Constant.SERVERCODE_EXITMATERIAL);
		} else {
			// Get iduser.
			Integer iduser = SecurityUtil.getIdUser();
			// Current date;
			Date currentDate = new Date();
			materialformdetail.setStatus(Constant.SERVERDB_STATUS_NEW);
			materialformdetail.setIdcreate(iduser);
			materialformdetail.setCreatedate(currentDate);
			materialformdetail.setIdowner(iduser);
			materialformdetail.setIdupdate(iduser);
			materialformdetail.setUpdatedate(currentDate);
			materialformdetail.setVersion(1);
			materialformdetail.setStartdate(currentDate);
			materialformdetail.setEnddate(currentDate);
			Materialformdetail result = materialformdetailRepository.save(materialformdetail);
			// Update totalAmount.
			Integer idmaterialform = materialformdetail.getIdmaterialform();
			double totalAmount = materialformdetailRepository.sumAmount(idmaterialform);
			materialformRepository.updateTotalAmount(idmaterialform, totalAmount);
			return result;
		}
	}

	@Override
	public Integer updateLock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Update lock.
		Integer result = materialformdetailRepository.updateLock(id, iduser, Constant.SERVERDB_lOCKTIMEOUT);
		if(result == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		return result;		
	}
	
	@Override
	public Integer updateUnlock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Update lock.
		Integer result = materialformdetailRepository.updateUnlock(id);
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
		Materialformdetail materialformdetail = (Materialformdetail) params.get("materialformdetail");
		// Get from DB.
		Materialformdetail materialformdetailDb = materialformdetailRepository.findOne(id);
		if(materialformdetailDb == null) { // not exist.
			throw new ServerException(Constant.SERVERCODE_NOTEXISTID);
		} else if (materialformdetail != null && materialformdetailRepository.checkExitsMaterialInMaterialform(id,materialformdetail.getIdmaterialform(), materialformdetail.getIdmaterial())) { // check material exit in material import																								// difference.
			throw new ServerException(Constant.SERVERCODE_EXITMATERIAL);
		} else if(materialformdetailDb.getIdlock() != iduser) { // locked.
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		} else if (materialformdetailDb.getVersion() != version) { // version difference.
			throw new ServerException(Constant.SERVERCODE_VERSIONDIFFERENCE);
		} else {
			// Keep history data.
			String historyStr = materialformdetailDb.toString();
			// Increase version.
			materialformdetailDb.setVersion(materialformdetailDb.getVersion() + 1);
			// return.
			result.put("materialformdetailDb", materialformdetailDb);
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
		History history = new History(id, "materialformdetail", historyStr);
		historyRepository.save(history);
		// return.
		return result;
	}
	
	@Override
	public Materialformdetail update(Integer id, Materialformdetail materialformdetail) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", materialformdetail.getVersion());
		params.put("materialformdetail", materialformdetail);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Materialformdetail materialformdetailDb = (Materialformdetail) resultPre.get("materialformdetailDb");
		// Ignore properties.
		String[] ignoreProperties = new String[] { "status", "idcreate", "idowner", "idupdate", "iddelete", "idlock", "createdate", "updatedate", "deletedate", "lockdate", "version" };
		// Copy data.
		BeanUtils.copyProperties(materialformdetail, materialformdetailDb, ignoreProperties);
		Date currentDate = new Date();
		if (materialformdetailDb.getStatus() == Constant.SERVERDB_STATUS_NEW) {
			materialformdetailDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		}
		materialformdetailDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		materialformdetailDb.setIdupdate(iduser);
		materialformdetailDb.setUpdatedate(currentDate);
		materialformdetailDb.setIdowner(iduser);
		// Save.
		materialformdetailDb = materialformdetailRepository.save(materialformdetailDb);
		// Update totalAmount.
		Integer idmaterialform = materialformdetail.getIdmaterialform();
		double totalAmount = materialformdetailRepository.sumAmount(idmaterialform);
		materialformRepository.updateTotalAmount(idmaterialform, totalAmount);
		
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return materialformdetailDb;
	}

	@Override
	public Materialformdetail updateWithLock(Integer id, Materialformdetail materialformdetail) throws JsonParseException, JsonMappingException, IOException {
		Materialformdetail result = null;
		// Lock to update.
		updateLock(id);
		// Update.
		result = update(id, materialformdetail);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}
	
	@Override
	public Materialformdetail updateForDelete(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", version);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Materialformdetail materialformdetailDb = (Materialformdetail) resultPre.get("materialformdetailDb");
		Date currentDate = new Date();
		materialformdetailDb.setStatus(Constant.SERVERDB_STATUS_DELETE);
		materialformdetailDb.setIddelete(iduser);
		materialformdetailDb.setDeletedate(currentDate);
		// Save.
		materialformdetailDb = materialformdetailRepository.save(materialformdetailDb);
		// Update totalAmount.
		Integer idmaterialform = materialformdetailDb.getIdmaterialform();
		double totalAmount = materialformdetailRepository.sumAmount(idmaterialform);
		materialformRepository.updateTotalAmount(idmaterialform, totalAmount);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return materialformdetailDb;
	}

	@Override
	public Materialformdetail updateForDeleteWithLock(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		Materialformdetail result = null;
		// Lock to update.
		updateLock(id);
		// Update data.
		result = updateForDelete(id, version);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}

	@Override
	public void delete(Materialformdetail materialformdetail) {
		materialformdetailRepository.delete(materialformdetail);
	}

	@Override
	public void deleteById(Integer id) {
		materialformdetailRepository.delete(id);
	}

	@Override
	public Materialformdetail getById(Integer id) {
		return materialformdetailRepository.findOne(id);
	}

	@Override
	public List<Materialformdetail> listAll() {
		return materialformdetailRepository.findAll();
	}

	@Override
	public long countAll() {
		return materialformdetailRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return materialformdetailRepository.exists(id);
	}
	
	public List<Materialformdetail> listWithCritera(SearchCriteria searchCriteria) {
		Specification<Materialformdetail> materialformdetailSpecification = new MaterialformdetailSpecification(searchCriteria);
		// Where status != delete.
		Specification<Materialformdetail> notDeleteSpec = new MaterialformdetailSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialformdetailSpecification = Specifications.where(materialformdetailSpecification).and(notDeleteSpec);
		// Execute.
        List<Materialformdetail> result = materialformdetailRepository.findAll(materialformdetailSpecification);
        return result;
	}
	
	public List<Materialformdetail> listWithCriteras(List<SearchCriteria> searchCriterias) {
        Specification<Materialformdetail> materialformdetailSpecification = materialformdetailSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Materialformdetail> notDeleteSpec = new MaterialformdetailSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialformdetailSpecification = Specifications.where(materialformdetailSpecification).and(notDeleteSpec);
		// Execute.
        List<Materialformdetail> result = materialformdetailRepository.findAll(materialformdetailSpecification);
        return result;
	}
	
	public Page<Materialformdetail> listAllByPage(Pageable pageable) {
		return materialformdetailRepository.findAll(pageable);
	}
	
	public Page<Materialformdetail> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<Materialformdetail> materialformdetailSpecification = new MaterialformdetailSpecification(searchCriteria);
		// Where status != delete.
		Specification<Materialformdetail> notDeleteSpec = new MaterialformdetailSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialformdetailSpecification = Specifications.where(materialformdetailSpecification).and(notDeleteSpec);
		// Execute.
		Page<Materialformdetail> result = materialformdetailRepository.findAll(materialformdetailSpecification, pageable);
        return result;
	}
	
	public Page<Materialformdetail> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Materialformdetail> materialformdetailSpecification = materialformdetailSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Materialformdetail> notDeleteSpec = new MaterialformdetailSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialformdetailSpecification = Specifications.where(materialformdetailSpecification).and(notDeleteSpec);
		// Execute.
		Page<Materialformdetail> result = materialformdetailRepository.findAll(materialformdetailSpecification, pageable);
        return result;
	}
	
	public Page<Map<String, Object>> listWithCriteriasByIdmaterialformAndPage(Integer idmaterialform, List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Materialformdetail> materialformSpecification = materialformdetailSpecificationsBuilder.build(searchCriterias);
		// Where idmaterialform.
		Specification<Materialformdetail> idmaterialformSpec = new MaterialformdetailSpecification(new SearchCriteria("idmaterialform", "=", idmaterialform));
		// Where status != delete.
		Specification<Materialformdetail> notDeleteSpec = new MaterialformdetailSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialformSpecification = Specifications.where(materialformSpecification).and(idmaterialformSpec).and(notDeleteSpec);
		// Execute.
		Page<Materialformdetail> materialformdetails = materialformdetailRepository.findAll(materialformSpecification, pageable);
		Page<Map<String, Object>> result = materialformdetails.map(this::convertToMap);
        return result;
	}

	private Map<String, Object> convertToMap(final Materialformdetail materialformdetail) {
	    final Map<String, Object> result = new HashMap<String, Object>();
	    result.put("id", materialformdetail.getId());
	    result.put("idmaterialform", materialformdetail.getIdmaterialform());
	    result.put("idmaterial", materialformdetail.getIdmaterial());
		result.put("idunit", materialformdetail.getIdunit());
		result.put("quantity", materialformdetail.getQuantity());
	    result.put("materialname", materialformdetail.getMaterial().getName());
	    result.put("materialthumbnail", materialformdetail.getMaterial().getThumbnail());
	    result.put("unitname", materialformdetail.getUnit().getName());
	    result.put("version", materialformdetail.getVersion());
	    result.put("amount", materialformdetail.getAmount());
	    result.put("code", materialformdetail.getMaterial().getCode());
	    return result;
	}

	@Override
	public Double sumAmount(Integer idmaterialform) {
		Double result = materialformdetailRepository.sumAmount(idmaterialform);
		return result;
	}

	@Override
	public Boolean isExistMaterial(Integer id, Integer integer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Map<String, Object>> getAllById(Integer id) {
		return materialformdetailRepository.getAllById(id);
	}

	@Override
	public void updatePrice(Integer id, Double price) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		materialformdetailRepository.updatePriceById(id, price,iduser);
		// Update totalAmount.
		Materialformdetail materialformdetail = materialformdetailRepository.getById(id);
		Integer idmaterialform = materialformdetail.getIdmaterialform();
		double totalAmount = materialformdetailRepository.sumAmount(idmaterialform);
		materialformRepository.updateTotalAmount(idmaterialform, totalAmount);
	}

	@Override
	public void updateQuantity(Integer id, Integer quantity) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		materialformdetailRepository.updateQuantityById(id, quantity,iduser);
		// Update totalAmount.
		Materialformdetail materialformdetail = materialformdetailRepository.getById(id);
		Integer idmaterialform = materialformdetail.getIdmaterialform();
		double totalAmount = materialformdetailRepository.sumAmount(idmaterialform);
		materialformRepository.updateTotalAmount(idmaterialform, totalAmount);
	}
	
	@Override
	public Materialformdetail updateRef(Integer id, Materialformdetail materialformdetail) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", materialformdetail.getVersion());
		params.put("materialformdetail", materialformdetail);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Materialformdetail materialformdetailDb = (Materialformdetail) resultPre.get("materialformdetailDb");
		materialformdetailDb.setIdref(materialformdetail.getIdref());
		materialformdetailDb.setReftype(materialformdetail.getReftype());
		Date currentDate = new Date();
		if (materialformdetailDb.getStatus() == Constant.SERVERDB_STATUS_NEW) {
			materialformdetailDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		}
		//materialformdetailDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		materialformdetailDb.setIdupdate(iduser);
		materialformdetailDb.setUpdatedate(currentDate);
		materialformdetailDb.setIdowner(iduser);
		// Save.
		materialformdetailDb = materialformdetailRepository.save(materialformdetailDb);
		// Update totalAmount.
		Integer idmaterialform = materialformdetail.getIdmaterialform();
		double totalAmount = materialformdetailRepository.sumAmount(idmaterialform);
		materialformRepository.updateTotalAmount(idmaterialform, totalAmount);
		
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return materialformdetailDb;
	}

	@Override
	public Materialformdetail updateRefWithLock(Integer id, Materialformdetail materialformdetail) throws JsonParseException, JsonMappingException, IOException {
		Materialformdetail result = null;
		// Lock to update.
		updateLock(id);
		// Update.
		result = updateRef(id, materialformdetail);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}
	
}

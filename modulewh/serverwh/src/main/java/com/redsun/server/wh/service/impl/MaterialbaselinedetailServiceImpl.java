

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
import com.redsun.server.wh.model.Materialbaselinedetail;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.HistoryRepository;
import com.redsun.server.wh.repository.MaterialbaselineRepository;
import com.redsun.server.wh.repository.MaterialbaselinedetailRepository;
import com.redsun.server.wh.repository.specification.MaterialbaselinedetailSpecification;
import com.redsun.server.wh.repository.specification.MaterialbaselinedetailSpecificationsBuilder;
import com.redsun.server.wh.service.MaterialbaselinedetailService;
import com.redsun.server.wh.util.SecurityUtil;

@Service("materialbaselinedetail")
@Transactional
public class MaterialbaselinedetailServiceImpl implements MaterialbaselinedetailService {
	
	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private MaterialbaselinedetailRepository materialbaselinedetailRepository;
	
	@Autowired
	private MaterialbaselineRepository materialbaselineRepository;
	
	@Autowired
	private MaterialbaselinedetailSpecificationsBuilder materialbaselinedetailSpecificationsBuilder;

	@Override
	public Materialbaselinedetail save(Materialbaselinedetail materialbaselinedetail) {
		return materialbaselinedetailRepository.save(materialbaselinedetail);
	}

	@Override
	public Materialbaselinedetail create(Materialbaselinedetail materialbaselinedetail) throws JsonParseException, JsonMappingException, IOException {
		if (materialbaselinedetailRepository.checkExitsMaterialInMaterialbaselineForCreate(materialbaselinedetail.getIdmaterialbaseline(), materialbaselinedetail.getIdmaterial())) { // check material exit in material import																								// difference.
			throw new ServerException(Constant.SERVERCODE_EXITMATERIAL);
		}
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Current date;
		Date currentDate = new Date();
		materialbaselinedetail.setStatus(Constant.SERVERDB_STATUS_NEW);
		materialbaselinedetail.setIdcreate(iduser);
		materialbaselinedetail.setCreatedate(currentDate);
		materialbaselinedetail.setIdowner(iduser);
		materialbaselinedetail.setIdupdate(iduser);
		materialbaselinedetail.setUpdatedate(currentDate);
		materialbaselinedetail.setVersion(1);
		Materialbaselinedetail result = materialbaselinedetailRepository.save(materialbaselinedetail);
		// Update totalAmount.
				Integer idmaterialbaseline = materialbaselinedetail.getIdmaterialbaseline();
				double totalAmount = materialbaselinedetailRepository.sumAmount(idmaterialbaseline);
				materialbaselineRepository.updateTotalAmount(idmaterialbaseline, totalAmount);
				return result;
	}

	@Override
	public Integer updateLock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Update lock.
		Integer result = materialbaselinedetailRepository.updateLock(id, iduser, Constant.SERVERDB_lOCKTIMEOUT);
		if(result == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		return result;		
	}
	
	@Override
	public Integer updateUnlock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Update lock.
		Integer result = materialbaselinedetailRepository.updateUnlock(id);
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
		Materialbaselinedetail materialbaselinedetail = (Materialbaselinedetail) params.get("materialbaselinedetail");
		// Get from DB.
		Materialbaselinedetail materialbaselinedetailDb = materialbaselinedetailRepository.findOne(id);
		if(materialbaselinedetailDb == null) { // not exist.
			throw new ServerException(Constant.SERVERCODE_NOTEXISTID);
		} else if(materialbaselinedetailDb.getIdlock() != iduser) { // locked.
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		} else if (materialbaselinedetailDb.getVersion() != version) { // version difference.
			throw new ServerException(Constant.SERVERCODE_VERSIONDIFFERENCE);
		} else if (materialbaselinedetail != null && materialbaselinedetailRepository.checkExitsMaterialInMaterialbaseline(id,materialbaselinedetail.getIdmaterialbaseline(), materialbaselinedetail.getIdmaterial())) { // check material exit in material import																								// difference.
			throw new ServerException(Constant.SERVERCODE_EXITMATERIAL);			
		} else {
			// Keep history data.
			String historyStr = materialbaselinedetailDb.toString();
			// Increase version.
			materialbaselinedetailDb.setVersion(materialbaselinedetailDb.getVersion() + 1);
			// return.
			result.put("materialbaselinedetailDb", materialbaselinedetailDb);
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
		History history = new History(id, "materialbaselinedetail", historyStr);
		historyRepository.save(history);
		// return.
		return result;
	}
	
	@Override
	public Materialbaselinedetail update(Integer id, Materialbaselinedetail materialbaselinedetail) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", materialbaselinedetail.getVersion());
		params.put("materialbaselinedetail", materialbaselinedetail);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Materialbaselinedetail materialbaselinedetailDb = (Materialbaselinedetail) resultPre.get("materialbaselinedetailDb");
		// Ignore properties.
		String[] ignoreProperties = new String[] { "status", "idcreate", "idowner", "idupdate", "iddelete", "idlock", "createdate", "updatedate", "deletedate", "lockdate", "version" };
		// Copy data.
		BeanUtils.copyProperties(materialbaselinedetail, materialbaselinedetailDb, ignoreProperties);
		Date currentDate = new Date();
		materialbaselinedetailDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		materialbaselinedetailDb.setIdupdate(iduser);
		materialbaselinedetailDb.setUpdatedate(currentDate);
		materialbaselinedetailDb.setIdowner(iduser);
		// Save.
		materialbaselinedetailDb = materialbaselinedetailRepository.save(materialbaselinedetailDb);
		// Update totalAmount.
				Integer idmaterialbaseline = materialbaselinedetail.getIdmaterialbaseline();
				double totalAmount = materialbaselinedetailRepository.sumAmount(idmaterialbaseline);
				materialbaselineRepository.updateTotalAmount(idmaterialbaseline, totalAmount);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return materialbaselinedetailDb;
	}

	@Override
	public Materialbaselinedetail updateWithLock(Integer id, Materialbaselinedetail materialbaselinedetail) throws JsonParseException, JsonMappingException, IOException {
		Materialbaselinedetail result = null;
		// Lock to update.
		updateLock(id);
		// Update.
		result = update(id, materialbaselinedetail);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}
	
	@Override
	public Materialbaselinedetail updateForDelete(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", version);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Materialbaselinedetail materialbaselinedetailDb = (Materialbaselinedetail) resultPre.get("materialbaselinedetailDb");
		Date currentDate = new Date();
		materialbaselinedetailDb.setStatus(Constant.SERVERDB_STATUS_DELETE);
		materialbaselinedetailDb.setIddelete(iduser);
		materialbaselinedetailDb.setDeletedate(currentDate);
		// Save.
		materialbaselinedetailDb = materialbaselinedetailRepository.save(materialbaselinedetailDb);
		// Update totalAmount.
		Integer idmaterialbaseline = materialbaselinedetailDb.getIdmaterialbaseline();
		double totalAmount = materialbaselinedetailRepository.sumAmount(idmaterialbaseline);
		materialbaselineRepository.updateTotalAmount(idmaterialbaseline, totalAmount);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return materialbaselinedetailDb;
	}

	@Override
	public Materialbaselinedetail updateForDeleteWithLock(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		Materialbaselinedetail result = null;
		// Lock to update.
		updateLock(id);
		// Update data.
		result = updateForDelete(id, version);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}


	@Override
	public void delete(Materialbaselinedetail materialbaselinedetail) {
		materialbaselinedetailRepository.delete(materialbaselinedetail);
	}

	@Override
	public void deleteById(Integer id) {
		materialbaselinedetailRepository.delete(id);
	}

	@Override
	public Materialbaselinedetail getById(Integer id) {
		return materialbaselinedetailRepository.findOne(id);
	}

	@Override
	public List<Materialbaselinedetail> listAll() {
		return materialbaselinedetailRepository.findAll();
	}

	@Override
	public long countAll() {
		return materialbaselinedetailRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return materialbaselinedetailRepository.exists(id);
	}
	
	public List<Materialbaselinedetail> listWithCritera(SearchCriteria searchCriteria) {
		Specification<Materialbaselinedetail> materialbaselinedetailSpecification = new MaterialbaselinedetailSpecification(searchCriteria);
		// Where status != delete.
		Specification<Materialbaselinedetail> notDeleteSpec = new MaterialbaselinedetailSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialbaselinedetailSpecification = Specifications.where(materialbaselinedetailSpecification).and(notDeleteSpec);
		// Execute.
        List<Materialbaselinedetail> result = materialbaselinedetailRepository.findAll(materialbaselinedetailSpecification);
        return result;
	}
	
	public List<Materialbaselinedetail> listWithCriteras(List<SearchCriteria> searchCriterias) {
        Specification<Materialbaselinedetail> materialbaselinedetailSpecification = materialbaselinedetailSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Materialbaselinedetail> notDeleteSpec = new MaterialbaselinedetailSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialbaselinedetailSpecification = Specifications.where(materialbaselinedetailSpecification).and(notDeleteSpec);
		// Execute.
        List<Materialbaselinedetail> result = materialbaselinedetailRepository.findAll(materialbaselinedetailSpecification);
        return result;
	}
	
	public Page<Materialbaselinedetail> listAllByPage(Pageable pageable) {
		return materialbaselinedetailRepository.findAll(pageable);
	}
	
	public Page<Materialbaselinedetail> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<Materialbaselinedetail> materialbaselinedetailSpecification = new MaterialbaselinedetailSpecification(searchCriteria);
		// Where status != delete.
		Specification<Materialbaselinedetail> notDeleteSpec = new MaterialbaselinedetailSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialbaselinedetailSpecification = Specifications.where(materialbaselinedetailSpecification).and(notDeleteSpec);
		// Execute.
		Page<Materialbaselinedetail> result = materialbaselinedetailRepository.findAll(materialbaselinedetailSpecification, pageable);
        return result;
	}
	
	public Page<Materialbaselinedetail> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Materialbaselinedetail> materialbaselinedetailSpecification = materialbaselinedetailSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Materialbaselinedetail> notDeleteSpec = new MaterialbaselinedetailSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialbaselinedetailSpecification = Specifications.where(materialbaselinedetailSpecification).and(notDeleteSpec);
		// Execute.
		Page<Materialbaselinedetail> result = materialbaselinedetailRepository.findAll(materialbaselinedetailSpecification, pageable);
        return result;
	}
	
	public Page<Map<String, Object>> listWithCriteriasByIdmaterialbaselineAndPage(Integer idmaterialbaseline, List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Materialbaselinedetail> materialbaselineSpecification = materialbaselinedetailSpecificationsBuilder.build(searchCriterias);
		// Where idmaterialbaseline.
		Specification<Materialbaselinedetail> idmaterialbaselineSpec = new MaterialbaselinedetailSpecification(new SearchCriteria("idmaterialbaseline", "=", idmaterialbaseline));
		// Where status != delete.
		Specification<Materialbaselinedetail> notDeleteSpec = new MaterialbaselinedetailSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		materialbaselineSpecification = Specifications.where(materialbaselineSpecification).and(idmaterialbaselineSpec).and(notDeleteSpec);
		// Execute.
		Page<Materialbaselinedetail> materialbaselinedetails = materialbaselinedetailRepository.findAll(materialbaselineSpecification, pageable);
		Page<Map<String, Object>> result = materialbaselinedetails.map(this::convertToMap);
        return result;
	}

	private Map<String, Object> convertToMap(final Materialbaselinedetail materialbaselinedetail) {
	    final Map<String, Object> result = new HashMap<String, Object>();
	    result.put("id", materialbaselinedetail.getId());
	    result.put("quantity", materialbaselinedetail.getQuantity());
	    result.put("amount", materialbaselinedetail.getAmount());
	    result.put("price", materialbaselinedetail.getPrice());
	    result.put("unitname", materialbaselinedetail.getUnit().getName());
	    result.put("materialcode", materialbaselinedetail.getMaterial().getCode());
	    result.put("idmaterialbaseline", materialbaselinedetail.getIdmaterialbaseline());
	    result.put("idmaterial", materialbaselinedetail.getIdmaterial());
	    result.put("materialname", materialbaselinedetail.getMaterial().getName());
	    result.put("note", materialbaselinedetail.getNote());
	    if (materialbaselinedetail.getIdsupplier()!=null) {
	    	result.put("supplier", materialbaselinedetail.getSupplier().getName());
		}
	    else {
	    	result.put("supplier", "");
	    }
	    result.put("materialthumbnail", materialbaselinedetail.getMaterial().getThumbnail());
	    result.put("version", materialbaselinedetail.getVersion());
	    result.put("startdate", materialbaselinedetail.getStartdate());
	    result.put("enddate", materialbaselinedetail.getEnddate());
	    return result;
	}
	
	@Override
	public Double sumAmount(Integer idmaterialbaseline) {
		Double result = materialbaselinedetailRepository.sumAmount(idmaterialbaseline);
		return result;
	}

	@Override
	public List<Map<String, Object>> getAllById(Integer id) {
		return materialbaselinedetailRepository.getAllById(id);
	}

	@Override
	public void updatePrice(Integer id, Double price) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		materialbaselinedetailRepository.updatePriceById(id, price,iduser);
		// Update totalAmount.
		Materialbaselinedetail materialbaselinedetail = materialbaselinedetailRepository.getById(id);
		Integer idmaterialbaseline = materialbaselinedetail.getIdmaterialbaseline();
		double totalAmount = materialbaselinedetailRepository.sumAmount(idmaterialbaseline);
		materialbaselineRepository.updateTotalAmount(idmaterialbaseline, totalAmount);
	}

	@Override
	public void updateQuantity(Integer id, Integer quantity)
			throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		materialbaselinedetailRepository.updateQuantityById(id, quantity,iduser);
		// Update totalAmount.
		Materialbaselinedetail materialbaselinedetail = materialbaselinedetailRepository.getById(id);
		Integer idmaterialbaseline = materialbaselinedetail.getIdmaterialbaseline();
		double totalAmount = materialbaselinedetailRepository.sumAmount(idmaterialbaseline);
		materialbaselineRepository.updateTotalAmount(idmaterialbaseline, totalAmount);
		
	}

}

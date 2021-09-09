

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
import com.redsun.server.wh.model.Quotation;
import com.redsun.server.wh.model.History;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.QuotationRepository;
import com.redsun.server.wh.repository.HistoryRepository;
import com.redsun.server.wh.repository.specification.QuotationSpecification;
import com.redsun.server.wh.repository.specification.QuotationSpecificationsBuilder;
import com.redsun.server.wh.service.QuotationService;
import com.redsun.server.wh.util.SecurityUtil;

@Service("quotation")
@Transactional
public class QuotationServiceImpl implements QuotationService {
	
	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private QuotationRepository quotationRepository;
	
	@Autowired
	private QuotationSpecificationsBuilder quotationSpecificationsBuilder;

	@Override
	public Quotation save(Quotation quotation) {
		return quotationRepository.save(quotation);
	}

	@Override
	public Quotation create(Quotation quotation) throws JsonParseException, JsonMappingException, IOException {
		if (quotationRepository.isExistCode(quotation.getId(), quotation.getCode())) { // exist code.
			throw new ServerException(Constant.SERVERCODE_EXISTCODE);
			
		} else {
			
			// Get iduser.
			Integer iduser = SecurityUtil.getIdUser();
			// Current date;
			Date currentDate = new Date();
			quotation.setStatus(Constant.SERVERDB_STATUS_NEW);
			quotation.setIdcreate(iduser);
			quotation.setCreatedate(currentDate);
			quotation.setIdowner(iduser);
			quotation.setIdupdate(iduser);
			quotation.setUpdatedate(currentDate);
			quotation.setVersion(1);
			return quotationRepository.save(quotation);

		}
		
		
		
	}

	@Override
	public Integer updateLock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Update lock.
		Integer result = quotationRepository.updateLock(id, iduser, Constant.SERVERDB_lOCKTIMEOUT);
		if(result == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		return result;		
	}
	
	@Override
	public Integer updateUnlock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Update lock.
		Integer result = quotationRepository.updateUnlock(id);
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
		Quotation quotation = (Quotation) params.get("quotation");
		// Get from DB.
		Quotation quotationDb = quotationRepository.findOne(id);
		if(quotationDb == null) { // not exist.
			throw new ServerException(Constant.SERVERCODE_NOTEXISTID);
		} else if(quotationDb.getIdlock() != iduser) { // locked.
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		} else if (quotationDb.getVersion() != version) { // version difference.
			throw new ServerException(Constant.SERVERCODE_VERSIONDIFFERENCE);
		} else if (quotation != null && quotationRepository.isExistCode(id, quotation.getCode())) { // exist code.
			throw new ServerException(Constant.SERVERCODE_EXISTCODE);
		} else {
			// Keep history data.
			String historyStr = quotationDb.toString();
			// Increase version.
			quotationDb.setVersion(quotationDb.getVersion() + 1);
			// return.
			result.put("quotationDb", quotationDb);
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
		History history = new History(id, "quotation", historyStr);
		historyRepository.save(history);
		// return.
		return result;
	}
	

	
	
	@Override
	public Quotation update(Integer id, Quotation quotation) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", quotation.getVersion());
		params.put("quotation", quotation);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Quotation quotationDb = (Quotation) resultPre.get("quotationDb");
		// Ignore properties.
		String[] ignoreProperties = new String[] { "status", "idcreate", "idowner", "idupdate", "iddelete", "idlock", "createdate", "updatedate", "deletedate", "lockdate", "version" };
		// Copy data.
		BeanUtils.copyProperties(quotation, quotationDb, ignoreProperties);
		Date currentDate = new Date();
		quotationDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		quotationDb.setIdupdate(iduser);
		quotationDb.setUpdatedate(currentDate);
		quotationDb.setIdowner(iduser);
		// Save.
		quotationDb = quotationRepository.save(quotationDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return quotationDb;
	}

	@Override
	public Quotation updateWithLock(Integer id, Quotation quotation) throws JsonParseException, JsonMappingException, IOException {
		Quotation result = null;
		// Lock to update.
		updateLock(id);
		// Update.
		result = update(id, quotation);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}

	@Override
	public Quotation updateStatusAndOwnerWithLock(Integer id, Quotation quotation) throws JsonParseException, JsonMappingException, IOException {
		// Lock to update.
		updateLock(id);
		
		// Update.
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", quotation.getVersion());
		params.put("quotation", quotation);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Quotation quotationDb = (Quotation) resultPre.get("quotationDb");
		// Ignore properties.
		String[] ignoreProperties = new String[] { "status", "idcreate", "idowner", "idupdate", "iddelete", "idlock", "createdate", "updatedate", "deletedate", "lockdate", "version" };
		// Copy data.
		BeanUtils.copyProperties(quotation, quotationDb, ignoreProperties);
		Date currentDate = new Date();
		quotationDb.setIdupdate(iduser);
		quotationDb.setUpdatedate(currentDate);
		// Save.
		quotationDb = quotationRepository.save(quotationDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		
		// Unlock of update.
		updateUnlock(id);
		return quotationDb;
	}
	
	@Override
	public Quotation updateForDelete(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", version);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Quotation quotationDb = (Quotation) resultPre.get("quotationDb");
		Date currentDate = new Date();
		quotationDb.setStatus(Constant.SERVERDB_STATUS_DELETE);
		quotationDb.setIddelete(iduser);
		quotationDb.setDeletedate(currentDate);
		// Save.
		quotationDb = quotationRepository.save(quotationDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return quotationDb;
	}

	@Override
	public Quotation updateForDeleteWithLock(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		Quotation result = null;
		// Lock to update.
		updateLock(id);
		// Update data.
		result = updateForDelete(id, version);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}


	@Override
	public void delete(Quotation quotation) {
		quotationRepository.delete(quotation);
	}

	@Override
	public void deleteById(Integer id) {
		quotationRepository.delete(id);
	}

	@Override
	public Quotation getById(Integer id) {
		return quotationRepository.findOne(id);
	}

	@Override
	public List<Quotation> listAll() {
		return quotationRepository.findAll();
	}

	@Override
	public long countAll() {
		return quotationRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return quotationRepository.exists(id);
	}
	
	public List<Quotation> listWithCritera(SearchCriteria searchCriteria) {
		Specification<Quotation> quotationSpecification = new QuotationSpecification(searchCriteria);
		// Where status != delete.
		Specification<Quotation> notDeleteSpec = new QuotationSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		quotationSpecification = Specifications.where(quotationSpecification).and(notDeleteSpec);
		// Execute.
        List<Quotation> result = quotationRepository.findAll(quotationSpecification);
        return result;
	}
	
	public List<Quotation> listWithCriteras(List<SearchCriteria> searchCriterias) {
        Specification<Quotation> quotationSpecification = quotationSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Quotation> notDeleteSpec = new QuotationSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		quotationSpecification = Specifications.where(quotationSpecification).and(notDeleteSpec);
		// Execute.
        List<Quotation> result = quotationRepository.findAll(quotationSpecification);
        return result;
	}
	
	public Page<Quotation> listAllByPage(Pageable pageable) {
		return quotationRepository.findAll(pageable);
	}
	
	public Page<Quotation> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<Quotation> quotationSpecification = new QuotationSpecification(searchCriteria);
		// Where status != delete.
		Specification<Quotation> notDeleteSpec = new QuotationSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		quotationSpecification = Specifications.where(quotationSpecification).and(notDeleteSpec);
		// Execute.
		Page<Quotation> result = quotationRepository.findAll(quotationSpecification, pageable);
        return result;
	}
	
	public Page<Map<String, Object>> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Quotation> quotationSpecification = quotationSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		//Specification<Quotation> notDeleteSpec = new QuotationSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		//quotationSpecification = Specifications.where(quotationSpecification).and(notDeleteSpec);
		// Execute.
		Page<Quotation> quotation = quotationRepository.findAll(quotationSpecification, pageable);
		Page<Map<String, Object>> result = quotation.map(this::convertToMap);
        return result;
	}
	
	
	
	private Map<String, Object> convertToMap(final Quotation quotation) {
	    final Map<String, Object> result = new HashMap<String, Object>();
	    result.put("id",quotation.getId());
	    result.put("suppliername", quotation.getSupplier().getName());
	    result.put("idsupplier", quotation.getIdsupplier());
	    result.put("code", quotation.getCode());
	    result.put("startdate", quotation.getStartdate());
	    result.put("enddate", quotation.getEnddate());
	    result.put("version", quotation.getVersion());
	    result.put("note", quotation.getNote());
	   return result;
	}
	
	//Get id and code
	@Override
	public List<Map<String, Object>> listAllForSelect() {
		// TODO Auto-generated method stub
		return quotationRepository.listForSelect();
	}

	@Override
	public Map<String, Object> getByIdForView(Integer id) {
		Quotation quotation = quotationRepository.getByIdForView(id);
		Map<String, Object> result = convertToMapForView(quotation);
		return result;
	}
	
	private Map<String, Object> convertToMapForView(Quotation quotation) {
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
	    Map<String, Object> result = new HashMap<String, Object>();
	    result.put("id", quotation.getId());
	    result.put("idsupplier",quotation.getSupplier().getName());
	    result.put("code", quotation.getCode());
	    result.put("startdate", dateformat.format(quotation.getStartdate()));
	    result.put("enddate", dateformat.format(quotation.getEnddate()));
	    result.put("note", quotation.getNote());
	    return result;
	}	

	

}

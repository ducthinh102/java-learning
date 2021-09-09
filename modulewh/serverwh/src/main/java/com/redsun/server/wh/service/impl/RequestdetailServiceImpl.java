

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
import com.redsun.server.wh.model.Requestdetail;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.HistoryRepository;
import com.redsun.server.wh.repository.RequestdetailRepository;
import com.redsun.server.wh.repository.specification.RequestdetailSpecification;
import com.redsun.server.wh.repository.specification.RequestdetailSpecificationsBuilder;
import com.redsun.server.wh.service.RequestdetailService;
import com.redsun.server.wh.util.SecurityUtil;

@Service("requestdetail")
@Transactional
public class RequestdetailServiceImpl implements RequestdetailService {
	
	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private RequestdetailRepository requestdetailRepository;
	
	@Autowired
	private RequestdetailSpecificationsBuilder requestdetailSpecificationsBuilder;

	@Override
	public Requestdetail save(Requestdetail requestdetail) {
		return requestdetailRepository.save(requestdetail);
	}

	@Override
	public Requestdetail create(Requestdetail requestdetail) throws JsonParseException, JsonMappingException, IOException {
		if (requestdetailRepository.checkExitsMaterialInRequestForCreate(requestdetail.getIdrequest(), requestdetail.getIdmaterial())) { // check material exit in material import																								// difference.
			throw new ServerException(Constant.SERVERCODE_EXITMATERIAL);
		}
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
			// Current date;
			Date currentDate = new Date();
			requestdetail.setStatus(Constant.SERVERDB_STATUS_NEW);
			requestdetail.setIdcreate(iduser);
			requestdetail.setCreatedate(currentDate);
			requestdetail.setIdowner(iduser);
			requestdetail.setIdupdate(iduser);
			requestdetail.setUpdatedate(currentDate);
			requestdetail.setVersion(1);
			return  requestdetailRepository.save(requestdetail);
	}

	@Override
	public Integer updateLock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Update lock.
		Integer result = requestdetailRepository.updateLock(id, iduser, Constant.SERVERDB_lOCKTIMEOUT);
		if(result == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		return result;		
	}
	
	@Override
	public Integer updateUnlock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Update lock.
		Integer result = requestdetailRepository.updateUnlock(id);
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
		Requestdetail requestdetail = (Requestdetail) params.get("requestdetail");
		// Get from DB.
		Requestdetail requestdetailDb = requestdetailRepository.findOne(id);
		if(requestdetailDb == null) { // not exist.
			throw new ServerException(Constant.SERVERCODE_NOTEXISTID);
		} else if(requestdetailDb.getIdlock() != iduser) { // locked.
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		} else if (requestdetailDb.getVersion() != version) { // version difference.
			throw new ServerException(Constant.SERVERCODE_VERSIONDIFFERENCE);
		} else if (requestdetail != null && requestdetailRepository.checkExitsMaterialInRequest(id,requestdetail.getIdrequest(), requestdetail.getIdmaterial())) { // check material exit in material import																								// difference.
			throw new ServerException(Constant.SERVERCODE_EXITMATERIAL);
		}	else {
			// Keep history data.
			String historyStr = requestdetailDb.toString();
			// Increase version.
			requestdetailDb.setVersion(requestdetailDb.getVersion() + 1);
			// return.
			result.put("requestdetailDb", requestdetailDb);
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
		History history = new History(id, "requestdetail", historyStr);
		historyRepository.save(history);
		// return.
		return result;
	}
	
	@Override
	public Requestdetail update(Integer id, Requestdetail requestdetail) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", requestdetail.getVersion());
		params.put("requestdetail", requestdetail);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Requestdetail requestdetailDb = (Requestdetail) resultPre.get("requestdetailDb");
		// Ignore properties.
		String[] ignoreProperties = new String[] { "status", "idcreate", "idowner", "idupdate", "iddelete", "idlock", "createdate", "updatedate", "deletedate", "lockdate", "version" };
		// Copy data.
		BeanUtils.copyProperties(requestdetail, requestdetailDb, ignoreProperties);
		Date currentDate = new Date();
		requestdetailDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		requestdetailDb.setIdupdate(iduser);
		requestdetailDb.setUpdatedate(currentDate);
		requestdetailDb.setIdowner(iduser);
		// Save.
		requestdetailDb = requestdetailRepository.save(requestdetailDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return requestdetailDb;
	}

	@Override
	public Requestdetail updateWithLock(Integer id, Requestdetail requestdetail) throws JsonParseException, JsonMappingException, IOException {
		Requestdetail result = null;
		// Lock to update.
		updateLock(id);
		// Update.
		result = update(id, requestdetail);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}
	
	@Override
	public Requestdetail updateForDelete(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", version);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Requestdetail requestdetailDb = (Requestdetail) resultPre.get("requestdetailDb");
		Date currentDate = new Date();
		requestdetailDb.setStatus(Constant.SERVERDB_STATUS_DELETE);
		requestdetailDb.setIddelete(iduser);
		requestdetailDb.setDeletedate(currentDate);
		// Save.
		requestdetailDb = requestdetailRepository.save(requestdetailDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return requestdetailDb;
	}

	@Override
	public Requestdetail updateForDeleteWithLock(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		Requestdetail result = null;
		// Lock to update.
		updateLock(id);
		// Update data.
		result = updateForDelete(id, version);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}


	@Override
	public void delete(Requestdetail requestdetail) {
		requestdetailRepository.delete(requestdetail);
	}

	@Override
	public void deleteById(Integer id) {
		requestdetailRepository.delete(id);
	}

	@Override
	public Requestdetail getById(Integer id) {
		return requestdetailRepository.findOne(id);
	}

	@Override
	public List<Requestdetail> listAll() {
		return requestdetailRepository.findAll();
	}

	@Override
	public long countAll() {
		return requestdetailRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return requestdetailRepository.exists(id);
	}
	
	public List<Requestdetail> listWithCritera(SearchCriteria searchCriteria) {
		Specification<Requestdetail> requestdetailSpecification = new RequestdetailSpecification(searchCriteria);
		// Where status != delete.
		Specification<Requestdetail> notDeleteSpec = new RequestdetailSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		requestdetailSpecification = Specifications.where(requestdetailSpecification).and(notDeleteSpec);
		// Execute.
        List<Requestdetail> result = requestdetailRepository.findAll(requestdetailSpecification);
        return result;
	}
	
	public List<Requestdetail> listWithCriteras(List<SearchCriteria> searchCriterias) {
        Specification<Requestdetail> requestdetailSpecification = requestdetailSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Requestdetail> notDeleteSpec = new RequestdetailSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		requestdetailSpecification = Specifications.where(requestdetailSpecification).and(notDeleteSpec);
		// Execute.
        List<Requestdetail> result = requestdetailRepository.findAll(requestdetailSpecification);
        return result;
	}
	
	public Page<Requestdetail> listAllByPage(Pageable pageable) {
		return requestdetailRepository.findAll(pageable);
	}
	
	public Page<Requestdetail> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<Requestdetail> requestdetailSpecification = new RequestdetailSpecification(searchCriteria);
		// Where status != delete.
		Specification<Requestdetail> notDeleteSpec = new RequestdetailSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		requestdetailSpecification = Specifications.where(requestdetailSpecification).and(notDeleteSpec);
		// Execute.
		Page<Requestdetail> result = requestdetailRepository.findAll(requestdetailSpecification, pageable);
        return result;
	}
	
	public Page<Requestdetail> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Requestdetail> requestdetailSpecification = requestdetailSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Requestdetail> notDeleteSpec = new RequestdetailSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		requestdetailSpecification = Specifications.where(requestdetailSpecification).and(notDeleteSpec);
		// Execute.
		Page<Requestdetail> result = requestdetailRepository.findAll(requestdetailSpecification, pageable);
        return result;
	}
	
	public Page<Map<String, Object>> listWithCriteriasByIdrequestAndPage(Integer idrequest, List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Requestdetail> requestSpecification = requestdetailSpecificationsBuilder.build(searchCriterias);
		// Where idrequest.
		Specification<Requestdetail> idrequestSpec = new RequestdetailSpecification(new SearchCriteria("idrequest", "=", idrequest));
		// Where status != delete.
		Specification<Requestdetail> notDeleteSpec = new RequestdetailSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		requestSpecification = Specifications.where(requestSpecification).and(idrequestSpec).and(notDeleteSpec);
		// Execute.
		Page<Requestdetail> requestdetails = requestdetailRepository.findAll(requestSpecification, pageable);
		Page<Map<String, Object>> result = requestdetails.map(this::convertToMap);
        return result;
	}

	private Map<String, Object> convertToMap(final Requestdetail requestdetail) {
	    final Map<String, Object> result = new HashMap<String, Object>();
	    result.put("id", requestdetail.getId());
	    result.put("quantity", requestdetail.getQuantity());
	    result.put("idrequest", requestdetail.getIdrequest());
	    result.put("idmaterial", requestdetail.getIdmaterial());
	    result.put("materialname", requestdetail.getMaterial().getName());
	    result.put("materialthumbnail", requestdetail.getMaterial().getThumbnail());
	    result.put("version", requestdetail.getVersion());
	    return result;
	}

	@Override
	public List<Map<String, Object>> getAllById(Integer id) {
		return requestdetailRepository.getAllById(id);
	}

	@Override
	public void updateQuantity(Integer id, Integer quantity)
			throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		requestdetailRepository.updateQuantityById(id, quantity,iduser);		
	}
	
	@Override
	public Map<String, Object> getByIdForView(Integer id) {
		Requestdetail requestdetail = requestdetailRepository.getByIdForView(id);
		Map<String, Object> result = convertToMapForView(requestdetail);
		return result;
	}
	
	private Map<String, Object> convertToMapForView(Requestdetail requestdetail) {
	    Map<String, Object> result = new HashMap<String, Object>();
	    //result.put("id", requestdetail.getId());
	    result.put("materialname", requestdetail.getMaterial().getName());
	    result.put("softquantity", requestdetail.getSoftquantity());
	    result.put("quantity", requestdetail.getQuantity());
	    result.put("workitem", requestdetail.getWorkitem());
	    result.put("deliverydate", requestdetail.getDeliverydate());
	    result.put("drawingname", requestdetail.getDrawingname());
	    result.put("teamname", requestdetail.getTeamname());
	    result.put("note", requestdetail.getNote());
	    return result;
	}
}



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
import com.redsun.server.wh.model.Request;
import com.redsun.server.wh.model.History;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.RequestRepository;
import com.redsun.server.wh.repository.HistoryRepository;
import com.redsun.server.wh.repository.specification.RequestSpecification;
import com.redsun.server.wh.repository.specification.RequestSpecificationsBuilder;
import com.redsun.server.wh.service.RequestService;
import com.redsun.server.wh.util.SecurityUtil;

@Service("request")
@Transactional
public class RequestServiceImpl implements RequestService {
	
	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private RequestRepository requestRepository;
	
	@Autowired
	private RequestSpecificationsBuilder requestSpecificationsBuilder;

	@Override
	public Request save(Request request) {
		return requestRepository.save(request);
	}

	@Override
	public Request create(Request request) throws JsonParseException, JsonMappingException, IOException {
		
		// Get iduser.				
		Integer iduser = SecurityUtil.getIdUser();
		// Current date;
		Date currentDate = new Date();
		request.setStatus(Constant.SERVERDB_STATUS_NEW);
		if (requestRepository.isExistCode(request.getId(),request.getCode(),request.getScope())) {
			throw new ServerException(Constant.SERVERCODE_EXISTCODE);
		}
		if (request.getStatus() == null)
			request.setStatus(Constant.SERVERDB_STATUS_NEW);
		request.setIdcreate(iduser);
		request.setCreatedate(currentDate);
		request.setIdowner(iduser);
		request.setIdupdate(iduser);
		request.setUpdatedate(currentDate);
		request.setVersion(1);
		return requestRepository.save(request);
	}

	@Override
	public Integer updateLock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Update lock.
		Integer result = requestRepository.updateLock(id, iduser, Constant.SERVERDB_lOCKTIMEOUT);
		if(result == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		return result;		
	}
	
	@Override
	public Integer updateUnlock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Update lock.
		Integer result = requestRepository.updateUnlock(id);
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
		Request request = (Request) params.get("request");
		// Get from DB.
		Request requestDb = requestRepository.findOne(id);
		if(requestDb == null) { // not exist.
			throw new ServerException(Constant.SERVERCODE_NOTEXISTID);
		} else if(requestDb.getIdlock() != iduser) { // locked.
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		} 
		else if (requestDb.getVersion() != version) { // version difference.
			throw new ServerException(Constant.SERVERCODE_VERSIONDIFFERENCE);
		}
		else if (request!=null&&requestRepository.isExistCode(request.getId(),request.getCode(),request.getScope())) {
			throw new ServerException(Constant.SERVERCODE_EXISTCODE);
		}
		else {
			// Keep history data.
			String historyStr = requestDb.toString();
			// Increase version.
			requestDb.setVersion(requestDb.getVersion() + 1);
			// return.
			result.put("requestDb", requestDb);
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
		History history = new History(id, "request", historyStr);
		historyRepository.save(history);
		// return.
		return result;
	}
	
	@Override
	public Request update(Integer id, Request request) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", request.getVersion());
		params.put("request", request);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Request requestDb = (Request) resultPre.get("requestDb");
		// Ignore properties.
		String[] ignoreProperties = new String[] { "status", "idcreate", "idowner", "idupdate", "iddelete", "idlock", "createdate", "updatedate", "deletedate", "lockdate", "version" };
		// Copy data.
		BeanUtils.copyProperties(request, requestDb, ignoreProperties);
		Date currentDate = new Date();
		if (requestDb.getStatus() == Constant.SERVERDB_STATUS_NEW) {
			requestDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		}
		requestDb.setIdupdate(iduser);
		requestDb.setUpdatedate(currentDate);
		requestDb.setIdowner(iduser);
		// Save.
		requestDb = requestRepository.save(requestDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return requestDb;
	}

	@Override
	public Request updateWithLock(Integer id, Request request) throws JsonParseException, JsonMappingException, IOException {
		Request result = null;
		// Lock to update.
		updateLock(id);
		// Update.
		result = update(id, request);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}
	
	@Override
	public Request updateStatusAndOwnerWithLock(Integer id, Request request) throws JsonParseException, JsonMappingException, IOException {
		// Lock to update.
		updateLock(id);
		
		// Update.
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", request.getVersion());
		params.put("request", request);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Request requestDb = (Request) resultPre.get("requestDb");
		// Ignore properties.
		String[] ignoreProperties = new String[] { "status", "idcreate", "idowner", "idupdate", "iddelete", "idlock", "createdate", "updatedate", "deletedate", "lockdate", "version" };
		// Copy data.
		BeanUtils.copyProperties(request, requestDb, ignoreProperties);
		Date currentDate = new Date();
		requestDb.setIdupdate(iduser);
		requestDb.setUpdatedate(currentDate);
		// Save.
		requestDb = requestRepository.save(requestDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		
		// Unlock of update.
		updateUnlock(id);
		return requestDb;
	}
	
	@Override
	public Request updateForDelete(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", version);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Request requestDb = (Request) resultPre.get("requestDb");
		Date currentDate = new Date();
		requestDb.setStatus(Constant.SERVERDB_STATUS_DELETE);
		requestDb.setIddelete(iduser);
		requestDb.setDeletedate(currentDate);
		// Save.
		requestDb = requestRepository.save(requestDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return requestDb;
	}

	@Override
	public Request updateForDeleteWithLock(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		Request result = null;
		// Lock to update.
		updateLock(id);
		// Update data.
		result = updateForDelete(id, version);
		requestRepository.updateForDeleteByIdRequest(iduser, id);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}

	@Override
	public void delete(Request request) {
		requestRepository.delete(request);
	}

	@Override
	public void deleteById(Integer id) {
		requestRepository.delete(id);
	}

	@Override
	public Request getById(Integer id) {
		return requestRepository.findOne(id);
	}

	@Override
	public List<Request> listAll() {
		return requestRepository.findAll();
	}

	@Override
	public long countAll() {
		return requestRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return requestRepository.exists(id);
	}

	
	public List<Request> listWithCritera(SearchCriteria searchCriteria) {
		Specification<Request> requestSpecification = new RequestSpecification(searchCriteria);
		// Where status != delete.
		Specification<Request> notDeleteSpec = new RequestSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		requestSpecification = Specifications.where(requestSpecification).and(notDeleteSpec);
		// Execute.
        List<Request> result = requestRepository.findAll(requestSpecification);
        return result;
	}
	
	public List<Request> listWithCriteras(List<SearchCriteria> searchCriterias) {
        Specification<Request> requestSpecification = requestSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Request> notDeleteSpec = new RequestSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		requestSpecification = Specifications.where(requestSpecification).and(notDeleteSpec);
		// Execute.
        List<Request> result = requestRepository.findAll(requestSpecification);
        return result;
	}
	
	public Page<Request> listAllByPage(Pageable pageable) {
		return requestRepository.findAll(pageable);
	}
	
	public Page<Request> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<Request> requestSpecification = new RequestSpecification(searchCriteria);
		// Where status != delete.
		Specification<Request> notDeleteSpec = new RequestSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		requestSpecification = Specifications.where(requestSpecification).and(notDeleteSpec);
		// Execute.
		Page<Request> result = requestRepository.findAll(requestSpecification, pageable);
        return result;
	}
	
	public Page<Map<String, Object>> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Request> requestSpecification = requestSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		//Specification<Request> notDeleteSpec = new RequestSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		//requestSpecification = Specifications.where(requestSpecification).and(notDeleteSpec);
		// Execute.
		Page<Request> requests = requestRepository.findAll(requestSpecification, pageable);
		Page<Map<String, Object>> result = requests.map(this::convertToMap);
        return result;
	}
	
	private Map<String, Object> convertToMap(final Request request) {
	    final Map<String, Object> result = new HashMap<String, Object>();
	    result.put("id", request.getId());
	    result.put("storename", request.getStore().getName());
	    result.put("writername", request.getWriter().getFirstname());
	    result.put("receivername", request.getReceiver().getFirstname());
	    result.put("responsiblename", request.getResponsible().getFirstname());
	    result.put("code", request.getCode());
	    result.put("name", request.getName());
	    result.put("receiverphonenumber", request.getReceiverphonenumber());
	    result.put("writerphonenumber", request.getWriterphonenumber());
	    result.put("address", request.getAddress());
	    result.put("time", request.getTimes());
	    result.put("requestdate", request.getRequestdate());
	    result.put("receivedate", request.getReceivedate());
	    result.put("note", request.getNote());
	    result.put("status", request.getStatus());
	    result.put("idowner", request.getIdowner());
	    result.put("version", request.getVersion());
	    return result;
	}

	@Override
	public List<Map<String, Object>> listAllForSelect() {
		return requestRepository.listForSelect();
	}
	
	@Override
	public Map<String, Object> getByIdForView(Integer id) {
		Request request = requestRepository.getByIdForView(id);
		Map<String, Object> result = convertToMapForView(request);
		return result;
	}
	
	private Map<String, Object> convertToMapForView(Request request) {
	    Map<String, Object> result = new HashMap<String, Object>();
	    result.put("id", request.getId());
	    result.put("idstore", request.getStore().getName());
	    result.put("idreceiver", request.getReceiver().getDisplayname());
	    result.put("idresponsible", request.getResponsible().getDisplayname());
	    result.put("idwriter", request.getWriter().getDisplayname());
	    result.put("code", request.getCode());
	    result.put("requestdate", request.getRequestdate());
	    result.put("receivedate", request.getReceivedate());
	    result.put("name", request.getName());
	    result.put("receiverphonenumber", request.getReceiverphonenumber());	    
	    result.put("writerphonenumber", request.getWriterphonenumber());
	    result.put("address", request.getAddress());
	    result.put("times", request.getTimes());
	    result.put("note", request.getNote());
	    return result;
	}

}

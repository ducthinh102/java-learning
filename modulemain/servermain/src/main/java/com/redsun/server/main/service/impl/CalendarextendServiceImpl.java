

package com.redsun.server.main.service.impl;

import java.io.IOException;
import java.util.ArrayList;
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
import com.redsun.server.main.common.Constant;
import com.redsun.server.main.controller.common.ServerException;
import com.redsun.server.main.model.Calendarextend;
import com.redsun.server.main.model.History;
import com.redsun.server.main.model.common.SearchCriteria;
import com.redsun.server.main.repository.CalendarextendRepository;
import com.redsun.server.main.repository.HistoryRepository;
import com.redsun.server.main.repository.specification.CalendarextendSpecification;
import com.redsun.server.main.repository.specification.CalendarextendSpecificationsBuilder;
import com.redsun.server.main.service.CalendarextendService;
import com.redsun.server.main.util.SecurityUtil;

@Service("calendarextend")
@Transactional
public class CalendarextendServiceImpl implements CalendarextendService {
	
	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private CalendarextendRepository calendarextendRepository;
	
	@Autowired
	private CalendarextendSpecificationsBuilder calendarextendSpecificationsBuilder;

	@Override
	public Calendarextend save(Calendarextend calendarextend) {
		return calendarextendRepository.save(calendarextend);
	}

	@Override
	public Calendarextend create(Calendarextend calendarextend) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Current date;
		Date currentDate = new Date();
		calendarextend.setStatus(Constant.SERVERDB_STATUS_NEW);
		calendarextend.setIdcreate(iduser);
		calendarextend.setCreatedate(currentDate);
		calendarextend.setIdowner(iduser);
		calendarextend.setIdupdate(iduser);
		calendarextend.setUpdatedate(currentDate);
		calendarextend.setVersion(1);
		return calendarextendRepository.save(calendarextend);
	}

	@Override
	public Integer updateLock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Update lock.
		Integer result = calendarextendRepository.updateLock(id, iduser, Constant.SERVERDB_lOCKTIMEOUT);
		if(result == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		return result;		
	}
	
	@Override
	public Integer updateUnlock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Update lock.
		Integer result = calendarextendRepository.updateUnlock(id);
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
		Calendarextend calendarextend = (Calendarextend) params.get("calendarextend");
		// Get from DB.
		Calendarextend calendarextendDb = calendarextendRepository.findOne(id);
		if(calendarextendDb == null) { // not exist.
			throw new ServerException(Constant.SERVERCODE_NOTEXISTID);
		} else if(calendarextendDb.getIdlock() != iduser) { // locked.
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		} else if (calendarextendDb.getVersion() != version) { // version difference.
			throw new ServerException(Constant.SERVERCODE_VERSIONDIFFERENCE);
		} else {
			// Keep history data.
			String historyStr = calendarextendDb.toString();
			// Increase version.
			calendarextendDb.setVersion(calendarextendDb.getVersion() + 1);
			// return.
			result.put("calendarextendDb", calendarextendDb);
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
		History history = new History(id, "calendarextend", historyStr);
		historyRepository.save(history);
		// return.
		return result;
	}
	
	@Override
	public Calendarextend update(Integer id, Calendarextend calendarextend) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", calendarextend.getVersion());
		params.put("calendarextend", calendarextend);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Calendarextend calendarextendDb = (Calendarextend) resultPre.get("calendarextendDb");
		// Ignore properties.
		String[] ignoreProperties = new String[] { "status", "idcreate", "idowner", "idupdate", "iddelete", "idlock", "createdate", "updatedate", "deletedate", "lockdate", "version" };
		// Copy data.
		BeanUtils.copyProperties(calendarextend, calendarextendDb, ignoreProperties);
		Date currentDate = new Date();
		calendarextendDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		calendarextendDb.setIdupdate(iduser);
		calendarextendDb.setUpdatedate(currentDate);
		calendarextendDb.setIdowner(iduser);
		// Save.
		calendarextendDb = calendarextendRepository.save(calendarextendDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return calendarextendDb;
	}

	@Override
	public Calendarextend updateWithLock(Integer id, Calendarextend calendarextend) throws JsonParseException, JsonMappingException, IOException {
		Calendarextend result = null;
		// Lock to update.
		updateLock(id);
		// Update.
		result = update(id, calendarextend);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}
	
	@Override
	public Calendarextend updateForDelete(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", version);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Calendarextend calendarextendDb = (Calendarextend) resultPre.get("calendarextendDb");
		Date currentDate = new Date();
		calendarextendDb.setStatus(Constant.SERVERDB_STATUS_DELETE);
		calendarextendDb.setIddelete(iduser);
		calendarextendDb.setDeletedate(currentDate);
		// Save.
		calendarextendDb = calendarextendRepository.save(calendarextendDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return calendarextendDb;
	}

	@Override
	public Calendarextend updateForDeleteWithLock(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		Calendarextend result = null;
		// Lock to update.
		updateLock(id);
		// Update data.
		result = updateForDelete(id, version);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}


	@Override
	public void delete(Calendarextend calendarextend) {
		calendarextendRepository.delete(calendarextend);
	}

	@Override
	public void deleteById(Integer id) {
		calendarextendRepository.delete(id);
	}

	@Override
	public Calendarextend getById(Integer id) {
		return calendarextendRepository.findOne(id);
	}

	@Override
	public List<Calendarextend> listAll() {
		return calendarextendRepository.findAll();
	}

	@Override
	public long countAll() {
		return calendarextendRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return calendarextendRepository.exists(id);
	}
	
	public List<Calendarextend> listWithCritera(SearchCriteria searchCriteria) {
		Specification<Calendarextend> calendarextendSpecification = new CalendarextendSpecification(searchCriteria);
		// Where status != delete.
		Specification<Calendarextend> notDeleteSpec = new CalendarextendSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		calendarextendSpecification = Specifications.where(calendarextendSpecification).and(notDeleteSpec);
		// Execute.
        List<Calendarextend> result = calendarextendRepository.findAll(calendarextendSpecification);
        return result;
	}
	
	public List<Calendarextend> listWithCriteras(List<SearchCriteria> searchCriterias) {
        Specification<Calendarextend> calendarextendSpecification = calendarextendSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Calendarextend> notDeleteSpec = new CalendarextendSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		calendarextendSpecification = Specifications.where(calendarextendSpecification).and(notDeleteSpec);
		// Execute.
        List<Calendarextend> result = calendarextendRepository.findAll(calendarextendSpecification);
        return result;
	}
	
	public Page<Calendarextend> listAllByPage(Pageable pageable) {
		return calendarextendRepository.findAll(pageable);
	}
	
	public Page<Calendarextend> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<Calendarextend> calendarextendSpecification = new CalendarextendSpecification(searchCriteria);
		// Where status != delete.
		Specification<Calendarextend> notDeleteSpec = new CalendarextendSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		calendarextendSpecification = Specifications.where(calendarextendSpecification).and(notDeleteSpec);
		// Execute.
		Page<Calendarextend> result = calendarextendRepository.findAll(calendarextendSpecification, pageable);
        return result;
	}
	
	public Page<Calendarextend> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Calendarextend> calendarextendSpecification = calendarextendSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Calendarextend> notDeleteSpec = new CalendarextendSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		calendarextendSpecification = Specifications.where(calendarextendSpecification).and(notDeleteSpec);
		// Execute.
		Page<Calendarextend> result = calendarextendRepository.findAll(calendarextendSpecification, pageable);
        return result;
	}
	
	public Page<Map<String, Object>> listWithCriteriasByIdcalendarAndPage(Integer idcalendar, List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Calendarextend> calendarSpecification = calendarextendSpecificationsBuilder.build(searchCriterias);
		// Where idcalendar.
		Specification<Calendarextend> idcalendarSpec = new CalendarextendSpecification(new SearchCriteria("idcalendar", "=", idcalendar));
		// Where status != delete.
		Specification<Calendarextend> notDeleteSpec = new CalendarextendSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		calendarSpecification = Specifications.where(calendarSpecification).and(idcalendarSpec).and(notDeleteSpec);
		// Execute.
		Page<Calendarextend> calendarextends = calendarextendRepository.findAll(calendarSpecification, pageable);
		Page<Map<String, Object>> result = calendarextends.map(this::convertToMap);
        return result;
	}
	
	public List<Map<String, Object>> listAllByIdcalendar(Integer idcalendar) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		// Where idcalendar.
		Specification<Calendarextend> idcalendarSpec = new CalendarextendSpecification(new SearchCriteria("idcalendar", "=", idcalendar));
		// Where status != delete.
		Specification<Calendarextend> notDeleteSpec = new CalendarextendSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		idcalendarSpec = Specifications.where(idcalendarSpec).and(notDeleteSpec);
		// Execute.
		List<Calendarextend> calendarextends = calendarextendRepository.findAll(idcalendarSpec);
		for (Calendarextend calendarextend : calendarextends) {
			result.add(convertToMap(calendarextend));
		}
        return result;
	}

	private Map<String, Object> convertToMap(final Calendarextend calendarextend) {
	    final Map<String, Object> result = new HashMap<String, Object>();
	    result.put("id", calendarextend.getId());
	    result.put("idcalendar", calendarextend.getIdcalendar());
	    result.put("code", calendarextend.getCode());
	    result.put("name", calendarextend.getName());
	    result.put("iswork", calendarextend.getIswork());
	    result.put("calendardate", calendarextend.getCalendardate());
	    result.put("version", calendarextend.getVersion());
	    return result;
	}

}



package com.redsun.server.main.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redsun.server.main.common.Constant;
import com.redsun.server.main.controller.common.ServerException;
import com.redsun.server.main.model.Calendar;
import com.redsun.server.main.model.History;
import com.redsun.server.main.model.User;
import com.redsun.server.main.model.common.SearchCriteria;
import com.redsun.server.main.repository.CalendarRepository;
import com.redsun.server.main.repository.HistoryRepository;
import com.redsun.server.main.repository.UserRepository;
import com.redsun.server.main.repository.specification.CalendarSpecification;
import com.redsun.server.main.repository.specification.CalendarSpecificationsBuilder;
import com.redsun.server.main.service.CalendarService;
import com.redsun.server.main.service.CalendarextendService;
import com.redsun.server.main.util.CommonUtil;
import com.redsun.server.main.util.SecurityUtil;

@Service("calendar")
@Transactional
public class CalendarServiceImpl implements CalendarService {
	
	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private CalendarRepository calendarRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CalendarextendService calendarextendService;
	
	@Autowired
	private CalendarSpecificationsBuilder calendarSpecificationsBuilder;
	
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public Calendar save(Calendar calendar) {
		return calendarRepository.save(calendar);
	}

	@Override
	public Calendar create(Calendar calendar) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Current date;
		Date currentDate = new Date();
		calendar.setStatus(Constant.SERVERDB_STATUS_NEW);
		calendar.setIdcreate(iduser);
		calendar.setCreatedate(currentDate);
		calendar.setIdowner(iduser);
		calendar.setIdupdate(iduser);
		calendar.setUpdatedate(currentDate);
		calendar.setVersion(1);
		return calendarRepository.save(calendar);
	}

	@Override
	public Integer updateLock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Update lock.
		Integer result = calendarRepository.updateLock(id, iduser, Constant.SERVERDB_lOCKTIMEOUT);
		if(result == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		return result;		
	}
	
	@Override
	public Integer updateUnlock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Update lock.
		Integer result = calendarRepository.updateUnlock(id);
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
		Calendar calendar = (Calendar) params.get("calendar");
		// Get from DB.
		Calendar calendarDb = calendarRepository.findOne(id);
		if(calendarDb == null) { // not exist.
			throw new ServerException(Constant.SERVERCODE_NOTEXISTID);
		} else if(calendarDb.getIdlock() != iduser) { // locked.
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		} else if (calendarDb.getVersion() != version) { // version difference.
			throw new ServerException(Constant.SERVERCODE_VERSIONDIFFERENCE);
		} else {
			// Keep history data.
			String historyStr = calendarDb.toString();
			// Increase version.
			calendarDb.setVersion(calendarDb.getVersion() + 1);
			// return.
			result.put("calendarDb", calendarDb);
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
		History history = new History(id, "calendar", historyStr);
		historyRepository.save(history);
		// return.
		return result;
	}
	
	@Override
	public Calendar update(Integer id, Calendar calendar) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", calendar.getVersion());
		params.put("calendar", calendar);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Calendar calendarDb = (Calendar) resultPre.get("calendarDb");
		// Ignore properties.
		String[] ignoreProperties = new String[] { "status", "idcreate", "idowner", "idupdate", "iddelete", "idlock", "createdate", "updatedate", "deletedate", "lockdate", "version" };
		// Copy data.
		BeanUtils.copyProperties(calendar, calendarDb, ignoreProperties);
		Date currentDate = new Date();
		calendarDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		calendarDb.setIdupdate(iduser);
		calendarDb.setUpdatedate(currentDate);
		calendarDb.setIdowner(iduser);
		// Save.
		calendarDb = calendarRepository.save(calendarDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return calendarDb;
	}

	@Override
	public Calendar updateWithLock(Integer id, Calendar calendar) throws JsonParseException, JsonMappingException, IOException {
		Calendar result = null;
		// Lock to update.
		updateLock(id);
		// Update.
		result = update(id, calendar);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}
	
	@Override
	public Calendar updateForDelete(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", version);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Calendar calendarDb = (Calendar) resultPre.get("calendarDb");
		Date currentDate = new Date();
		calendarDb.setStatus(Constant.SERVERDB_STATUS_DELETE);
		calendarDb.setIddelete(iduser);
		calendarDb.setDeletedate(currentDate);
		// Save.
		calendarDb = calendarRepository.save(calendarDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return calendarDb;
	}

	@Override
	public Calendar updateForDeleteWithLock(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		Calendar result = null;
		// Lock to update.
		updateLock(id);
		// Update data.
		result = updateForDelete(id, version);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}


	@Override
	public void delete(Calendar calendar) {
		calendarRepository.delete(calendar);
	}

	@Override
	public void deleteById(Integer id) {
		calendarRepository.delete(id);
	}

	@Override
	public Calendar getById(Integer id) {
		return calendarRepository.findOne(id);
	}

	@Override
	public List<Map<String, Object>> listForSelect() {
		return calendarRepository.listForSelect(Constant.SERVERDB_STATUS_DELETE);
	}

	@Override
	public Map<String, Object> getWithCalendarextendById(Integer id) {
		Map<String, Object> result = new HashMap<String, Object>();
		Calendar calendar = calendarRepository.findOne(id);
		result.put("id", calendar.getId());
		result.put("code", calendar.getCode());
		result.put("name", calendar.getName());
		result.put("content", calendar.getContent());
		// calendarextends.
		List<Map<String, Object>> calendarextends = calendarextendService.listAllByIdcalendar(id);
		result.put("calendarextends", calendarextends);
		return result;
	}

	@Override
	public Map<String, Object> getWithCalendarextend() throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> result = new HashMap<String, Object>();
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Get idcalendar of current user.
		User user = userRepository.findOne(iduser);
		Integer idcalendar = user.getIdcalendar();
		Map<String, Object> calendar = getWithCalendarextendById(idcalendar);
		Map<String, Object> content = objectMapper.readValue(calendar.get("content").toString(), new TypeReference<Map<String, Object>>(){});
		// dayofweek.
		result.put("dayofweek", content.get("dayofweek"));
		// calendarextends.
		List<Map<String, Object>> calendarextends = (List<Map<String, Object>>) calendar.get("calendarextends");
		List<Date> workdays = new ArrayList<Date>();
		List<Date> offdays = new ArrayList<Date>();
		for (Map<String, Object> calendarextend : calendarextends) {
			boolean iswork = (boolean) calendarextend.get("iswork");
			Date calendardate = (Date) calendarextend.get("calendardate");
			if(iswork){
				workdays.add(calendardate);
			} else {
				offdays.add(calendardate);
			}
		}
		result.put("workdays", workdays);
		result.put("offdays", offdays);
		
		// TODO.
		Date startDate = new Date();
		Date endDate = DateUtils.addDays(startDate, 5);
		Integer duration = CommonUtil.getDurationWithCalendar(startDate, endDate, result);
		Date startDate1 = CommonUtil.getStartDateWithCalendar(endDate, duration, result);
		Date endDate1 = CommonUtil.getEndDateWithCalendar(startDate1, duration, result);
		// return.
		return result;
	}

	@Override
	public List<Calendar> listAll() {
		return calendarRepository.findAll();
	}

	@Override
	public long countAll() {
		return calendarRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return calendarRepository.exists(id);
	}
	
	public List<Calendar> listWithCritera(SearchCriteria searchCriteria) {
		Specification<Calendar> calendarSpecification = new CalendarSpecification(searchCriteria);
		// Where status != delete.
		Specification<Calendar> notDeleteSpec = new CalendarSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		calendarSpecification = Specifications.where(calendarSpecification).and(notDeleteSpec);
		// Execute.
        List<Calendar> result = calendarRepository.findAll(calendarSpecification);
        return result;
	}
	
	public List<Calendar> listWithCriteras(List<SearchCriteria> searchCriterias) {
        Specification<Calendar> calendarSpecification = calendarSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Calendar> notDeleteSpec = new CalendarSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		calendarSpecification = Specifications.where(calendarSpecification).and(notDeleteSpec);
		// Execute.
        List<Calendar> result = calendarRepository.findAll(calendarSpecification);
        return result;
	}
	
	public Page<Calendar> listAllByPage(Pageable pageable) {
		return calendarRepository.findAll(pageable);
	}
	
	public Page<Calendar> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<Calendar> calendarSpecification = new CalendarSpecification(searchCriteria);
		// Where status != delete.
		Specification<Calendar> notDeleteSpec = new CalendarSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		calendarSpecification = Specifications.where(calendarSpecification).and(notDeleteSpec);
		// Execute.
		Page<Calendar> result = calendarRepository.findAll(calendarSpecification, pageable);
        return result;
	}
	
	public Page<Calendar> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Calendar> calendarSpecification = calendarSpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Calendar> notDeleteSpec = new CalendarSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		calendarSpecification = Specifications.where(calendarSpecification).and(notDeleteSpec);
		// Execute.
		Page<Calendar> result = calendarRepository.findAll(calendarSpecification, pageable);
        return result;
	}

}

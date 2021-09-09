

package com.redsun.server.main.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
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
import com.redsun.server.main.model.History;
import com.redsun.server.main.model.Notify;
import com.redsun.server.main.model.common.SearchCriteria;
import com.redsun.server.main.model.event.NotifyEvent;
import com.redsun.server.main.repository.HistoryRepository;
import com.redsun.server.main.repository.NotifyRepository;
import com.redsun.server.main.repository.specification.NotifySpecification;
import com.redsun.server.main.repository.specification.NotifySpecificationsBuilder;
import com.redsun.server.main.service.NotifyService;
import com.redsun.server.main.util.SecurityUtil;

@Service("notify")
@Transactional
public class NotifyServiceImpl implements NotifyService {
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private NotifyRepository notifyRepository;
	
	@Autowired
	private NotifySpecificationsBuilder notifySpecificationsBuilder;

	@Override
	public Notify save(Notify notify) {
		return notifyRepository.save(notify);
	}

	@Override
	public Notify create(Notify notify) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Current date;
		Date currentDate = new Date();
		notify.setStatus(Constant.SERVERDB_STATUS_NEW);
		notify.setIdcreate(iduser);
		notify.setCreatedate(currentDate);
		notify.setIdowner(iduser);
		notify.setIdupdate(iduser);
		notify.setUpdatedate(currentDate);
		notify.setIsactive(true);
		notify.setVersion(1);
		Notify result = notifyRepository.save(notify);
		// Public event.
		publisher.publishEvent(new NotifyEvent(result));
		// return.
		return result;
	}

	@Override
	public Integer updateLock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Update lock.
		Integer result = notifyRepository.updateLock(id, iduser, Constant.SERVERDB_lOCKTIMEOUT);
		if(result == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		return result;		
	}
	
	@Override
	public Integer updateUnlock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Update lock.
		Integer result = notifyRepository.updateUnlock(id);
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
		Notify notify = (Notify) params.get("notify");
		// Get from DB.
		Notify notifyDb = notifyRepository.findOne(id);
		if(notifyDb == null) { // not exist.
			throw new ServerException(Constant.SERVERCODE_NOTEXISTID);
		} else if(notifyDb.getIdlock() != iduser) { // locked.
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		} else if (notifyDb.getVersion() != version) { // version difference.
			throw new ServerException(Constant.SERVERCODE_VERSIONDIFFERENCE);
		} else {
			// Keep history data.
			String historyStr = notifyDb.toString();
			// Increase version.
			notifyDb.setVersion(notifyDb.getVersion() + 1);
			// return.
			result.put("notifyDb", notifyDb);
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
		History history = new History(id, "notify", historyStr);
		historyRepository.save(history);
		// return.
		return result;
	}
	
	@Override
	public Notify update(Integer id, Notify notify) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", notify.getVersion());
		params.put("notify", notify);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Notify notifyDb = (Notify) resultPre.get("notifyDb");
		// Ignore properties.
		String[] ignoreProperties = new String[] { "status", "idcreate", "idowner", "idupdate", "iddelete", "idlock", "createdate", "updatedate", "deletedate", "lockdate", "version" };
		// Copy data.
		BeanUtils.copyProperties(notify, notifyDb, ignoreProperties);
		Date currentDate = new Date();
		notifyDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		notifyDb.setIdupdate(iduser);
		notifyDb.setUpdatedate(currentDate);
		notifyDb.setIdowner(iduser);
		// Save.
		notifyDb = notifyRepository.save(notifyDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// Public event.
		publisher.publishEvent(new NotifyEvent(notifyDb));
		// return.
		return notifyDb;
	}

	@Override
	public Notify updateWithLock(Integer id, Notify notify) throws JsonParseException, JsonMappingException, IOException {
		Notify result = null;
		// Lock to update.
		updateLock(id);
		// Update.
		result = update(id, notify);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}
	
	@Override
	public Notify updateForDelete(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", version);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Notify notifyDb = (Notify) resultPre.get("notifyDb");
		Date currentDate = new Date();
		notifyDb.setStatus(Constant.SERVERDB_STATUS_DELETE);
		notifyDb.setIddelete(iduser);
		notifyDb.setDeletedate(currentDate);
		// Save.
		notifyDb = notifyRepository.save(notifyDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		// Public event.
		publisher.publishEvent(new NotifyEvent(notifyDb));
		updatePost(params);
		// return.
		return notifyDb;
	}

	@Override
	public Notify updateForDeleteWithLock(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		Notify result = null;
		// Lock to update.
		updateLock(id);
		// Update data.
		result = updateForDelete(id, version);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}


	@Override
	public void delete(Notify notify) {
		notifyRepository.delete(notify);
	}

	@Override
	public void deleteById(Integer id) {
		notifyRepository.delete(id);
	}

	@Override
	public Notify getById(Integer id) {
		return notifyRepository.findOne(id);
	}

	@Override
	public List<Notify> listAll() {
		return notifyRepository.findAll();
	}

	@Override
	public long countAll() {
		return notifyRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return notifyRepository.exists(id);
	}
	
	public List<Notify> listWithCritera(SearchCriteria searchCriteria) {
		Specification<Notify> notifySpecification = new NotifySpecification(searchCriteria);
		// Where status != delete.
		Specification<Notify> notDeleteSpec = new NotifySpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		notifySpecification = Specifications.where(notifySpecification).and(notDeleteSpec);
		// Execute.
        List<Notify> result = notifyRepository.findAll(notifySpecification);
        return result;
	}
	
	public List<Notify> listWithCriteras(List<SearchCriteria> searchCriterias) {
        Specification<Notify> notifySpecification = notifySpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Notify> notDeleteSpec = new NotifySpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		notifySpecification = Specifications.where(notifySpecification).and(notDeleteSpec);
		// Execute.
        List<Notify> result = notifyRepository.findAll(notifySpecification);
        return result;
	}
	
	public Page<Notify> listAllByPage(Pageable pageable) {
		return notifyRepository.findAll(pageable);
	}
	
	public Page<Notify> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<Notify> notifySpecification = new NotifySpecification(searchCriteria);
		// Where status != delete.
		Specification<Notify> notDeleteSpec = new NotifySpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		notifySpecification = Specifications.where(notifySpecification).and(notDeleteSpec);
		// Execute.
		Page<Notify> result = notifyRepository.findAll(notifySpecification, pageable);
        return result;
	}
	
	public Page<Notify> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Notify> notifySpecification = notifySpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Notify> notDeleteSpec = new NotifySpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		notifySpecification = Specifications.where(notifySpecification).and(notDeleteSpec);
		// Execute.
		Page<Notify> result = notifyRepository.findAll(notifySpecification, pageable);
        return result;
	}
	
	public Page<Notify> listWithCriterasByIdreceiverAndPage(Integer idreceiver, List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Notify> notifySpecification = notifySpecificationsBuilder.build(searchCriterias);
		// Where status != delete.
		Specification<Notify> notDeleteSpec = new NotifySpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		notifySpecification = Specifications.where(notifySpecification).and(notDeleteSpec);
		// Where isactive == true.
		Specification<Notify> isactive = new NotifySpecification(new SearchCriteria("isactive", "=", true));
		notifySpecification = Specifications.where(notifySpecification).and(isactive);
		// Where idreceiver.
		Specification<Notify> idreceiverparam = new NotifySpecification(new SearchCriteria("idreceiver", "=", idreceiver));
		notifySpecification = Specifications.where(notifySpecification).and(idreceiverparam);
		// Execute.
		Page<Notify> result = notifyRepository.findAll(notifySpecification, pageable);
        return result;
	}

}

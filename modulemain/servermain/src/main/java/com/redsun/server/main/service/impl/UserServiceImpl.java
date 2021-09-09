

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
import com.redsun.server.main.model.User;
import com.redsun.server.main.model.History;
import com.redsun.server.main.model.common.SearchCriteria;
import com.redsun.server.main.repository.UserRepository;
import com.redsun.server.main.repository.HistoryRepository;
import com.redsun.server.main.repository.specification.UserSpecification;
import com.redsun.server.main.repository.specification.UserSpecificationsBuilder;
import com.redsun.server.main.service.UserService;
import com.redsun.server.main.util.SecurityUtil;
import com.redsun.server.main.model.event.UserEvent;
import com.redsun.server.main.model.event.listener.UserEventListener;
import com.redsun.server.main.util.CommonUtil;

@Service("user")
@Transactional
public class UserServiceImpl implements UserService {
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private UserEventListener userEventListener;

	@Autowired
	private HistoryRepository historyRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserSpecificationsBuilder userSpecificationsBuilder;

	@Override
	public User save(User user) {
		return userRepository.save(user);
	}

	@Override
	public User create(User user) throws JsonParseException, JsonMappingException, IOException {
		if (userRepository.isExistUsername(user.getId(), user.getUsername())) { // exist scope.
			throw new ServerException(Constant.SERVERCODE_EXISTSCOPE);
		} else {
			// Get iduser.
			Integer iduser = SecurityUtil.getIdUser();
			// Current date;
			Date currentDate = new Date();
			String password = user.getPassword();
			password = CommonUtil.encryptPassword(password);
			user.setPassword(password);
			user.setStatus(Constant.SERVERDB_STATUS_NEW);
			user.setIdcreate(iduser);
			user.setCreatedate(currentDate);
			user.setIdowner(iduser);
			user.setIdupdate(iduser);
			user.setUpdatedate(currentDate);
			user.setVersion(1);
			return userRepository.save(user);
		}
	}

	@Override
	public User createWithEvent(User user, Map<String, Object> syncObject) throws JsonParseException, JsonMappingException, IOException {
		if (userRepository.isExistUsername(user.getId(), user.getUsername())) { // exist scope.
			throw new ServerException(Constant.SERVERCODE_EXISTSCOPE);
		} else {
			// Get iduser.
			Integer iduser = (Integer) syncObject.get("iduser");
			// Current date;
			Date currentDate = new Date();
			String password = user.getPassword();
			password = CommonUtil.encryptPassword(password);
			user.setPassword(password);
			user.setStatus(Constant.SERVERDB_STATUS_NEW);
			user.setIdcreate(iduser);
			user.setCreatedate(currentDate);
			user.setIdowner(iduser);
			user.setIdupdate(iduser);
			user.setUpdatedate(currentDate);
			user.setVersion(1);
			User result = userRepository.save(user);
			// Notify syncObject.
			synchronized(syncObject) {
				syncObject.replace("id", result.getId());
				syncObject.notify();
			}
			// Public event.
			publisher.publishEvent(new UserEvent(result));
			return result;
		}
	}

	@Override
	public Integer updateLock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Update lock.
		Integer result = userRepository.updateLock(id, iduser, Constant.SERVERDB_lOCKTIMEOUT);
		if(result == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		return result;		
	}
	
	@Override
	public Integer updateUnlock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Update unlock.
		Integer result = userRepository.updateUnlock(id);
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
		User user = (User) params.get("user");
		// Get from DB.
		User userDb = userRepository.findOne(id);
		if(userDb == null) { // not exist.
			throw new ServerException(Constant.SERVERCODE_NOTEXISTID);
		} else if(userDb.getIdlock() != iduser) { // locked.
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		} else if (userDb.getVersion() != version) { // version difference.
			throw new ServerException(Constant.SERVERCODE_VERSIONDIFFERENCE);
		} else if (user != null && userRepository.isExistUsername(id, user.getUsername())) { // exist scope.
			throw new ServerException(Constant.SERVERCODE_EXISTUSERNAME);
		} else {
			// Keep history data.
			String historyStr = userDb.toString();
			// Increase version.
			userDb.setVersion(userDb.getVersion() + 1);
			// return.
			result.put("userDb", userDb);
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
		History history = new History(id, "user", historyStr);
		historyRepository.save(history);
		// return.
		return result;
	}

	@Override
	public User updatePassword(Integer id, String password) throws JsonParseException, JsonMappingException, IOException {
		User user = userRepository.findOne(id);
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", user.getVersion());
		params.put("user", user);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		User userDb = (User) resultPre.get("userDb");
		Date currentDate = new Date();
		password = CommonUtil.encryptPassword(password);
		userDb.setPassword(password);
		userDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		userDb.setIdupdate(iduser);
		userDb.setUpdatedate(currentDate);
		userDb.setIdowner(iduser);
		// Save.
		userDb = userRepository.save(userDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return userDb;
	}

	@Override
	public User updatePasswordWithLock(Integer id, String password) throws JsonParseException, JsonMappingException, IOException {
		User result = null;
		// Lock to update.
		updateLock(id);
		// Update.
		result = updatePassword(id, password);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}

	@Override
	public User update(Integer id, User user) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", user.getVersion());
		params.put("user", user);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		User userDb = (User) resultPre.get("userDb");
		// Ignore properties.
		String[] ignoreProperties = new String[] { "id", "password", "status", "idcreate", "idowner", "idupdate", "iddelete", "idlock", "createdate", "updatedate", "deletedate", "lockdate", "version" };
		// Copy data.
		BeanUtils.copyProperties(user, userDb, ignoreProperties);
		Date currentDate = new Date();
		userDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		userDb.setIdupdate(iduser);
		userDb.setUpdatedate(currentDate);
		userDb.setIdowner(iduser);
		// Save.
		userDb = userRepository.save(userDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return userDb;
	}

	@Override
	public User updateWithLock(Integer id, User user) throws JsonParseException, JsonMappingException, IOException {
		User result = null;
		// Lock to update.
		updateLock(id);
		// Update.
		result = update(id, user);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}

	@Override
	public User updateWithLockAndEvent(Integer id, User user, Map<String, Object> syncObject) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = (Integer) syncObject.get("iduser");
		// Update lock.
		Integer result1 = userRepository.updateLock(id, iduser, Constant.SERVERDB_lOCKTIMEOUT);
		if(result1 == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", user.getVersion());
		params.put("user", user);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		User userDb = (User) resultPre.get("userDb");
		// Ignore properties.
		String[] ignoreProperties = new String[] { "id", "password", "status", "idcreate", "idowner", "idupdate", "iddelete", "idlock", "createdate", "updatedate", "deletedate", "lockdate", "version" };
		// Copy data.
		BeanUtils.copyProperties(user, userDb, ignoreProperties);
		Date currentDate = new Date();
		userDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		userDb.setIdupdate(iduser);
		userDb.setUpdatedate(currentDate);
		userDb.setIdowner(iduser);
		// Save.
		User result = userRepository.save(userDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// Update unlock.
		result1 = userRepository.updateUnlock(id);
		if(result1 == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		// Notify syncObject.
		synchronized(syncObject) {
			syncObject.put("id", result.getId());
			syncObject.put("version", result.getVersion());
			syncObject.notify();
		}
		// Public event.
		publisher.publishEvent(new UserEvent(result));
		return result;
	}

	@Override
	public User updateForDelete(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", version);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		User userDb = (User) resultPre.get("userDb");
		Date currentDate = new Date();
		userDb.setStatus(Constant.SERVERDB_STATUS_DELETE);
		userDb.setIddelete(iduser);
		userDb.setDeletedate(currentDate);
		// Save.
		userDb = userRepository.save(userDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return userDb;
	}

	@Override
	public User updateForDeleteWithLock(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		User result = null;
		// Lock to update.
		updateLock(id);
		// Update.
		result = updateForDelete(id, version);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}
	
	@Override
	public User updateForDeleteWithLockAndEvent(Integer id, Integer version, Map<String, Object> syncObject) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = (Integer) syncObject.get("iduser");
		// Update lock.
		Integer result1 = userRepository.updateLock(id, iduser, Constant.SERVERDB_lOCKTIMEOUT);
		if(result1 == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", version);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		User userDb = (User) resultPre.get("userDb");
		Date currentDate = new Date();
		userDb.setStatus(Constant.SERVERDB_STATUS_DELETE);
		userDb.setIddelete(iduser);
		userDb.setDeletedate(currentDate);
		// Save.
		User result = userRepository.save(userDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// Update unlock.
		result1 = userRepository.updateUnlock(id);
		if(result1 == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		synchronized(syncObject) {
			syncObject.put("id", result.getId());
			syncObject.put("version", result.getVersion());
			syncObject.notify();
		}
		// Public event.
		publisher.publishEvent(new UserEvent(result));
		return result;
	}
	
	@Override
	public void delete(User user) {
		userRepository.delete(user);
	}

	@Override
	public void deleteWithEvent(User user, Map<String, Object> syncObject) {
		userRepository.delete(user);
		// Notify syncObject.
		synchronized(syncObject) {
			syncObject.notify();
		}
		// Public event.
		publisher.publishEvent(new UserEvent(user));
	}

	@Override
	public void deleteById(Integer id) {
		userRepository.delete(id);
	}

	@Override
	public void deleteWithEventById(Integer id, Map<String, Object> syncObject) {
		userRepository.delete(id);
		// Notify syncObject.
		synchronized(syncObject) {
			syncObject.put("id", id);
			syncObject.notify();
		}
		// Public event.
		User user = new User();
		user.setId(id);
		publisher.publishEvent(new UserEvent(user));
	}

	@Override
	public Boolean confirmStatus(Integer id, int status) {
		return userEventListener.confirmStatus(id, status);
	}

	@Override
	public User getById(Integer id) {
		return userRepository.findOne(id);
	}

	@Override
	public User getByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public List<User> listAll() {
		return userRepository.findAll();
	}

	@Override
	public long countAll() {
		return userRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return userRepository.exists(id);
	}
	
	public List<User> listWithCritera(SearchCriteria searchCriteria) {
		Specification<User> userSpecification = new UserSpecification(searchCriteria);
        List<User> result = userRepository.findAll(userSpecification);
        return result;
	}
	
	public List<User> listWithCriteras(List<SearchCriteria> searchCriterias) {
        Specification<User> userSpecification = userSpecificationsBuilder.build(searchCriterias);
        List<User> result = userRepository.findAll(userSpecification);
        return result;
	}
	
	public Page<User> listAllByPage(Pageable pageable) {
		return userRepository.findAll(pageable);
	}
	
	public Page<User> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<User> userSpecification = new UserSpecification(searchCriteria);
		Page<User> result = userRepository.findAll(userSpecification, pageable);
        return result;
	}
	
	public Page<User> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<User> userSpecification = userSpecificationsBuilder.build(searchCriterias);
		Page<User> result = userRepository.findAll(userSpecification, pageable);
        return result;
	}
	
	public List<Map<String, Object>> listAllForSelect() {
		List<Map<String, Object>> result = userRepository.listAllForSelect();
		// return.
		return result;
	}
	
	public List<Map<String, Object>> listForSelectByIds(List<Integer> ids) {
		List<Map<String, Object>> result = userRepository.listForSelectByIds(ids);
		return result;
	}

}

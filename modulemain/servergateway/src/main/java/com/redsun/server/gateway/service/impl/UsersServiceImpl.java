

package com.redsun.server.gateway.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.redsun.server.gateway.model.UserRoles;
import com.redsun.server.gateway.model.Users;
import com.redsun.server.gateway.model.common.SearchCriteria;
import com.redsun.server.gateway.model.event.UserEvent;
import com.redsun.server.gateway.model.event.listener.UserEventListener;
import com.redsun.server.gateway.repository.UserRolesRepository;
import com.redsun.server.gateway.repository.UsersRepository;
import com.redsun.server.gateway.repository.specification.UsersSpecificationsBuilder;
import com.redsun.server.gateway.service.UsersService;
import com.redsun.server.gateway.util.CommonUtil;
@Service("users")
@Transactional
public class UsersServiceImpl implements UsersService {
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@Autowired
	private UserEventListener userEventListener;
	
	@Autowired
	private UsersRepository usersRepository;
	
	@Autowired
	private UserRolesRepository userRolesRepository;
	
	@Autowired
	private UsersSpecificationsBuilder usersSpecificationsBuilder;

	@Override
	public Users save(Users users) {
		Users result = null;
		if(users.getId() > -1){
			this.update(users.getUsername(), users);
		} else {
			this.create(users);
		}
		
		return result;
	}

	@Override
	public Users create(Users users) {
		// Encode password.
		String password = users.getPassword();
		password = CommonUtil.encryptPassword(password);
		users.setPassword(password);
		users.setVersion(1);
		Users result = usersRepository.save(users);
		
		// Create user role with 'USER'.
		UserRoles userRoles = new UserRoles();
		userRoles.setUsername(users.getUsername());
		userRoles.setRole("USER");
		userRolesRepository.save(userRoles);
		
		return result;
	}

	@Override
	public Users createWithEvent(Users users, Map<String, Object> syncObject) {
		Users result = this.create(users);
		users.setId(result.getId());
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
	public Users update(String username, Users users) {
		/*
		// Encode password.
		String password = users.getPassword();
		password = CommonUtil.encryptPassword(password);
		users.setPassword(password);
		*/		
		Users userDb = usersRepository.findByUsername(username);
		// Ignore properties.
		String[] ignoreProperties = new String[] { "id", "username", "password", "version" };
		// Copy data.
		BeanUtils.copyProperties(users, userDb, ignoreProperties);
		userDb.setVersion(userDb.getVersion() + 1);
		Users result = usersRepository.save(userDb);
		return result;
	}

	@Override
	public Users updatePassword(String username, String password) {
		Users userDb = usersRepository.findByUsername(username);
		password = CommonUtil.encryptPassword(password);
		userDb.setPassword(password);
		//userDb.setVersion(userDb.getVersion() + 1);
		Users result = usersRepository.save(userDb);
		return result;
	}

	@Override
	public Users updateWithEvent(String username, Users users, Map<String, Object> syncObject) {
		Users userDb = usersRepository.findByUsername(username);
		// Ignore properties.
		String[] ignoreProperties = new String[] { "id", "username", "password", "version" };
		// Copy data.
		BeanUtils.copyProperties(users, userDb, ignoreProperties);
		userDb.setVersion(userDb.getVersion() + 1);
		// Save.
		Users result = usersRepository.save(userDb);
		// Notify syncObject.
		synchronized(syncObject) {
			syncObject.put("id", userDb.getId());
			syncObject.put("version", result.getVersion());
			syncObject.notify();
		}
		// Public event.
		publisher.publishEvent(new UserEvent(result));
		return result;
	}

	@Override
	public Users updatePasswordWithEvent(String username, String password, Map<String, Object> syncObject) {
		Users userDb = usersRepository.findByUsername(username);
		password = CommonUtil.encryptPassword(password);
		userDb.setPassword(password);
		userDb.setVersion(userDb.getVersion() + 1);
		// Save.
		Users result = usersRepository.save(userDb);
		// Notify syncObject.
		synchronized(syncObject) {
			syncObject.put("id", userDb.getId());
			syncObject.put("version", result.getVersion());
			syncObject.notify();
		}
		// Public event.
		publisher.publishEvent(new UserEvent(result));
		return result;
	}

	@Override
	public Users updateForDelete(String username) {
		Users userDb = usersRepository.findByUsername(username);
		userDb.setEnabled(false);
		userDb.setVersion(userDb.getVersion() + 1);
		Users result = usersRepository.save(userDb);
		return result;
	}

	@Override
	public void delete(Users users) {
		usersRepository.delete(users);
	}

	@Override
	public void deleteWithEvent(Users users, Map<String, Object> syncObject) {
		usersRepository.delete(users);
		// Notify syncObject.
		synchronized(syncObject) {
			syncObject.notify();
		}
		// Public event.
		publisher.publishEvent(new UserEvent(users));
	}

	@Override
	public void deleteById(Integer id) {
		usersRepository.delete(id);
	}

	@Override
	public void deleteWithEventById(Integer id, Map<String, Object> syncObject) {
		usersRepository.delete(id);
		// Public event.
		Users users = new Users();
		users.setId(id);
		// Notify syncObject.
		synchronized(syncObject) {
			syncObject.put("id", id);
			syncObject.notify();
		}
		// Public event.
		publisher.publishEvent(new UserEvent(users));
	}

	@Override
	public Boolean confirmStatus(Integer id, int status) {
		return userEventListener.confirmStatus(id, status);
	}
	
	@Override
	public Users getById(Integer id) {
		return usersRepository.findOne(id);
	}
	
	@Override
	public Users getByUsername(String username) {
		return usersRepository.findByUsername(username);
	}

	@Override
	public List<Users> listAll() {
		return usersRepository.findAll();
	}

	@Override
	public long countAll() {
		return usersRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return usersRepository.exists(id);
	}

	
	public Page<Users> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Users> userSpecification = usersSpecificationsBuilder.build(searchCriterias);
		Page<Users> result = usersRepository.findAll(userSpecification, pageable);
        return result;
	}
}

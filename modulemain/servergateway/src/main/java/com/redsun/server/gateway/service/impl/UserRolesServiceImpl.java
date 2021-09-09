

package com.redsun.server.gateway.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.redsun.server.gateway.common.Role;
import com.redsun.server.gateway.model.UserRoles;
import com.redsun.server.gateway.model.common.SearchCriteria;
import com.redsun.server.gateway.repository.UserRolesRepository;
import com.redsun.server.gateway.repository.specification.UserRolesSpecificationsBuilder;
import com.redsun.server.gateway.service.UserRolesService;
@Service("userRoleRoles")
@Transactional
public class UserRolesServiceImpl implements UserRolesService {
	
	@Autowired
	private UserRolesRepository userRoleRolesRepository;
	
	@Autowired
	private UserRolesSpecificationsBuilder userRoleRolesSpecificationsBuilder;

	@Override
	public UserRoles save(UserRoles userRoleRoles) {
		return userRoleRolesRepository.save(userRoleRoles);
	}
	
	@Override
	public List<UserRoles> saveWithUsername(String username, List<Integer> idroles) {
		List<UserRoles> result = new ArrayList<UserRoles>();
		this.deleteByUsername(username);
		for (Integer idrole : idroles) {
			UserRoles userRoles = new UserRoles();
			userRoles.setUsername(username);
			userRoles.setRole(Role.getRole(idrole));
			userRoles = this.create(userRoles);
			result.add(userRoles);
		}
		// return.
		return result;
	}

	@Override
	public UserRoles create(UserRoles userRoles) {
		return userRoleRolesRepository.save(userRoles);
	}

	@Override
	public void deleteById(Integer id) {
		userRoleRolesRepository.delete(id);
	}

	@Override
	public void deleteByUsername(String username) {
		userRoleRolesRepository.deleteByUsername(username);
	}
	
	@Override
	public UserRoles getById(Integer id) {
		return userRoleRolesRepository.findOne(id);
	}
	
	@Override
	public List<UserRoles> listByUsername(String username) {
		return userRoleRolesRepository.findByUsername(username);
	}

	@Override
	public List<UserRoles> listAll() {
		return userRoleRolesRepository.findAll();
	}

	@Override
	public long countAll() {
		return userRoleRolesRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return userRoleRolesRepository.exists(id);
	}

	
	public Page<UserRoles> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<UserRoles> userRoleSpecification = userRoleRolesSpecificationsBuilder.build(searchCriterias);
		Page<UserRoles> result = userRoleRolesRepository.findAll(userRoleSpecification, pageable);
        return result;
	}
	
	public String getRoleNameByIdRole1(Integer idrole) {
		if(idrole == Role.ADMIN.getId()){
			return "ADMIN";
		}
		if(idrole == Role.USER.getId()){
			return "USER";
		}
		if(idrole == Role.EXTERNAL.getId()){
			return "EXTERNAL";
		}
		if(idrole == Role.OWNER.getId()){
			return "OWNER";
		}
		if(idrole == Role.SUPPLIER.getId()){
			return "SUPPLIER";
		}
		if(idrole == Role.DIRECTOR.getId()){
			return "DIRECTOR";
		}
		if(idrole == Role.MATERIAL.getId()){
			return "MATERIAL";
		}
		if(idrole == Role.MATERIAL_CODE.getId()){
			return "MATERIAL_CODE";
		}
		if(idrole == Role.STORE.getId()){
			return "STORE";
		}
		return "";
	}
	
	public List<Map<String, Object>> listAllRoles(){
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		Map<String, Object> map;
		// ADMIN.
		map = new HashMap<String, Object>();
		map.put("value", Role.ADMIN.getId());
		map.put("display", "ADMIN");
		result.add(map);
		// USER.
		map = new HashMap<String, Object>();
		map.put("value", Role.USER.getId());
		map.put("display", "USER");
		result.add(map);
		// EXTERNAL.
		map = new HashMap<String, Object>();
		map.put("value", Role.EXTERNAL.getId());
		map.put("display", "EXTERNAL");
		result.add(map);
		// OWNER
		map = new HashMap<String, Object>();
		map.put("value", Role.OWNER.getId());
		map.put("display", "OWNER");
		result.add(map);
		// SUPPLIER
		map = new HashMap<String, Object>();
		map.put("value", Role.SUPPLIER.getId());
		map.put("display", "SUPPLIER");
		result.add(map);
		// DIRECTOR
		map = new HashMap<String, Object>();
		map.put("value", Role.DIRECTOR.getId());
		map.put("display", "DIRECTOR");
		result.add(map);
		// MATERIAL
		map = new HashMap<String, Object>();
		map.put("value", Role.MATERIAL.getId());
		map.put("display", "MATERIAL");
		result.add(map);
		// MANAGER MATERIAL CODE
		map = new HashMap<String, Object>();
		map.put("value", Role.MATERIAL_CODE.getId());
		map.put("display", "MATERIAL_CODE");
		result.add(map);
		// STORE
		map = new HashMap<String, Object>();
		map.put("value", Role.STORE.getId());
		map.put("display", "STORE");
		result.add(map);
		
		// return.
		return result;
	}
	
	public List<Integer> listIdRolesByUsername(String username){
		List<Integer> result = new ArrayList<Integer>();
		List<UserRoles> userRoles = this.listByUsername(username);
		for (UserRoles userRole : userRoles) {
			result.add(Role.valueOf(userRole.getRole()).getId());
		}
		return result;
	}
}

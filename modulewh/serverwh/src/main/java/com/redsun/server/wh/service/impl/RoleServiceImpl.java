

package com.redsun.server.wh.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.redsun.server.wh.model.Role;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.RoleRepository;
import com.redsun.server.wh.repository.specification.RoleSpecification;
import com.redsun.server.wh.repository.specification.RoleSpecificationsBuilder;
import com.redsun.server.wh.service.RoleService;

@Service("role")
@Transactional
public class RoleServiceImpl implements RoleService {
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private RoleSpecificationsBuilder roleSpecificationsBuilder;

	@Override
	public Role save(Role role) {
		return roleRepository.save(role);
	}

	@Override
	public Role create(Role role) {
		return roleRepository.save(role);
	}

	@Override
	public Role update(Integer id, Role role) {
		//Role dbRole = roleRepository.findOne([Integer id]);
		//roleRepository.save(dbRole);
		return roleRepository.save(role);
	}

	@Override
	public void delete(Role role) {
		roleRepository.delete(role);
	}

	@Override
	public void deleteById(Integer id) {
		roleRepository.delete(id);
	}

	@Override
	public Role getById(Integer id) {
		return roleRepository.findOne(id);
	}

	@Override
	public List<Role> listAll() {
		return roleRepository.findAll();
	}

	@Override
	public long countAll() {
		return roleRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return roleRepository.exists(id);
	}
	
	public List<Role> listWithCritera(SearchCriteria searchCriteria) {
		Specification<Role> roleSpecification = new RoleSpecification(searchCriteria);
        List<Role> result = roleRepository.findAll(roleSpecification);
        return result;
	}
	
	public List<Role> listWithCriteras(List<SearchCriteria> searchCriterias) {
        Specification<Role> roleSpecification = roleSpecificationsBuilder.build(searchCriterias);
        List<Role> result = roleRepository.findAll(roleSpecification);
        return result;
	}
	
	public Page<Role> listAllByPage(Pageable pageable) {
		return roleRepository.findAll(pageable);
	}
	
	public Page<Role> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<Role> roleSpecification = new RoleSpecification(searchCriteria);
		Page<Role> result = roleRepository.findAll(roleSpecification, pageable);
        return result;
	}
	
	public Page<Role> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Role> roleSpecification = roleSpecificationsBuilder.build(searchCriterias);
		Page<Role> result = roleRepository.findAll(roleSpecification, pageable);
        return result;
	}

}

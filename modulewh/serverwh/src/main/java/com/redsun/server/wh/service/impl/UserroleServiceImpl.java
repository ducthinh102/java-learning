

package com.redsun.server.wh.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.redsun.server.wh.model.Userrole;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.UserroleRepository;
import com.redsun.server.wh.repository.specification.UserroleSpecification;
import com.redsun.server.wh.repository.specification.UserroleSpecificationsBuilder;
import com.redsun.server.wh.service.UserroleService;

@Service("userrole")
@Transactional
public class UserroleServiceImpl implements UserroleService {
	
	@Autowired
	private UserroleRepository userroleRepository;
	
	@Autowired
	private UserroleSpecificationsBuilder userroleSpecificationsBuilder;

	@Override
	public Userrole save(Userrole userrole) {
		return userroleRepository.save(userrole);
	}

	@Override
	public Userrole create(Userrole userrole) {
		return userroleRepository.save(userrole);
	}

	@Override
	public Userrole update(Integer id, Userrole userrole) {
		//Userrole dbUserrole = userroleRepository.findOne([Integer id]);
		//userroleRepository.save(dbUserrole);
		return userroleRepository.save(userrole);
	}

	@Override
	public void delete(Userrole userrole) {
		userroleRepository.delete(userrole);
	}

	@Override
	public void deleteById(Integer id) {
		userroleRepository.delete(id);
	}

	@Override
	public Userrole getById(Integer id) {
		return userroleRepository.findOne(id);
	}

	@Override
	public List<Userrole> listAll() {
		return userroleRepository.findAll();
	}

	@Override
	public long countAll() {
		return userroleRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return userroleRepository.exists(id);
	}
	
	public List<Userrole> listWithCritera(SearchCriteria searchCriteria) {
		Specification<Userrole> userroleSpecification = new UserroleSpecification(searchCriteria);
        List<Userrole> result = userroleRepository.findAll(userroleSpecification);
        return result;
	}
	
	public List<Userrole> listWithCriteras(List<SearchCriteria> searchCriterias) {
        Specification<Userrole> userroleSpecification = userroleSpecificationsBuilder.build(searchCriterias);
        List<Userrole> result = userroleRepository.findAll(userroleSpecification);
        return result;
	}
	
	public Page<Userrole> listAllByPage(Pageable pageable) {
		return userroleRepository.findAll(pageable);
	}
	
	public Page<Userrole> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<Userrole> userroleSpecification = new UserroleSpecification(searchCriteria);
		Page<Userrole> result = userroleRepository.findAll(userroleSpecification, pageable);
        return result;
	}
	
	public Page<Userrole> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Userrole> userroleSpecification = userroleSpecificationsBuilder.build(searchCriterias);
		Page<Userrole> result = userroleRepository.findAll(userroleSpecification, pageable);
        return result;
	}

}

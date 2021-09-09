

package com.redsun.server.wh.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.redsun.server.wh.model.Unitcoefficient;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.UnitcoefficientRepository;
import com.redsun.server.wh.repository.specification.UnitcoefficientSpecification;
import com.redsun.server.wh.repository.specification.UnitcoefficientSpecificationsBuilder;
import com.redsun.server.wh.service.UnitcoefficientService;

@Service("unitcoefficient")
@Transactional
public class UnitcoefficientServiceImpl implements UnitcoefficientService {
	
	@Autowired
	private UnitcoefficientRepository unitcoefficientRepository;
	
	@Autowired
	private UnitcoefficientSpecificationsBuilder unitcoefficientSpecificationsBuilder;

	@Override
	public Unitcoefficient save(Unitcoefficient unitcoefficient) {
		return unitcoefficientRepository.save(unitcoefficient);
	}

	@Override
	public Unitcoefficient create(Unitcoefficient unitcoefficient) {
		return unitcoefficientRepository.save(unitcoefficient);
	}

	@Override
	public Unitcoefficient update(Integer id, Unitcoefficient unitcoefficient) {
		//Unitcoefficient dbUnitcoefficient = unitcoefficientRepository.findOne([Integer id]);
		//unitcoefficientRepository.save(dbUnitcoefficient);
		return unitcoefficientRepository.save(unitcoefficient);
	}

	@Override
	public void delete(Unitcoefficient unitcoefficient) {
		unitcoefficientRepository.delete(unitcoefficient);
	}

	@Override
	public void deleteById(Integer id) {
		unitcoefficientRepository.delete(id);
	}

	@Override
	public Unitcoefficient getById(Integer id) {
		return unitcoefficientRepository.findOne(id);
	}

	@Override
	public List<Unitcoefficient> listAll() {
		return unitcoefficientRepository.findAll();
	}

	@Override
	public long countAll() {
		return unitcoefficientRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return unitcoefficientRepository.exists(id);
	}
	
	public List<Unitcoefficient> listWithCritera(SearchCriteria searchCriteria) {
		Specification<Unitcoefficient> unitcoefficientSpecification = new UnitcoefficientSpecification(searchCriteria);
        List<Unitcoefficient> result = unitcoefficientRepository.findAll(unitcoefficientSpecification);
        return result;
	}
	
	public List<Unitcoefficient> listWithCriteras(List<SearchCriteria> searchCriterias) {
        Specification<Unitcoefficient> unitcoefficientSpecification = unitcoefficientSpecificationsBuilder.build(searchCriterias);
        List<Unitcoefficient> result = unitcoefficientRepository.findAll(unitcoefficientSpecification);
        return result;
	}
	
	public Page<Unitcoefficient> listAllByPage(Pageable pageable) {
		return unitcoefficientRepository.findAll(pageable);
	}
	
	public Page<Unitcoefficient> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<Unitcoefficient> unitcoefficientSpecification = new UnitcoefficientSpecification(searchCriteria);
		Page<Unitcoefficient> result = unitcoefficientRepository.findAll(unitcoefficientSpecification, pageable);
        return result;
	}
	
	public Page<Unitcoefficient> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Unitcoefficient> unitcoefficientSpecification = unitcoefficientSpecificationsBuilder.build(searchCriterias);
		Page<Unitcoefficient> result = unitcoefficientRepository.findAll(unitcoefficientSpecification, pageable);
        return result;
	}

}



package com.redsun.server.wh.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.redsun.server.wh.model.Materialamount;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.MaterialamountRepository;
import com.redsun.server.wh.repository.specification.MaterialamountSpecification;
import com.redsun.server.wh.repository.specification.MaterialamountSpecificationsBuilder;
import com.redsun.server.wh.service.MaterialamountService;

@Service("materialamount")
@Transactional
public class MaterialamountServiceImpl implements MaterialamountService {
	
	@Autowired
	private MaterialamountRepository materialamountRepository;
	
	@Autowired
	private MaterialamountSpecificationsBuilder materialamountSpecificationsBuilder;

	@Override
	public Materialamount save(Materialamount materialamount) {
		return materialamountRepository.save(materialamount);
	}

	@Override
	public Materialamount create(Materialamount materialamount) {
		return materialamountRepository.save(materialamount);
	}

	@Override
	public Materialamount update(Integer id, Materialamount materialamount) {
		//Materialamount dbMaterialamount = materialamountRepository.findOne([Integer id]);
		//materialamountRepository.save(dbMaterialamount);
		return materialamountRepository.save(materialamount);
	}

	@Override
	public void delete(Materialamount materialamount) {
		materialamountRepository.delete(materialamount);
	}

	@Override
	public void deleteById(Integer id) {
		materialamountRepository.delete(id);
	}

	@Override
	public Materialamount getById(Integer id) {
		return materialamountRepository.findOne(id);
	}

	@Override
	public List<Materialamount> listAll() {
		return materialamountRepository.findAll();
	}

	@Override
	public long countAll() {
		return materialamountRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return materialamountRepository.exists(id);
	}
	
	public List<Materialamount> listWithCritera(SearchCriteria searchCriteria) {
		Specification<Materialamount> materialamountSpecification = new MaterialamountSpecification(searchCriteria);
        List<Materialamount> result = materialamountRepository.findAll(materialamountSpecification);
        return result;
	}
	
	public List<Materialamount> listWithCriteras(List<SearchCriteria> searchCriterias) {
        Specification<Materialamount> materialamountSpecification = materialamountSpecificationsBuilder.build(searchCriterias);
        List<Materialamount> result = materialamountRepository.findAll(materialamountSpecification);
        return result;
	}
	
	public Page<Materialamount> listAllByPage(Pageable pageable) {
		return materialamountRepository.findAll(pageable);
	}
	
	public Page<Materialamount> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<Materialamount> materialamountSpecification = new MaterialamountSpecification(searchCriteria);
		Page<Materialamount> result = materialamountRepository.findAll(materialamountSpecification, pageable);
        return result;
	}
	
	public Page<Materialamount> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Materialamount> materialamountSpecification = materialamountSpecificationsBuilder.build(searchCriterias);
		Page<Materialamount> result = materialamountRepository.findAll(materialamountSpecification, pageable);
        return result;
	}

}

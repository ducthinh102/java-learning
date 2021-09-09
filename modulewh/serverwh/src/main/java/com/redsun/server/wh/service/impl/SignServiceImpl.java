

package com.redsun.server.wh.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.redsun.server.wh.model.Sign;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.SignRepository;
import com.redsun.server.wh.repository.specification.SignSpecification;
import com.redsun.server.wh.repository.specification.SignSpecificationsBuilder;
import com.redsun.server.wh.service.SignService;

@Service("sign")
@Transactional
public class SignServiceImpl implements SignService {
	
	@Autowired
	private SignRepository signRepository;
	
	@Autowired
	private SignSpecificationsBuilder signSpecificationsBuilder;

	@Override
	public Sign save(Sign sign) {
		return signRepository.save(sign);
	}

	@Override
	public Sign create(Sign sign) {
		return signRepository.save(sign);
	}

	@Override
	public Sign update(Integer id, Sign sign) {
		//Sign dbSign = signRepository.findOne([Integer id]);
		//signRepository.save(dbSign);
		return signRepository.save(sign);
	}

	@Override
	public void delete(Sign sign) {
		signRepository.delete(sign);
	}

	@Override
	public void deleteById(Integer id) {
		signRepository.delete(id);
	}

	@Override
	public Sign getById(Integer id) {
		return signRepository.findOne(id);
	}

	@Override
	public List<Sign> listAll() {
		return signRepository.findAll();
	}

	@Override
	public long countAll() {
		return signRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return signRepository.exists(id);
	}
	
	public List<Sign> listWithCritera(SearchCriteria searchCriteria) {
		Specification<Sign> signSpecification = new SignSpecification(searchCriteria);
        List<Sign> result = signRepository.findAll(signSpecification);
        return result;
	}
	
	public List<Sign> listWithCriteras(List<SearchCriteria> searchCriterias) {
        Specification<Sign> signSpecification = signSpecificationsBuilder.build(searchCriterias);
        List<Sign> result = signRepository.findAll(signSpecification);
        return result;
	}
	
	public Page<Sign> listAllByPage(Pageable pageable) {
		return signRepository.findAll(pageable);
	}
	
	public Page<Sign> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<Sign> signSpecification = new SignSpecification(searchCriteria);
		Page<Sign> result = signRepository.findAll(signSpecification, pageable);
        return result;
	}
	
	public Page<Sign> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Sign> signSpecification = signSpecificationsBuilder.build(searchCriterias);
		Page<Sign> result = signRepository.findAll(signSpecification, pageable);
        return result;
	}

}

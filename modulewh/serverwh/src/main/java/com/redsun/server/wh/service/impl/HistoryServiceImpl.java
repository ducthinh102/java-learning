

package com.redsun.server.wh.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.redsun.server.wh.model.History;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.HistoryRepository;
import com.redsun.server.wh.repository.specification.HistorySpecification;
import com.redsun.server.wh.repository.specification.HistorySpecificationsBuilder;
import com.redsun.server.wh.service.HistoryService;

@Service("history")
@Transactional
public class HistoryServiceImpl implements HistoryService {
	
	@Autowired
	private HistoryRepository historyRepository;
	
	@Autowired
	private HistorySpecificationsBuilder historySpecificationsBuilder;

	@Override
	public History save(History history) {
		return historyRepository.save(history);
	}

	@Override
	public History create(History history) {
		return historyRepository.save(history);
	}

	@Override
	public History update(Integer id, History history) {
		//History dbHistory = historyRepository.findOne([Integer id]);
		//historyRepository.save(dbHistory);
		return historyRepository.save(history);
	}

	@Override
	public void delete(History history) {
		historyRepository.delete(history);
	}

	@Override
	public void deleteById(Integer id) {
		historyRepository.delete(id);
	}

	@Override
	public History getById(Integer id) {
		return historyRepository.findOne(id);
	}

	@Override
	public List<History> listAll() {
		return historyRepository.findAll();
	}

	@Override
	public long countAll() {
		return historyRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return historyRepository.exists(id);
	}
	
	public List<History> listWithCritera(SearchCriteria searchCriteria) {
		Specification<History> historySpecification = new HistorySpecification(searchCriteria);
        List<History> result = historyRepository.findAll(historySpecification);
        return result;
	}
	
	public List<History> listWithCriteras(List<SearchCriteria> searchCriterias) {
        Specification<History> historySpecification = historySpecificationsBuilder.build(searchCriterias);
        List<History> result = historyRepository.findAll(historySpecification);
        return result;
	}
	
	public Page<History> listAllByPage(Pageable pageable) {
		return historyRepository.findAll(pageable);
	}
	
	public Page<History> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<History> historySpecification = new HistorySpecification(searchCriteria);
		Page<History> result = historyRepository.findAll(historySpecification, pageable);
        return result;
	}
	
	public Page<History> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<History> historySpecification = historySpecificationsBuilder.build(searchCriterias);
		Page<History> result = historyRepository.findAll(historySpecification, pageable);
        return result;
	}

}

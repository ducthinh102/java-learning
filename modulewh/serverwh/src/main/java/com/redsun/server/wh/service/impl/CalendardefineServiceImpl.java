

package com.redsun.server.wh.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.redsun.server.wh.model.Calendardefine;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.CalendardefineRepository;
import com.redsun.server.wh.repository.specification.CalendardefineSpecification;
import com.redsun.server.wh.repository.specification.CalendardefineSpecificationsBuilder;
import com.redsun.server.wh.service.CalendardefineService;

@Service("calendardefine")
@Transactional
public class CalendardefineServiceImpl implements CalendardefineService {
	
	@Autowired
	private CalendardefineRepository calendardefineRepository;
	
	@Autowired
	private CalendardefineSpecificationsBuilder calendardefineSpecificationsBuilder;

	@Override
	public Calendardefine save(Calendardefine calendardefine) {
		return calendardefineRepository.save(calendardefine);
	}

	@Override
	public Calendardefine create(Calendardefine calendardefine) {
		return calendardefineRepository.save(calendardefine);
	}

	@Override
	public Calendardefine update(Integer id, Calendardefine calendardefine) {
		//Calendardefine dbCalendardefine = calendardefineRepository.findOne([Integer id]);
		//calendardefineRepository.save(dbCalendardefine);
		return calendardefineRepository.save(calendardefine);
	}

	@Override
	public void delete(Calendardefine calendardefine) {
		calendardefineRepository.delete(calendardefine);
	}

	@Override
	public void deleteById(Integer id) {
		calendardefineRepository.delete(id);
	}

	@Override
	public Calendardefine getById(Integer id) {
		return calendardefineRepository.findOne(id);
	}

	@Override
	public List<Calendardefine> listAll() {
		return calendardefineRepository.findAll();
	}

	@Override
	public long countAll() {
		return calendardefineRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return calendardefineRepository.exists(id);
	}
	
	public List<Calendardefine> listWithCritera(SearchCriteria searchCriteria) {
		Specification<Calendardefine> calendardefineSpecification = new CalendardefineSpecification(searchCriteria);
        List<Calendardefine> result = calendardefineRepository.findAll(calendardefineSpecification);
        return result;
	}
	
	public List<Calendardefine> listWithCriteras(List<SearchCriteria> searchCriterias) {
        Specification<Calendardefine> calendardefineSpecification = calendardefineSpecificationsBuilder.build(searchCriterias);
        List<Calendardefine> result = calendardefineRepository.findAll(calendardefineSpecification);
        return result;
	}
	
	public Page<Calendardefine> listAllByPage(Pageable pageable) {
		return calendardefineRepository.findAll(pageable);
	}
	
	public Page<Calendardefine> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<Calendardefine> calendardefineSpecification = new CalendardefineSpecification(searchCriteria);
		Page<Calendardefine> result = calendardefineRepository.findAll(calendardefineSpecification, pageable);
        return result;
	}
	
	public Page<Calendardefine> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Calendardefine> calendardefineSpecification = calendardefineSpecificationsBuilder.build(searchCriterias);
		Page<Calendardefine> result = calendardefineRepository.findAll(calendardefineSpecification, pageable);
        return result;
	}

}



package com.redsun.server.wh.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.redsun.server.wh.model.Calendar;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.CalendarRepository;
import com.redsun.server.wh.repository.specification.CalendarSpecification;
import com.redsun.server.wh.repository.specification.CalendarSpecificationsBuilder;
import com.redsun.server.wh.service.CalendarService;

@Service("calendar")
@Transactional
public class CalendarServiceImpl implements CalendarService {
	
	@Autowired
	private CalendarRepository calendarRepository;
	
	@Autowired
	private CalendarSpecificationsBuilder calendarSpecificationsBuilder;

	@Override
	public Calendar save(Calendar calendar) {
		return calendarRepository.save(calendar);
	}

	@Override
	public Calendar create(Calendar calendar) {
		return calendarRepository.save(calendar);
	}

	@Override
	public Calendar update(Integer id, Calendar calendar) {
		//Calendar dbCalendar = calendarRepository.findOne([Integer id]);
		//calendarRepository.save(dbCalendar);
		return calendarRepository.save(calendar);
	}

	@Override
	public void delete(Calendar calendar) {
		calendarRepository.delete(calendar);
	}

	@Override
	public void deleteById(Integer id) {
		calendarRepository.delete(id);
	}

	@Override
	public Calendar getById(Integer id) {
		return calendarRepository.findOne(id);
	}

	@Override
	public List<Calendar> listAll() {
		return calendarRepository.findAll();
	}

	@Override
	public long countAll() {
		return calendarRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return calendarRepository.exists(id);
	}
	
	public List<Calendar> listWithCritera(SearchCriteria searchCriteria) {
		Specification<Calendar> calendarSpecification = new CalendarSpecification(searchCriteria);
        List<Calendar> result = calendarRepository.findAll(calendarSpecification);
        return result;
	}
	
	public List<Calendar> listWithCriteras(List<SearchCriteria> searchCriterias) {
        Specification<Calendar> calendarSpecification = calendarSpecificationsBuilder.build(searchCriterias);
        List<Calendar> result = calendarRepository.findAll(calendarSpecification);
        return result;
	}
	
	public Page<Calendar> listAllByPage(Pageable pageable) {
		return calendarRepository.findAll(pageable);
	}
	
	public Page<Calendar> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<Calendar> calendarSpecification = new CalendarSpecification(searchCriteria);
		Page<Calendar> result = calendarRepository.findAll(calendarSpecification, pageable);
        return result;
	}
	
	public Page<Calendar> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Calendar> calendarSpecification = calendarSpecificationsBuilder.build(searchCriterias);
		Page<Calendar> result = calendarRepository.findAll(calendarSpecification, pageable);
        return result;
	}

}

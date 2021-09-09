

package com.redsun.server.wh.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.redsun.server.wh.model.Assignment;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.AssignmentRepository;
import com.redsun.server.wh.repository.specification.AssignmentSpecification;
import com.redsun.server.wh.repository.specification.AssignmentSpecificationsBuilder;
import com.redsun.server.wh.service.AssignmentService;

@Service("assignment")
@Transactional
public class AssignmentServiceImpl implements AssignmentService {
	
	@Autowired
	private AssignmentRepository assignmentRepository;
	
	@Autowired
	private AssignmentSpecificationsBuilder assignmentSpecificationsBuilder;

	@Override
	public Assignment save(Assignment assignment) {
		return assignmentRepository.save(assignment);
	}

	@Override
	public Assignment create(Assignment assignment) {
		return assignmentRepository.save(assignment);
	}

	@Override
	public Assignment update(Integer id, Assignment assignment) {
		//Assignment dbAssignment = assignmentRepository.findOne([Integer id]);
		//assignmentRepository.save(dbAssignment);
		return assignmentRepository.save(assignment);
	}

	@Override
	public void delete(Assignment assignment) {
		assignmentRepository.delete(assignment);
	}

	@Override
	public void deleteById(Integer id) {
		assignmentRepository.delete(id);
	}

	@Override
	public Assignment getById(Integer id) {
		return assignmentRepository.findOne(id);
	}

	@Override
	public List<Assignment> listAll() {
		return assignmentRepository.findAll();
	}

	@Override
	public long countAll() {
		return assignmentRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return assignmentRepository.exists(id);
	}
	
	public List<Assignment> listWithCritera(SearchCriteria searchCriteria) {
		Specification<Assignment> assignmentSpecification = new AssignmentSpecification(searchCriteria);
        List<Assignment> result = assignmentRepository.findAll(assignmentSpecification);
        return result;
	}
	
	public List<Assignment> listWithCriteras(List<SearchCriteria> searchCriterias) {
        Specification<Assignment> assignmentSpecification = assignmentSpecificationsBuilder.build(searchCriterias);
        List<Assignment> result = assignmentRepository.findAll(assignmentSpecification);
        return result;
	}
	
	public Page<Assignment> listAllByPage(Pageable pageable) {
		return assignmentRepository.findAll(pageable);
	}
	
	public Page<Assignment> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<Assignment> assignmentSpecification = new AssignmentSpecification(searchCriteria);
		Page<Assignment> result = assignmentRepository.findAll(assignmentSpecification, pageable);
        return result;
	}
	
	public Page<Assignment> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Assignment> assignmentSpecification = assignmentSpecificationsBuilder.build(searchCriterias);
		Page<Assignment> result = assignmentRepository.findAll(assignmentSpecification, pageable);
        return result;
	}
	
	public Assignment getByIdworkflowexecute(Integer idworkflowexecute) {
		return assignmentRepository.getFirstByIdworkflowexecute(idworkflowexecute);
	}

}

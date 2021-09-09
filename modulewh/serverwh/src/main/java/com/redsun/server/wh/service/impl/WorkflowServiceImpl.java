

package com.redsun.server.wh.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.redsun.server.wh.model.Workflow;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.WorkflowRepository;
import com.redsun.server.wh.repository.specification.WorkflowSpecification;
import com.redsun.server.wh.repository.specification.WorkflowSpecificationsBuilder;
import com.redsun.server.wh.service.WorkflowService;

@Service("workflow")
@Transactional
public class WorkflowServiceImpl implements WorkflowService {
	
	@Autowired
	private WorkflowRepository workflowRepository;
	
	@Autowired
	private WorkflowSpecificationsBuilder workflowSpecificationsBuilder;

	@Override
	public Workflow save(Workflow workflow) {
		return workflowRepository.save(workflow);
	}

	@Override
	public Workflow create(Workflow workflow) {
		return workflowRepository.save(workflow);
	}

	@Override
	public Workflow update(Integer id, Workflow workflow) {
		//Workflow dbWorkflow = workflowRepository.findOne([Integer id]);
		//workflowRepository.save(dbWorkflow);
		return workflowRepository.save(workflow);
	}

	@Override
	public void delete(Workflow workflow) {
		workflowRepository.delete(workflow);
	}

	@Override
	public void deleteById(Integer id) {
		workflowRepository.delete(id);
	}

	@Override
	public Workflow getById(Integer id) {
		return workflowRepository.findOne(id);
	}

	@Override
	public List<Workflow> listAll() {
		return workflowRepository.findAll();
	}

	@Override
	public long countAll() {
		return workflowRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return workflowRepository.exists(id);
	}
	
	public List<Workflow> listWithCritera(SearchCriteria searchCriteria) {
		Specification<Workflow> workflowSpecification = new WorkflowSpecification(searchCriteria);
        List<Workflow> result = workflowRepository.findAll(workflowSpecification);
        return result;
	}
	
	public List<Workflow> listWithCriteras(List<SearchCriteria> searchCriterias) {
        Specification<Workflow> workflowSpecification = workflowSpecificationsBuilder.build(searchCriterias);
        List<Workflow> result = workflowRepository.findAll(workflowSpecification);
        return result;
	}
	
	public Page<Workflow> listAllByPage(Pageable pageable) {
		return workflowRepository.findAll(pageable);
	}
	
	public Page<Workflow> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<Workflow> workflowSpecification = new WorkflowSpecification(searchCriteria);
		Page<Workflow> result = workflowRepository.findAll(workflowSpecification, pageable);
        return result;
	}
	
	public Page<Workflow> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Workflow> workflowSpecification = workflowSpecificationsBuilder.build(searchCriterias);
		Page<Workflow> result = workflowRepository.findAll(workflowSpecification, pageable);
        return result;
	}

}

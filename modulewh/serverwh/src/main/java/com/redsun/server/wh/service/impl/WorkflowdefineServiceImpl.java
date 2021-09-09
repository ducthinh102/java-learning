

package com.redsun.server.wh.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redsun.server.wh.common.Constant;
import com.redsun.server.wh.controller.common.ServerException;
import com.redsun.server.wh.model.History;
import com.redsun.server.wh.model.Workflowdefine;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.HistoryRepository;
import com.redsun.server.wh.repository.WorkflowdefineRepository;
import com.redsun.server.wh.repository.specification.WorkflowdefineSpecification;
import com.redsun.server.wh.repository.specification.WorkflowdefineSpecificationsBuilder;
import com.redsun.server.wh.service.WorkflowdefineService;
import com.redsun.server.wh.util.SecurityUtil;

@Service("workflowdefine")
@Transactional
public class WorkflowdefineServiceImpl implements WorkflowdefineService {
	
	@Autowired
	private HistoryRepository historyRepository;
	
	@Autowired
	private WorkflowdefineRepository workflowdefineRepository;
	
	@Autowired
	private WorkflowdefineSpecificationsBuilder workflowdefineSpecificationsBuilder;

	@Override
	public Workflowdefine save(Workflowdefine workflowdefine) {
		return workflowdefineRepository.save(workflowdefine);
	}

	@Override
	public Workflowdefine create(Workflowdefine workflowdefine) throws JsonParseException, JsonMappingException, IOException {
		// Workflowdefine is not existed
		Workflowdefine workflowdefineDB = workflowdefineRepository.getFirstByIdworkflowAndStepAndStatusNot(workflowdefine.getIdworkflow(), workflowdefine.getStep(), Constant.SERVERDB_STATUS_DELETE);
		if(workflowdefineDB == null) {
			workflowdefine.setIdowner(SecurityUtil.getIdUser());
			workflowdefine.setIdcreate(SecurityUtil.getIdUser());
			workflowdefine.setIdupdate(SecurityUtil.getIdUser());
			workflowdefine.setCreatedate(new Date());
			workflowdefine.setUpdatedate(new Date());
			workflowdefine.setStatus(Constant.SERVERDB_STATUS_NEW);
			workflowdefine.setVersion(1);
			
			Workflowdefine newWorkflowdefine = workflowdefineRepository.save(workflowdefine);

			// Save history.
			String historyStr = newWorkflowdefine.toString();
			History history = new History(newWorkflowdefine.getId(), "workflowdefine", historyStr);
			historyRepository.save(history);
			
			return newWorkflowdefine;
		}
			
		return null;
	}

	@Override
	public Integer updateLock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Update lock.
		Integer result = workflowdefineRepository.updateLock(id, iduser, Constant.SERVERDB_lOCKTIMEOUT);
		if(result == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		return result;		
	}
	
	@Override
	public Integer updateUnlock(Integer id) throws JsonParseException, JsonMappingException, IOException {
		// Update lock.
		Integer result = workflowdefineRepository.updateUnlock(id);
		if(result == 0) {
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		}
		return result;
	}
	
	private Map<String, Object> updatePre(Map<String, Object> params) throws JsonParseException, JsonMappingException, IOException {
		Map<String, Object> result = new HashMap<String, Object>();
		// Get value from params.
		Integer iduser = (Integer) params.get("iduser");
		Integer id = (Integer) params.get("id");
		Integer version = (Integer) params.get("version");
		// Get from DB.
		Workflowdefine workflowdefineDb = workflowdefineRepository.findOne(id);
		if(workflowdefineDb == null) { // not exist.
			throw new ServerException(Constant.SERVERCODE_NOTEXISTID);
		} else if(workflowdefineDb.getIdlock() != iduser) { // locked.
			throw new ServerException(Constant.SERVERCODE_LOCKED);
		} else if (workflowdefineDb.getVersion() != version) { // version difference.
			throw new ServerException(Constant.SERVERCODE_VERSIONDIFFERENCE);
		}
		else {
			// Keep history data.
			String historyStr = workflowdefineDb.toString();
			// Increase version.
			workflowdefineDb.setVersion(workflowdefineDb.getVersion() + 1);
			// return.
			result.put("workflowdefineDb", workflowdefineDb);
			result.put("historyStr", historyStr);
			return result;
		}
	}

	private Map<String, Object> updatePost(Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		// Get value from params.
		Integer id = (Integer) params.get("id");
		String historyStr = (String) params.get("historyStr");
		// Save history.
		History history = new History(id, "workflowdefine", historyStr);
		historyRepository.save(history);
		// return.
		return result;
	}
	
	@Override
	public Workflowdefine update(Integer id, Workflowdefine workflowdefine) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", workflowdefine.getVersion());
		params.put("workflowdefine", workflowdefine);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Workflowdefine workflowdefineDb = (Workflowdefine) resultPre.get("workflowdefineDb");
		// Ignore properties.
		String[] ignoreProperties = new String[] { "status", "idcreate", "idowner", "idupdate", "iddelete", "idlock", "createdate", "updatedate", "deletedate", "lockdate", "version" };
		// Copy data.
		BeanUtils.copyProperties(workflowdefine, workflowdefineDb, ignoreProperties);
		Date currentDate = new Date();
		workflowdefineDb.setStatus(Constant.SERVERDB_STATUS_UPDATE);
		workflowdefineDb.setIdupdate(iduser);
		workflowdefineDb.setUpdatedate(currentDate);
		workflowdefineDb.setIdowner(iduser);
		// Save.
		workflowdefineDb = workflowdefineRepository.save(workflowdefineDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return workflowdefineDb;
	}

	@Override
	public Workflowdefine updateWithLock(Integer id, Workflowdefine workflowdefine) throws JsonParseException, JsonMappingException, IOException {
		Workflowdefine result = null;
		// Lock to update.
		updateLock(id);
		// Update.
		result = update(id, workflowdefine);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}

	@Override
	public Workflowdefine updateForDelete(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		// Get iduser.
		Integer iduser = SecurityUtil.getIdUser();
		// Pre update.
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("iduser", iduser);
		params.put("id", id);
		params.put("version", version);
		Map<String, Object> resultPre = updatePre(params);
		// Update data.
		Workflowdefine workflowdefineDb = (Workflowdefine) resultPre.get("workflowdefineDb");
		Date currentDate = new Date();
		workflowdefineDb.setStatus(Constant.SERVERDB_STATUS_DELETE);
		workflowdefineDb.setIddelete(iduser);
		workflowdefineDb.setDeletedate(currentDate);
		// Save.
		workflowdefineDb = workflowdefineRepository.save(workflowdefineDb);
		// Post update.
		params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("historyStr", resultPre.get("historyStr"));
		updatePost(params);
		// return.
		return workflowdefineDb;
	}

	@Override
	public Workflowdefine updateForDeleteWithLock(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException {
		Workflowdefine result = null;
		// Lock to update.
		updateLock(id);
		// Update data.
		result = updateForDelete(id, version);
		// Unlock of update.
		updateUnlock(id);
		return result;
	}

	@Override
	public void delete(Workflowdefine workflowdefine) {
		workflowdefineRepository.delete(workflowdefine);
	}

	@Override
	public void deleteById(Integer id) {
		workflowdefineRepository.delete(id);
	}

	@Override
	public Workflowdefine getById(Integer id) {
		return workflowdefineRepository.findOne(id);
	}

	@Override
	public List<Workflowdefine> listAll() {
		return workflowdefineRepository.findAll();
	}

	@Override
	public long countAll() {
		return workflowdefineRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return workflowdefineRepository.exists(id);
	}
	
	public List<Workflowdefine> listWithCritera(SearchCriteria searchCriteria) {
		Specification<Workflowdefine> workflowdefineSpecification = new WorkflowdefineSpecification(searchCriteria);
        List<Workflowdefine> result = workflowdefineRepository.findAll(workflowdefineSpecification);
        return result;
	}
	
	public List<Workflowdefine> listWithCriteras(List<SearchCriteria> searchCriterias) {
        Specification<Workflowdefine> workflowdefineSpecification = workflowdefineSpecificationsBuilder.build(searchCriterias);
        List<Workflowdefine> result = workflowdefineRepository.findAll(workflowdefineSpecification);
        return result;
	}
	
	public Page<Workflowdefine> listAllByPage(Pageable pageable) {
		return workflowdefineRepository.findAll(pageable);
	}
	
	public Page<Workflowdefine> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<Workflowdefine> workflowdefineSpecification = new WorkflowdefineSpecification(searchCriteria);
		Page<Workflowdefine> result = workflowdefineRepository.findAll(workflowdefineSpecification, pageable);
        return result;
	}
	
	public Page<Workflowdefine> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Workflowdefine> workflowdefineSpecification = workflowdefineSpecificationsBuilder.build(searchCriterias);
		
		// Where status != delete.
		Specification<Workflowdefine> notDeleteSpec = new WorkflowdefineSpecification(new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE));
		workflowdefineSpecification = Specifications.where(workflowdefineSpecification).and(notDeleteSpec);
				
		Page<Workflowdefine> result = workflowdefineRepository.findAll(workflowdefineSpecification, pageable);
        return result;
	}

	@Override
	public Boolean isStepExisted(Integer idworkflow, Integer step) {
		Workflowdefine w = workflowdefineRepository.getFirstByIdworkflowAndStepAndStatusNot(idworkflow, step, Constant.SERVERDB_STATUS_DELETE);
		if(w != null)
			return true;
		return false;
	}

	@Override
	public Map<String, List<Integer>> getSendRulesByIdworkflowAndStep(Integer idworkflow, Integer step) throws JsonParseException, JsonMappingException, IOException {
		
		Map<String, List<Integer>> result = new HashMap<String, List<Integer>>();
		List<Integer> receiverIds = new ArrayList<Integer>();
		List<Integer> approverIds = new ArrayList<Integer>();
		List<Integer> definedStep = new ArrayList<Integer>();
		
		List<Workflowdefine> workflowdefines = workflowdefineRepository.findByIdworkflowAndStepGreaterThanEqualAndStatusNotOrderByStepAsc(idworkflow, step, Constant.SERVERDB_STATUS_DELETE);
		if(workflowdefines == null || workflowdefines.size() < 1)
			return null;
		
		Integer userId = SecurityUtil.getIdUser();
		
		workflowdefinesloop:
		for(Integer i=0; i<workflowdefines.size() && i<2; i++) {
			Workflowdefine w = workflowdefines.get(i);
			String transmitStr = w.getTransmit();
			if(transmitStr == null || transmitStr.isEmpty())
				return null;
			List<Map<String, List<Integer>>> transmitList = new ObjectMapper().readValue(transmitStr, new TypeReference<List<Map<String, List<Integer>>>>(){});
			for(Map<String, List<Integer>> transmitItem : transmitList) {
				List<Integer> senderIds = transmitItem.get("sender");
				for(Integer senderId : senderIds) {
					if(userId == senderId) {
						if( (transmitItem.get("receiver") == null) || (transmitItem.get("approver") == null) )
							return null;
						receiverIds.addAll(transmitItem.get("receiver"));
						approverIds.addAll(transmitItem.get("approver"));
						definedStep.add(w.getStep());
						break workflowdefinesloop;
					}
				}
			}
		}
		
		if( !receiverIds.isEmpty() && !approverIds.isEmpty() ) {
			result.put("receivers", receiverIds);
			result.put("approvers", approverIds);
			result.put("steps", definedStep);
			return result;
		} 
		
		return null;
		
	}

	@Override
	public Map<String, List<Integer>> getSendRulesByIdworkflow(Integer idworkflow) throws JsonParseException, JsonMappingException, IOException {
		
		Map<String, List<Integer>> result = new HashMap<String, List<Integer>>();
		List<Integer> receiverIds = new ArrayList<Integer>();
		List<Integer> approverIds = new ArrayList<Integer>();
		List<Integer> step = new ArrayList<Integer>();
		
		List<Workflowdefine> workflowdefines = workflowdefineRepository.findByIdworkflowAndStatusNotOrderByStepAsc(idworkflow, Constant.SERVERDB_STATUS_DELETE);
		if(workflowdefines == null || workflowdefines.size() < 1)
			return null;
		
		Integer userId = SecurityUtil.getIdUser();
		
		workflowdefinesloop:
		for(Integer i=0; i<workflowdefines.size() && i<2; i++) {
			Workflowdefine w = workflowdefines.get(i);
			String transmitStr = w.getTransmit();
			if(transmitStr == null || transmitStr.isEmpty())
				continue;
			List<Map<String, List<Integer>>> transmitList = new ObjectMapper().readValue(transmitStr, new TypeReference<List<Map<String, List<Integer>>>>(){});
			for(Map<String, List<Integer>> transmitItem : transmitList) {
				List<Integer> senderIds = transmitItem.get("sender");
				for(Integer senderId : senderIds) {
					if(userId == senderId) {
						if( (transmitItem.get("receiver") == null) || (transmitItem.get("approver") == null) )
							return null;
						receiverIds.addAll(transmitItem.get("receiver"));
						approverIds.addAll(transmitItem.get("approver"));
						step.add(w.getStep());
						break workflowdefinesloop;
					}
				}
			}
		}
		
		if( !receiverIds.isEmpty() && !approverIds.isEmpty() ) {
			result.put("receivers", receiverIds);
			result.put("approvers", approverIds);
			result.put("steps", step);
			return result;
		} 
		
		return null;
		
	}

	@Override
	public Boolean checkIsReceiverUsersByIdworkflowAndStepAndIdreceiver(Integer idworkflow, Integer step, Integer idreceiver) throws JsonParseException, JsonMappingException, IOException {
		
		Workflowdefine w = workflowdefineRepository.getFirstByIdworkflowAndStepAndStatusNot(idworkflow, step, Constant.SERVERDB_STATUS_DELETE);
		if(w == null)
			return false;
		
		String transmitStr = w.getTransmit();
		if(transmitStr == null || transmitStr.isEmpty())
			return false;
		List<Map<String, List<Integer>>> transmitList = new ObjectMapper().readValue(transmitStr, new TypeReference<List<Map<String, List<Integer>>>>(){});
		for(Map<String, List<Integer>> transmitItem : transmitList) {
			List<Integer> receiverIds = transmitItem.get("receiver");
			if(receiverIds.contains(idreceiver)) {
				return true;
			}
		}
		
		return false;
		
	}
	
	@Override
	public List<Integer> getAssigneeUsersByIdworkflowAndStepAndIdsender(Integer idworkflow, Integer step, Integer idsender) throws JsonParseException, JsonMappingException, IOException {
		
		List<Integer> result = new ArrayList<Integer>();
		
		Workflowdefine w = workflowdefineRepository.getFirstByIdworkflowAndStepAndStatusNot(idworkflow, step, Constant.SERVERDB_STATUS_DELETE);
		if(w == null)
			return result;
		
		String transmitStr = w.getTransmit();
		if(transmitStr == null || transmitStr.isEmpty())
			return result;
		List<Map<String, List<Integer>>> transmitList = new ObjectMapper().readValue(transmitStr, new TypeReference<List<Map<String, List<Integer>>>>(){});
		for(Map<String, List<Integer>> transmitItem : transmitList) {
			List<Integer> senderIds = transmitItem.get("sender");
			if(senderIds.contains(idsender)) {
				senderIds.remove(new Integer(idsender));
				result.addAll(senderIds);
			}
		}
		
		return result;
		
	}

	@Override
	public List<Integer> getAssigneeUsersByIdworkflowAndIdsender(Integer idworkflow, Integer idsender) throws JsonParseException, JsonMappingException, IOException {

		List<Integer> result = new ArrayList<Integer>();
		List<Workflowdefine> workflowdefines = workflowdefineRepository.findByIdworkflowAndStatusNot(idworkflow, Constant.SERVERDB_STATUS_DELETE);
		
		for(Workflowdefine workflowdefine : workflowdefines) {
			
			String str = workflowdefine.getTransmit();
			if(str == null || str.isEmpty())
				return result;
			List<Map<String, List<Integer>>> transmitList = new ObjectMapper().readValue(str, new TypeReference<List<Map<String, List<Integer>>>>(){});
			
			for(Map<String, List<Integer>> transmitItem : transmitList) {
				List<Integer> senderIds = transmitItem.get("sender");
				if(senderIds.contains(idsender)) {
					senderIds.remove(new Integer(idsender));
					result.addAll(senderIds);
				}
			}
			
		}
		
		return result;
		
	}
	
}

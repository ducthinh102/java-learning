package com.redsun.server.wh.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redsun.server.wh.common.Constant;
import com.redsun.server.wh.model.Assignment;
import com.redsun.server.wh.model.Materialbaseline;
import com.redsun.server.wh.model.Materialexport;
import com.redsun.server.wh.model.Materialform;
import com.redsun.server.wh.model.Materialimport;
import com.redsun.server.wh.model.Notify;
import com.redsun.server.wh.model.Purchase;
import com.redsun.server.wh.model.Quotation;
import com.redsun.server.wh.model.Request;
import com.redsun.server.wh.model.Sign;
import com.redsun.server.wh.model.Workflowexecute;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.repository.WorkflowexecuteRepository;
import com.redsun.server.wh.repository.specification.WorkflowexecuteSpecification;
import com.redsun.server.wh.repository.specification.WorkflowexecuteSpecificationsBuilder;
import com.redsun.server.wh.service.AssignmentService;
import com.redsun.server.wh.service.MaterialbaselineService;
import com.redsun.server.wh.service.MaterialexportService;
import com.redsun.server.wh.service.MaterialformService;
import com.redsun.server.wh.service.MaterialimportService;
import com.redsun.server.wh.service.NotifyService;
import com.redsun.server.wh.service.PurchaseService;
import com.redsun.server.wh.service.QuotationService;
import com.redsun.server.wh.service.RequestService;
import com.redsun.server.wh.service.SignService;
import com.redsun.server.wh.service.UserService;
import com.redsun.server.wh.service.WorkflowdefineService;
import com.redsun.server.wh.service.WorkflowexecuteService;
import com.redsun.server.wh.util.SecurityUtil;

@Service("workflowexecute")
@Transactional
public class WorkflowexecuteServiceImpl implements WorkflowexecuteService {

	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	private WorkflowexecuteRepository workflowexecuteRepository;

	@Autowired
	private WorkflowdefineService workflowdefineService;
	
	@Autowired
	private QuotationService quotationService;

	@Autowired
	private PurchaseService purchaseService;

	@Autowired
	private RequestService requestService;

	@Autowired
	private MaterialimportService materialimportService;
	
	@Autowired
	private MaterialexportService materialexportService;

	@Autowired
	private MaterialbaselineService materialbaselineService;
	
	@Autowired
	private MaterialformService materialformService;
	
	@Autowired
	private AssignmentService assignmentService;
	
	@Autowired
	private SignService signService;
	
	@Autowired
	private NotifyService notifyService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private WorkflowexecuteSpecificationsBuilder workflowexecuteSpecificationsBuilder;

	@Override
	public Workflowexecute save(Workflowexecute workflowexecute) {
		return workflowexecuteRepository.save(workflowexecute);
	}

	@Override
	public Workflowexecute create(Workflowexecute workflowexecute) throws JsonParseException, JsonMappingException, IOException {
		workflowexecute.setIdcreate(SecurityUtil.getIdUser());
		workflowexecute.setIdupdate(SecurityUtil.getIdUser());
		workflowexecute.setCreatedate(new Date());
		workflowexecute.setUpdatedate(new Date());
		workflowexecute.setVersion(1);
		return workflowexecuteRepository.save(workflowexecute);
	}

	@Override
	public Workflowexecute update(Integer id, Workflowexecute workflowexecute) throws JsonParseException, JsonMappingException, IOException {
		Integer oldVersion = (workflowexecute.getVersion() != null) ? workflowexecute.getVersion() : 0;
		
		workflowexecute.setIdupdate(SecurityUtil.getIdUser());
		workflowexecute.setUpdatedate(new Date());
		workflowexecute.setVersion(oldVersion + 1);
		return workflowexecuteRepository.save(workflowexecute);
	}

	@Override
	public void delete(Workflowexecute workflowexecute) {
		workflowexecuteRepository.delete(workflowexecute);
	}

	@Override
	public void deleteById(Integer id) {
		workflowexecuteRepository.delete(id);
	}

	@Override
	public Workflowexecute getById(Integer id) {
		return workflowexecuteRepository.findOne(id);
	}

	@Override
	public List<Workflowexecute> listAll() {
		return workflowexecuteRepository.findAll();
	}

	@Override
	public long countAll() {
		return workflowexecuteRepository.count();
	}

	@Override
	public boolean isExist(Integer id) {
		return workflowexecuteRepository.exists(id);
	}
	
	public List<Workflowexecute> listWithCritera(SearchCriteria searchCriteria) {
		Specification<Workflowexecute> workflowexecuteSpecification = new WorkflowexecuteSpecification(searchCriteria);
        List<Workflowexecute> result = workflowexecuteRepository.findAll(workflowexecuteSpecification);
        return result;
	}
	
	public List<Workflowexecute> listWithCriteras(List<SearchCriteria> searchCriterias) {
        Specification<Workflowexecute> workflowexecuteSpecification = workflowexecuteSpecificationsBuilder.build(searchCriterias);
        List<Workflowexecute> result = workflowexecuteRepository.findAll(workflowexecuteSpecification);
        return result;
	}
	
	public Page<Workflowexecute> listAllByPage(Pageable pageable) {
		return workflowexecuteRepository.findAll(pageable);
	}
	
	public Page<Workflowexecute> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable) {
		Specification<Workflowexecute> workflowexecuteSpecification = new WorkflowexecuteSpecification(searchCriteria);
		Page<Workflowexecute> result = workflowexecuteRepository.findAll(workflowexecuteSpecification, pageable);
        return result;
	}
	
	public Page<Workflowexecute> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable) {
		Specification<Workflowexecute> workflowexecuteSpecification = workflowexecuteSpecificationsBuilder.build(searchCriterias);
		Page<Workflowexecute> result = workflowexecuteRepository.findAll(workflowexecuteSpecification, pageable);
        return result;
	}
	
	public Page<Map<String, Object>> listWithCriteriasByIdworkflowAndIdtabAndPage(List<SearchCriteria> searchCriterias, Integer idworkflow, String idtab, Pageable pageable) throws JsonParseException, JsonMappingException, IOException {
		
		// Get current login iduser.
		Integer iduser = SecurityUtil.getIdUser();
		
		if(searchCriterias.size() > 0) {
			searchCriterias.add(new SearchCriteria("AND"));
		}
		
		if(Objects.equals(idtab, Constant.TAB_DELETED)){
			// status: deleted
			SearchCriteria deleteSearchCriteria = new SearchCriteria("status", "=", Constant.SERVERDB_STATUS_DELETE);
			deleteSearchCriteria.setLogic("AND");
			searchCriterias.add(deleteSearchCriteria);
		} else {
			// status: not deleted
			SearchCriteria notDeleteSearchCriteria = new SearchCriteria("status", "!=", Constant.SERVERDB_STATUS_DELETE);
			notDeleteSearchCriteria.setLogic("AND");
			searchCriterias.add(notDeleteSearchCriteria);
		}
		
		List<Workflowexecute> workflowexecutes = new ArrayList<Workflowexecute>();
		
		switch(idtab) {
		case Constant.TAB_CREATED:
			// idcreate is current iduser
			SearchCriteria idcreateSearchCriteria = new SearchCriteria("idcreate", "=", iduser);
			idcreateSearchCriteria.setLogic("AND");
			searchCriterias.add(idcreateSearchCriteria);
			break;
			
		case Constant.TAB_RECEIVED:
			// idcreate is not current iduser
			SearchCriteria idNotCreateSearchCriteria = new SearchCriteria("idcreate", "!=", iduser);
			idNotCreateSearchCriteria.setLogic("AND");
			searchCriterias.add(idNotCreateSearchCriteria);
			
			// List items is approved by current user or sent , waitapprove to current user
			workflowexecutes = workflowexecuteRepository.listAllForReceive(idworkflow, iduser, Constant.SERVERDB_STATUS_APPROVE, Constant.SERVERDB_STATUS_SEND, Constant.SERVERDB_STATUS_RECEIVE, Constant.SERVERDB_STATUS_SENDBACK);
			
			// List received items with status APPROVE by another user
			List<Workflowexecute> receivedWorkflowexecutes = workflowexecuteRepository.findByIdworkflowAndIscurrentAndStatus(idworkflow, true, Constant.SERVERDB_STATUS_APPROVE);
			for(Workflowexecute rw: receivedWorkflowexecutes) {
				Integer currentStep = rw.getStep();
				if(workflowdefineService.checkIsReceiverUsersByIdworkflowAndStepAndIdreceiver(idworkflow, currentStep, iduser))
					workflowexecutes.add(rw);
			}
			break;

		case Constant.TAB_ASSIGNED:
			// list of workflowexecute.
			workflowexecutes = workflowexecuteRepository.listAllForWaitapprove(idworkflow, iduser, Constant.SERVERDB_STATUS_WAITAPPROVE);
			break;
			
		case Constant.TAB_DELETED:
			// idowner is current iduser
			SearchCriteria idownerDeletedSearchCriteria = new SearchCriteria("idowner", "=", iduser);
			idownerDeletedSearchCriteria.setLogic("OR");
			searchCriterias.add(idownerDeletedSearchCriteria);
			// iddelete is current iduser
			SearchCriteria iddeleteSearchCriteria = new SearchCriteria("iddelete", "=", iduser);
			iddeleteSearchCriteria.setLogic("AND");
			searchCriterias.add(iddeleteSearchCriteria);
			break;
			
		default:
			return null;
		}
		
		if( Objects.equals(idtab, Constant.TAB_RECEIVED) || Objects.equals(idtab, Constant.TAB_ASSIGNED) ) {
			// id in workflowexecute.
			List<Integer> ids = new ArrayList<Integer>();
			ids.add(0);
			for (Workflowexecute item : workflowexecutes) {
				ids.add(item.getIdref());
			}
			
			SearchCriteria idSearchCriteria = new SearchCriteria("id", "in", ids);
			idSearchCriteria.setLogic("OR");
			searchCriterias.add(new SearchCriteria("AND"));
			searchCriterias.add(idSearchCriteria);
			
			if(Objects.equals(idtab, Constant.TAB_RECEIVED)) {
				// idowner is current iduser
				SearchCriteria idownerReceivedSearchCriteria = new SearchCriteria("idowner", "=", iduser);
				idownerReceivedSearchCriteria.setLogic("OR");
				searchCriterias.add(idownerReceivedSearchCriteria);
			}
		}
		
		Page<Map<String, Object>> result = null;
		switch(idworkflow) {
			case Constant.QUOTATION:
				result = quotationService.listWithCriterasByPage(searchCriterias, pageable).map(this::convertToMap);
				break;
			case Constant.PURCHASE:
				result = purchaseService.listWithCriterasByPage(searchCriterias, pageable);
				break;
			case Constant.REQUEST:
			case Constant.REQUESTEXTRA:
				result = requestService.listWithCriterasByPage(searchCriterias, pageable);
				break;
			case Constant.MATERIALIMPORT:
				result = materialimportService.listWithCriterasByPage(searchCriterias, pageable);
				break;
			case Constant.MATERIALEXPORT:
				result = materialexportService.listWithCriterasByPage(searchCriterias, pageable);
				break;
			case Constant.MATERIALBASELINE:
				result= materialbaselineService.listWithCriterasByPage(searchCriterias, pageable).map(this::convertToMap);
				break;
			case Constant.MATERIALPRE:
			case Constant.MATERIALTECH:
				result = materialformService.listWithCriterasByPage(searchCriterias, pageable).map(this::convertToMap);
				break;
			default:
				break;
		}
		
		return result;
	}
	
	public Object execute(Integer idworkflow, Integer idref, Integer command) throws JsonParseException, JsonMappingException, IOException {

		Integer userId = SecurityUtil.getIdUser();
		Integer newStatus = 0;
		Integer newIdowner = userId;
		List<Integer> newReceivers = new ArrayList<Integer>();
		Boolean result = false;
		
		///////////////////////////////////////////////////////
		// Find current workflowexecutes
		///////////////////////////////////////////////////////
		
		List<Workflowexecute> workflowexecutes = workflowexecuteRepository.findByIdworkflowAndIdrefAndIscurrentOrderByIdDesc(idworkflow, idref, true);
		
		// Workflowexecutes is not existed in DB
		if(workflowexecutes == null || workflowexecutes.size() < 1) {
			if(command != Constant.SERVERDB_STATUS_SEND)
				return null;

			// Get receiver id list from workflowdefine
			Map<String, List<Integer>> rules = workflowdefineService.getSendRulesByIdworkflow(idworkflow);
			if(rules == null)
				return null;
			List<Integer> approverIds = rules.get("approvers");
			List<Integer> receiverIds = rules.get("receivers");
			Integer step = rules.get("steps").get(0);
			
			// Sender same with approver, send directly
			if(approverIds.contains(userId)) {
				newStatus = Constant.SERVERDB_STATUS_SEND;
				newIdowner = userId;
				newReceivers.addAll(receiverIds);
				for(Integer receiverId : receiverIds) {
					// Create new workflowexecute for new step
					Workflowexecute newWorkflowexecute = new Workflowexecute();
					newWorkflowexecute.setStatus(newStatus);
					newWorkflowexecute.setIdworkflow(idworkflow);
					newWorkflowexecute.setIdref(idref);
					newWorkflowexecute.setIdsender(userId);
					newWorkflowexecute.setReftype(Constant.WORKFLOW.get(idworkflow));
					newWorkflowexecute.setStep(step);
					newWorkflowexecute.setIscurrent(true);
					newWorkflowexecute.setIdowner(userId);
					newWorkflowexecute.setIdreceiver(receiverId);
					newWorkflowexecute = this.create(newWorkflowexecute);
				}
			}
			// Sender different with approver, send to approval
			else {
				newStatus = Constant.SERVERDB_STATUS_WAITAPPROVE;
				newIdowner = userId;
				newReceivers.addAll(approverIds);
				for(Integer approverId : approverIds) {
					// Create new workflowexecute for new step
					Workflowexecute newWorkflowexecute = new Workflowexecute();
					newWorkflowexecute.setStatus(newStatus);
					newWorkflowexecute.setIdworkflow(idworkflow);
					newWorkflowexecute.setIdref(idref);
					newWorkflowexecute.setIdsender(userId);
					newWorkflowexecute.setReftype(Constant.WORKFLOW.get(idworkflow));
					newWorkflowexecute.setStep(step);
					newWorkflowexecute.setIscurrent(true);
					newWorkflowexecute.setIdowner(userId);
					newWorkflowexecute.setIdreceiver(approverId);
					newWorkflowexecute = this.create(newWorkflowexecute);
					
					// Create assignment
					Assignment assignment = new Assignment();
					assignment.setIdworkflowexecute(newWorkflowexecute.getId());
					assignment.setIdref(idref);
					assignment.setIdassignee(userId);
					assignment.setReftype(Constant.WORKFLOW.get(idworkflow));
					assignment.setStatus(Constant.SERVERDB_STATUS_WAITAPPROVE);
					assignment.setIdowner(userId);
					assignment.setIdcreate(userId);
					assignment.setIdupdate(userId);
					assignment.setCreatedate(new Date());
					assignment.setUpdatedate(new Date());
					assignment.setVersion(1);
					assignmentService.create(assignment);
				}
			}
			
			result = true;
		}
		
		else {
		
			// Workflowexecutes is existed in DB
			Integer currentStep = workflowexecutes.get(0).getStep();
			Integer currentStatus = workflowexecutes.get(0).getStatus();
			
			// Status do not change
			if(currentStatus == command)
				return null;
			
			///////////////////////////////////////////////////////
			// Create/Update workflowexecute
			///////////////////////////////////////////////////////
			
			switch (command) {
				case Constant.SERVERDB_STATUS_SEND:
					if(currentStatus != Constant.SERVERDB_STATUS_RECEIVE && currentStatus != Constant.SERVERDB_STATUS_GETBACK && currentStatus != Constant.SERVERDB_STATUS_SENDBACK && currentStatus != Constant.SERVERDB_STATUS_RECEIVESENDBACK)
						return null;
					
					List<Integer> newIdReceivers;
					
					// Get send rules from workflowdefine
					Map<String, List<Integer>> sendRules = workflowdefineService.getSendRulesByIdworkflowAndStep(idworkflow, currentStep);
					if(sendRules == null)
						return null;
					
					List<Integer> receiverIds = sendRules.get("receivers");
					List<Integer> approverIds = sendRules.get("approvers");
					Integer step = sendRules.get("steps").get(0);
					
					// Send directly, no need approval
					if(approverIds.contains(userId)) {
						newIdReceivers = receiverIds;
						newStatus = Constant.SERVERDB_STATUS_SEND;
					}
					// Need approval before sending
					else {
						newIdReceivers = approverIds;
						newStatus = Constant.SERVERDB_STATUS_WAITAPPROVE;
					}
					
					newReceivers.addAll(newIdReceivers);
					
					// Save new workflowexecute to DB
					for(Integer idReceiver : newIdReceivers) {
						// Create new workflowexecute for new step
						Workflowexecute newWorkflowexecute = new Workflowexecute();
						newWorkflowexecute.setIdworkflow(idworkflow);
						newWorkflowexecute.setIdref(idref);
						newWorkflowexecute.setIdsender(userId);
						newWorkflowexecute.setReftype(Constant.WORKFLOW.get(idworkflow));
						newWorkflowexecute.setStep(step);
						newWorkflowexecute.setIscurrent(true);
						newWorkflowexecute.setIdowner(userId);
						newWorkflowexecute.setIdreceiver(idReceiver);
						newWorkflowexecute.setStatus(newStatus);
						newWorkflowexecute = this.create(newWorkflowexecute);
						
						if(newStatus == Constant.SERVERDB_STATUS_WAITAPPROVE) {
							// Create assignment
							Assignment assignment = new Assignment();
							assignment.setIdworkflowexecute(newWorkflowexecute.getId());
							assignment.setIdref(idref);
							assignment.setIdassignee(userId);
							assignment.setReftype(Constant.WORKFLOW.get(idworkflow));
							assignment.setStatus(Constant.SERVERDB_STATUS_WAITAPPROVE);
							assignment.setIdowner(userId);
							assignment.setIdcreate(userId);
							assignment.setIdupdate(userId);
							assignment.setCreatedate(new Date());
							assignment.setUpdatedate(new Date());
							assignment.setVersion(1);
							assignmentService.create(assignment);
						}
					}
					
					// Set old workflowexecute iscurrent to false
					for(Workflowexecute w : workflowexecutes) {
						w.setIscurrent(false);
						this.update(w.getId(), w);
					}
	
					newIdowner = userId;
					result = true;
					break;
					
				case Constant.SERVERDB_STATUS_RECEIVE:
					if(currentStatus != Constant.SERVERDB_STATUS_SEND && currentStatus != Constant.SERVERDB_STATUS_APPROVE && currentStatus != Constant.SERVERDB_STATUS_SENDBACK)
						return null;
					
					// Update workflowexecutes iscurrent and status
					for(Workflowexecute w : workflowexecutes) {
						
						if(w.getIdreceiver() == userId) {
							
							// Check current status
							if(currentStatus == Constant.SERVERDB_STATUS_SENDBACK)
								newStatus = Constant.SERVERDB_STATUS_RECEIVESENDBACK;
							else
								newStatus = Constant.SERVERDB_STATUS_RECEIVE;
							
							// Create new workflowexecute with new step and status Receive
							Workflowexecute receiveWorkflowexecute = copyProperties(w);
							receiveWorkflowexecute.setStatus(newStatus);
							this.create(receiveWorkflowexecute);
							
							newIdowner = userId;
							newReceivers.add(w.getIdsender());
							result = true;
						}
						
						w.setIscurrent(false);
						this.update(w.getId(), w);
					}

					if(currentStatus == Constant.SERVERDB_STATUS_APPROVE) {
						Workflowexecute receiveApprove = copyProperties(workflowexecutes.get(0));
						receiveApprove.setIdreceiver(userId);
						receiveApprove.setIscurrent(true);
						receiveApprove.setStatus(Constant.SERVERDB_STATUS_RECEIVE);
						this.create(receiveApprove);
						
						newStatus = Constant.SERVERDB_STATUS_RECEIVE;
						newIdowner = userId;
						newReceivers.add(receiveApprove.getIdsender());
						result = true;
					}
					
					break;
					
				case Constant.SERVERDB_STATUS_GETBACK:
					if(currentStatus != Constant.SERVERDB_STATUS_SEND && currentStatus != Constant.SERVERDB_STATUS_WAITAPPROVE)
						return null;
					
					// Update workflowexecutes iscurrent and status
					for(Workflowexecute w : workflowexecutes) {
						
						Integer i = 0;
						
						if(w.getIdsender() == userId) {
							if(i==0) {
								// Create new workflowexecute for Get Back
								Workflowexecute newGetBackWorkflowexecute = copyProperties(w);
								newGetBackWorkflowexecute.setStatus(Constant.SERVERDB_STATUS_GETBACK);
								newGetBackWorkflowexecute.setIdreceiver(userId);
								newGetBackWorkflowexecute.setIscurrent(true);
								this.create(newGetBackWorkflowexecute);
							}
							i++;
						
							w.setIscurrent(false);
							this.update(w.getId(), w);
						}
						
						newStatus = Constant.SERVERDB_STATUS_GETBACK;
						newIdowner = userId;
						result = true;
					}
					
					break;
					
				case Constant.SERVERDB_STATUS_SENDBACK:
					if(currentStatus != Constant.SERVERDB_STATUS_RECEIVE
						&& currentStatus != Constant.SERVERDB_STATUS_RECEIVESENDBACK
						&& currentStatus != Constant.SERVERDB_STATUS_GETBACK
						&& currentStatus != Constant.SERVERDB_STATUS_WAITAPPROVE)
						return null;
					
					Workflowexecute sendbackWorkflowexecute = new Workflowexecute();
					
					if(currentStatus == Constant.SERVERDB_STATUS_WAITAPPROVE) {
						// Update all current workflowexecute iscurrent to False
						for(Workflowexecute w : workflowexecutes) {
							w.setIscurrent(false);
							this.update(w.getId(), w);
						}
						
						sendbackWorkflowexecute.setStep(currentStep);
						sendbackWorkflowexecute.setIdreceiver(workflowexecutes.get(0).getIdsender());
						newReceivers.add(workflowexecutes.get(0).getIdsender());
					}
					else {
						// Get last workflowexecute with status RECEIVE in current step or less
						Workflowexecute lastReceive = 
								workflowexecuteRepository.findFirstByIdworkflowAndIdrefAndIdreceiverAndStepLessThanEqualAndStatusOrderByIdDesc(
										idworkflow, idref,
										userId, currentStep, Constant.SERVERDB_STATUS_RECEIVE);
						if(lastReceive != null) {
							if(lastReceive.getIscurrent() == true) {
								lastReceive.setIscurrent(false);
								this.update(lastReceive.getId(), lastReceive);
							}
							
							sendbackWorkflowexecute.setStep(lastReceive.getStep());
							sendbackWorkflowexecute.setIdreceiver(lastReceive.getIdsender());
							newReceivers.add(lastReceive.getIdsender());
							
							// Update all current workflowexecute iscurrent to False
							for(Workflowexecute w : workflowexecutes) {
								w.setIscurrent(false);
								this.update(w.getId(), w);
							}
						}
						else
							return null;
					}
					
					sendbackWorkflowexecute.setStatus(Constant.SERVERDB_STATUS_SENDBACK);
					sendbackWorkflowexecute.setIdsender(userId);
					sendbackWorkflowexecute.setIdowner(userId);
					sendbackWorkflowexecute.setIscurrent(true);
					sendbackWorkflowexecute.setIdworkflow(idworkflow);
					sendbackWorkflowexecute.setIdref(idref);
					sendbackWorkflowexecute.setReftype(Constant.WORKFLOW.get(idworkflow));
					sendbackWorkflowexecute.setIdsender(userId);
					this.create(sendbackWorkflowexecute);
					
					newStatus = Constant.SERVERDB_STATUS_SENDBACK;
					result = true;
					break;
					
				case Constant.SERVERDB_STATUS_CANCEL:
					if(currentStatus != Constant.SERVERDB_STATUS_WAITAPPROVE)
						return null;
					
					// Update workflowexecutes iscurrent and status
					for(Workflowexecute w : workflowexecutes) {
						if(w.getIdreceiver() == userId) {
							// Create new workflowexecute for Get Back
							Workflowexecute cancelWorkflowexecute = copyProperties(w);
							cancelWorkflowexecute.setStatus(Constant.SERVERDB_STATUS_CANCEL);
							cancelWorkflowexecute.setIscurrent(true);
							this.create(cancelWorkflowexecute);
							
							newReceivers.add(w.getIdsender());
						}
						
						w.setIscurrent(false);
						this.update(w.getId(), w);
					}
					
					newStatus = Constant.SERVERDB_STATUS_CANCEL;
					result = true;
					break;
					
				case Constant.SERVERDB_STATUS_APPROVE:
					if(currentStatus != Constant.SERVERDB_STATUS_WAITAPPROVE)
						return null;
					
					for(Workflowexecute workflowexecute : workflowexecutes) {
						
						// Workflowexecute of current user
						if(workflowexecute.getIdreceiver() == userId) {
							
							// Check assignment existed in DB
							Assignment assignmentDB = assignmentService.getByIdworkflowexecute(workflowexecute.getId());
							if(assignmentDB == null)
								return null;
							
							// Create new sign
							Sign sign = new Sign();
							sign.setIdsigner(userId);
							sign.setIdassignment(assignmentDB.getId());
							sign.setSigndate(new Date());
							sign.setSignature("signed");
							sign.setStatus(Constant.SERVERDB_STATUS_NEW);
							sign.setIdowner(userId);
							sign.setIdcreate(userId);
							sign.setIdupdate(userId);
							sign.setCreatedate(new Date());
							sign.setUpdatedate(new Date());
							sign.setVersion(1);
							signService.save(sign);
							
							Workflowexecute approveWorkflowexecute = copyProperties(workflowexecute);
							approveWorkflowexecute.setIscurrent(true);
							approveWorkflowexecute.setStatus(Constant.SERVERDB_STATUS_APPROVE);
							this.create(approveWorkflowexecute);
							
							newReceivers.add(workflowexecute.getIdsender());
							result = true;
						}
						
						// Workflowexecute of other user
						workflowexecute.setIscurrent(false);
						this.update(workflowexecute.getId(),workflowexecute);
					}
					
					newStatus = Constant.SERVERDB_STATUS_APPROVE;
					newIdowner = null;
					break;
					
				default:
					return null;
			}
		
		}
		
		if(result == false)
			return null;
		
		///////////////////////////////////////////////////////
		// Send notification to receiver
		///////////////////////////////////////////////////////
		for(Integer idReceiver: newReceivers) {
			Notify notify = new Notify();
			notify.setIdsender(userId);
			notify.setIdreceiver(idReceiver);
			notify.setIdref(idref);
			notify.setReftype(Constant.WORKFLOW.get(idworkflow));
			notify.setContent(command.toString());
			notify.setMethod(110); // send message and email
			notify.setPriority(1);
			notifyService.create(notify);
		}
		
		///////////////////////////////////////////////////////
		// Update workflow item status
		///////////////////////////////////////////////////////
		switch(idworkflow) {
			case Constant.QUOTATION:
				Quotation quotation = quotationService.getById(idref);
				quotation.setStatus(newStatus);
				if(newIdowner != null)
					quotation.setIdowner(newIdowner);
				return quotationService.updateStatusAndOwnerWithLock(quotation.getId(), quotation);
				
			case Constant.PURCHASE:
				Purchase purchase = purchaseService.getById(idref);
				purchase.setStatus(newStatus);
				if(newIdowner != null)
					purchase.setIdowner(newIdowner);
				return purchaseService.updateStatusAndOwnerWithLock(purchase.getId(), purchase);
				
			case Constant.REQUEST:
				Request request = requestService.getById(idref);
				request.setStatus(newStatus);
				if(newIdowner != null)
					request.setIdowner(newIdowner);
				return requestService.updateStatusAndOwnerWithLock(request.getId(), request);
				
			case Constant.REQUESTEXTRA:
				Request requestextra = requestService.getById(idref);
				requestextra.setStatus(newStatus);
				if(newIdowner != null)
					requestextra.setIdowner(newIdowner);
				return requestService.updateStatusAndOwnerWithLock(requestextra.getId(), requestextra);
				
			case Constant.MATERIALIMPORT:
				Materialimport materialimport = materialimportService.getById(idref);
				materialimport.setStatus(newStatus);
				if(newIdowner != null)
					materialimport.setIdowner(newIdowner);
				return materialimportService.updateStatusAndOwnerWithLock(materialimport.getId(), materialimport);
				
			case Constant.MATERIALEXPORT:
				Materialexport materialexport = materialexportService.getById(idref);
				materialexport.setStatus(newStatus);
				if(newIdowner != null)
					materialexport.setIdowner(newIdowner);
				return materialexportService.updateStatusAndOwnerWithLock(materialexport.getId(), materialexport);
				
			case Constant.MATERIALBASELINE:
				Materialbaseline materialbaseline = materialbaselineService.getById(idref);
				materialbaseline.setStatus(newStatus);
				if(newIdowner != null)
					materialbaseline.setIdowner(newIdowner);
				return materialbaselineService.updateStatusAndOwnerWithLock(materialbaseline.getId(), materialbaseline);
				
			case Constant.MATERIALPRE:
				Materialform materialpre = materialformService.getById(idref);
				materialpre.setStatus(newStatus);
				if(newIdowner != null)
					materialpre.setIdowner(newIdowner);
				return materialformService.updateStatusAndOwnerWithLock(materialpre.getId(), materialpre);
				
			case Constant.MATERIALTECH:
				Materialform materialtech = materialformService.getById(idref);
				materialtech.setStatus(newStatus);
				if(newIdowner != null)
					materialtech.setIdowner(newIdowner);
				return materialformService.updateStatusAndOwnerWithLock(materialtech.getId(), materialtech);

			default:
				return null;
		}
		
	}
	
	public Object assign(Integer idworkflow, Integer idref, Integer idassignee) throws JsonParseException, JsonMappingException, IOException {
		
		Object result = null;
		
		switch(idworkflow) {
			case Constant.QUOTATION:
				Quotation quotation = quotationService.getById(idref);
				if(quotation != null) {
					quotation.setIdowner(idassignee);
					quotation.setStatus(Constant.SERVERDB_STATUS_UPDATE);
					result = quotationService.updateStatusAndOwnerWithLock(quotation.getId(), quotation);
				}
				break;
				
			case Constant.PURCHASE:
				Purchase purchase = purchaseService.getById(idref);
				if(purchase != null) {
					purchase.setIdowner(idassignee);
					purchase.setStatus(Constant.SERVERDB_STATUS_UPDATE);
					result = purchaseService.updateStatusAndOwnerWithLock(purchase.getId(), purchase);
				}
				break;
				
			case Constant.REQUEST:
				Request request = requestService.getById(idref);
				if(request != null) {
					request.setIdowner(idassignee);
					request.setStatus(Constant.SERVERDB_STATUS_UPDATE);
					result = requestService.updateStatusAndOwnerWithLock(request.getId(), request);
				}
				break;
				
			case Constant.REQUESTEXTRA:
				Request requestextra = requestService.getById(idref);
				if(requestextra != null) {
					requestextra.setIdowner(idassignee);
					requestextra.setStatus(Constant.SERVERDB_STATUS_UPDATE);
					result = requestService.updateStatusAndOwnerWithLock(requestextra.getId(), requestextra);
				}
				break;
				
			case Constant.MATERIALIMPORT:
				Materialimport materialimport = materialimportService.getById(idref);
				if(materialimport != null) {
					materialimport.setIdowner(idassignee);
					materialimport.setStatus(Constant.SERVERDB_STATUS_UPDATE);
					result = materialimportService.updateStatusAndOwnerWithLock(materialimport.getId(), materialimport);
				}
				break;
				
			case Constant.MATERIALEXPORT:
				Materialexport materialexport = materialexportService.getById(idref);
				if(materialexport != null) {
					materialexport.setIdowner(idassignee);
					materialexport.setStatus(Constant.SERVERDB_STATUS_UPDATE);
					result = materialexportService.updateStatusAndOwnerWithLock(materialexport.getId(), materialexport);
				}
				break;
				
			case Constant.MATERIALBASELINE:
				Materialbaseline materialbaseline = materialbaselineService.getById(idref);
				if(materialbaseline != null) {
					materialbaseline.setIdowner(idassignee);
					materialbaseline.setStatus(Constant.SERVERDB_STATUS_UPDATE);
					result = materialbaselineService.updateStatusAndOwnerWithLock(materialbaseline.getId(), materialbaseline);
				}
				break;
				
			case Constant.MATERIALPRE:
				Materialform materialpre = materialformService.getById(idref);
				if(materialpre != null) {
					materialpre.setIdowner(idassignee);
					materialpre.setStatus(Constant.SERVERDB_STATUS_UPDATE);
					result = materialformService.updateStatusAndOwnerWithLock(materialpre.getId(), materialpre);
				}
				break;
				
			case Constant.MATERIALTECH:
				Materialform materialtech = materialformService.getById(idref);
				if(materialtech != null) {
					materialtech.setIdowner(idassignee);
					materialtech.setStatus(Constant.SERVERDB_STATUS_UPDATE);
					result = materialformService.updateStatusAndOwnerWithLock(materialtech.getId(), materialtech);
				}
				break;
	
			default:
				return result;
		}
		
		///////////////////////////////////////////////////////
		// Send notification to receiver
		///////////////////////////////////////////////////////
		Notify notify = new Notify();
		notify.setIdsender(SecurityUtil.getIdUser());
		notify.setIdreceiver(idassignee);
		notify.setIdref(idref);
		notify.setReftype(Constant.WORKFLOW.get(idworkflow));
		notify.setContent(result.toString());
		notify.setMethod(110); // send message and email
		notify.setPriority(1);
		notifyService.create(notify);

		List<Workflowexecute> currentWorkflowexecutes = workflowexecuteRepository.findByIdworkflowAndIdrefAndIscurrentOrderByIdDesc(idworkflow, idref, true);
		for(Workflowexecute w : currentWorkflowexecutes) {
			w.setIscurrent(false);
			this.update(w.getId(), w);
		}
		
		return result;
		
	}
	
	private Boolean checkIsReceiverOfApprove(Integer idworkflow, Integer idref) throws JsonParseException, JsonMappingException, IOException {
		Workflowexecute approvedWorkflowexecute = workflowexecuteRepository.findFirstByIdworkflowAndIdrefAndIscurrentAndStatus(idworkflow, idref, true, Constant.SERVERDB_STATUS_APPROVE);
		if(approvedWorkflowexecute == null)
			return false;
		
		Integer currentStep = approvedWorkflowexecute.getStep();
		Integer userid = SecurityUtil.getIdUser();
		return workflowdefineService.checkIsReceiverUsersByIdworkflowAndStepAndIdreceiver(idworkflow, currentStep, userid);
	}
	
	public Map<String, Object> checkButtonVisibility(Integer idworkflow, Integer idref) throws JsonParseException, JsonMappingException, IOException {
		Integer currentStep = 0;
		Integer currentStatus = Constant.SERVERDB_STATUS_NEW;
		Workflowexecute currentWorkflowexecute = null;
		Boolean isReceiverOfSend = false;
		Boolean isOwner = this.isOwner(idworkflow, idref);

		// Result
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("send", false);
		result.put("sendback", false);
		result.put("getback", false);
		result.put("receive", false);
		result.put("assigneeUsers", null);
		
		// Get current login iduser.
		Integer iduser = SecurityUtil.getIdUser();
		
		// Get receiver id list from workflowdefine
		Map<String, List<Integer>> rules = null;
		
		List<Workflowexecute> workflowexecutes = workflowexecuteRepository.findByIdworkflowAndIdrefAndIscurrentOrderByIdDesc(idworkflow, idref, true);
		// Workflowexecutes is existed in DB
		if(workflowexecutes != null && workflowexecutes.size() > 0) {
			currentWorkflowexecute = workflowexecutes.get(0);
			currentStep = currentWorkflowexecute.getStep();
			currentStatus = currentWorkflowexecute.getStatus();
			
			rules = workflowdefineService.getSendRulesByIdworkflowAndStep(idworkflow, currentStep);
			
			for(Workflowexecute w : workflowexecutes) {
				if(w.getIdreceiver() == iduser
					&&(w.getStatus() == Constant.SERVERDB_STATUS_SEND
					|| w.getStatus() == Constant.SERVERDB_STATUS_SENDBACK))
				{
					isReceiverOfSend = true;
					break;
				}
			}
		}
		else {
			rules = workflowdefineService.getSendRulesByIdworkflow(idworkflow);
		}
		
		// Check buttons visibility by status in workflowexecute
		if(isOwner
		&&(rules != null)
		&&(currentStatus == Constant.SERVERDB_STATUS_NEW
		|| currentStatus == Constant.SERVERDB_STATUS_UPDATE
		|| currentStatus == Constant.SERVERDB_STATUS_RECEIVE
		|| currentStatus == Constant.SERVERDB_STATUS_GETBACK
		|| currentStatus == Constant.SERVERDB_STATUS_RECEIVESENDBACK))
			result.put("send", true);
			
		if(isOwner
		&&(currentStatus == Constant.SERVERDB_STATUS_SEND
		|| currentStatus == Constant.SERVERDB_STATUS_WAITAPPROVE))
			result.put("getback", true);
		
		if(isOwner
		&&(currentStatus == Constant.SERVERDB_STATUS_RECEIVE
		|| currentStatus == Constant.SERVERDB_STATUS_RECEIVESENDBACK
		|| currentStatus == Constant.SERVERDB_STATUS_GETBACK))
		{
			if(currentStep > 0) {
				// Get last workflowexecute with status RECEIVE in current step or less
				Workflowexecute lastReceive = 
						workflowexecuteRepository.findFirstByIdworkflowAndIdrefAndIdreceiverAndStepLessThanEqualAndStatusOrderByIdDesc(
								idworkflow, idref,
								iduser, currentStep, Constant.SERVERDB_STATUS_RECEIVE);
				if(lastReceive != null) {
					result.put("sendback", true);
				}
			}
		}
		
		if(currentWorkflowexecute != null
			&&( 
				( 	isReceiverOfSend
					&& (currentStatus == Constant.SERVERDB_STATUS_SEND
					|| currentStatus == Constant.SERVERDB_STATUS_SENDBACK)
				)
				||
				(
					checkIsReceiverOfApprove(idworkflow, idref)
					&& (currentStatus == Constant.SERVERDB_STATUS_APPROVE)
				)
			)
		)
			result.put("receive", true);
		
		if(isOwner
		&&(currentStatus == Constant.SERVERDB_STATUS_NEW
		|| currentStatus == Constant.SERVERDB_STATUS_UPDATE
		|| currentStatus == Constant.SERVERDB_STATUS_RECEIVE
		|| currentStatus == Constant.SERVERDB_STATUS_GETBACK
		|| currentStatus == Constant.SERVERDB_STATUS_RECEIVESENDBACK)) {
			List<Map<String, Object>> assigneeUsers = getAssigneeUsersByIdworkflowAndIdref(idworkflow, idref);
			if(assigneeUsers != null && assigneeUsers.size() > 0)
				result.put("assigneeUsers", assigneeUsers);
		}
		
		return result;
	}
	
	public Boolean isOwner(Integer idworkflow, Integer idref) throws JsonParseException, JsonMappingException, IOException {
		
		Integer userId = SecurityUtil.getIdUser();
		
		switch(idworkflow) {
			case Constant.QUOTATION:
				Quotation quotation = quotationService.getById(idref);
				if(quotation != null && quotation.getIdowner() == userId)
					return true;
				return false;
				
			case Constant.PURCHASE:
				Purchase purchase = purchaseService.getById(idref);
				if(purchase != null && purchase.getIdowner() == userId)
					return true;
				return false;
				
			case Constant.REQUEST:
				Request request = requestService.getById(idref);
				if(request != null && request.getIdowner() == userId)
					return true;
				return false;
				
			case Constant.REQUESTEXTRA:
				Request requestextra = requestService.getById(idref);
				if(requestextra != null && requestextra.getIdowner() == userId)
					return true;
				return false;
				
			case Constant.MATERIALIMPORT:
				Materialimport materialimport = materialimportService.getById(idref);
				if(materialimport != null && materialimport.getIdowner() == userId)
					return true;
				return false;
				
			case Constant.MATERIALEXPORT:
				Materialexport materialexport = materialexportService.getById(idref);
				if(materialexport != null && materialexport.getIdowner() == userId)
					return true;
				return false;
				
			case Constant.MATERIALBASELINE:
				Materialbaseline materialbaseline = materialbaselineService.getById(idref);
				if(materialbaseline != null && materialbaseline.getIdowner() == userId)
					return true;
				return false;
				
			case Constant.MATERIALPRE:
				Materialform materialpre = materialformService.getById(idref);
				if(materialpre != null && materialpre.getIdowner() == userId)
					return true;
				return false;
				
			case Constant.MATERIALTECH:
				Materialform materialtech = materialformService.getById(idref);
				if(materialtech != null && materialtech.getIdowner() == userId)
					return true;
				return false;
	
			default:
				return false;
		}
		
	}
	
	public List<Map<String, Object>> getAssigneeUsersByIdworkflowAndIdref(Integer idworkflow, Integer idref) throws JsonParseException, JsonMappingException, IOException {

		Integer userId = SecurityUtil.getIdUser();
		
		Workflowexecute w = workflowexecuteRepository.findFirstByIdworkflowAndIdrefAndIscurrent(idworkflow, idref, true);
		if(w != null) {
			Integer step = w.getStep();
			List<Integer> users = workflowdefineService.getAssigneeUsersByIdworkflowAndStepAndIdsender(idworkflow, step, userId);
			if(users.size() > 0)
				return userService.listForSelectByIds(users);
		}
		
		List<Integer> assigneeUsers = workflowdefineService.getAssigneeUsersByIdworkflowAndIdsender(idworkflow, userId);
		if(assigneeUsers != null && assigneeUsers.size() > 0)
			return userService.listForSelectByIds(assigneeUsers);
		return null;
	}
	
	private Map<String, Object> convertToMap(final Object object) {
	    Map<String,Object> result = objectMapper.convertValue(object, new TypeReference<Map<String, Object>>(){});
	    return result;
	}
	
	private Workflowexecute copyProperties(Workflowexecute sourceWorkflowexecute) {
		Workflowexecute newWorkflowexecute = new Workflowexecute();
		String[] ignoreProperties = new String[] { "id" };
		BeanUtils.copyProperties(sourceWorkflowexecute, newWorkflowexecute, ignoreProperties);
		return newWorkflowexecute;
	}

}

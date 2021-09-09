
package com.redsun.server.wh.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.redsun.server.wh.model.Workflowdefine;
import com.redsun.server.wh.model.common.SearchCriteria;

public interface WorkflowdefineService {

	/**
	 * Saves (create or update) the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param workflowdefine
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Workflowdefine save(Workflowdefine workflowdefine);

	/**
	 * Create the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param workflowdefine
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Workflowdefine create(Workflowdefine workflowdefine) throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param id
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Integer updateLock(Integer id) throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param id
	 * @param idlock
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Integer updateUnlock(Integer id) throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param workflowdefine
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Workflowdefine update(Integer id, Workflowdefine workflowdefine) throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param workflowdefine
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Workflowdefine updateWithLock(Integer id, Workflowdefine workflowdefine) throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param workflowdefine
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Workflowdefine updateForDelete(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param workflowdefine
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Workflowdefine updateForDeleteWithLock(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Deletes the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param workflowdefine
	 * @return true if found and deleted, false if not found
	 */
	void delete(Workflowdefine workflowdefine);

	/**
	 * Deletes the entity by its Primary Key <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param id
	 * @return true if found and deleted, false if not found
	 */
	void deleteById(Integer id);

	/**
	 * Loads the entity for the given Primary Key <br>
	 * @param id
	 * @return the entity loaded (or null if not found)
	 */
	Workflowdefine getById(Integer id);

	/**
	 * Loads ALL the entities (use with caution)
	 * @return
	 */
	List<Workflowdefine> listAll();

	/**
	 * Count all the occurrences
	 * @return
	 */
	long countAll();

	/**
	 * Check exist
	 * @return
	 */
	boolean isExist(Integer id);

	/**
	 * Search by a criteria.
	 * @return
	 */
	List<Workflowdefine> listWithCritera(SearchCriteria searchCriteria);

	/**
	 * Search by multiple criteria.
	 * @return
	 */
	List<Workflowdefine> listWithCriteras(List<SearchCriteria> searchCriterias);

	/**
	 * Loads ALL the entities by page (use with caution)
	 * @return
	 */
	Page<Workflowdefine> listAllByPage(Pageable pageable);

	/**
	 * Search by a criteria by page.
	 * @return
	 */
	Page<Workflowdefine> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable);

	/**
	 * Search by multiple criteria by page.
	 * @return
	 */
	Page<Workflowdefine> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable);
	
	/**
	 * Check step with same workflow is duplicated
	 * @return
	 */
	Boolean isStepExisted(Integer idworkflow, Integer step);
	
	/**
	 * Loads the receiver list and approver list <br>
	 * @param idworkflow
	 * @param step
	 * @return object contains receiver list and approver list (or null if not found)
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Map<String, List<Integer>> getSendRulesByIdworkflowAndStep(Integer idworkflow, Integer step) throws JsonParseException, JsonMappingException, IOException;
	
	/**
	 * Loads the receiver list and approver list <br>
	 * @param idworkflow
	 * @param step
	 * @return object contains receiver list and approver list (or null if not found)
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Map<String, List<Integer>> getSendRulesByIdworkflow(Integer idworkflow) throws JsonParseException, JsonMappingException, IOException;
	
	/**
	 * Loads the receiver list by approver and sender <br>
	 * @param idworkflow
	 * @param step
	 * @param senderUserId
	 * @return object contains receiver list (or null if not found)
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Boolean checkIsReceiverUsersByIdworkflowAndStepAndIdreceiver(Integer idworkflow, Integer step, Integer idsender) throws JsonParseException, JsonMappingException, IOException;
	
	/**
	 * Loads the receiver list by approver and sender <br>
	 * @param idworkflow
	 * @param step
	 * @param senderUserId
	 * @return object contains receiver list (or null if not found)
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	List<Integer> getAssigneeUsersByIdworkflowAndStepAndIdsender(Integer idworkflow, Integer step, Integer idsender) throws JsonParseException, JsonMappingException, IOException;
	
	/**
	 * Loads the assign user list <br>
	 * @param idworkflow
	 * @param step
	 * @return object contains receiver list and approver list (or null if not found)
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	List<Integer> getAssigneeUsersByIdworkflowAndIdsender(Integer idworkflow, Integer idsender) throws JsonParseException, JsonMappingException, IOException;
	
}

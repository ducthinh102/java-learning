
package com.redsun.server.wh.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.redsun.server.wh.model.Workflowexecute;
import com.redsun.server.wh.model.common.SearchCriteria;

public interface WorkflowexecuteService {

	/**
	 * Saves (create or update) the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param workflowexecute
	 * @return
	 */
	Workflowexecute save(Workflowexecute workflowexecute);

	/**
	 * Create the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param workflowexecute
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Workflowexecute create(Workflowexecute workflowexecute) throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param workflowexecute
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Workflowexecute update(Integer id, Workflowexecute workflowexecute) throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Deletes the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param workflowexecute
	 * @return true if found and deleted, false if not found
	 */
	void delete(Workflowexecute workflowexecute);

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
	Workflowexecute getById(Integer id);

	/**
	 * Loads ALL the entities (use with caution)
	 * @return
	 */
	List<Workflowexecute> listAll();

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
	List<Workflowexecute> listWithCritera(SearchCriteria searchCriteria);

	/**
	 * Search by multiple criteria.
	 * @return
	 */
	List<Workflowexecute> listWithCriteras(List<SearchCriteria> searchCriterias);

	/**
	 * Loads ALL the entities by page (use with caution)
	 * @return
	 */
	Page<Workflowexecute> listAllByPage(Pageable pageable);

	/**
	 * Search by a criteria by page.
	 * @return
	 */
	Page<Workflowexecute> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable);

	/**
	 * Search by multiple criteria by page.
	 * @return
	 */
	Page<Workflowexecute> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable);
	
	/**
	 * Search by multiple criteria by page.
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Page<Map<String, Object>> listWithCriteriasByIdworkflowAndIdtabAndPage(List<SearchCriteria> searchCriterias, Integer idworkflow, String idtab, Pageable pageable) throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param workflowexecute
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Object execute(Integer idworkflow, Integer idref, Integer command) throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param workflowexecute
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Object assign(Integer idworkflow, Integer idref, Integer idassignee) throws JsonParseException, JsonMappingException, IOException;
		
	/**
	 * Check buttons visibility
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Map<String, Object> checkButtonVisibility(Integer idworkflow, Integer idref) throws JsonParseException, JsonMappingException, IOException;
	
	/**
	 * Check is owner
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Boolean isOwner(Integer idworkflow, Integer idref) throws JsonParseException, JsonMappingException, IOException;
	
	/**
	 * Get assign users
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	List<Map<String, Object>> getAssigneeUsersByIdworkflowAndIdref(Integer idworkflow, Integer idref) throws JsonParseException, JsonMappingException, IOException;
	
}


package com.redsun.server.wh.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.redsun.server.wh.model.Assignment;
import com.redsun.server.wh.model.common.SearchCriteria;

public interface AssignmentService {

	/**
	 * Saves (create or update) the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param assignment
	 * @return
	 */
	Assignment save(Assignment assignment);

	/**
	 * Create the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param assignment
	 * @return
	 */
	Assignment create(Assignment assignment);

	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param assignment
	 * @return
	 */
	Assignment update(Integer id, Assignment assignment);

	/**
	 * Deletes the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param assignment
	 * @return true if found and deleted, false if not found
	 */
	void delete(Assignment assignment);

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
	Assignment getById(Integer id);

	/**
	 * Loads ALL the entities (use with caution)
	 * @return
	 */
	List<Assignment> listAll();

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
	List<Assignment> listWithCritera(SearchCriteria searchCriteria);

	/**
	 * Search by multiple criteria.
	 * @return
	 */
	List<Assignment> listWithCriteras(List<SearchCriteria> searchCriterias);

	/**
	 * Loads ALL the entities by page (use with caution)
	 * @return
	 */
	Page<Assignment> listAllByPage(Pageable pageable);

	/**
	 * Search by a criteria by page.
	 * @return
	 */
	Page<Assignment> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable);

	/**
	 * Search by multiple criteria by page.
	 * @return
	 */
	Page<Assignment> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable);
	
	/**
	 * Loads the entity for the given idworkflowexecute <br>
	 * @param idworkflowexecute
	 * @return the entity loaded (or null if not found)
	 */
	Assignment getByIdworkflowexecute(Integer idworkflowexecute);
	
}

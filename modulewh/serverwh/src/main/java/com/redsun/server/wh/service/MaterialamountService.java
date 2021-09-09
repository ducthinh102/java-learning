
package com.redsun.server.wh.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.redsun.server.wh.model.Materialamount;
import com.redsun.server.wh.model.common.SearchCriteria;

public interface MaterialamountService {

	/**
	 * Saves (create or update) the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param materialamount
	 * @return
	 */
	Materialamount save(Materialamount materialamount);

	/**
	 * Create the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param materialamount
	 * @return
	 */
	Materialamount create(Materialamount materialamount);

	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param materialamount
	 * @return
	 */
	Materialamount update(Integer id, Materialamount materialamount);

	/**
	 * Deletes the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param materialamount
	 * @return true if found and deleted, false if not found
	 */
	void delete(Materialamount materialamount);

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
	Materialamount getById(Integer id);

	/**
	 * Loads ALL the entities (use with caution)
	 * @return
	 */
	List<Materialamount> listAll();

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
	List<Materialamount> listWithCritera(SearchCriteria searchCriteria);

	/**
	 * Search by multiple criteria.
	 * @return
	 */
	List<Materialamount> listWithCriteras(List<SearchCriteria> searchCriterias);

	/**
	 * Loads ALL the entities by page (use with caution)
	 * @return
	 */
	Page<Materialamount> listAllByPage(Pageable pageable);

	/**
	 * Search by a criteria by page.
	 * @return
	 */
	Page<Materialamount> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable);

	/**
	 * Search by multiple criteria by page.
	 * @return
	 */
	Page<Materialamount> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable);
	
}

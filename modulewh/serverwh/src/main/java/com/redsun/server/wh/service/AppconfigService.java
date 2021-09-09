
package com.redsun.server.wh.service;

import java.io.IOException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.redsun.server.wh.model.Appconfig;
import com.redsun.server.wh.model.common.SearchCriteria;

public interface AppconfigService {

	/**
	 * Saves (create or update) the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param appconfig
	 * @return
	 */
	Appconfig save(Appconfig appconfig);

	/**
	 * Create the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param appconfig
	 * @return
	 */
	Appconfig create(Appconfig appconfig) throws JsonParseException, JsonMappingException, IOException;

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
	 * @param appconfig
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Appconfig update(Integer id, Appconfig appconfig) throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param appconfig
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Appconfig updateWithLock(Integer id, Appconfig appconfig) throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param appconfig
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Appconfig updateForDelete(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param appconfig
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Appconfig updateForDeleteWithLock(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException;

	
	/**
	 * Deletes the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param appconfig
	 * @return true if found and deleted, false if not found
	 */
	void delete(Appconfig appconfig);

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
	Appconfig getById(Integer id);

	/**
	 * Loads ALL the entities (use with caution)
	 * @return
	 */
	List<Appconfig> listAll();

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
	List<Appconfig> listWithCritera(SearchCriteria searchCriteria);

	/**
	 * Search by multiple criteria.
	 * @return
	 */
	List<Appconfig> listWithCriteras(List<SearchCriteria> searchCriterias);

	/**
	 * Loads ALL the entities by page (use with caution)
	 * @return
	 */
	Page<Appconfig> listAllByPage(Pageable pageable);

	/**
	 * Search by a criteria by page.
	 * @return
	 */
	Page<Appconfig> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable);

	/**
	 * Search by multiple criteria by page.
	 * @return
	 */
	Page<Appconfig> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable);
	
}

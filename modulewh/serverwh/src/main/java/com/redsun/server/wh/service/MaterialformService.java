
package com.redsun.server.wh.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.redsun.server.wh.model.Materialform;
import com.redsun.server.wh.model.common.SearchCriteria;

public interface MaterialformService {

	/**
	 * Saves (create or update) the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param materialform
	 * @return
	 */
	Materialform save(Materialform materialform);

	/**
	 * Create the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param materialform
	 * @return
	 */
	Materialform create(Materialform materialform) throws JsonParseException, JsonMappingException, IOException;

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
	 * @param materialform
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Materialform update(Integer id, Materialform materialform) throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param materialform
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Materialform updateWithLock(Integer id, Materialform materialform) throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param materialform
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Materialform updateStatusAndOwnerWithLock(Integer id, Materialform materialform) throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param materialform
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Materialform updateForDelete(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param materialform
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Materialform updateForDeleteWithLock(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException;

	
	/**
	 * Deletes the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param materialform
	 * @return true if found and deleted, false if not found
	 */
	void delete(Materialform materialform);

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
	Materialform getById(Integer id);

	/**
	 * Loads ALL the entities (use with caution)
	 * @return
	 */
	List<Materialform> listAll();

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
	List<Materialform> listWithCritera(SearchCriteria searchCriteria);

	/**
	 * Search by multiple criteria.
	 * @return
	 */
	List<Materialform> listWithCriteras(List<SearchCriteria> searchCriterias);

	/**
	 * Loads ALL the entities by page (use with caution)
	 * @return
	 */
	Page<Materialform> listAllByPage(Pageable pageable);

	/**
	 * Search by a criteria by page.
	 * @return
	 */
	Page<Materialform> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable);

	/**
	 * Search by multiple criteria by page.
	 * @return
	 */
	Page<Materialform> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable);
	
	List<Map<String, Object>> listAllForSelectByScope(String scope);
}

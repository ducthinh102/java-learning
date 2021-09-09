
package com.redsun.server.wh.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.redsun.server.wh.model.Permission;
import com.redsun.server.wh.model.common.SearchCriteria;

public interface PermissionService {

	/**
	 * Saves (create or update) the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param permission
	 * @return
	 */
	Permission save(Permission permission);

	/**
	 * Create the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param permission
	 * @return
	 */
	Permission create(Permission permission) throws JsonParseException, JsonMappingException, IOException;
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
	 * @param permission
	 * @return
	 */
	Permission update(Integer id, Permission permission)throws JsonParseException, JsonMappingException, IOException;
	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param appconfig
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Permission updateWithLock(Integer id, Permission permission) throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Deletes the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param permission
	 * @return true if found and deleted, false if not found
	 */
	void delete(Permission permission);

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
	Permission getById(Integer id);

	/**
	 * Loads the entity for the given target <br>
	 * @param id
	 * @return the entity loaded (or null if not found)
	 */
	Permission getByTarget(String target);

	/**
	 * Loads ALL the entities (use with caution)
	 * @return
	 */
	List<Permission> listAll();

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
	List<Permission> listWithCritera(SearchCriteria searchCriteria);

	/**
	 * Search by multiple criteria.
	 * @return
	 */
	List<Permission> listWithCriteras(List<SearchCriteria> searchCriterias);

	/**
	 * Loads ALL the entities by page (use with caution)
	 * @return
	 */
	Page<Permission> listAllByPage(Pageable pageable);

	/**
	 * Search by a criteria by page.
	 * @return
	 */
	Page<Permission> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable);

	/**
	 * Search by multiple criteria by page.
	 * @return
	 */
	Page<Permission> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable);
	
}

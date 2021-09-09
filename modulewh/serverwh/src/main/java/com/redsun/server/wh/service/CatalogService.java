package com.redsun.server.wh.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.redsun.server.wh.model.Catalog;
import com.redsun.server.wh.model.common.SearchCriteria;

public interface CatalogService {

	/**
	 * Saves (create or update) the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * 
	 * @param permission
	 * @return
	 */
	Catalog save(Catalog catalog);

	/**
	 * Create the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * 
	 * @param permission
	 * @return
	 */
	Catalog create(Catalog catalog) throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * 
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
	 * 
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
	 * 
	 * @param permission
	 * @return
	 */
	Catalog update(Integer id, Catalog catalog) throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * 
	 * @param appconfig
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	Catalog updateWithLock(Integer id, Catalog catalog) throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * 
	 * @param user
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	Catalog updateForDelete(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * 
	 * @param user
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	Catalog updateForDeleteWithLock(Integer id, Integer version)
			throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Deletes the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * 
	 * @param permission
	 * @return true if found and deleted, false if not found
	 */
	void delete(Catalog catalog);
	/**
	 * Loads the entity for the given Primary Key <br>
	 * 
	 * @param id
	 * @return the entity loaded (or null if not found)
	 */
	Catalog getById(Integer id);

	/**
	 * Loads ALL the entities (use with caution)
	 * 
	 * @return
	 */
	void updateForDeleteByIdParent(Integer id)throws JsonParseException, JsonMappingException, IOException;
	/**
	 * Loads ALL the entities (use with caution)
	 * 
	 * @return
	 */
	List<Catalog> listAll();

	/**
	 * Count all the occurrences
	 * 
	 * @return
	 */
	long countAll();

	/**
	 * Check exist
	 * 
	 * @return
	 */
	boolean isExist(Integer id);

	/**
	 * Load list by scope
	 * 
	 * @return
	 */
	List<Catalog> getListByScope(String scope);
	/**
	 * Search by a criteria.
	 * 
	 * @return
	 */
	List<Catalog> listWithCritera(SearchCriteria searchCriteria,String scope);

	/**
	 * Search by multiple criteria.
	 * 
	 * @return
	 */
	List<Catalog> listWithCriteras(List<SearchCriteria> searchCriterias,String scope);

	/**
	 * Loads ALL the entities by page (use with caution)
	 * 
	 * @return
	 */
	Page<Catalog> listAllByPage(Pageable pageable);

	/**
	 * Search by a criteria by page.
	 * 
	 * @return
	 */
	Page<Catalog> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable,String scope);

	/**
	 * Search by multiple criteria by page.
	 * 
	 * @return
	 */
	Page<Catalog> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable,String scope);

	/**
	 * List all for select by scope.
	 * 
	 * @return
	 */
	List<Map<String, Object>> listAllForSelectByScope(String scope);

}

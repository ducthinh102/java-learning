
package com.redsun.server.wh.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.redsun.server.wh.model.Materialexport;
import com.redsun.server.wh.model.common.SearchCriteria;

public interface MaterialexportService {

	/**
	 * Saves (create or update) the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * 
	 * @param materialexport
	 * @return
	 */
	Materialexport save(Materialexport materialexport);

	/**
	 * Create the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * 
	 * @param materialexport
	 * @return
	 */
	Materialexport create(Materialexport materialexport) throws JsonParseException, JsonMappingException, IOException;

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
	 * @param materialexport
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	Materialexport update(Integer id, Materialexport materialexport)
			throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * 
	 * @param materialexport
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	Materialexport updateWithLock(Integer id, Materialexport materialexport)
			throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * 
	 * @param materialexport
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	Materialexport updateStatusAndOwnerWithLock(Integer id, Materialexport materialexport)
			throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * 
	 * @param materialexport
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	Materialexport updateForDelete(Integer id, Integer version)
			throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * 
	 * @param materialexport
	 * @return
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	Materialexport updateForDeleteWithLock(Integer id, Integer version)
			throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Deletes the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * 
	 * @param materialexport
	 * @return true if found and deleted, false if not found
	 */
	void delete(Materialexport materialexport);

	/**
	 * Deletes the entity by its Primary Key <br>
	 * Transactional operation ( begin transaction and commit )
	 * 
	 * @param id
	 * @return true if found and deleted, false if not found
	 */
	void deleteById(Integer id);

	/**
	 * Loads the entity for the given Primary Key <br>
	 * 
	 * @param id
	 * @return the entity loaded (or null if not found)
	 */
	Materialexport getById(Integer id);

	/**
	 * Loads ALL the entities (use with caution)
	 * 
	 * @return
	 */
	List<Materialexport> listAll();

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
	 * Search by a criteria.
	 * 
	 * @return
	 */
	List<Materialexport> listWithCritera(SearchCriteria searchCriteria);

	/**
	 * Search by multiple criteria.
	 * 
	 * @return
	 */
	List<Materialexport> listWithCriteras(List<SearchCriteria> searchCriterias);

	/**
	 * Loads ALL the entities by page (use with caution)
	 * 
	 * @return
	 */
	Page<Materialexport> listAllByPage(Pageable pageable);

	/**
	 * Search by a criteria by page.
	 * 
	 * @return
	 */
	Page<Materialexport> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable);

	/**
	 * Search by multiple criteria by page.
	 * 
	 * @return
	 */
	Page<Map<String, Object>> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable);

	List<Map<String, Object>> listAllForSelect();

	Map<String, Object> getByIdForView(Integer id);
	
	Integer totalExportQuantity(Integer idstore,Integer idmaterial);

}

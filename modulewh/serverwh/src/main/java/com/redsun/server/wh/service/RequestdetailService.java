
package com.redsun.server.wh.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.redsun.server.wh.model.Requestdetail;
import com.redsun.server.wh.model.common.SearchCriteria;

public interface RequestdetailService {

	/**
	 * Saves (create or update) the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param requestdetail
	 * @return
	 */
	Requestdetail save(Requestdetail requestdetail);

	/**
	 * Create the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param requestdetail
	 * @return
	 */
	Requestdetail create(Requestdetail requestdetail) throws JsonParseException, JsonMappingException, IOException;

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
	 * @param requestdetail
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Requestdetail update(Integer id, Requestdetail requestdetail) throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param requestdetail
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Requestdetail updateWithLock(Integer id, Requestdetail requestdetail) throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param requestdetail
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Requestdetail updateForDelete(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param requestdetail
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Requestdetail updateForDeleteWithLock(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException;

	
	/**
	 * Deletes the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param requestdetail
	 * @return true if found and deleted, false if not found
	 */
	void delete(Requestdetail requestdetail);

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
	Requestdetail getById(Integer id);

	/**
	 * Loads ALL the entities (use with caution)
	 * @return
	 */
	List<Requestdetail> listAll();

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
	List<Requestdetail> listWithCritera(SearchCriteria searchCriteria);

	/**
	 * Search by multiple criteria.
	 * @return
	 */
	List<Requestdetail> listWithCriteras(List<SearchCriteria> searchCriterias);

	/**
	 * Loads ALL the entities by page (use with caution)
	 * @return
	 */
	Page<Requestdetail> listAllByPage(Pageable pageable);

	/**
	 * Search by a criteria by page.
	 * @return
	 */
	Page<Requestdetail> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable);

	/**
	 * Search by multiple criteria by page.
	 * @return
	 */
	Page<Requestdetail> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable);

	/**
	 * Search by multiple criteria by page.
	 * @return
	 */
	Page<Map<String, Object>> listWithCriteriasByIdrequestAndPage(Integer idrequest, List<SearchCriteria> searchCriterias, Pageable pageable);
	
	/**
	 * Loads the entity for the given Primary Key <br>
	 * @param id
	 * @return the entity loaded (or null if not found)
	 */
	List<Map<String, Object>> getAllById(Integer id);
	
	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param id,quantity
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	void updateQuantity(Integer id, Integer quantity) throws JsonParseException, JsonMappingException, IOException ;

	Map<String, Object> getByIdForView(Integer id);	
}

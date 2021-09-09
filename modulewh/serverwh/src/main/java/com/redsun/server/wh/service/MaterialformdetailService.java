
package com.redsun.server.wh.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.redsun.server.wh.model.Materialformdetail;
import com.redsun.server.wh.model.common.SearchCriteria;

public interface MaterialformdetailService {

	/**
	 * Saves (create or update) the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param materialformdetail
	 * @return
	 */
	Materialformdetail save(Materialformdetail materialformdetail);

	/**
	 * Create the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param materialformdetail
	 * @return
	 */
	Materialformdetail create(Materialformdetail materialformdetail) throws JsonParseException, JsonMappingException, IOException;

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
	 * @param materialformdetail
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Materialformdetail update(Integer id, Materialformdetail materialformdetail) throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param materialformdetail
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Materialformdetail updateWithLock(Integer id, Materialformdetail materialformdetail) throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param materialformdetail
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Materialformdetail updateForDelete(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param materialformdetail
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Materialformdetail updateForDeleteWithLock(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException;

	
	/**
	 * Deletes the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param materialformdetail
	 * @return true if found and deleted, false if not found
	 */
	void delete(Materialformdetail materialformdetail);

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
	Materialformdetail getById(Integer id);

	/**
	 * Loads ALL the entities (use with caution)
	 * @return
	 */
	List<Materialformdetail> listAll();

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
	List<Materialformdetail> listWithCritera(SearchCriteria searchCriteria);

	/**
	 * Search by multiple criteria.
	 * @return
	 */
	List<Materialformdetail> listWithCriteras(List<SearchCriteria> searchCriterias);

	/**
	 * Loads ALL the entities by page (use with caution)
	 * @return
	 */
	Page<Materialformdetail> listAllByPage(Pageable pageable);

	/**
	 * Search by a criteria by page.
	 * @return
	 */
	Page<Materialformdetail> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable);

	/**
	 * Search by multiple criteria by page.
	 * @return
	 */
	Page<Materialformdetail> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable);

	/**
	 * Search by multiple criteria by page.
	 * @return
	 */
	Page<Map<String, Object>> listWithCriteriasByIdmaterialformAndPage(Integer idmaterialform, List<SearchCriteria> searchCriterias, Pageable pageable);

	Double sumAmount(@Param("idmaterialform") Integer idmaterialform);
	
	/**
	 * Loads the entity for the given Primary Key <br>
	 * @param id
	 * @return the entity loaded (or null if not found)
	 */
	List<Map<String, Object>> getAllById(Integer id);
	
	Boolean isExistMaterial(@Param("id") Integer id, @Param("materialformdetail") Integer integer);
	
	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param id,price
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	void updatePrice(Integer id, Double price) throws JsonParseException, JsonMappingException, IOException ;
	
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

	Materialformdetail updateRef(Integer id, Materialformdetail materialformdetail) throws JsonParseException, JsonMappingException, IOException;
	
	Materialformdetail updateRefWithLock(Integer id, Materialformdetail materialformdetail) throws JsonParseException, JsonMappingException, IOException;
	
}

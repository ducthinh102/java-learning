
package com.redsun.server.wh.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.redsun.server.wh.model.Material;
import com.redsun.server.wh.model.common.SearchCriteria;

public interface MaterialService {

	/**
	 * Saves (create or update) the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param material
	 * @return
	 */
	Material save(Material material);

	/**
	 * Create the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param material
	 * @return
	 */
	Material create(Material material) throws JsonParseException, JsonMappingException, IOException;
	
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
	Material updateWithLock(Integer id, Material material) throws JsonParseException, JsonMappingException, IOException;
	
	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param material
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Material updateForDelete(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param material
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Material updateForDeleteWithLock(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param material
	 * @return
	 */
	Material update(Integer id, Material material) throws Exception;

	/**
	 * Deletes the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param material
	 * @return true if found and deleted, false if not found
	 */
	void delete(Material material);

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
	Material getById(Integer id);
	
	/**
	 * Loads and locks the entity for the given Primary Key <br>
	 * @param id
	 * @return the entity loaded and locked (or null if not found or not locked)
	 */
	Material getAndLockById(Integer id, Integer userid);

	/**
	 * Get confirmed material by code
	 * @return
	 */
	Material getConfirmedItemByCode(String code);
	
	/**
	 * Loads ALL the entities (use with caution)
	 * @return
	 */
	List<Material> listAll();

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
	List<Material> listWithCritera(SearchCriteria searchCriteria);

	/**
	 * Search by multiple criteria.
	 * @return
	 */
	List<Material> listWithCriteras(List<SearchCriteria> searchCriterias);

	/**
	 * Loads ALL the entities by page (use with caution)
	 * @return
	 */
	Page<Material> listAllByPage(Pageable pageable);

	/**
	 * Search by a criteria by page.
	 * @return
	 */
	Page<Material> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable);

	/**
	 * Search by multiple criteria by page.
	 * @return
	 */
	Page<Material> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable);

	/**
	 * Search by multiple criteria by page.
	 * @return
	 */
	Page<Map<String, Object>> listWithCriteriasByIdrefAndReftypeAndPage(Integer idref, String reftype, List<SearchCriteria> searchCriterias, Pageable pageable);
	

	/**
	 * Search the unconfirmed entities by multiple criteria by page.
	 * @return
	 */
	Page<Material> listUnconfirmedWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable);

	/**
	 * Find confirmed materials by name
	 * @return
	 */
	List<Material> findConfirmedItemsByName(String name);
	
	/**
	 * Updates reference, thumbnail and attached files of the new material and quantity, thumbnail and attached files of the old material <br>
	 * @param newMaterial
	 * @return the new material if updated (or null if not updated)
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	Material updateUnconfirmedItem(Material newMaterial) throws JsonParseException, JsonMappingException, IOException;
	
	/**
	 * Updates reference of multiple unconfirmed materials
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	List<Material> updateUnconfirmedItems(List<Integer> ids) throws JsonParseException, JsonMappingException, IOException;

	List<Map<String, Object>> loadAllMaterialConfirmed();

	List<Map<String, Object>> loadAllFromBaselineByIdstore(Integer idstore);
	
	 List<Map<String, Object>> listAllForSelect();
	 
	 Map<String, Object> getByIdForView(Integer id);

	Map<String, Object> getByIdForViewSub(Integer id);
}


package com.redsun.server.wh.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.redsun.server.wh.model.User;
import com.redsun.server.wh.model.common.SearchCriteria;

public interface UserService {

	/**
	 * Saves (create or update) the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param user
	 * @return
	 */
	User save(User user);

	/**
	 * Create the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param user
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	User create(User user) throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Create with event the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param user
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	User createWithEvent(User user, Map<String, Object> syncObject) throws JsonParseException, JsonMappingException, IOException;

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
	 * @param user
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	User updatePassword(Integer id, String password) throws JsonParseException, JsonMappingException, IOException;
	
	/**
	 * Update the given password <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param user
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	User updatePasswordWithLock(Integer id, String password) throws JsonParseException, JsonMappingException, IOException;
	
	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param user
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	User update(Integer id, User user) throws JsonParseException, JsonMappingException, IOException;
	
	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param user
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	User updateWithLock(Integer id, User user) throws JsonParseException, JsonMappingException, IOException;
	
	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param user
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	User updateWithLockAndEvent(Integer id, User user, Map<String, Object> syncObject) throws JsonParseException, JsonMappingException, IOException;
	
	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param user
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	User updateForDelete(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException;
	
	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param user
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	User updateForDeleteWithLock(Integer id, Integer version) throws JsonParseException, JsonMappingException, IOException;
	
	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param user
	 * @return
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	User updateForDeleteWithLockAndEvent(Integer id, Integer version, Map<String, Object> syncObject) throws JsonParseException, JsonMappingException, IOException;

	/**
	 * Delete the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param user
	 * @return true if found and deleted, false if not found
	 */
	void delete(User user);

	/**
	 * Delete with event the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param user
	 * @return true if found and deleted, false if not found
	 */
	void deleteWithEvent(User user, Map<String, Object> syncObject);

	/**
	 * Delete the entity by its Primary Key <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param id
	 * @return true if found and deleted, false if not found
	 */
	void deleteById(Integer id);

	/**
	 * Delete with event  the entity by its Primary Key <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param id
	 * @return true if found and deleted, false if not found
	 */
	void deleteWithEventById(Integer id, Map<String, Object> syncObject);

	/**
	 * Confirm transaction service.
	 * @param id
	 * @return true if found and deleted, false if not found
	 */
	Boolean confirmStatus(Integer id, int status);

	/**
	 * Loads the entity for the given Primary Key <br>
	 * @param id
	 * @return the entity loaded (or null if not found)
	 */
	User getById(Integer id);

	/**
	 * Loads the entity for the given username <br>
	 * @param username
	 * @return the entity loaded (or null if not found)
	 */
	User getByUsername(String username);

	/**
	 * Loads ALL the entities (use with caution)
	 * @return
	 */
	List<User> listAll();
	
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
	List<User> listWithCritera(SearchCriteria searchCriteria);

	/**
	 * Search by multiple criteria.
	 * @return
	 */
	List<User> listWithCriteras(List<SearchCriteria> searchCriterias);

	/**
	 * Loads ALL the entities by page (use with caution)
	 * @return
	 */
	Page<User> listAllByPage(Pageable pageable);

	/**
	 * Search by a criteria by page.
	 * @return
	 */
	Page<User> listWithCriteraByPage(SearchCriteria searchCriteria, Pageable pageable);

	/**
	 * Search by multiple criteria by page.
	 * @return
	 */
	Page<User> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable);

	/**
	 * Loads ALL the entities for select.
	 * @return
	 */
	List<Map<String, Object>> listAllForSelect();
	
	/**
	 * Loads ALL the entities for select by ids.
	 * @return
	 */
	List<Map<String, Object>> listForSelectByIds(List<Integer> ids);
	
}

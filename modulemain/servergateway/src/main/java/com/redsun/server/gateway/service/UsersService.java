
package com.redsun.server.gateway.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.redsun.server.gateway.model.Users;
import com.redsun.server.gateway.model.common.SearchCriteria;

public interface UsersService {

	/**
	 * Saves (create or update) the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param users
	 * @return
	 */
	Users save(Users users);

	/**
	 * Create the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param users
	 * @return
	 */
	Users create(Users users);

	/**
	 * Create with event the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param users
	 * @return
	 */
	Users createWithEvent(Users users, Map<String, Object> syncObject);

	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param users
	 * @return
	 */
	Users update(String username, Users users);

	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param users
	 * @return
	 */
	Users updatePassword(String username, String password);

	/**
	 * Update with event the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param users
	 * @return
	 */
	Users updateWithEvent(String username, Users users, Map<String, Object> syncObject);


	/**
	 * Update with event the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param users
	 * @return
	 */
	Users updatePasswordWithEvent(String username, String password, Map<String, Object> syncObject);


	/**
	 * Update the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param users
	 * @return
	 */
	Users updateForDelete(String username);
	
	/**
	 * Deletes the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param users
	 * @return true if found and deleted, false if not found
	 */
	void delete(Users users);

	/**
	 * Delete with event the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param users
	 * @return true if found and deleted, false if not found
	 */
	void deleteWithEvent(Users users, Map<String, Object> syncObject);

	/**
	 * Deletes the entity by its Primary Key <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param id
	 * @return true if found and deleted, false if not found
	 */
	void deleteById(Integer id);

	/**
	 * Delete with event the entity by its Primary Key <br>
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
	Users getById(Integer id);

	/**
	 * Loads the entity for the given username <br>
	 * @param username
	 * @return the entity loaded (or null if not found)
	 */
	Users getByUsername(String username);

	/**
	 * Loads ALL the entities (use with caution)
	 * @return
	 */
	List<Users> listAll();

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
	 * Search by multiple criteria by page.
	 * @return
	 */
	Page<Users> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable);
	
}

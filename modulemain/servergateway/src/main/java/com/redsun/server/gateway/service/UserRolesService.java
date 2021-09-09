
package com.redsun.server.gateway.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.redsun.server.gateway.model.UserRoles;
import com.redsun.server.gateway.model.common.SearchCriteria;

public interface UserRolesService {

	/**
	 * Saves (create or update) the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param userRoles
	 * @return
	 */
	UserRoles save(UserRoles userRoles);

	/**
	 * Saves (create or update) the given username <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param userRoles
	 * @return
	 */
	List<UserRoles> saveWithUsername(String username, List<Integer> idroles);


	/**
	 * Create the given entity <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param userRoles
	 * @return
	 */
	UserRoles create(UserRoles userRoles);

	/**
	 * Deletes the entity by its Primary Key <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param id
	 */
	void deleteById(Integer id);

	/**
	 * Deletes the entity by username <br>
	 * Transactional operation ( begin transaction and commit )
	 * @param username
	 */
	void deleteByUsername(String username);
	
	/**
	 * Loads the entity for the given Primary Key <br>
	 * @param id
	 * @return the entity loaded (or null if not found)
	 */
	UserRoles getById(Integer id);

	/**
	 * Loads the entity for the given username <br>
	 * @param username
	 * @return the entity loaded (or null if not found)
	 */
	List<UserRoles> listByUsername(String username);

	/**
	 * Loads ALL the entities (use with caution)
	 * @return
	 */
	List<UserRoles> listAll();

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
	Page<UserRoles> listWithCriterasByPage(List<SearchCriteria> searchCriterias, Pageable pageable);
	
	/**
	 * Loads all roles.
	 * @return
	 */
	List<Map<String, Object>> listAllRoles();

	/**
	 * Loads roles for the given username <br>
	 * @param username
	 * @return the entity loaded (or null if not found)
	 */
	List<Integer> listIdRolesByUsername(String username);
	
}

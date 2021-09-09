
package com.redsun.server.gateway.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.redsun.server.gateway.model.UserRoles;

@Repository
public interface UserRolesRepository extends JpaRepository<UserRoles, Integer>, JpaSpecificationExecutor<UserRoles> {
	
	List<UserRoles> findByUsername(String username);

	Integer deleteByUsername(String username);
}
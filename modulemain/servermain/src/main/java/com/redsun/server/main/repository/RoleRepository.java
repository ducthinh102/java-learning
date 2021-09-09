
package com.redsun.server.main.repository;

import com.redsun.server.main.model.Role ;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>, JpaSpecificationExecutor<Role> {

	
	@Modifying
	@Query("UPDATE Role role SET role.idlock = :idlock, role.lockdate = CURRENT_TIMESTAMP() WHERE role.id = :id AND ((role.idlock = null AND role.lockdate = null) OR (role.lockdate != null AND EXTRACT(EPOCH FROM(CURRENT_TIMESTAMP() - role.lockdate)) > :timeout))")
	Integer updateLock(@Param("id") Integer id, @Param("idlock") Integer idlock, @Param("timeout") Integer timeout);

	@Modifying
	@Query("UPDATE Role role SET role.idlock = null, role.lockdate = null WHERE role.id = :id")
	Integer updateUnlock(@Param("id") Integer id);
	
	@Modifying
	@Query("SELECT COUNT(role) > 0  FROM Role role WHERE role.id = :id AND role.idlock = :idlock")
	Boolean updateCheckLock(@Param("id") Integer id, @Param("idlock") Integer idlock);
	
	@Query("SELECT new map (role.idcreate as idcreate, role.idowner as idowner) FROM Role role WHERE role.id = :id")
	List<Map<String, Object>> getAuthorizePropertiesById(@Param("id") Integer id);

}
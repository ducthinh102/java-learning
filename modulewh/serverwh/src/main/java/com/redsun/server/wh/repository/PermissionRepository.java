
package com.redsun.server.wh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.redsun.server.wh.model.Permission;



@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer>, JpaSpecificationExecutor<Permission> {

	@Modifying
	@Query("UPDATE Permission p SET p.idlock = :idlock, p.lockdate = CURRENT_TIMESTAMP() WHERE p.id = :id AND ((p.idlock = null AND p.lockdate = null) OR (p.lockdate != null AND EXTRACT(EPOCH FROM(CURRENT_TIMESTAMP() - p.lockdate)) > :timeout))")
	Integer updateLock(@Param("id") Integer id, @Param("idlock") Integer idlock, @Param("timeout") Integer timeout);

	@Modifying
	@Query("UPDATE Permission p SET p.idlock = null, p.lockdate = null WHERE p.id = :id")
	Integer updateUnlock(@Param("id") Integer id);
	
	List<Permission> findByTarget(String target);

}
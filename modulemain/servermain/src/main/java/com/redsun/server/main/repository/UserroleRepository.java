
package com.redsun.server.main.repository;

import com.redsun.server.main.model.Userrole ;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface UserroleRepository extends JpaRepository<Userrole, Integer>, JpaSpecificationExecutor<Userrole> {

	
	@Modifying
	@Query("UPDATE Userrole userrole SET userrole.idlock = :idlock, userrole.lockdate = CURRENT_TIMESTAMP() WHERE userrole.id = :id AND ((userrole.idlock = null AND userrole.lockdate = null) OR (userrole.lockdate != null AND EXTRACT(EPOCH FROM(CURRENT_TIMESTAMP() - userrole.lockdate)) > :timeout))")
	Integer updateLock(@Param("id") Integer id, @Param("idlock") Integer idlock, @Param("timeout") Integer timeout);

	@Modifying
	@Query("UPDATE Userrole userrole SET userrole.idlock = null, userrole.lockdate = null WHERE userrole.id = :id")
	Integer updateUnlock(@Param("id") Integer id);
	
	@Modifying
	@Query("SELECT COUNT(userrole) > 0  FROM Userrole userrole WHERE userrole.id = :id AND userrole.idlock = :idlock")
	Boolean updateCheckLock(@Param("id") Integer id, @Param("idlock") Integer idlock);
	
	@Query("SELECT new map (userrole.idcreate as idcreate, userrole.idowner as idowner) FROM Userrole userrole WHERE userrole.id = :id")
	List<Map<String, Object>> getAuthorizePropertiesById(@Param("id") Integer id);

}

package com.redsun.server.wh.repository;

import java.util.List;
import java.util.Map;

import com.redsun.server.wh.model.Unitcoefficient ;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface UnitcoefficientRepository extends JpaRepository<Unitcoefficient, Integer>, JpaSpecificationExecutor<Unitcoefficient> {

	
	@Modifying
	@Query("UPDATE Unitcoefficient unitcoefficient SET unitcoefficient.idlock = :idlock, unitcoefficient.lockdate = CURRENT_TIMESTAMP() WHERE unitcoefficient.id = :id AND ((unitcoefficient.idlock = null AND unitcoefficient.lockdate = null) OR (unitcoefficient.lockdate != null AND EXTRACT(EPOCH FROM(CURRENT_TIMESTAMP() - unitcoefficient.lockdate)) > :timeout))")
	Integer updateLock(@Param("id") Integer id, @Param("idlock") Integer idlock, @Param("timeout") Integer timeout);

	@Modifying
	@Query("UPDATE Unitcoefficient unitcoefficient SET unitcoefficient.idlock = null, unitcoefficient.lockdate = null WHERE unitcoefficient.id = :id")
	Integer updateUnlock(@Param("id") Integer id);
	
	@Modifying
	@Query("SELECT COUNT(unitcoefficient) > 0  FROM Unitcoefficient unitcoefficient WHERE unitcoefficient.id = :id AND unitcoefficient.idlock = :idlock")
	Boolean updateCheckLock(@Param("id") Integer id, @Param("idlock") Integer idlock);
	
	@Query("SELECT new map (unitcoefficient.idcreate as idcreate, unitcoefficient.idowner as idowner) FROM Unitcoefficient unitcoefficient WHERE unitcoefficient.id = :id")
	List<Map<String, Object>> getAuthorizePropertiesById(@Param("id") Integer id);

}
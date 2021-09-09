
package com.redsun.server.main.repository;

import com.redsun.server.main.model.Appconfig ;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface AppconfigRepository extends JpaRepository<Appconfig, Integer>, JpaSpecificationExecutor<Appconfig> {

	
	@Modifying
	@Query("UPDATE Appconfig appconfig SET appconfig.idlock = :idlock, appconfig.lockdate = CURRENT_TIMESTAMP() WHERE appconfig.id = :id AND ((appconfig.idlock = null AND appconfig.lockdate = null) OR (appconfig.lockdate != null AND EXTRACT(EPOCH FROM(CURRENT_TIMESTAMP() - appconfig.lockdate)) > :timeout))")
	Integer updateLock(@Param("id") Integer id, @Param("idlock") Integer idlock, @Param("timeout") Integer timeout);

	@Modifying
	@Query("UPDATE Appconfig appconfig SET appconfig.idlock = null, appconfig.lockdate = null WHERE appconfig.id = :id")
	Integer updateUnlock(@Param("id") Integer id);
	
	@Modifying
	@Query("SELECT COUNT(appconfig) > 0  FROM Appconfig appconfig WHERE appconfig.id = :id AND appconfig.idlock = :idlock")
	Boolean updateCheckLock(@Param("id") Integer id, @Param("idlock") Integer idlock);
	
	@Query("SELECT new map (appconfig.idcreate as idcreate, appconfig.idowner as idowner) FROM Appconfig appconfig WHERE appconfig.id = :id")
	List<Map<String, Object>> getAuthorizePropertiesById(@Param("id") Integer id);

}

package com.redsun.server.main.repository;

import com.redsun.server.main.model.Notify ;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface NotifyRepository extends JpaRepository<Notify, Integer>, JpaSpecificationExecutor<Notify> {

	
	@Modifying
	@Query("UPDATE Notify notify SET notify.idlock = :idlock, notify.lockdate = CURRENT_TIMESTAMP() WHERE notify.id = :id AND ((notify.idlock = null AND notify.lockdate = null) OR (notify.lockdate != null AND EXTRACT(EPOCH FROM(CURRENT_TIMESTAMP() - notify.lockdate)) > :timeout))")
	Integer updateLock(@Param("id") Integer id, @Param("idlock") Integer idlock, @Param("timeout") Integer timeout);

	@Modifying
	@Query("UPDATE Notify notify SET notify.idlock = null, notify.lockdate = null WHERE notify.id = :id")
	Integer updateUnlock(@Param("id") Integer id);
	
	@Modifying
	@Query("SELECT COUNT(notify) > 0  FROM Notify notify WHERE notify.id = :id AND notify.idlock = :idlock")
	Boolean updateCheckLock(@Param("id") Integer id, @Param("idlock") Integer idlock);
	
	@Query("SELECT new map (notify.idcreate as idcreate, notify.idowner as idowner) FROM Notify notify WHERE notify.id = :id")
	List<Map<String, Object>> getAuthorizePropertiesById(@Param("id") Integer id);

}
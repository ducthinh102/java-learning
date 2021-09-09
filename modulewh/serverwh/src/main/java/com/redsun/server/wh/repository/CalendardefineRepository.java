
package com.redsun.server.wh.repository;

import com.redsun.server.wh.model.Calendardefine ;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface CalendardefineRepository extends JpaRepository<Calendardefine, Integer>, JpaSpecificationExecutor<Calendardefine> {

	
	@Modifying
	@Query("UPDATE Calendardefine calendardefine SET calendardefine.idlock = :idlock, calendardefine.lockdate = CURRENT_TIMESTAMP() WHERE calendardefine.id = :id AND ((calendardefine.idlock = null AND calendardefine.lockdate = null) OR (calendardefine.lockdate != null AND EXTRACT(EPOCH FROM(CURRENT_TIMESTAMP() - calendardefine.lockdate)) > :timeout))")
	Integer updateLock(@Param("id") Integer id, @Param("idlock") Integer idlock, @Param("timeout") Integer timeout);

	@Modifying
	@Query("UPDATE Calendardefine calendardefine SET calendardefine.idlock = null, calendardefine.lockdate = null WHERE calendardefine.id = :id")
	Integer updateUnlock(@Param("id") Integer id);
	
	@Modifying
	@Query("SELECT COUNT(calendardefine) > 0  FROM Calendardefine calendardefine WHERE calendardefine.id = :id AND calendardefine.idlock = :idlock")
	Boolean updateCheckLock(@Param("id") Integer id, @Param("idlock") Integer idlock);
	
	@Query("SELECT new map (calendardefine.idcreate as idcreate, calendardefine.idowner as idowner) FROM Calendardefine calendardefine WHERE calendardefine.id = :id")
	List<Map<String, Object>> getAuthorizePropertiesById(@Param("id") Integer id);

}
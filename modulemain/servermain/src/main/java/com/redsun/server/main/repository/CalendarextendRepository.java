
package com.redsun.server.main.repository;

import com.redsun.server.main.model.Calendarextend ;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface CalendarextendRepository extends JpaRepository<Calendarextend, Integer>, JpaSpecificationExecutor<Calendarextend> {

	
	@Modifying
	@Query("UPDATE Calendarextend calendarextend SET calendarextend.idlock = :idlock, calendarextend.lockdate = CURRENT_TIMESTAMP() WHERE calendarextend.id = :id AND ((calendarextend.idlock = null AND calendarextend.lockdate = null) OR (calendarextend.lockdate != null AND EXTRACT(EPOCH FROM(CURRENT_TIMESTAMP() - calendarextend.lockdate)) > :timeout))")
	Integer updateLock(@Param("id") Integer id, @Param("idlock") Integer idlock, @Param("timeout") Integer timeout);

	@Modifying
	@Query("UPDATE Calendarextend calendarextend SET calendarextend.idlock = null, calendarextend.lockdate = null WHERE calendarextend.id = :id")
	Integer updateUnlock(@Param("id") Integer id);
	
	@Modifying
	@Query("SELECT COUNT(calendarextend) > 0  FROM Calendarextend calendarextend WHERE calendarextend.id = :id AND calendarextend.idlock = :idlock")
	Boolean updateCheckLock(@Param("id") Integer id, @Param("idlock") Integer idlock);
	
	@Query("SELECT new map (calendarextend.idcreate as idcreate, calendarextend.idowner as idowner) FROM Calendarextend calendarextend WHERE calendarextend.id = :id")
	List<Map<String, Object>> getAuthorizePropertiesById(@Param("id") Integer id);

}
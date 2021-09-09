
package com.redsun.server.main.repository;

import com.redsun.server.main.model.Calendar ;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface CalendarRepository extends JpaRepository<Calendar, Integer>, JpaSpecificationExecutor<Calendar> {

	
	@Modifying
	@Query("UPDATE Calendar calendar SET calendar.idlock = :idlock, calendar.lockdate = CURRENT_TIMESTAMP() WHERE calendar.id = :id AND ((calendar.idlock = null AND calendar.lockdate = null) OR (calendar.lockdate != null AND EXTRACT(EPOCH FROM(CURRENT_TIMESTAMP() - calendar.lockdate)) > :timeout))")
	Integer updateLock(@Param("id") Integer id, @Param("idlock") Integer idlock, @Param("timeout") Integer timeout);

	@Modifying
	@Query("UPDATE Calendar calendar SET calendar.idlock = null, calendar.lockdate = null WHERE calendar.id = :id")
	Integer updateUnlock(@Param("id") Integer id);
	
	@Modifying
	@Query("SELECT COUNT(calendar) > 0  FROM Calendar calendar WHERE calendar.id = :id AND calendar.idlock = :idlock")
	Boolean updateCheckLock(@Param("id") Integer id, @Param("idlock") Integer idlock);
	
	@Query("SELECT new map (calendar.idcreate as idcreate, calendar.idowner as idowner) FROM Calendar calendar WHERE calendar.id = :id")
	List<Map<String, Object>> getAuthorizePropertiesById(@Param("id") Integer id);
	
	@Query(value="SELECT new map(calendar.id as value, 'calendar.name' as display) FROM Calendar calendar WHERE calendar.status != :deleteStatus")
	List<Map<String, Object>> listForSelect(@Param("deleteStatus") Integer deleteStatus);

}
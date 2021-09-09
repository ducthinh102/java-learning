
package com.redsun.server.wh.repository;

import com.redsun.server.wh.model.Workflow ;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface WorkflowRepository extends JpaRepository<Workflow, Integer>, JpaSpecificationExecutor<Workflow> {

	
	@Modifying
	@Query("UPDATE Workflow workflow SET workflow.idlock = :idlock, workflow.lockdate = CURRENT_TIMESTAMP() WHERE workflow.id = :id AND ((workflow.idlock = null AND workflow.lockdate = null) OR (workflow.lockdate != null AND EXTRACT(EPOCH FROM(CURRENT_TIMESTAMP() - workflow.lockdate)) > :timeout))")
	Integer updateLock(@Param("id") Integer id, @Param("idlock") Integer idlock, @Param("timeout") Integer timeout);

	@Modifying
	@Query("UPDATE Workflow workflow SET workflow.idlock = null, workflow.lockdate = null WHERE workflow.id = :id")
	Integer updateUnlock(@Param("id") Integer id);
	
	@Modifying
	@Query("SELECT COUNT(workflow) > 0  FROM Workflow workflow WHERE workflow.id = :id AND workflow.idlock = :idlock")
	Boolean updateCheckLock(@Param("id") Integer id, @Param("idlock") Integer idlock);
	
	@Query("SELECT new map (workflow.idcreate as idcreate, workflow.idowner as idowner) FROM Workflow workflow WHERE workflow.id = :id")
	List<Map<String, Object>> getAuthorizePropertiesById(@Param("id") Integer id);

}
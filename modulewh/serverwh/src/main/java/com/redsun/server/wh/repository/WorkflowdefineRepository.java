
package com.redsun.server.wh.repository;

import com.redsun.server.wh.model.Workflowdefine ;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface WorkflowdefineRepository extends JpaRepository<Workflowdefine, Integer>, JpaSpecificationExecutor<Workflowdefine> {
	
	@Modifying(clearAutomatically = true)
	@Query("UPDATE Workflowdefine workflowdefine SET workflowdefine.idlock = :idlock, workflowdefine.lockdate = CURRENT_TIMESTAMP() WHERE workflowdefine.id = :id AND ((workflowdefine.idlock = null AND workflowdefine.lockdate = null) OR (workflowdefine.lockdate != null AND EXTRACT(EPOCH FROM(CURRENT_TIMESTAMP() - workflowdefine.lockdate)) > :timeout))")
	Integer updateLock(@Param("id") Integer id, @Param("idlock") Integer idlock, @Param("timeout") Integer timeout);

	@Modifying
	@Query("UPDATE Workflowdefine workflowdefine SET workflowdefine.idlock = null, workflowdefine.lockdate = null WHERE workflowdefine.id = :id")
	Integer updateUnlock(@Param("id") Integer id);
	
	@Modifying
	@Query("SELECT COUNT(workflowdefine) > 0  FROM Workflowdefine workflowdefine WHERE workflowdefine.id = :id AND workflowdefine.idlock = :idlock")
	Boolean updateCheckLock(@Param("id") Integer id, @Param("idlock") Integer idlock);
	
	@Query("SELECT new map (workflowdefine.idcreate as idcreate, workflowdefine.idowner as idowner) FROM Workflowdefine workflowdefine WHERE workflowdefine.id = :id")
	List<Map<String, Object>> getAuthorizePropertiesById(@Param("id") Integer id);

	Workflowdefine getFirstByIdworkflowAndStepAndStatusNot(Integer idworkflow, Integer step, Integer statusDelete);

	List<Workflowdefine> findByIdworkflowAndStepGreaterThanEqualAndStatusNotOrderByStepAsc(Integer idworkflow, Integer step, Integer statusDelete);

	List<Workflowdefine> findByIdworkflowAndStatusNotOrderByStepAsc(Integer idworkflow, Integer statusDelete);

	List<Workflowdefine> findByIdworkflowAndStatusNot(Integer idworkflow, Integer statusDelete);

}
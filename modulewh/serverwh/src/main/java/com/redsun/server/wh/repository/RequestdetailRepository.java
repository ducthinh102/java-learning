
package com.redsun.server.wh.repository;

import com.redsun.server.wh.model.Requestdetail ;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface RequestdetailRepository extends JpaRepository<Requestdetail, Integer>, JpaSpecificationExecutor<Requestdetail> {

	
	@Modifying
	@Query("UPDATE Requestdetail requestdetail SET requestdetail.idlock = :idlock, requestdetail.lockdate = CURRENT_TIMESTAMP() WHERE requestdetail.id = :id AND ((requestdetail.idlock = null AND requestdetail.lockdate = null) OR (requestdetail.lockdate != null AND EXTRACT(EPOCH FROM(CURRENT_TIMESTAMP() - requestdetail.lockdate)) > :timeout))")
	Integer updateLock(@Param("id") Integer id, @Param("idlock") Integer idlock, @Param("timeout") Integer timeout);

	@Modifying
	@Query("UPDATE Requestdetail requestdetail SET requestdetail.idlock = null, requestdetail.lockdate = null WHERE requestdetail.id = :id")
	Integer updateUnlock(@Param("id") Integer id);
	
	@Modifying
	@Query("SELECT COUNT(requestdetail) > 0  FROM Requestdetail requestdetail WHERE requestdetail.id = :id AND requestdetail.idlock = :idlock")
	Boolean updateCheckLock(@Param("id") Integer id, @Param("idlock") Integer idlock);
	
	@Query("SELECT new map (requestdetail.idcreate as idcreate, requestdetail.idowner as idowner) FROM Requestdetail requestdetail WHERE requestdetail.id = :id")
	List<Map<String, Object>> getAuthorizePropertiesById(@Param("id") Integer id);
	
	@Query("SELECT COUNT(m)>0 FROM Requestdetail m WHERE m.id!=:id AND m.idrequest = :idrequest AND m.idmaterial =:idmaterial  AND m.status != 3")
	Boolean checkExitsMaterialInRequest(@Param("id")Integer id,@Param("idrequest")Integer idrequest,@Param("idmaterial")Integer idmaterial);

	@Query("SELECT COUNT(m)>0 FROM Requestdetail m WHERE  m.idrequest = :idrequest AND m.idmaterial =:idmaterial  AND m.status != 3")
	Boolean checkExitsMaterialInRequestForCreate(@Param("idrequest")Integer idrequest,@Param("idmaterial")Integer idmaterial);

	@Query("SELECT new map (requestdetail.id as id, requestdetail.idmaterial as idmaterial, requestdetail.material.name as materialname, requestdetail.version as version, requestdetail.quantity as quantity, requestdetail.softquantity as softquantity, requestdetail.workitem as workitem, requestdetail.deliverydate as deliverydate, requestdetail.drawingname as drawingname, requestdetail.teamname as teamname, requestdetail.note as note) FROM Requestdetail requestdetail WHERE requestdetail.idrequest = :id AND requestdetail.status != 3 ")
	List<Map<String, Object>> getAllById(@Param("id") Integer id);
	
	@Query("SELECT requestdetail FROM Requestdetail requestdetail WHERE requestdetail.id =:id")
	Requestdetail getByIdForView(@Param("id")Integer id);
	
	@Modifying
	@Query("UPDATE Requestdetail requestdetail SET requestdetail.quantity =:quantity, requestdetail.idupdate =:idupdate ,requestdetail.updatedate = CURRENT_TIMESTAMP(), requestdetail.version = requestdetail.version+1, requestdetail.status = 2  WHERE requestdetail.id =:id AND requestdetail.status!=3")
	Integer updateQuantityById(@Param("id") Integer id, @Param("quantity")Integer quantity, @Param("idupdate") Integer idupdate );	
}
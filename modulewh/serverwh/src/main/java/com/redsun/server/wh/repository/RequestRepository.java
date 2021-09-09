
package com.redsun.server.wh.repository;

import com.redsun.server.wh.model.Request ;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface RequestRepository extends JpaRepository<Request, Integer>, JpaSpecificationExecutor<Request> {

	@Modifying
	@Query("UPDATE Requestdetail request SET request.status = 3, request.iddelete = :iddelete, request.deletedate = CURRENT_TIMESTAMP(), request.version = request.version + 1 WHERE request.idrequest = :idrequest")
	Integer updateForDeleteByIdRequest(@Param("iddelete") Integer iddelete, @Param("idrequest") Integer idrequest);
	
	@Query(value = "Select count(request)>0 FROM Request request Where request.id != :id AND request.code = :code AND request.scope = :scope")
	Boolean isExistCode(@Param("id") Integer id, @Param("code") String code, @Param("scope") String scope);
	
	@Modifying(clearAutomatically = true)
	@Query("UPDATE Request request SET request.idlock = :idlock, request.lockdate = CURRENT_TIMESTAMP() WHERE request.id = :id AND ((request.idlock = null AND request.lockdate = null) OR (request.lockdate != null AND EXTRACT(EPOCH FROM(CURRENT_TIMESTAMP() - request.lockdate)) > :timeout))")
	Integer updateLock(@Param("id") Integer id, @Param("idlock") Integer idlock, @Param("timeout") Integer timeout);

	@Modifying
	@Query("UPDATE Request request SET request.idlock = null, request.lockdate = null WHERE request.id = :id")
	Integer updateUnlock(@Param("id") Integer id);
	
	@Modifying
	@Query("SELECT COUNT(request) > 0  FROM Request request WHERE request.id = :id AND request.idlock = :idlock")
	Boolean updateCheckLock(@Param("id") Integer id, @Param("idlock") Integer idlock);
	
	@Query("SELECT new map (request.idcreate as idcreate, request.idowner as idowner) FROM Request request WHERE request.id = :id")
	List<Map<String, Object>> getAuthorizePropertiesById(@Param("id") Integer id);
	
	@Query("SELECT new map (request.id as value, request.code as display) FROM Request request WHERE request.status != 3 ")
	List<Map<String, Object>> listForSelect();
	
	@Query("SELECT request FROM Request request WHERE request.id =:id")
	Request getByIdForView(@Param("id")Integer id);
	
	Page<Request> findByIdIn(List<Integer> ids, Pageable pageable);

}
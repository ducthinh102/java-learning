
package com.redsun.server.main.repository;

import com.redsun.server.main.model.Attachment ;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Integer>, JpaSpecificationExecutor<Attachment> {

	
	@Modifying
	@Query("UPDATE Attachment attachment SET attachment.idlock = :idlock, attachment.lockdate = CURRENT_TIMESTAMP() WHERE attachment.id = :id AND ((attachment.idlock = null AND attachment.lockdate = null) OR (attachment.lockdate != null AND EXTRACT(EPOCH FROM(CURRENT_TIMESTAMP() - attachment.lockdate)) > :timeout))")
	Integer updateLock(@Param("id") Integer id, @Param("idlock") Integer idlock, @Param("timeout") Integer timeout);

	@Modifying
	@Query("UPDATE Attachment attachment SET attachment.idlock = null, attachment.lockdate = null WHERE attachment.id = :id")
	Integer updateUnlock(@Param("id") Integer id);
	
	@Modifying
	@Query("SELECT COUNT(attachment) > 0  FROM Attachment attachment WHERE attachment.id = :id AND attachment.idlock = :idlock")
	Boolean updateCheckLock(@Param("id") Integer id, @Param("idlock") Integer idlock);
	
	@Query("SELECT new map (attachment.idcreate as idcreate, attachment.idowner as idowner) FROM Attachment attachment WHERE attachment.id = :id")
	List<Map<String, Object>> getAuthorizePropertiesById(@Param("id") Integer id);

}
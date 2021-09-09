
package com.redsun.server.main.repository;

import com.redsun.server.main.model.Comment ;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface CommentRepository extends JpaRepository<Comment, Integer>, JpaSpecificationExecutor<Comment> {

	
	@Modifying
	@Query("UPDATE Comment comment SET comment.idlock = :idlock, comment.lockdate = CURRENT_TIMESTAMP() WHERE comment.id = :id AND ((comment.idlock = null AND comment.lockdate = null) OR (comment.lockdate != null AND EXTRACT(EPOCH FROM(CURRENT_TIMESTAMP() - comment.lockdate)) > :timeout))")
	Integer updateLock(@Param("id") Integer id, @Param("idlock") Integer idlock, @Param("timeout") Integer timeout);

	@Modifying
	@Query("UPDATE Comment comment SET comment.idlock = null, comment.lockdate = null WHERE comment.id = :id")
	Integer updateUnlock(@Param("id") Integer id);
	
	@Modifying
	@Query("SELECT COUNT(comment) > 0  FROM Comment comment WHERE comment.id = :id AND comment.idlock = :idlock")
	Boolean updateCheckLock(@Param("id") Integer id, @Param("idlock") Integer idlock);
	
	@Query("SELECT new map (comment.idcreate as idcreate, comment.idowner as idowner) FROM Comment comment WHERE comment.id = :id")
	List<Map<String, Object>> getAuthorizePropertiesById(@Param("id") Integer id);

}
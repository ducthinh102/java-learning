
package com.redsun.server.main.repository;

import com.redsun.server.main.model.User ;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface UserRepository extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {

	
	@Modifying
	@Query("UPDATE User user SET user.idlock = :idlock, user.lockdate = CURRENT_TIMESTAMP() WHERE user.id = :id AND ((user.idlock = null AND user.lockdate = null) OR (user.lockdate != null AND EXTRACT(EPOCH FROM(CURRENT_TIMESTAMP() - user.lockdate)) > :timeout))")
	Integer updateLock(@Param("id") Integer id, @Param("idlock") Integer idlock, @Param("timeout") Integer timeout);

	@Modifying
	@Query("UPDATE User user SET user.idlock = null, user.lockdate = null WHERE user.id = :id")
	Integer updateUnlock(@Param("id") Integer id);
	
	@Modifying
	@Query("SELECT COUNT(user) > 0  FROM User user WHERE user.id = :id AND user.idlock = :idlock")
	Boolean updateCheckLock(@Param("id") Integer id, @Param("idlock") Integer idlock);
	
	@Query("SELECT new map (user.idcreate as idcreate, user.idowner as idowner) FROM User user WHERE user.id = :id")
	List<Map<String, Object>> getAuthorizePropertiesById(@Param("id") Integer id);
	
	@Query(value="SELECT COUNT(u) > 0  FROM User u Where u.id != :id AND u.username = :username")
	Boolean isExistUsername(@Param("id") Integer id, @Param("username") String username);
	
	User findByUsername(String username);
	
	@Query(value="SELECT new map(u.id as id, (CASE WHEN u.firstname!=null AND u.lastname!=null THEN CONCAT(u.firstname ,' ',u.lastname,' (',u.username,')') ELSE u.username END) as display) FROM User u WHERE u.status != 3")
	List<Map<String, Object>> listAllForSelect();
	
	@Query(value="SELECT new map(u.id as id, u.username as display) FROM User u WHERE u.status != 3 AND u.id IN :ids")
	List<Map<String, Object>> listForSelectByIds(@Param("ids") List<Integer> ids);

}
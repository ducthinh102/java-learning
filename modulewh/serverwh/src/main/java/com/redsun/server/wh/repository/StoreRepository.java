
package com.redsun.server.wh.repository;

import java.util.List;
import java.util.Map;

import com.redsun.server.wh.model.Store ;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface StoreRepository extends JpaRepository<Store, Integer>, JpaSpecificationExecutor<Store> {

	@Query(value="SELECT COUNT(s) > 0  FROM Store s WHERE s.id != :id AND s.code = :code AND s.status != 3")
	Boolean isExistCode(@Param("id") Integer id, @Param("code") String code);

	@Query(value="SELECT COUNT(s) > 0  FROM Store s WHERE s.id != :id AND s.name = :name AND s.status != 3")
	Boolean isExistName(@Param("id") Integer id, @Param("name") String name);

	@Modifying
	@Query("UPDATE Store store SET store.idlock = :idlock, store.lockdate = CURRENT_TIMESTAMP() WHERE store.id = :id AND ((store.idlock = null AND store.lockdate = null) OR (store.lockdate != null AND EXTRACT(EPOCH FROM(CURRENT_TIMESTAMP() - store.lockdate)) > :timeout))")
	Integer updateLock(@Param("id") Integer id, @Param("idlock") Integer idlock, @Param("timeout") Integer timeout);

	@Modifying
	@Query("UPDATE Store store SET store.idlock = null, store.lockdate = null WHERE store.id = :id")
	Integer updateUnlock(@Param("id") Integer id);
	
	@Modifying
	@Query("SELECT COUNT(store) > 0  FROM Store store WHERE store.id = :id AND store.idlock = :idlock")
	Boolean updateCheckLock(@Param("id") Integer id, @Param("idlock") Integer idlock);
	
	@Query("SELECT new map (store.idcreate as idcreate, store.idowner as idowner) FROM Store store WHERE store.id = :id")
	List<Map<String, Object>> getAuthorizePropertiesById(@Param("id") Integer id);
	
	@Query(value="SELECT new map(store.id as value, store.name as display) FROM Store store WHERE store.status != 3")
	List<Map<String, Object>> listForSelect();

	
}
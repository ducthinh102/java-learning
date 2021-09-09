
package com.redsun.server.wh.repository;

import com.redsun.server.wh.model.Materialstore ;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface MaterialstoreRepository extends JpaRepository<Materialstore, Integer>, JpaSpecificationExecutor<Materialstore> {

	@Modifying
	@Query("UPDATE Materialstore materialstore SET materialstore.idlock = :idlock, materialstore.lockdate = CURRENT_TIMESTAMP() WHERE materialstore.id = :id AND ((materialstore.idlock = null AND materialstore.lockdate = null) OR (materialstore.lockdate != null AND EXTRACT(EPOCH FROM(CURRENT_TIMESTAMP() - materialstore.lockdate)) > :timeout))")
	Integer updateLock(@Param("id") Integer id, @Param("idlock") Integer idlock, @Param("timeout") Integer timeout);

	@Modifying
	@Query("UPDATE Materialstore materialstore SET materialstore.idlock = null, materialstore.lockdate = null WHERE materialstore.id = :id")
	Integer updateUnlock(@Param("id") Integer id);
	
	@Modifying
	@Query("SELECT COUNT(materialstore) > 0  FROM Materialstore materialstore WHERE materialstore.id = :id AND materialstore.idlock = :idlock")
	Boolean updateCheckLock(@Param("id") Integer id, @Param("idlock") Integer idlock);
	
	@Query("SELECT new map (materialstore.idcreate as idcreate, materialstore.idowner as idowner) FROM Materialstore materialstore WHERE materialstore.id = :id")
	List<Map<String, Object>> getAuthorizePropertiesById(@Param("id") Integer id);
	
	@Query("SELECT new map (m.quantity as quantity, m.id as id, m.version as version) FROM Materialstore m WHERE m.idstore =:idstore AND m.idmaterial =:idmaterial")
	Map<String, Object> quantityMaterial(@Param("idstore")Integer idstore,@Param("idmaterial")Integer idmaterial);
	
	@Query("SELECT m FROM Materialstore m WHERE m.idstore =:idstore AND m.idmaterial =:idmaterial AND m.status != 3")
	Materialstore getMaterial(@Param("idstore")Integer idstore,@Param("idmaterial")Integer idmaterial);
	
	@Modifying
	@Query("UPDATE Materialstore m SET m.quantity = m.quantity - :quantity, m.status = 2, m.version = m.version + 1, m.updatedate = CURRENT_TIMESTAMP()  WHERE m.idstore =:idstore AND m.idmaterial =:idmaterial AND m.status !=3")
	Integer removeMaterial(@Param("idstore")Integer idstore,@Param("idmaterial")Integer idmaterial,@Param("quantity")Integer quantity);
	
	@Modifying
	@Query("UPDATE Materialstore m SET m.quantity = m.quantity + :quantity, m.status = 2, m.version = m.version + 1, m.updatedate = CURRENT_TIMESTAMP() WHERE m.idstore =:idstore AND m.idmaterial =:idmaterial AND m.status !=3")
	Integer addMaterial(@Param("idstore")Integer idstore,@Param("idmaterial")Integer idmaterial,@Param("quantity")Integer quantity);
	
	@Modifying
	@Query("UPDATE Materialstore m SET m.quantity = m.quantity - :quantity, m.status = 2, m.version = m.version + 1 WHERE m.idstore =:idstore AND m.idmaterial =:idmaterial AND m.status !=3")
	Integer subMaterial(@Param("idstore")Integer idstore,@Param("idmaterial")Integer idmaterial,@Param("quantity")Integer quantity);
	
	@Modifying
	@Query("UPDATE Materialstore m SET m.quantity = m.quantity + :quantity, m.status = 2, m.version = m.version + 1  WHERE m.idstore =:idstore AND m.idmaterial =:idmaterial AND m.status !=3")
	Integer returnMaterial(@Param("idstore")Integer idstore,@Param("idmaterial")Integer idmaterial,@Param("quantity")Integer quantity);
	
	@Query("SELECT SUM(m.quantity) FROM Materialstore m WHERE m.idstore = :idstore AND m.status != 3")
	Integer storequantity(@Param("idstore")Integer idstore);
	
	@Query("SELECT new map (m.quantity as quantity, m.idmaterial as idmaterial) FROM Materialstore m WHERE m.idstore =:idstore AND m.idmaterial =:idmaterial AND m.status!=3")
	Map<String, Object> checkQuantityMaterial(@Param("idstore")Integer idstore,@Param("idmaterial")Integer idmaterial);
	
}
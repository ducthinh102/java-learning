
package com.redsun.server.wh.repository;

import com.redsun.server.wh.model.Materialform ;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface MaterialformRepository extends JpaRepository<Materialform, Integer>, JpaSpecificationExecutor<Materialform> {

	
	@Modifying(clearAutomatically = true)
	@Query("UPDATE Materialform materialform SET materialform.idlock = :idlock, materialform.lockdate = CURRENT_TIMESTAMP() WHERE materialform.id = :id AND ((materialform.idlock = null AND materialform.lockdate = null) OR (materialform.lockdate != null AND EXTRACT(EPOCH FROM(CURRENT_TIMESTAMP() - materialform.lockdate)) > :timeout))")
	Integer updateLock(@Param("id") Integer id, @Param("idlock") Integer idlock, @Param("timeout") Integer timeout);

	@Modifying
	@Query("UPDATE Materialform materialform SET materialform.idlock = null, materialform.lockdate = null WHERE materialform.id = :id")
	Integer updateUnlock(@Param("id") Integer id);
	
	@Modifying
	@Query("SELECT COUNT(materialform) > 0  FROM Materialform materialform WHERE materialform.id = :id AND materialform.idlock = :idlock")
	Boolean updateCheckLock(@Param("id") Integer id, @Param("idlock") Integer idlock);
	
	@Query("SELECT new map (materialform.idcreate as idcreate, materialform.idowner as idowner) FROM Materialform materialform WHERE materialform.id = :id")
	List<Map<String, Object>> getAuthorizePropertiesById(@Param("id") Integer id);

	@Modifying
	@Query("UPDATE Materialformdetail materialform SET materialform.status = 3, materialform.iddelete = :iddelete, materialform.deletedate = CURRENT_TIMESTAMP(), materialform.version = materialform.version + 1 WHERE materialform.idmaterialform = :idmaterialform")
	Integer updateForDeleteByIdMaterialform(@Param("iddelete") Integer iddelete, @Param("idmaterialform") Integer idmaterialform);
	
	@Query(value = "Select count(materialform)>0 FROM Materialform materialform Where materialform.id != :id AND materialform.code = :code AND materialform.scope = :scope")
	Boolean isExistCode(@Param("id") Integer id, @Param("code") String code, @Param("scope") String scope);
	
	@Query(value="SELECT COUNT(materialform) > 0  FROM Materialform materialform Where materialform.id != :id AND materialform.name = :name AND materialform.scope = :scope")
	Boolean isExistName(@Param("id") Integer id, @Param("name") String name, @Param("scope") String scope);
	
	@Modifying
	@Query("UPDATE Materialform materialform SET materialform.totalamount = :totalAmount WHERE materialform.id = :id")
	Integer updateTotalAmount(@Param("id") Integer id, @Param("totalAmount") double totalAmount);
	
	@Query("SELECT new map (materialform.id as value, materialform.code as display) FROM Materialform materialform WHERE materialform.scope = :scope AND materialform.status != 3 ")
	List<Map<String, Object>> listForSelect(@Param("scope") String scope);
}
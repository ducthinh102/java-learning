
package com.redsun.server.wh.repository;

import com.redsun.server.wh.model.Materialbaseline ;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface MaterialbaselineRepository extends JpaRepository<Materialbaseline, Integer>, JpaSpecificationExecutor<Materialbaseline> {

	@Modifying
	@Query("UPDATE Materialbaselinedetail materialbaseline SET materialbaseline.status = 3, materialbaseline.iddelete = :iddelete, materialbaseline.deletedate = CURRENT_TIMESTAMP(), materialbaseline.version = materialbaseline.version + 1 WHERE materialbaseline.idmaterialbaseline = :idmaterialbaseline")
	Integer updateForDeleteByIdMaterialbaseline(@Param("iddelete") Integer iddelete, @Param("idmaterialbaseline") Integer idmaterialbaseline);
	
	@Modifying(clearAutomatically = true)
	@Query("UPDATE Materialbaseline materialbaseline SET materialbaseline.idlock = :idlock, materialbaseline.lockdate = CURRENT_TIMESTAMP() WHERE materialbaseline.id = :id AND ((materialbaseline.idlock = null AND materialbaseline.lockdate = null) OR (materialbaseline.lockdate != null AND EXTRACT(EPOCH FROM(CURRENT_TIMESTAMP() - materialbaseline.lockdate)) > :timeout))")
	Integer updateLock(@Param("id") Integer id, @Param("idlock") Integer idlock, @Param("timeout") Integer timeout);

	@Modifying
	@Query("UPDATE Materialbaseline materialbaseline SET materialbaseline.idlock = null, materialbaseline.lockdate = null WHERE materialbaseline.id = :id")
	Integer updateUnlock(@Param("id") Integer id);
	
	@Modifying
	@Query("SELECT COUNT(materialbaseline) > 0  FROM Materialbaseline materialbaseline WHERE materialbaseline.id = :id AND materialbaseline.idlock = :idlock")
	Boolean updateCheckLock(@Param("id") Integer id, @Param("idlock") Integer idlock);
	
	@Query("SELECT new map (materialbaseline.idcreate as idcreate, materialbaseline.idowner as idowner) FROM Materialbaseline materialbaseline WHERE materialbaseline.id = :id")
	List<Map<String, Object>> getAuthorizePropertiesById(@Param("id") Integer id);
	
	@Query(value = "Select count(materialbaseline)>0 FROM Materialbaseline materialbaseline Where materialbaseline.id != :id AND materialbaseline.code = :code")
	Boolean isExistCode(@Param("id") Integer id, @Param("code") String code);
	
	@Query("SELECT new map (materialbaseline.id as value, materialbaseline.code as display) FROM Materialbaseline materialbaseline WHERE materialbaseline.status != 3 ")
	List<Map<String, Object>> listForSelect();
	
	@Modifying
	@Query("UPDATE Materialbaseline materialbaseline SET materialbaseline.totalamount = :totalAmount WHERE materialbaseline.id = :id")
	Integer updateTotalAmount(@Param("id") Integer id, @Param("totalAmount") double totalAmount);
}
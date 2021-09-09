
package com.redsun.server.wh.repository;

import com.redsun.server.wh.model.Materialformdetail ;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface MaterialformdetailRepository extends JpaRepository<Materialformdetail, Integer>, JpaSpecificationExecutor<Materialformdetail> {

	
	@Modifying
	@Query("UPDATE Materialformdetail materialformdetail SET materialformdetail.idlock = :idlock, materialformdetail.lockdate = CURRENT_TIMESTAMP() WHERE materialformdetail.id = :id AND ((materialformdetail.idlock = null AND materialformdetail.lockdate = null) OR (materialformdetail.lockdate != null AND EXTRACT(EPOCH FROM(CURRENT_TIMESTAMP() - materialformdetail.lockdate)) > :timeout))")
	Integer updateLock(@Param("id") Integer id, @Param("idlock") Integer idlock, @Param("timeout") Integer timeout);

	@Modifying
	@Query("UPDATE Materialformdetail materialformdetail SET materialformdetail.idlock = null, materialformdetail.lockdate = null WHERE materialformdetail.id = :id")
	Integer updateUnlock(@Param("id") Integer id);
	
	@Modifying
	@Query("SELECT COUNT(materialformdetail) > 0  FROM Materialformdetail materialformdetail WHERE materialformdetail.id = :id AND materialformdetail.idlock = :idlock")
	Boolean updateCheckLock(@Param("id") Integer id, @Param("idlock") Integer idlock);
	
	@Query("SELECT new map (materialformdetail.idcreate as idcreate, materialformdetail.idowner as idowner) FROM Materialformdetail materialformdetail WHERE materialformdetail.id = :id")
	List<Map<String, Object>> getAuthorizePropertiesById(@Param("id") Integer id);

	@Query(value = "SELECT COALESCE(SUM(materialformdetail.amount), 0) FROM Materialformdetail materialformdetail WHERE materialformdetail.status != 3 AND materialformdetail.idmaterialform = :idmaterialform")
	Double sumAmount(@Param("idmaterialform") Integer idmaterialform);
	
	@Query("SELECT COUNT(materialformdetail)>0 FROM Materialformdetail materialformdetail WHERE materialformdetail.id!=:id AND materialformdetail.idmaterialform = :idmaterialform AND materialformdetail.idmaterial =:idmaterial  AND materialformdetail.status != 3")
	Boolean checkExitsMaterialInMaterialform(@Param("id")Integer id,@Param("idmaterialform")Integer idmaterialform,@Param("idmaterial")Integer idmaterial);

	@Query("SELECT COUNT(materialformdetail)>0 FROM Materialformdetail materialformdetail WHERE materialformdetail.idmaterialform = :idmaterialform AND materialformdetail.idmaterial =:idmaterial AND materialformdetail.status != 3")
	Boolean checkExitsMaterialInMaterialformForCreate(@Param("idmaterialform")Integer idmaterialform,@Param("idmaterial")Integer idmaterial);
	
	@Query("SELECT new map (materialformdetail.idmaterial as idmaterial, materialformdetail.material.name as materialname,materialformdetail.idunit as idunit, materialformdetail.price as price, materialformdetail.quantity as quantity, materialformdetail.amount as amount, materialformdetail.startdate as startdate, materialformdetail.enddate as enddate, materialformdetail.note as note, materialformdetail.id as id, materialformdetail.version as version) FROM Materialformdetail materialformdetail WHERE materialformdetail.idmaterialform = :id AND materialformdetail.status != 3 ")
	List<Map<String, Object>> getAllById(@Param("id") Integer id);
	
	@Query("SELECT materialformdetail FROM Materialformdetail materialformdetail WHERE materialformdetail.id = :id AND materialformdetail.status != 3 ")
	Materialformdetail getById(@Param("id") Integer id);
	
	@Modifying
	@Query("UPDATE Materialformdetail materialformdetail SET materialformdetail.price =:price, materialformdetail.amount = materialformdetail.quantity*:price, materialformdetail.idupdate =:idupdate ,materialformdetail.updatedate = CURRENT_TIMESTAMP(), materialformdetail.version = materialformdetail.version+1, materialformdetail.status = 2  WHERE materialformdetail.id =:id AND materialformdetail.status!=3")
	Integer updatePriceById(@Param("id") Integer id, @Param("price")Double price, @Param("idupdate") Integer idupdate );
	
	@Modifying
	@Query("UPDATE Materialformdetail materialformdetail SET materialformdetail.quantity =:quantity, materialformdetail.amount = materialformdetail.price*:quantity, materialformdetail.idupdate =:idupdate ,materialformdetail.updatedate = CURRENT_TIMESTAMP(), materialformdetail.version = materialformdetail.version+1, materialformdetail.status = 2  WHERE materialformdetail.id =:id AND materialformdetail.status!=3")
	Integer updateQuantityById(@Param("id") Integer id, @Param("quantity")Integer quantity, @Param("idupdate") Integer idupdate );	

	@Modifying
	@Query("UPDATE Materialform materialform SET materialform.idref = :idref, materialform.reftype = :reftype WHERE materialform.id = :id")
	Integer updateRef(@Param("id") Integer id, @Param("idref") Integer idref, @Param("reftype") String reftype);
	
}
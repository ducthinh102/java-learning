
package com.redsun.server.wh.repository;

import com.redsun.server.wh.model.Materialbaselinedetail ;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface MaterialbaselinedetailRepository extends JpaRepository<Materialbaselinedetail, Integer>, JpaSpecificationExecutor<Materialbaselinedetail> {

	
	@Modifying
	@Query("UPDATE Materialbaselinedetail materialbaselinedetail SET materialbaselinedetail.idlock = :idlock, materialbaselinedetail.lockdate = CURRENT_TIMESTAMP() WHERE materialbaselinedetail.id = :id AND ((materialbaselinedetail.idlock = null AND materialbaselinedetail.lockdate = null) OR (materialbaselinedetail.lockdate != null AND EXTRACT(EPOCH FROM(CURRENT_TIMESTAMP() - materialbaselinedetail.lockdate)) > :timeout))")
	Integer updateLock(@Param("id") Integer id, @Param("idlock") Integer idlock, @Param("timeout") Integer timeout);

	@Modifying
	@Query("UPDATE Materialbaselinedetail materialbaselinedetail SET materialbaselinedetail.idlock = null, materialbaselinedetail.lockdate = null WHERE materialbaselinedetail.id = :id")
	Integer updateUnlock(@Param("id") Integer id);
	
	@Modifying
	@Query("SELECT COUNT(materialbaselinedetail) > 0  FROM Materialbaselinedetail materialbaselinedetail WHERE materialbaselinedetail.id = :id AND materialbaselinedetail.idlock = :idlock")
	Boolean updateCheckLock(@Param("id") Integer id, @Param("idlock") Integer idlock);
	
	@Query("SELECT new map (materialbaselinedetail.idcreate as idcreate, materialbaselinedetail.idowner as idowner) FROM Materialbaselinedetail materialbaselinedetail WHERE materialbaselinedetail.id = :id")
	List<Map<String, Object>> getAuthorizePropertiesById(@Param("id") Integer id);
	
	@Query("SELECT COUNT(m)>0 FROM Materialbaselinedetail m WHERE  m.idmaterialbaseline = :idmaterialbaseline AND m.idmaterial =:idmaterial  AND m.status != 3")
	Boolean checkExitsMaterialInMaterialbaselineForCreate(@Param("idmaterialbaseline")Integer idmaterialbaseline,@Param("idmaterial")Integer idmaterial);
	
	@Query("SELECT COUNT(m)>0 FROM Materialbaselinedetail m WHERE m.id!=:id AND m.idmaterialbaseline = :idmaterialbaseline AND m.idmaterial =:idmaterial  AND m.status != 3")
	Boolean checkExitsMaterialInMaterialbaseline(@Param("id")Integer id,@Param("idmaterialbaseline")Integer idmaterialbaseline,@Param("idmaterial")Integer idmaterial);
	
	@Query(value = "SELECT COALESCE(SUM(materialbaselinedetail.amount), 0) FROM Materialbaselinedetail materialbaselinedetail WHERE materialbaselinedetail.status != 3 AND materialbaselinedetail.idmaterialbaseline = :idmaterialbaseline")
	Double sumAmount(@Param("idmaterialbaseline") Integer idmaterialbaseline);
	
	@Query("SELECT new map (materialbaselinedetail.idsupplier as idsupplier,materialbaselinedetail.material.name as materialname,materialbaselinedetail.idmaterial as idmaterial,materialbaselinedetail.idunit as idunit,materialbaselinedetail.price as price,materialbaselinedetail.quantity as quantity,materialbaselinedetail.amount as amount,materialbaselinedetail.startdate as startdate,materialbaselinedetail.enddate as enddate,materialbaselinedetail.note as note,materialbaselinedetail.version as version,materialbaselinedetail.id as id ) FROM Materialbaselinedetail materialbaselinedetail WHERE materialbaselinedetail.idmaterialbaseline = :id AND materialbaselinedetail.status != 3 ")
	List<Map<String, Object>> getAllById(@Param("id") Integer id);
	
	@Query("SELECT materialbaselinedetail.idmaterial FROM Materialbaselinedetail materialbaselinedetail WHERE materialbaselinedetail.materialbaseline.store.id = :idstore")
	List<Integer> listIdmaterialByIdstore(@Param("idstore") Integer idstore);
	
	@Modifying
	@Query("UPDATE Materialbaselinedetail materialbaselinedetail SET materialbaselinedetail.price =:price, materialbaselinedetail.amount = materialbaselinedetail.quantity*:price, materialbaselinedetail.idupdate =:idupdate ,materialbaselinedetail.updatedate = CURRENT_TIMESTAMP(), materialbaselinedetail.version = materialbaselinedetail.version+1, materialbaselinedetail.status = 2  WHERE materialbaselinedetail.id =:id AND materialbaselinedetail.status!=3")
	Integer updatePriceById(@Param("id") Integer id, @Param("price")Double price, @Param("idupdate") Integer idupdate );
	
	@Modifying
	@Query("UPDATE Materialbaselinedetail materialbaselinedetail SET materialbaselinedetail.quantity =:quantity, materialbaselinedetail.amount = materialbaselinedetail.price*:quantity, materialbaselinedetail.idupdate =:idupdate ,materialbaselinedetail.updatedate = CURRENT_TIMESTAMP(), materialbaselinedetail.version = materialbaselinedetail.version+1, materialbaselinedetail.status = 2  WHERE materialbaselinedetail.id =:id AND materialbaselinedetail.status!=3")
	Integer updateQuantityById(@Param("id") Integer id, @Param("quantity")Integer quantity, @Param("idupdate") Integer idupdate );
	
	@Query("SELECT materialbaselinedetail FROM Materialbaselinedetail materialbaselinedetail WHERE materialbaselinedetail.id = :id AND materialbaselinedetail.status != 3 ")
	Materialbaselinedetail getById(@Param("id") Integer id);
}
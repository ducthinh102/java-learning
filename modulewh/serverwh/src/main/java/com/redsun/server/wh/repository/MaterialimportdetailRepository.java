
package com.redsun.server.wh.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.redsun.server.wh.model.Materialimportdetail ;



@Repository
public interface MaterialimportdetailRepository extends JpaRepository<Materialimportdetail, Integer>, JpaSpecificationExecutor<Materialimportdetail> {

	
	@Modifying
	@Query("UPDATE Materialimportdetail materialimportdetail SET materialimportdetail.idlock = :idlock, materialimportdetail.lockdate = CURRENT_TIMESTAMP() WHERE materialimportdetail.id = :id AND ((materialimportdetail.idlock = null AND materialimportdetail.lockdate = null) OR (materialimportdetail.lockdate != null AND EXTRACT(EPOCH FROM(CURRENT_TIMESTAMP() - materialimportdetail.lockdate)) > :timeout))")
	Integer updateLock(@Param("id") Integer id, @Param("idlock") Integer idlock, @Param("timeout") Integer timeout);

	@Modifying
	@Query("UPDATE Materialimportdetail materialimportdetail SET materialimportdetail.idlock = null, materialimportdetail.lockdate = null WHERE materialimportdetail.id = :id")
	Integer updateUnlock(@Param("id") Integer id);
	
	@Modifying
	@Query("SELECT COUNT(materialimportdetail) > 0  FROM Materialimportdetail materialimportdetail WHERE materialimportdetail.id = :id AND materialimportdetail.idlock = :idlock")
	Boolean updateCheckLock(@Param("id") Integer id, @Param("idlock") Integer idlock);
	
	@Query("SELECT new map (materialimportdetail.idcreate as idcreate, materialimportdetail.idowner as idowner) FROM Materialimportdetail materialimportdetail WHERE materialimportdetail.id = :id")
	List<Map<String, Object>> getAuthorizePropertiesById(@Param("id") Integer id);
	
	@Query("SELECT COUNT(m)>0 FROM Materialimportdetail m WHERE m.id!=:id AND m.idmaterialimport = :idmaterialimport AND m.idmaterial =:idmaterial AND m.status != 3")
	Boolean checkExitsMaterial(@Param("id")Integer id,@Param("idmaterialimport")Integer idmaterialimport,@Param("idmaterial")Integer idmaterial);
	
	@Query("SELECT new map (m.quantity as quantity, m.id as id, m.version as version) FROM Materialimportdetail m WHERE m.idmaterialimport =:idmaterialimport AND m.idmaterial =:idmaterial AND m.status != 3")
	Map<String, Object> quantityMaterial(@Param("idmaterialimport")Integer idmaterialimport,@Param("idmaterial")Integer idmaterial);
	
	@Query("SELECT m FROM Materialimportdetail m WHERE m.id =:id")
	Materialimportdetail getByIdForView(@Param("id")Integer id);
	
	@Query("SELECT m FROM Materialimportdetail m WHERE m.idmaterialimport = :idmaterialimport AND m.status != 3")
	List<Materialimportdetail> listMaterialByIdMaterialImport(@Param("idmaterialimport")Integer idmaterialimport);
	
	@Query("SELECT new map (materialimportdetail.id as id, materialimportdetail.idmaterialimport as idmaterialimport, materialimportdetail.idmaterial as idmaterial, materialimportdetail.material.name as materialname, materialimportdetail.version as version,  materialimportdetail.quantity as quantity, materialimportdetail.price as price, materialimportdetail.amount as amount, materialimportdetail.idunit as idunit, materialimportdetail.note as note) FROM Materialimportdetail materialimportdetail WHERE materialimportdetail.idmaterialimport = :id AND materialimportdetail.status != 3 ")
	List<Map<String, Object>> getAllById(@Param("id") Integer id);
	
	@Modifying
	@Query("UPDATE Materialimportdetail materialimportdetail SET materialimportdetail.price =:price, materialimportdetail.amount = materialimportdetail.quantity*:price, materialimportdetail.idupdate =:idupdate ,materialimportdetail.updatedate = CURRENT_TIMESTAMP(), materialimportdetail.version = materialimportdetail.version+1, materialimportdetail.status = 2  WHERE materialimportdetail.id =:id AND materialimportdetail.status!=3")
	Integer updatePriceById(@Param("id") Integer id, @Param("price")Double price, @Param("idupdate") Integer idupdate );
	
	@Modifying
	@Query("UPDATE Materialimportdetail materialimportdetail SET materialimportdetail.quantity =:quantity, materialimportdetail.amount = materialimportdetail.price*:quantity, materialimportdetail.idupdate =:idupdate ,materialimportdetail.updatedate = CURRENT_TIMESTAMP(), materialimportdetail.version = materialimportdetail.version+1, materialimportdetail.status = 2  WHERE materialimportdetail.id =:id AND materialimportdetail.status!=3")
	Integer updateQuantityById(@Param("id") Integer id, @Param("quantity")Integer quantity, @Param("idupdate") Integer idupdate );
}

package com.redsun.server.wh.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.redsun.server.wh.model.Materialexportdetail;


@Repository
public interface MaterialexportdetailRepository extends JpaRepository<Materialexportdetail, Integer>, JpaSpecificationExecutor<Materialexportdetail> {

	
	@Modifying
	@Query("UPDATE Materialexportdetail materialexportdetail SET materialexportdetail.idlock = :idlock, materialexportdetail.lockdate = CURRENT_TIMESTAMP() WHERE materialexportdetail.id = :id AND ((materialexportdetail.idlock = null AND materialexportdetail.lockdate = null) OR (materialexportdetail.lockdate != null AND EXTRACT(EPOCH FROM(CURRENT_TIMESTAMP() - materialexportdetail.lockdate)) > :timeout))")
	Integer updateLock(@Param("id") Integer id, @Param("idlock") Integer idlock, @Param("timeout") Integer timeout);

	@Modifying
	@Query("UPDATE Materialexportdetail materialexportdetail SET materialexportdetail.idlock = null, materialexportdetail.lockdate = null WHERE materialexportdetail.id = :id")
	Integer updateUnlock(@Param("id") Integer id);
	
	@Modifying
	@Query("SELECT COUNT(materialexportdetail) > 0  FROM Materialexportdetail materialexportdetail WHERE materialexportdetail.id = :id AND materialexportdetail.idlock = :idlock")
	Boolean updateCheckLock(@Param("id") Integer id, @Param("idlock") Integer idlock);
	
	@Query("SELECT new map (materialexportdetail.idcreate as idcreate, materialexportdetail.idowner as idowner) FROM Materialexportdetail materialexportdetail WHERE materialexportdetail.id = :id")
	List<Map<String, Object>> getAuthorizePropertiesById(@Param("id") Integer id);
	
	@Query("SELECT COUNT(m)>0 FROM Materialexportdetail m WHERE m.id!=:id AND m.idmaterialexport = :idmaterialexport AND m.idmaterial =:idmaterial AND m.status != 3")
	Boolean checkExitsMaterial(@Param("id")Integer id,@Param("idmaterialexport")Integer idmaterialexport,@Param("idmaterial")Integer idmaterial);
	
	@Query("SELECT new map (m.quantity as quantity, m.id as id, m.version as version) FROM Materialexportdetail m WHERE m.idmaterialexport =:idmaterialexport AND m.idmaterial =:idmaterial AND m.status != 3")
	Map<String, Object> quantityMaterial(@Param("idmaterialexport")Integer idmaterialexport,@Param("idmaterial")Integer idmaterial);
	
	@Query("SELECT m FROM Materialexportdetail m WHERE m.id =:id")
	Materialexportdetail getByIdForView(@Param("id")Integer id);
	
	@Query("SELECT m FROM Materialexportdetail m WHERE m.idmaterialexport = :idmaterialexport AND m.status != 3")
	List<Materialexportdetail> listMaterialByIdMaterialExport(@Param("idmaterialexport")Integer idmaterialexport);
	
	@Query("SELECT new map (materialexportdetail.id as id, materialexportdetail.idmaterialexport as idmaterialexport, materialexportdetail.idmaterial as idmaterial, materialexportdetail.material.name as materialname, materialexportdetail.version as version,  materialexportdetail.quantity as quantity, materialexportdetail.price as price, materialexportdetail.amount as amount, materialexportdetail.idunit as idunit, materialexportdetail.note as note) FROM Materialexportdetail materialexportdetail WHERE materialexportdetail.idmaterialexport = :id AND materialexportdetail.status != 3 ")
	List<Map<String, Object>> getAllById(@Param("id") Integer id);
	
	@Modifying
	@Query("UPDATE Materialexportdetail materialexportdetail SET materialexportdetail.quantity =:quantity, materialexportdetail.amount = materialexportdetail.price*:quantity, materialexportdetail.idupdate =:idupdate ,materialexportdetail.updatedate = CURRENT_TIMESTAMP(), materialexportdetail.version = materialexportdetail.version+1, materialexportdetail.status = 2  WHERE materialexportdetail.id =:id AND materialexportdetail.status!=3")
	Integer updateQuantityById(@Param("id") Integer id, @Param("quantity")Integer quantity, @Param("idupdate") Integer idupdate );
	
}

package com.redsun.server.wh.repository;

import java.util.List;
import java.util.Map;

import com.redsun.server.wh.model.Material ;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface MaterialRepository extends JpaRepository<Material, Integer>, JpaSpecificationExecutor<Material> {

	
	@Modifying(clearAutomatically = true)
	@Query("UPDATE Material material SET material.idlock = :idlock, material.lockdate = CURRENT_TIMESTAMP() WHERE material.id = :id AND ((material.idlock = null AND material.lockdate = null) OR (material.lockdate != null AND EXTRACT(EPOCH FROM(CURRENT_TIMESTAMP() - material.lockdate)) > :timeout))")
	Integer updateLock(@Param("id") Integer id, @Param("idlock") Integer idlock, @Param("timeout") Integer timeout);

	@Modifying
	@Query("UPDATE Material material SET material.idlock = null, material.lockdate = null WHERE material.id = :id")
	Integer updateUnlock(@Param("id") Integer id);
	
	@Modifying
	@Query("SELECT COUNT(material) > 0  FROM Material material WHERE material.id = :id AND material.idlock = :idlock")
	Boolean updateCheckLock(@Param("id") Integer id, @Param("idlock") Integer idlock);
	
	@Query("SELECT new map (material.idcreate as idcreate, material.idowner as idowner) FROM Material material WHERE material.id = :id")
	List<Map<String, Object>> getAuthorizePropertiesById(@Param("id") Integer id);
	
	@Query(value="SELECT COUNT(m) > 0  FROM Material m Where m.id != :id AND m.code = :code AND m.idref = 0")
	Boolean isExistCode(@Param("id") Integer id, @Param("code") String code);
	
	@Query(value="SELECT COUNT(m) > 0  FROM Material m Where m.id != :id AND m.name = :name AND m.idref = 0")
	Boolean isExistName(@Param("id") Integer id, @Param("name") String name);
	
	@Query(value="SELECT m FROM Material m WHERE m.idref = 0 AND m.code = :code")
	List<Material> findConfirmedItemsByCode(@Param("code") String code);
	
	@Query("SELECT new map (material.id as value, material.name as display) FROM Material material WHERE material.idref = 0 AND material.reftype = 'materialcode' AND material.status != 3")
	List<Map<String, Object>> loadAllMaterialConfirmed();
	
	@Query("SELECT new map (materialbaselinedetail.material.id as value, materialbaselinedetail.material.name as display) FROM Materialbaselinedetail materialbaselinedetail WHERE materialbaselinedetail.status != 3 AND materialbaselinedetail.materialbaseline.store.id = :idstore")
	List<Map<String, Object>> loadAllFromBaselineByIdstore(@Param("idstore") Integer idstore);
	
	@Query("SELECT material FROM Material material WHERE material.idref = 0 AND material.status != 3")
	List<Material> listAllForSelect();
	
	@Query("SELECT material FROM Material material WHERE material.id =:id")
	Material getByIdForView(@Param("id")Integer id);
	
	
	@Query(value="SELECT m FROM Material m WHERE m.idref = 0 AND LOWER(m.name) LIKE LOWER(CONCAT('%',:name,'%'))")
	List<Material> findConfirmedItemsByName(@Param("name") String name);


}
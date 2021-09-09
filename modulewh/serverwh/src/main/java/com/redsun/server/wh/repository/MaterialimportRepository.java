
package com.redsun.server.wh.repository;

import com.redsun.server.wh.model.Materialimport ;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface MaterialimportRepository extends JpaRepository<Materialimport, Integer>, JpaSpecificationExecutor<Materialimport> {

	@Modifying(clearAutomatically = true)
	@Query("UPDATE Materialimport materialimport SET materialimport.idlock = :idlock, materialimport.lockdate = CURRENT_TIMESTAMP() WHERE materialimport.id = :id AND ((materialimport.idlock = null AND materialimport.lockdate = null) OR (materialimport.lockdate != null AND EXTRACT(EPOCH FROM(CURRENT_TIMESTAMP() - materialimport.lockdate)) > :timeout))")
	Integer updateLock(@Param("id") Integer id, @Param("idlock") Integer idlock, @Param("timeout") Integer timeout);

	@Modifying
	@Query("UPDATE Materialimport materialimport SET materialimport.idlock = null, materialimport.lockdate = null WHERE materialimport.id = :id")
	Integer updateUnlock(@Param("id") Integer id);
	
	@Modifying
	@Query("SELECT COUNT(materialimport) > 0  FROM Materialimport materialimport WHERE materialimport.id = :id AND materialimport.idlock = :idlock")
	Boolean updateCheckLock(@Param("id") Integer id, @Param("idlock") Integer idlock);
	
	@Query("SELECT new map (materialimport.idcreate as idcreate, materialimport.idowner as idowner) FROM Materialimport materialimport WHERE materialimport.id = :id")
	List<Map<String, Object>> getAuthorizePropertiesById(@Param("id") Integer id);
	
	@Query(value = "Select count(m)>0 FROM Materialimport m Where m.id != :id AND m.code = :code")
	Boolean isExistCode(@Param("id") Integer id, @Param("code") String code);
	
	@Modifying
	@Query("UPDATE Materialimportdetail m SET m.status = 3, m.iddelete = :iddelete, m.deletedate = CURRENT_TIMESTAMP(), m.version = m.version + 1 WHERE m.idmaterialimport = :idmaterialimport")
	Integer updateForDeleteByIdMaterial(@Param("iddelete") Integer iddelete, @Param("idmaterialimport") Integer idmaterialimport);
	
	@Query("SELECT materialimport FROM Materialimport materialimport WHERE materialimport.id =:id")
	Materialimport getByIdForView(@Param("id")Integer id);
	
	@Query("SELECT new map (materialimport.id as value, materialimport.code as display) FROM Materialimport materialimport WHERE materialimport.status != 3 ")
	List<Map<String, Object>> listForSelect();
	
	Page<Materialimport> findByIdIn(List<Integer> ids, Pageable pageable);
	
	@Query("SELECT SUM(materialimportdetail.quantity) FROM Materialimport materialimport, Materialimportdetail materialimportdetail WHERE materialimport.id=materialimportdetail.idmaterialimport AND materialimport.idstore=:idstore AND materialimportdetail.idmaterial=:idmaterial")
	Integer totalImportQuantity(@Param("idstore")Integer idstore, @Param("idmaterial") Integer idmaterial);
	
}
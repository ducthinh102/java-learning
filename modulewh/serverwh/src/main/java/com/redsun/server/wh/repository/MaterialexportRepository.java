
package com.redsun.server.wh.repository;

import com.redsun.server.wh.model.Materialexport;
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
public interface MaterialexportRepository extends JpaRepository<Materialexport, Integer>, JpaSpecificationExecutor<Materialexport> {

	@Modifying(clearAutomatically = true)
	@Query("UPDATE Materialexport materialexport SET materialexport.idlock = :idlock, materialexport.lockdate = CURRENT_TIMESTAMP() WHERE materialexport.id = :id AND ((materialexport.idlock = null AND materialexport.lockdate = null) OR (materialexport.lockdate != null AND EXTRACT(EPOCH FROM(CURRENT_TIMESTAMP() - materialexport.lockdate)) > :timeout))")
	Integer updateLock(@Param("id") Integer id, @Param("idlock") Integer idlock, @Param("timeout") Integer timeout);

	@Modifying
	@Query("UPDATE Materialexport materialexport SET materialexport.idlock = null, materialexport.lockdate = null WHERE materialexport.id = :id")
	Integer updateUnlock(@Param("id") Integer id);
	
	@Modifying
	@Query("SELECT COUNT(materialexport) > 0  FROM Materialexport materialexport WHERE materialexport.id = :id AND materialexport.idlock = :idlock")
	Boolean updateCheckLock(@Param("id") Integer id, @Param("idlock") Integer idlock);
	
	@Query("SELECT new map (materialexport.idcreate as idcreate, materialexport.idowner as idowner) FROM Materialexport materialexport WHERE materialexport.id = :id")
	List<Map<String, Object>> getAuthorizePropertiesById(@Param("id") Integer id);
	
	@Query(value = "Select count(m)>0 FROM Materialexport m Where m.id != :id AND m.code = :code AND m.status != 3")
	Boolean isExistCode(@Param("id") Integer id, @Param("code") String code);
	
	@Modifying
	@Query("UPDATE Materialexportdetail m SET m.status = 3, m.iddelete = :iddelete, m.deletedate = CURRENT_TIMESTAMP(), m.version = m.version + 1 WHERE m.idmaterialexport = :idmaterialexport")
	Integer updateForDeleteByIdMaterial(@Param("iddelete") Integer iddelete, @Param("idmaterialexport") Integer idmaterialexport);
	
	@Query("SELECT materialexport FROM Materialexport materialexport WHERE materialexport.id =:id")
	Materialexport getByIdForView(@Param("id")Integer id);
	
	@Query("SELECT new map (materialexport.id as value, materialexport.code as display) FROM Materialexport materialexport WHERE materialexport.status != 3 ")
	List<Map<String, Object>> listForSelect();
	
	Page<Materialexport> findByIdIn(List<Integer> ids, Pageable pageable);
	
	@Query("SELECT SUM(materialexportdetail.quantity) FROM Materialexport materialexport, Materialexportdetail materialexportdetail WHERE materialexport.id=materialexportdetail.idmaterialexport AND materialexport.idstore=:idstore AND materialexportdetail.idmaterial=:idmaterial")
	Integer totalExportQuantity(@Param("idstore")Integer idstore, @Param("idmaterial") Integer idmaterial);
	
}
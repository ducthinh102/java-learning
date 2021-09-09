
package com.redsun.server.wh.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.redsun.server.wh.model.Quotationdetail;

@Repository
public interface QuotationdetailRepository
		extends JpaRepository<Quotationdetail, Integer>, JpaSpecificationExecutor<Quotationdetail> {

	@Modifying
	@Query("UPDATE Quotationdetail quotationdetail SET quotationdetail.idlock = :idlock, quotationdetail.lockdate = CURRENT_TIMESTAMP() WHERE quotationdetail.id = :id AND ((quotationdetail.idlock = null AND quotationdetail.lockdate = null) OR (quotationdetail.lockdate != null AND EXTRACT(EPOCH FROM(CURRENT_TIMESTAMP() - quotationdetail.lockdate)) > :timeout))")
	Integer updateLock(@Param("id") Integer id, @Param("idlock") Integer idlock, @Param("timeout") Integer timeout);

	@Modifying
	@Query("UPDATE Quotationdetail quotationdetail SET quotationdetail.idlock = null, quotationdetail.lockdate = null WHERE quotationdetail.id = :id")
	Integer updateUnlock(@Param("id") Integer id);

	@Modifying
	@Query("SELECT COUNT(quotationdetail) > 0  FROM Quotationdetail quotationdetail WHERE quotationdetail.id = :id AND quotationdetail.idlock = :idlock")
	Boolean updateCheckLock(@Param("id") Integer id, @Param("idlock") Integer idlock);

	@Query("SELECT new map (quotationdetail.idcreate as idcreate, quotationdetail.idowner as idowner) FROM Quotationdetail quotationdetail WHERE quotationdetail.id = :id")
	List<Map<String, Object>> getAuthorizePropertiesById(@Param("id") Integer id);

	@Query("SELECT new map (quotationdetail.id as id, quotationdetail.materialcode as materialcode, quotationdetail.idmaterial as idmaterial, quotationdetail.material.name as materialname, quotationdetail.version as version, quotationdetail.idunit as idunit, quotationdetail.price as price, quotationdetail.note as note) FROM Quotationdetail quotationdetail WHERE quotationdetail.idquotation = :id AND quotationdetail.status != 3 ")
	List<Map<String, Object>> getAllById(@Param("id") Integer id);

	@Query("SELECT m FROM Quotationdetail m WHERE m.id =:id")
	Quotationdetail getByIdForView(@Param("id") Integer id);

	@Query("SELECT COUNT(ac) > 0  FROM Quotationdetail ac WHERE ac.id != :id AND ac.idmaterial = :idmaterial AND ac.status!=3")
	Boolean isExistMaterial(@Param("id") Integer id, @Param("idmaterial") Integer idmaterial);
	
	@Modifying
	@Query("UPDATE Quotationdetail quotationdetail SET quotationdetail.price =:price, quotationdetail.idupdate =:idupdate ,quotationdetail.updatedate = CURRENT_TIMESTAMP(), quotationdetail.version = quotationdetail.version+1, quotationdetail.status = 2  WHERE quotationdetail.id =:id AND quotationdetail.status!=3")
	Integer updatePriceById(@Param("id") Integer id, @Param("price")Double price, @Param("idupdate") Integer idupdate );

}
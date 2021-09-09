
package com.redsun.server.wh.repository;

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

import com.redsun.server.wh.model.Quotation;



@Repository
public interface QuotationRepository extends JpaRepository<Quotation, Integer>, JpaSpecificationExecutor<Quotation> {

	
	@Modifying(clearAutomatically = true)
	@Query("UPDATE Quotation quotation SET quotation.idlock = :idlock, quotation.lockdate = CURRENT_TIMESTAMP() WHERE quotation.id = :id AND ((quotation.idlock = null AND quotation.lockdate = null) OR (quotation.lockdate != null AND EXTRACT(EPOCH FROM(CURRENT_TIMESTAMP() - quotation.lockdate)) > :timeout))")
	Integer updateLock(@Param("id") Integer id, @Param("idlock") Integer idlock, @Param("timeout") Integer timeout);

	@Modifying
	@Query("UPDATE Quotation quotation SET quotation.idlock = null, quotation.lockdate = null WHERE quotation.id = :id")
	Integer updateUnlock(@Param("id") Integer id);
	
	@Modifying
	@Query("SELECT COUNT(quotation) > 0  FROM Quotation quotation WHERE quotation.id = :id AND quotation.idlock = :idlock")
	Boolean updateCheckLock(@Param("id") Integer id, @Param("idlock") Integer idlock);
	
	@Query("SELECT new map (quotation.idcreate as idcreate, quotation.idowner as idowner) FROM Quotation quotation WHERE quotation.id = :id")
	List<Map<String, Object>> getAuthorizePropertiesById(@Param("id") Integer id);
	
	@Query("SELECT new map (quotation.id as value, quotation.code as display) FROM Quotation quotation WHERE quotation.status != 3 ")
	List<Map<String, Object>> listForSelect();
	
	@Query("SELECT COUNT(ac) > 0  FROM Quotation ac WHERE ac.id != :id AND ac.code = :code")
	Boolean isExistCode(@Param("id") Integer id, @Param("code") String code);
	
	@Query("SELECT quotation FROM Quotation quotation WHERE quotation.id =:id")
	Quotation getByIdForView(@Param("id")Integer id);
	
	List<Quotation> findByCode(String code);

	Page<Quotation> findByIdIn(List<Integer> ids, Pageable pageable);

}
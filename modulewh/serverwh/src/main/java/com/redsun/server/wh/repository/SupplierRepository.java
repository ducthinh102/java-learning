
package com.redsun.server.wh.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.redsun.server.wh.model.Supplier;



@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Integer>, JpaSpecificationExecutor<Supplier> {

	
	@Modifying
	@Query("UPDATE Supplier supplier SET supplier.idlock = :idlock, supplier.lockdate = CURRENT_TIMESTAMP() WHERE supplier.id = :id AND ((supplier.idlock = null AND supplier.lockdate = null) OR (supplier.lockdate != null AND EXTRACT(EPOCH FROM(CURRENT_TIMESTAMP() - supplier.lockdate)) > :timeout))")
	Integer updateLock(@Param("id") Integer id, @Param("idlock") Integer idlock, @Param("timeout") Integer timeout);

	@Modifying
	@Query("UPDATE Supplier supplier SET supplier.idlock = null, supplier.lockdate = null WHERE supplier.id = :id")
	Integer updateUnlock(@Param("id") Integer id);
	
	@Modifying
	@Query("SELECT COUNT(supplier) > 0  FROM Supplier supplier WHERE supplier.id = :id AND supplier.idlock = :idlock")
	Boolean updateCheckLock(@Param("id") Integer id, @Param("idlock") Integer idlock);
	
	@Query("SELECT new map (supplier.idcreate as idcreate, supplier.idowner as idowner) FROM Supplier supplier WHERE supplier.id = :id")
	List<Map<String, Object>> getAuthorizePropertiesById(@Param("id") Integer id);
	
	@Query("SELECT COUNT(ac) > 0  FROM Supplier ac WHERE ac.id != :id AND ac.code = :code")
	Boolean isExistCode(@Param("id") Integer id, @Param("code") String code);
	
	@Query("SELECT COUNT(ac) > 0  FROM Supplier ac WHERE ac.id != :id AND ac.name = :name")
	Boolean isExistName(@Param("id") Integer id, @Param("name") String name);
	
	List<Supplier> findByName(String name);

	List<Supplier> findByCode(String code);
	
	@Query(value="SELECT new map(sp.id as id, sp.name as display) FROM Supplier sp WHERE sp.status != 3")
	List<Map<String, Object>> listAllForSelect();


}
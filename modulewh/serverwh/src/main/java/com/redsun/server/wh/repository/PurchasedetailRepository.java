
package com.redsun.server.wh.repository;

import com.redsun.server.wh.model.Materialimport;
import com.redsun.server.wh.model.Purchasedetail ;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface PurchasedetailRepository extends JpaRepository<Purchasedetail, Integer>, JpaSpecificationExecutor<Purchasedetail> {
	
	@Modifying
	@Query("UPDATE Purchasedetail purchasedetail SET purchasedetail.idlock = :idlock, purchasedetail.lockdate = CURRENT_TIMESTAMP() WHERE purchasedetail.id = :id AND ((purchasedetail.idlock = null AND purchasedetail.lockdate = null) OR (purchasedetail.lockdate != null AND EXTRACT(EPOCH FROM(CURRENT_TIMESTAMP() - purchasedetail.lockdate)) > :timeout))")
	Integer updateLock(@Param("id") Integer id, @Param("idlock") Integer idlock, @Param("timeout") Integer timeout);

	@Modifying
	@Query("UPDATE Purchasedetail purchasedetail SET purchasedetail.idlock = null, purchasedetail.lockdate = null WHERE purchasedetail.id = :id")
	Integer updateUnlock(@Param("id") Integer id);
	
	@Modifying
	@Query("SELECT COUNT(purchasedetail) > 0  FROM Purchasedetail purchasedetail WHERE purchasedetail.id = :id AND purchasedetail.idlock = :idlock")
	Boolean updateCheckLock(@Param("id") Integer id, @Param("idlock") Integer idlock);
	
	@Query("SELECT new map (purchasedetail.idcreate as idcreate, purchasedetail.idowner as idowner) FROM Purchasedetail purchasedetail WHERE purchasedetail.id = :id")
	List<Map<String, Object>> getAuthorizePropertiesById(@Param("id") Integer id);
	
	@Query(value = "SELECT COALESCE(SUM(purchasedetail.amount), 0) FROM Purchasedetail purchasedetail WHERE purchasedetail.status != 3 AND purchasedetail.idpurchase = :idpurchase")
	Double sumAmount(@Param("idpurchase") Integer idpurchase);
	
	@Query("SELECT new map (purchasedetail.id as id, purchasedetail.materialcode as materialcode, purchasedetail.idmaterial as idmaterial, purchasedetail.material.name as materialname, purchasedetail.version as version, purchasedetail.quantity as quantity, purchasedetail.price as price, purchasedetail.amount as amount, purchasedetail.idunit as idunit, purchasedetail.note as note) FROM Purchasedetail purchasedetail WHERE purchasedetail.idpurchase = :id AND purchasedetail.status != 3 ")
	List<Map<String, Object>> getAllById(@Param("id") Integer id);
	
	@Query("SELECT COUNT(p)>0 FROM Purchasedetail p WHERE p.id!=:id AND p.idpurchase = :idpurchase AND p.idmaterial =:idmaterial  AND p.status != 3")
	Boolean checkExitsMaterialInPurchase(@Param("id")Integer id, @Param("idpurchase")Integer idpurchase, @Param("idmaterial")Integer idmaterial);
	
	@Query("SELECT COUNT(m)>0 FROM Purchasedetail m WHERE  m.idpurchase = :idpurchase AND m.idmaterial =:idmaterial  AND m.status != 3")
	Boolean checkExitsMaterialInPurchaseForCreate(@Param("idpurchase")Integer idpurchase, @Param("idmaterial")Integer idmaterial);
	
	
	/*@Query("SELECT new map (purchasedetail.quantity as quantity, purchasedetail.id as id, purchasedetail.version as version) FROM Purchasedetail purchasedetail WHERE purchasedetail.idmaterial =:idmaterial AND purchasedetail.status != 3")
	Map<String, Object> quantityMaterial(@Param("idmaterial")Integer idmaterial);

	@Query("SELECT COUNT(purchasedetail) > 0  FROM Purchasedetail purchasedetail WHERE purchasedetail.idmaterial =:idmaterial AND purchasedetail.price != :price AND purchasedetail.status != 3")
	Boolean checkPriceDiffCreate(@Param("idmaterial")Integer idmaterial,@Param("price")Double double1);
	*/
	
	@Modifying
	@Query("UPDATE Purchasedetail purchasedetail SET purchasedetail.price =:price, purchasedetail.amount = purchasedetail.quantity*:price, purchasedetail.idupdate =:idupdate ,purchasedetail.updatedate = CURRENT_TIMESTAMP(), purchasedetail.version = purchasedetail.version+1, purchasedetail.status = 2  WHERE purchasedetail.id =:id AND purchasedetail.status!=3")
	Integer updatePriceById(@Param("id") Integer id, @Param("price")Double price, @Param("idupdate") Integer idupdate );
	
	@Modifying
	@Query("UPDATE Purchasedetail purchasedetail SET purchasedetail.quantity =:quantity, purchasedetail.amount = purchasedetail.price*:quantity, purchasedetail.idupdate =:idupdate ,purchasedetail.updatedate = CURRENT_TIMESTAMP(), purchasedetail.version = purchasedetail.version+1, purchasedetail.status = 2  WHERE purchasedetail.id =:id AND purchasedetail.status!=3")
	Integer updateQuantityById(@Param("id") Integer id, @Param("quantity")Integer quantity, @Param("idupdate") Integer idupdate );	
	
	@Query("SELECT purchasedetail FROM Purchasedetail purchasedetail WHERE purchasedetail.id =:id AND purchasedetail.status != 3 ")
	Purchasedetail getById(@Param("id")Integer id);
	
}
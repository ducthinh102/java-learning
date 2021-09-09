
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

import com.redsun.server.wh.model.Purchase ;



@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Integer>, JpaSpecificationExecutor<Purchase> {

	@Modifying
	@Query("UPDATE Purchasedetail purchase SET purchase.status = 3, purchase.iddelete = :iddelete, purchase.deletedate = CURRENT_TIMESTAMP(), purchase.version = purchase.version + 1 WHERE purchase.idpurchase = :idpurchase")
	Integer updateForDeleteByIdPurchase(@Param("iddelete") Integer iddelete, @Param("idpurchase") Integer idpurchase);
	
	@Modifying(clearAutomatically = true)
	@Query("UPDATE Purchase purchase SET purchase.idlock = :idlock, purchase.lockdate = CURRENT_TIMESTAMP() WHERE purchase.id = :id AND ((purchase.idlock = null AND purchase.lockdate = null) OR (purchase.lockdate != null AND EXTRACT(EPOCH FROM(CURRENT_TIMESTAMP() - purchase.lockdate)) > :timeout))")
	Integer updateLock(@Param("id") Integer id, @Param("idlock") Integer idlock, @Param("timeout") Integer timeout);

	@Modifying
	@Query("UPDATE Purchase purchase SET purchase.idlock = null, purchase.lockdate = null WHERE purchase.id = :id")
	Integer updateUnlock(@Param("id") Integer id);
	
	@Query("SELECT COUNT(purchase) > 0  FROM Purchase purchase WHERE purchase.id = :id AND purchase.idlock = :idlock")
	Boolean updateCheckLock(@Param("id") Integer id, @Param("idlock") Integer idlock);
	
	@Query("SELECT new map (purchase.idcreate as idcreate, purchase.idowner as idowner) FROM Purchase purchase WHERE purchase.id = :id")
	List<Map<String, Object>> getAuthorizePropertiesById(@Param("id") Integer id);

	List<Purchase> findByCode(String code);
	
	@Query(value = "Select count(m)>0 FROM Purchase m Where m.id != :id AND m.code = :code")
	Boolean isExistCode(@Param("id") Integer id, @Param("code") String code);
	
	@Query(value="SELECT COUNT(m) > 0  FROM Purchase m Where m.id != :id AND m.name = :name")
	Boolean isExistName(@Param("id") Integer id, @Param("name") String name);
	
	@Query("SELECT new map (purchase.id as value, purchase.code as display) FROM Purchase purchase WHERE purchase.status != 3 ")
	List<Map<String, Object>> listForSelect();

	Page<Purchase> findByIdIn(List<Integer> ids, Pageable pageable);
	
	@Modifying
	@Query("UPDATE Purchase purchase SET purchase.totalamount = :totalAmount WHERE purchase.id = :id")
	Integer updateTotalAmount(@Param("id") Integer id, @Param("totalAmount") double totalAmount);

}
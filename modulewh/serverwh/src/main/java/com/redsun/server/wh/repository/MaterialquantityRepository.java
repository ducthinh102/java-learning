
package com.redsun.server.wh.repository;

import com.redsun.server.wh.model.Materialquantity ;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface MaterialquantityRepository extends JpaRepository<Materialquantity, Integer>, JpaSpecificationExecutor<Materialquantity> {

	
	@Modifying
	@Query("UPDATE Materialquantity materialquantity SET materialquantity.idlock = :idlock, materialquantity.lockdate = CURRENT_TIMESTAMP() WHERE materialquantity.id = :id AND ((materialquantity.idlock = null AND materialquantity.lockdate = null) OR (materialquantity.lockdate != null AND EXTRACT(EPOCH FROM(CURRENT_TIMESTAMP() - materialquantity.lockdate)) > :timeout))")
	Integer updateLock(@Param("id") Integer id, @Param("idlock") Integer idlock, @Param("timeout") Integer timeout);

	@Modifying
	@Query("UPDATE Materialquantity materialquantity SET materialquantity.idlock = null, materialquantity.lockdate = null WHERE materialquantity.id = :id")
	Integer updateUnlock(@Param("id") Integer id);
	
	@Modifying
	@Query("SELECT COUNT(materialquantity) > 0  FROM Materialquantity materialquantity WHERE materialquantity.id = :id AND materialquantity.idlock = :idlock")
	Boolean updateCheckLock(@Param("id") Integer id, @Param("idlock") Integer idlock);

}
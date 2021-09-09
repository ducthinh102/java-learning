package com.redsun.server.wh.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.redsun.server.wh.model.Catalog;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog, Integer>, JpaSpecificationExecutor<Catalog> {
	
	@Query(value = "Select count(cat)>0 FROM Catalog cat Where cat.id != :id AND cat.code = :code AND cat.scope = :scope")
	Boolean isExistCode(@Param("id") Integer id, @Param("code") String code, @Param("scope") String scope);

	@Query(value = "Select count(cat)>0 FROM Catalog cat Where cat.id != :id AND cat.name = :name AND cat.scope = :scope")
	Boolean isExistName(@Param("id") Integer id, @Param("name") String name, @Param("scope") String scope);

	@Modifying
	@Query("UPDATE Catalog cat SET cat.idlock = :idlock, cat.lockdate = CURRENT_TIMESTAMP() WHERE cat.id = :id AND ((cat.idlock = null AND cat.lockdate = null) OR (cat.lockdate != null AND EXTRACT(EPOCH FROM(CURRENT_TIMESTAMP() - cat.lockdate)) > :timeout))")
	Integer updateLock(@Param("id") Integer id, @Param("idlock") Integer idlock, @Param("timeout") Integer timeout);

	@Modifying
	@Query("UPDATE Catalog cat SET cat.idlock = null, cat.lockdate = null WHERE cat.id = :id")
	Integer updateUnlock(@Param("id") Integer id);
	
	@Query("SELECT new map (cat.idcreate as idcreate, cat.idowner as idowner) FROM Catalog cat WHERE cat.id = :id")
	List<Map<String, Object>> getAuthorizePropertiesById(@Param("id") Integer id);
	
	@Query("SELECT cat FROM Catalog cat WHERE cat.idparent = :idparent")
	List<Catalog> findByIdParent(@Param("idparent")Integer idparent);

	List<Catalog> findByScopeAndStatusLessThanEqual(String scope, int status);

	@Modifying
	@Query("UPDATE Catalog cat SET cat.status = 3, cat.iddelete = :iddelete, cat.deletedate = CURRENT_TIMESTAMP(), cat.version = cat.version + 1 WHERE cat.idparent = :idparent")
	Integer updateForDeleteByIdParentSys(@Param("iddelete") Integer iddelete, @Param("idparent") Integer idparent);
	
	@Query(value="SELECT new map(cat.id as value, cat.name as display, cat.code as code, cat.scope as scope, cat.idparent as idparent) FROM Catalog cat WHERE cat.status != 3 AND cat.scope =:scope")
	List<Map<String, Object>> listForSelectByScope(@Param("scope") String scope);
}

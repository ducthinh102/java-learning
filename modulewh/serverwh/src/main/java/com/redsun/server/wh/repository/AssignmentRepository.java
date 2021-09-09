
package com.redsun.server.wh.repository;

import com.redsun.server.wh.model.Assignment ;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Integer>, JpaSpecificationExecutor<Assignment> {

	@Query(value="SELECT a.idref FROM Assignment a WHERE a.reftype = :reftype AND a.idassignee = :idassignee AND a.id NOT IN (SELECT idassignment FROM Sign)")
	List<Integer> listAllIdrefByReftypeAndIdassignee(@Param("reftype") String reftype, @Param("idassignee") Integer idassignee);
	
	Assignment getFirstByIdrefOrderByIdDesc(@Param("idref") Integer idref);
	
	Assignment getFirstByIdworkflowexecute(@Param("idworkflowexecute") Integer idworkflowexecute);
	
}
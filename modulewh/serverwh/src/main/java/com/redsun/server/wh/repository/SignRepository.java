
package com.redsun.server.wh.repository;

import com.redsun.server.wh.model.Sign ;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface SignRepository extends JpaRepository<Sign, Integer>, JpaSpecificationExecutor<Sign> {

	Sign getByIdassignment(@Param("idassignment") Integer idassignment);
}
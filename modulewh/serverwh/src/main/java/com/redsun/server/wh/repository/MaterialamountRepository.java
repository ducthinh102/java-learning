
package com.redsun.server.wh.repository;

import com.redsun.server.wh.model.Materialamount ;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;



@Repository
public interface MaterialamountRepository extends JpaRepository<Materialamount, Integer>, JpaSpecificationExecutor<Materialamount> {


}
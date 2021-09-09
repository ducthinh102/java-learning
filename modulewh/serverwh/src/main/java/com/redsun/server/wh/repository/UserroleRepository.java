
package com.redsun.server.wh.repository;

import com.redsun.server.wh.model.Userrole ;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;



@Repository
public interface UserroleRepository extends JpaRepository<Userrole, Integer>, JpaSpecificationExecutor<Userrole> {


}
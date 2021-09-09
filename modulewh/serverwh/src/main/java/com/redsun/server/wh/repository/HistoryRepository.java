
package com.redsun.server.wh.repository;

import com.redsun.server.wh.model.History ;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;



@Repository
public interface HistoryRepository extends JpaRepository<History, Integer>, JpaSpecificationExecutor<History> {


}
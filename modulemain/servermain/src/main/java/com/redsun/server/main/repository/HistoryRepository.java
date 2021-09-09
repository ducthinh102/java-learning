
package com.redsun.server.main.repository;

import com.redsun.server.main.model.History ;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;



@Repository
public interface HistoryRepository extends JpaRepository<History, Integer>, JpaSpecificationExecutor<History> {


}
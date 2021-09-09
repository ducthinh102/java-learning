
package com.redsun.server.wh.repository;

import com.redsun.server.wh.model.Workflowexecute ;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface WorkflowexecuteRepository extends JpaRepository<Workflowexecute, Integer>, JpaSpecificationExecutor<Workflowexecute> {

	List<Workflowexecute> findByIdworkflowAndIdrefAndIscurrentOrderByIdDesc(Integer idworkflow, Integer idref, Boolean iscurrent);
	
	Workflowexecute findFirstByIdworkflowAndIdrefAndIscurrent(Integer idworkflow, Integer idref, Boolean iscurrent);
	
	Workflowexecute findFirstByIdworkflowAndIdrefAndIscurrentAndStatus(Integer idworkflow, Integer idref, Boolean iscurrent, Integer status);
	
	Workflowexecute findFirstByIdworkflowAndIdrefAndIdreceiverAndStepLessThanEqualAndStatusOrderByIdDesc(Integer idworkflow, Integer idref, Integer idreceiver, Integer step, Integer status);
	
	List<Workflowexecute> findByIdworkflowAndIscurrentAndStatus(Integer idworkflow, Boolean iscurrent, Integer status);
	
	@Query(value="SELECT w.idref FROM Workflowexecute w WHERE w.idworkflow = :idworkflow AND w.idreceiver = :idreceiver AND w.status IN :statusList AND w.iscurrent = true")
	List<Integer> listAllIdrefByReceiverIdAndStatus(@Param("idworkflow") Integer idworkflow, @Param("idreceiver") Integer idreceiver, @Param("statusList") List<Integer> statusList);
	
	@Query(value="SELECT w.idref FROM Workflowexecute w WHERE w.idworkflow = :idworkflow AND w.idsender = :idsender AND w.status IN :statusList")
	List<Integer> listAllIdrefBySenderIdAndStatus(@Param("idworkflow") Integer idworkflow, @Param("idsender") Integer idsender, @Param("statusList") List<Integer> statusList);
	
	@Query(value="SELECT w.idref FROM Workflowexecute w WHERE w.idworkflow = :idworkflow AND w.idsender = :idsender AND w.status NOT IN :statusList")
	List<Integer> listAllIdrefBySenderIdAndNotInStatus(@Param("idworkflow") Integer idworkflow, @Param("idsender") Integer idsender, @Param("statusList") List<Integer> statusList);

	@Query(value="SELECT w FROM Workflowexecute w WHERE w.idworkflow = :idworkflow AND (w.idsender = :iduser OR w.idreceiver = :iduser) AND w.status != :waitforapprove")
	List<Workflowexecute> listAllForCreate(@Param("idworkflow") Integer idworkflow, @Param("iduser") Integer iduser, @Param("waitforapprove") Integer waitforapprove);
	
	@Query(value="SELECT w FROM Workflowexecute w WHERE w.idworkflow = :idworkflow AND ( ((w.status = :send OR w.status = :receive OR w.status = :approve) AND w.idreceiver = :iduser ) OR (w.iscurrent = true AND w.status = :sendback AND w.idreceiver = :iduser) )")
	List<Workflowexecute> listAllForReceive(@Param("idworkflow") Integer idworkflow, @Param("iduser") Integer iduser, @Param("approve") Integer approve, @Param("send") Integer send, @Param("receive") Integer receive, @Param("sendback") Integer sendback);
	
	@Query(value="SELECT w FROM Workflowexecute w WHERE w.idworkflow = :idworkflow AND w.idreceiver = :iduser AND w.status = :waitforapprove AND w.iscurrent = true")
	List<Workflowexecute> listAllForWaitapprove(@Param("idworkflow") Integer idworkflow, @Param("iduser") Integer iduser, @Param("waitforapprove") Integer waitforapprove);

}
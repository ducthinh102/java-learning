
package com.redsun.server.wh.authorize;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.redsun.server.wh.controller.WorkflowexecuteRestController;
import com.redsun.server.wh.model.Workflowexecute;
import com.redsun.server.wh.model.common.SearchCriteria;

@Component("workflowexecuteAuthorize")
public class WorkflowexecuteAuthorize {

	public static final Logger logger = LoggerFactory.getLogger(WorkflowexecuteRestController.class);

	public boolean canCreate(Workflowexecute workflowexecute) {
		return true;
	}

	public boolean canRead(Integer id) {
		return true;
	}

	public boolean canRead(List<SearchCriteria> searchCriterias) {
		return true;
	}

	public boolean canUpdate(Workflowexecute workflowexecute) {
		return true;
	}

	public boolean canDelete(Integer id) {
		return true;
	}

}

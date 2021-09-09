
package com.redsun.server.wh.authorize;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.redsun.server.wh.controller.AssignmentRestController;
import com.redsun.server.wh.model.Assignment;
import com.redsun.server.wh.model.common.SearchCriteria;

@Component("assignmentAuthorize")
public class AssignmentAuthorize {

	public static final Logger logger = LoggerFactory.getLogger(AssignmentRestController.class);

	public boolean canCreate(Assignment assignment) {
		return true;
	}

	public boolean canRead(Integer id) {
		return true;
	}

	public boolean canRead(List<SearchCriteria> searchCriterias) {
		return true;
	}

	public boolean canUpdate(Assignment assignment) {
		return true;
	}

	public boolean canDelete(Integer id) {
		return true;
	}

}


package com.redsun.server.wh.authorize;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.redsun.server.wh.controller.HistoryRestController;
import com.redsun.server.wh.model.History;
import com.redsun.server.wh.model.common.SearchCriteria;

@Component("historyAuthorize")
public class HistoryAuthorize {

	public static final Logger logger = LoggerFactory.getLogger(HistoryRestController.class);

	public boolean canCreate(History history) {
		return true;
	}

	public boolean canRead(Integer id) {
		return true;
	}

	public boolean canRead(List<SearchCriteria> searchCriterias) {
		return true;
	}

	public boolean canUpdate(History history) {
		return true;
	}

	public boolean canDelete(Integer id) {
		return true;
	}

}

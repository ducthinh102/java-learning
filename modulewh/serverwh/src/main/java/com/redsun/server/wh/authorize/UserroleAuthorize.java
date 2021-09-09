
package com.redsun.server.wh.authorize;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.redsun.server.wh.controller.UserroleRestController;
import com.redsun.server.wh.model.Userrole;
import com.redsun.server.wh.model.common.SearchCriteria;

@Component("userroleAuthorize")
public class UserroleAuthorize {

	public static final Logger logger = LoggerFactory.getLogger(UserroleRestController.class);

	public boolean canCreate(Userrole userrole) {
		return true;
	}

	public boolean canRead(Integer id) {
		return true;
	}

	public boolean canRead(List<SearchCriteria> searchCriterias) {
		return true;
	}

	public boolean canUpdate(Userrole userrole) {
		return true;
	}

	public boolean canDelete(Integer id) {
		return true;
	}

}

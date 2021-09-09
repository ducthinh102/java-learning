
package com.redsun.server.wh.authorize;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.redsun.server.wh.controller.SignRestController;
import com.redsun.server.wh.model.Sign;
import com.redsun.server.wh.model.common.SearchCriteria;

@Component("signAuthorize")
public class SignAuthorize {

	public static final Logger logger = LoggerFactory.getLogger(SignRestController.class);

	public boolean canCreate(Sign sign) {
		return true;
	}

	public boolean canRead(Integer id) {
		return true;
	}

	public boolean canRead(List<SearchCriteria> searchCriterias) {
		return true;
	}

	public boolean canUpdate(Sign sign) {
		return true;
	}

	public boolean canDelete(Integer id) {
		return true;
	}

}

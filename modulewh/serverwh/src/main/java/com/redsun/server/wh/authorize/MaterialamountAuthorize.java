
package com.redsun.server.wh.authorize;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.redsun.server.wh.controller.MaterialamountRestController;
import com.redsun.server.wh.model.Materialamount;
import com.redsun.server.wh.model.common.SearchCriteria;

@Component("materialamountAuthorize")
public class MaterialamountAuthorize {

	public static final Logger logger = LoggerFactory.getLogger(MaterialamountRestController.class);

	public boolean canCreate(Materialamount materialamount) {
		return true;
	}

	public boolean canRead(Integer id) {
		return true;
	}

	public boolean canRead(List<SearchCriteria> searchCriterias) {
		return true;
	}

	public boolean canUpdate(Materialamount materialamount) {
		return true;
	}

	public boolean canDelete(Integer id) {
		return true;
	}

}

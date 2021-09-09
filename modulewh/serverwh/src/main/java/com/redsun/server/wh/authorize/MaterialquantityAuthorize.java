
package com.redsun.server.wh.authorize;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.redsun.server.wh.controller.MaterialquantityRestController;
import com.redsun.server.wh.model.Materialquantity;
import com.redsun.server.wh.model.common.SearchCriteria;

@Component("materialquantityAuthorize")
public class MaterialquantityAuthorize {

	public static final Logger logger = LoggerFactory.getLogger(MaterialquantityRestController.class);

	public boolean canCreate(Materialquantity materialquantity) {
		return true;
	}

	public boolean canRead(Integer id) {
		return true;
	}

	public boolean canRead(List<SearchCriteria> searchCriterias) {
		return true;
	}

	public boolean canUpdate(Materialquantity materialquantity) {
		return true;
	}

	public boolean canUpdateForDelete(Integer id, Integer version) {
		return true;
	}

	public boolean canDelete(Integer id) {
		return true;
	}

}

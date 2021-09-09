
package com.redsun.server.wh.authorize;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.redsun.server.wh.controller.WarehouseRestController;
import com.redsun.server.wh.model.Warehouse;
import com.redsun.server.wh.model.common.SearchCriteria;
import com.redsun.server.wh.service.WarehouseService;

@Component("warehouseAuthorize")
public class WarehouseAuthorize {

	public static final Logger logger = LoggerFactory.getLogger(WarehouseRestController.class);

	private String elementKey = "";
	
	@Autowired
	private WarehouseService warehouseService;

	public boolean canCreate(Warehouse warehouse) {
		return true;
	}

	public boolean canRead(Integer id) {
		return true;
	}

	public boolean canRead(List<SearchCriteria> searchCriterias) {
		return true;
	}

	public boolean canUpdate(Warehouse warehouse) {
		return true;
	}

	public boolean canDelete(Integer id) {
		return true;
	}
}

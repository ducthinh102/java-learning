package com.redsun.server.wh.repository.specification;

import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;
import com.redsun.server.wh.model.Warehouse;
import com.redsun.server.wh.model.common.SearchCriteria;

@Component
public class WarehouseSpecificationsBuilder {
 
    public Specification<Warehouse> build(List<SearchCriteria> searchCriterias) {
        if (searchCriterias.size() == 0) {
            return null;
        }
 
        List<Specification<Warehouse>> specificationWarehouse = new ArrayList<Specification<Warehouse>>();
        for (SearchCriteria searchCriteria : searchCriterias) {
			if(!searchCriteria.getValue().equals("")) {
        		specificationWarehouse.add(new WarehouseSpecification(searchCriteria));
			}
        }
 		
		if(specificationWarehouse.size() == 0) {
        	return null;
        }
        Specification<Warehouse> result = specificationWarehouse.get(0);
        for (int i = 1; i < specificationWarehouse.size(); i++) {
        	String logicOperation = ((WarehouseSpecification)specificationWarehouse.get(i-1)).getSearchCriteria().getLogic();
        	if(logicOperation != null && logicOperation.toUpperCase().equals("AND")) {
                result = Specifications.where(result).and(specificationWarehouse.get(i));
        	} 
        	
        	else {
                result = Specifications.where(result).or(specificationWarehouse.get(i));
        	}
        }
        return result;
    }

}

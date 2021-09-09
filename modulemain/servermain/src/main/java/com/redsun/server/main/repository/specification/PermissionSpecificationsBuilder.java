

package com.redsun.server.main.repository.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;

import com.redsun.server.main.model.Permission;
import com.redsun.server.main.model.common.SearchCriteria;

@Component
public class PermissionSpecificationsBuilder {
 
    public Specification<Permission> build(List<SearchCriteria> searchCriterias) {
        if (searchCriterias.size() == 0) {
            return null;
        }
 
        List<Specification<Permission>> specificationPermissions = new ArrayList<Specification<Permission>>();
        for (SearchCriteria searchCriteria : searchCriterias) {
			if(!searchCriteria.getValue().equals("")) {
        		specificationPermissions.add(new PermissionSpecification(searchCriteria));
			}
        }
 		
		if(specificationPermissions.size() == 0) {
        	return null;
        }
        Specification<Permission> result = specificationPermissions.get(0);
        for (int i = 1; i < specificationPermissions.size(); i++) {
        	String logicOperation = ((PermissionSpecification)specificationPermissions.get(i-1)).getSearchCriteria().getLogic();
        	if(logicOperation != null && logicOperation.toUpperCase().equals("AND")) {
                result = Specifications.where(result).and(specificationPermissions.get(i));
        	} else {
                result = Specifications.where(result).or(specificationPermissions.get(i));
        	}
        }
        return result;
    }

}

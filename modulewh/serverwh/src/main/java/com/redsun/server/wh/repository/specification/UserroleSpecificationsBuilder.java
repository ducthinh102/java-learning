

package com.redsun.server.wh.repository.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;

import com.redsun.server.wh.model.Userrole;
import com.redsun.server.wh.model.common.SearchCriteria;

@Component
public class UserroleSpecificationsBuilder {
 
    public Specification<Userrole> build(List<SearchCriteria> searchCriterias) {
        if (searchCriterias.size() == 0) {
            return null;
        }
 
        List<Specification<Userrole>> specificationUserroles = new ArrayList<Specification<Userrole>>();
        for (SearchCriteria searchCriteria : searchCriterias) {
			if(!searchCriteria.getValue().equals("")) {
        		specificationUserroles.add(new UserroleSpecification(searchCriteria));
			}
        }
 		
		if(specificationUserroles.size() == 0) {
        	return null;
        }
        Specification<Userrole> result = specificationUserroles.get(0);
        for (int i = 1; i < specificationUserroles.size(); i++) {
        	String logicOperation = ((UserroleSpecification)specificationUserroles.get(i-1)).getSearchCriteria().getLogic();
        	if(logicOperation != null && logicOperation.toUpperCase().equals("AND")) {
                result = Specifications.where(result).and(specificationUserroles.get(i));
        	} else {
                result = Specifications.where(result).or(specificationUserroles.get(i));
        	}
        }
        return result;
    }

}

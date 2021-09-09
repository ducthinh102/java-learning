

package com.redsun.server.wh.repository.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;

import com.redsun.server.wh.model.Assignment;
import com.redsun.server.wh.model.common.SearchCriteria;

@Component
public class AssignmentSpecificationsBuilder {
 
    public Specification<Assignment> build(List<SearchCriteria> searchCriterias) {
        if (searchCriterias.size() == 0) {
            return null;
        }
 
        List<Specification<Assignment>> specificationAssignments = new ArrayList<Specification<Assignment>>();
        for (SearchCriteria searchCriteria : searchCriterias) {
			if(!searchCriteria.getValue().equals("")) {
        		specificationAssignments.add(new AssignmentSpecification(searchCriteria));
			}
        }
 		
		if(specificationAssignments.size() == 0) {
        	return null;
        }
        Specification<Assignment> result = specificationAssignments.get(0);
        for (int i = 1; i < specificationAssignments.size(); i++) {
        	String logicOperation = ((AssignmentSpecification)specificationAssignments.get(i-1)).getSearchCriteria().getLogic();
        	if(logicOperation != null && logicOperation.toUpperCase().equals("AND")) {
                result = Specifications.where(result).and(specificationAssignments.get(i));
        	} else {
                result = Specifications.where(result).or(specificationAssignments.get(i));
        	}
        }
        return result;
    }

}



package com.redsun.server.wh.repository.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;

import com.redsun.server.wh.model.Workflowexecute;
import com.redsun.server.wh.model.common.SearchCriteria;

@Component
public class WorkflowexecuteSpecificationsBuilder {
 
    public Specification<Workflowexecute> build(List<SearchCriteria> searchCriterias) {
        if (searchCriterias.size() == 0) {
            return null;
        }
 
        List<Specification<Workflowexecute>> specificationWorkflowexecutes = new ArrayList<Specification<Workflowexecute>>();
        for (SearchCriteria searchCriteria : searchCriterias) {
			if(!searchCriteria.getValue().equals("")) {
        		specificationWorkflowexecutes.add(new WorkflowexecuteSpecification(searchCriteria));
			}
        }
 		
		if(specificationWorkflowexecutes.size() == 0) {
        	return null;
        }
        Specification<Workflowexecute> result = specificationWorkflowexecutes.get(0);
        for (int i = 1; i < specificationWorkflowexecutes.size(); i++) {
        	String logicOperation = ((WorkflowexecuteSpecification)specificationWorkflowexecutes.get(i-1)).getSearchCriteria().getLogic();
        	if(logicOperation != null && logicOperation.toUpperCase().equals("AND")) {
                result = Specifications.where(result).and(specificationWorkflowexecutes.get(i));
        	} else {
                result = Specifications.where(result).or(specificationWorkflowexecutes.get(i));
        	}
        }
        return result;
    }

}



package com.redsun.server.wh.repository.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;

import com.redsun.server.wh.model.History;
import com.redsun.server.wh.model.common.SearchCriteria;

@Component
public class HistorySpecificationsBuilder {
 
    public Specification<History> build(List<SearchCriteria> searchCriterias) {
        if (searchCriterias.size() == 0) {
            return null;
        }
 
        List<Specification<History>> specificationHistorys = new ArrayList<Specification<History>>();
        for (SearchCriteria searchCriteria : searchCriterias) {
			if(!searchCriteria.getValue().equals("")) {
        		specificationHistorys.add(new HistorySpecification(searchCriteria));
			}
        }
 		
		if(specificationHistorys.size() == 0) {
        	return null;
        }
        Specification<History> result = specificationHistorys.get(0);
        for (int i = 1; i < specificationHistorys.size(); i++) {
        	String logicOperation = ((HistorySpecification)specificationHistorys.get(i-1)).getSearchCriteria().getLogic();
        	if(logicOperation != null && logicOperation.toUpperCase().equals("AND")) {
                result = Specifications.where(result).and(specificationHistorys.get(i));
        	} else {
                result = Specifications.where(result).or(specificationHistorys.get(i));
        	}
        }
        return result;
    }

}



package com.redsun.server.wh.repository.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;

import com.redsun.server.wh.model.Sign;
import com.redsun.server.wh.model.common.SearchCriteria;

@Component
public class SignSpecificationsBuilder {
 
    public Specification<Sign> build(List<SearchCriteria> searchCriterias) {
        if (searchCriterias.size() == 0) {
            return null;
        }
 
        List<Specification<Sign>> specificationSigns = new ArrayList<Specification<Sign>>();
        for (SearchCriteria searchCriteria : searchCriterias) {
			if(!searchCriteria.getValue().equals("")) {
        		specificationSigns.add(new SignSpecification(searchCriteria));
			}
        }
 		
		if(specificationSigns.size() == 0) {
        	return null;
        }
        Specification<Sign> result = specificationSigns.get(0);
        for (int i = 1; i < specificationSigns.size(); i++) {
        	String logicOperation = ((SignSpecification)specificationSigns.get(i-1)).getSearchCriteria().getLogic();
        	if(logicOperation != null && logicOperation.toUpperCase().equals("AND")) {
                result = Specifications.where(result).and(specificationSigns.get(i));
        	} else {
                result = Specifications.where(result).or(specificationSigns.get(i));
        	}
        }
        return result;
    }

}

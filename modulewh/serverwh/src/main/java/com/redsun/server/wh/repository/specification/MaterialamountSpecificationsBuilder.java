

package com.redsun.server.wh.repository.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;

import com.redsun.server.wh.model.Materialamount;
import com.redsun.server.wh.model.common.SearchCriteria;

@Component
public class MaterialamountSpecificationsBuilder {
 
    public Specification<Materialamount> build(List<SearchCriteria> searchCriterias) {
        if (searchCriterias.size() == 0) {
            return null;
        }
 
        List<Specification<Materialamount>> specificationMaterialamounts = new ArrayList<Specification<Materialamount>>();
        for (SearchCriteria searchCriteria : searchCriterias) {
			if(!searchCriteria.getValue().equals("")) {
        		specificationMaterialamounts.add(new MaterialamountSpecification(searchCriteria));
			}
        }
 		
		if(specificationMaterialamounts.size() == 0) {
        	return null;
        }
        Specification<Materialamount> result = specificationMaterialamounts.get(0);
        for (int i = 1; i < specificationMaterialamounts.size(); i++) {
        	String logicOperation = ((MaterialamountSpecification)specificationMaterialamounts.get(i-1)).getSearchCriteria().getLogic();
        	if(logicOperation != null && logicOperation.toUpperCase().equals("AND")) {
                result = Specifications.where(result).and(specificationMaterialamounts.get(i));
        	} else {
                result = Specifications.where(result).or(specificationMaterialamounts.get(i));
        	}
        }
        return result;
    }

}

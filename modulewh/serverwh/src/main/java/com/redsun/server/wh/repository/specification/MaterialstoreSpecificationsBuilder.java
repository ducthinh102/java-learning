

package com.redsun.server.wh.repository.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;

import com.redsun.server.wh.model.Materialstore;
import com.redsun.server.wh.model.common.SearchCriteria;

@Component
public class MaterialstoreSpecificationsBuilder {
 
    public Specification<Materialstore> build(List<SearchCriteria> searchCriterias) {
        if (searchCriterias.size() == 0) {
            return null;
        }
 
        List<Specification<Materialstore>> specificationMaterialstores = new ArrayList<Specification<Materialstore>>();
        for (SearchCriteria searchCriteria : searchCriterias) {
			if(!searchCriteria.getValue().equals("")) {
        		specificationMaterialstores.add(new MaterialstoreSpecification(searchCriteria));
			}
        }
 		
		if(specificationMaterialstores.size() == 0) {
        	return null;
        }
        Specification<Materialstore> result = specificationMaterialstores.get(0);
        for (int i = 1; i < specificationMaterialstores.size(); i++) {
        	String logicOperation = ((MaterialstoreSpecification)specificationMaterialstores.get(i-1)).getSearchCriteria().getLogic();
        	if(logicOperation != null && logicOperation.toUpperCase().equals("AND")) {
                result = Specifications.where(result).and(specificationMaterialstores.get(i));
        	} else {
                result = Specifications.where(result).or(specificationMaterialstores.get(i));
        	}
        }
        return result;
    }

}

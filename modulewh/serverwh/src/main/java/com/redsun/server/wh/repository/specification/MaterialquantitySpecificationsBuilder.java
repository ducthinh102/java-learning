

package com.redsun.server.wh.repository.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;

import com.redsun.server.wh.model.Materialquantity;
import com.redsun.server.wh.model.common.SearchCriteria;

@Component
public class MaterialquantitySpecificationsBuilder {
 
    public Specification<Materialquantity> build(List<SearchCriteria> searchCriterias) {
        if (searchCriterias.size() == 0) {
            return null;
        }
 
        List<Specification<Materialquantity>> specificationMaterialquantitys = new ArrayList<Specification<Materialquantity>>();
        for (SearchCriteria searchCriteria : searchCriterias) {
			if(!searchCriteria.getValue().equals("")) {
        		specificationMaterialquantitys.add(new MaterialquantitySpecification(searchCriteria));
			}
        }
 		
		if(specificationMaterialquantitys.size() == 0) {
        	return null;
        }
        Specification<Materialquantity> result = specificationMaterialquantitys.get(0);
        for (int i = 1; i < specificationMaterialquantitys.size(); i++) {
        	String logicOperation = ((MaterialquantitySpecification)specificationMaterialquantitys.get(i-1)).getSearchCriteria().getLogic();
        	if(logicOperation != null && logicOperation.toUpperCase().equals("AND")) {
                result = Specifications.where(result).and(specificationMaterialquantitys.get(i));
        	} else {
                result = Specifications.where(result).or(specificationMaterialquantitys.get(i));
        	}
        }
        return result;
    }

}

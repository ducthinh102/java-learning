
package com.redsun.server.wh.repository.specification;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;

import com.redsun.server.wh.model.Purchase;
import com.redsun.server.wh.model.common.SearchCriteria;

@Component
public class PurchaseSpecificationsBuilder {
 
    public Specification<Purchase> build(List<SearchCriteria> searchCriterias) {
        if (searchCriterias.size() == 0) {
			return null;
		}
		SearchCriteria searchCriteria = searchCriterias.get(0);
		Specification<Purchase> spec1 = null;
		String logic1 = null;
		Specification<Purchase> spec2 = new PurchaseSpecification(searchCriteria);
		String logic2 = searchCriteria.getLogic();
		int size = searchCriterias.size();
		for (int i = 1; i < size; i++) {
			searchCriteria = searchCriterias.get(i);
			if(searchCriteria.getKey().equals("")) {
				if(spec1 == null) {
					spec1 = spec2;
					logic1 = searchCriteria.getLogic();
				} else {
					if (logic1 != null && logic1.toUpperCase().equals("AND")) {
						spec1 = Specifications.where(spec1).and(spec2);
					}
					else {
						spec1 = Specifications.where(spec1).or(spec2);
					}
				}
				i++;
				searchCriteria = searchCriterias.get(i);
				spec2 = new PurchaseSpecification(searchCriteria);
				logic2 = searchCriteria.getLogic();
			} else {
				if (logic2 != null && logic2.toUpperCase().equals("AND")) {
					spec2 = Specifications.where(spec2).and(new PurchaseSpecification(searchCriteria));
				}
				else {
					spec2 = Specifications.where(spec2).or(new PurchaseSpecification(searchCriteria));
				}
				logic2 = searchCriteria.getLogic();
			}
		}
		if(spec1 == null) {
			spec1 = spec2;
		}
		else {
			if (logic1 != null && logic1.toUpperCase().equals("AND")) {
				spec1 = Specifications.where(spec1).and(spec2);
			}
			else {
				spec1 = Specifications.where(spec1).or(spec2);
			}
		}
		return spec1;
    }

}
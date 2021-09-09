
package com.redsun.server.main.repository.specification;

import java.util.List;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;

import com.redsun.server.main.model.Notify;
import com.redsun.server.main.model.common.SearchCriteria;

@Component
public class NotifySpecificationsBuilder {
 
    public Specification<Notify> build(List<SearchCriteria> searchCriterias) {
        if (searchCriterias.size() == 0) {
			return null;
		}
		SearchCriteria searchCriteria = searchCriterias.get(0);
		Specification<Notify> spec1 = null;
		String logic1 = null;
		Specification<Notify> spec2 = new NotifySpecification(searchCriteria);
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
				spec2 = new NotifySpecification(searchCriteria);
				logic2 = searchCriteria.getLogic();
			} else {
				if (logic2 != null && logic2.toUpperCase().equals("AND")) {
					spec2 = Specifications.where(spec2).and(new NotifySpecification(searchCriteria));
				}
				else {
					spec2 = Specifications.where(spec2).or(new NotifySpecification(searchCriteria));
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

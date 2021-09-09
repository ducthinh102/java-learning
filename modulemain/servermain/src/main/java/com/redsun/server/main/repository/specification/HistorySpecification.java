

package com.redsun.server.main.repository.specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.redsun.server.main.model.History;
import com.redsun.server.main.model.common.SearchCriteria;


public class HistorySpecification implements Specification<History> {
	 
    private SearchCriteria searchCriteria;
 
    /**
	 * @return the searchCriteria
	 */
	public SearchCriteria getSearchCriteria() {
		return searchCriteria;
	}

	/**
	 * @param searchCriteria the searchCriteria to set
	 */
	public void setSearchCriteria(SearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	public HistorySpecification(SearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	@Override
    public Predicate toPredicate(Root<History> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		Predicate result = null;
        if (searchCriteria.getOperation().equalsIgnoreCase(">")) {
        	result = builder.greaterThanOrEqualTo(root.<String> get(searchCriteria.getKey()), searchCriteria.getValue().toString());
        } 
        else if (searchCriteria.getOperation().equalsIgnoreCase("<")) {
        	result = builder.lessThanOrEqualTo(root.<String> get(searchCriteria.getKey()), searchCriteria.getValue().toString());
        } 
        else if (searchCriteria.getOperation().equalsIgnoreCase("like")) {
            result = builder.like(root.<String>get(searchCriteria.getKey()), "%" + searchCriteria.getValue() + "%");
        }
		else if (searchCriteria.getOperation().equalsIgnoreCase("=")) {
			result = builder.equal(root.get(searchCriteria.getKey()), searchCriteria.getValue());
		}
        return result;
    }
}

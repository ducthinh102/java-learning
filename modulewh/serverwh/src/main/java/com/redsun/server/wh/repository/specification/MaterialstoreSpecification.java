

package com.redsun.server.wh.repository.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.redsun.server.wh.model.Material;
import com.redsun.server.wh.model.Materialstore;
import com.redsun.server.wh.model.common.SearchCriteria;


public class MaterialstoreSpecification implements Specification<Materialstore> {
	 
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

	public MaterialstoreSpecification(SearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	@Override
    public Predicate toPredicate(Root<Materialstore> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		Predicate result = null;
		query.distinct(true);
		// material.
				if(searchCriteria.getKey().startsWith("material.")){
					//root.fetch("material", JoinType.INNER);
					Join<Materialstore, Material> material = root.join("material");
					if (searchCriteria.getOperation().equalsIgnoreCase("like")) {
			            result = builder.like(builder.lower(material.<String>get(searchCriteria.getKey().replace("material.", ""))), "%" + searchCriteria.getValue().toString().toLowerCase() + "%");
			        }
				}
				// store.
				else if(searchCriteria.getKey().startsWith("store.")){
					//root.fetch("store", JoinType.INNER);
					Join<Materialstore, Material> store = root.join("store");
					if (searchCriteria.getOperation().equalsIgnoreCase("like")) {
			            result = builder.like(builder.lower(store.<String>get(searchCriteria.getKey().replace("store.", ""))), "%" + searchCriteria.getValue().toString().toLowerCase() + "%");
			        }
				}
			else if (searchCriteria.getOperation().equalsIgnoreCase(">")) {
			Comparable<Object> value = (Comparable<Object>) searchCriteria.getValue();
        	result = builder.greaterThan(root.get(searchCriteria.getKey()), value);
        } 
        else if (searchCriteria.getOperation().equalsIgnoreCase("<")) {
        	Comparable<Object> value = (Comparable<Object>) searchCriteria.getValue();
        	result = builder.lessThan(root.get(searchCriteria.getKey()), value);
        } 
        else if (searchCriteria.getOperation().equalsIgnoreCase(">=")) {
        	Comparable<Object> value = (Comparable<Object>) searchCriteria.getValue();
        	result = builder.greaterThanOrEqualTo(root.get(searchCriteria.getKey()), value);
        } 
        else if (searchCriteria.getOperation().equalsIgnoreCase("<=")) {
        	Comparable<Object> value = (Comparable<Object>) searchCriteria.getValue();
        	result = builder.lessThanOrEqualTo(root.get(searchCriteria.getKey()), value);
        } 
        else if (searchCriteria.getOperation().equalsIgnoreCase("like")) {
            result = builder.like(builder.lower(root.<String>get(searchCriteria.getKey())), "%" + searchCriteria.getValue().toString().toLowerCase() + "%");
        }
		else if (searchCriteria.getOperation().equalsIgnoreCase("=")) {
			Comparable<Object> value = (Comparable<Object>) searchCriteria.getValue();
			result = builder.equal(root.get(searchCriteria.getKey()), value);
		}
		else if (searchCriteria.getOperation().equalsIgnoreCase("!=")) {
			Comparable<Object> value = (Comparable<Object>) searchCriteria.getValue();
			result = builder.notEqual(root.get(searchCriteria.getKey()), value);
		}
		else if (searchCriteria.getOperation().equalsIgnoreCase("in")) {
			List<Integer> value = new ArrayList<Integer>();
			value.addAll((ArrayList<Integer>)searchCriteria.getValue());
			result = root.get(searchCriteria.getKey()).in(value);
		}
        return result;
    }
}


package com.redsun.server.wh.repository.specification;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.redsun.server.wh.model.Purchase;
import com.redsun.server.wh.model.Purchasedetail;
import com.redsun.server.wh.model.Store;
import com.redsun.server.wh.model.Supplier;
import com.redsun.server.wh.model.User;
import com.redsun.server.wh.model.common.SearchCriteria;

public class PurchaseSpecification implements Specification<Purchase> {
	 
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

	public PurchaseSpecification(SearchCriteria searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	@Override
    public Predicate toPredicate(Root<Purchase> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
		Predicate result = null;
		query.distinct(true);
		// store.
		if (searchCriteria.getKey().startsWith("store.")) {
			Join<Purchasedetail, Store> store = root.join("store");
			if (searchCriteria.getOperation().equalsIgnoreCase("like")) {
				result = builder.like(builder.lower(store.<String>get(searchCriteria.getKey().replaceAll("store.",  ""))), "%" + searchCriteria.getValue().toString().toLowerCase() + "%");
			}
		}
		else if (searchCriteria.getKey().startsWith("receiver.")) {
			Join<Purchasedetail, User> receiver = root.join("receiver");
			if (searchCriteria.getOperation().equalsIgnoreCase("like")) {
				result = builder.like(builder.lower(receiver.<String>get(searchCriteria.getKey().replace("receiver.", ""))),"%" + searchCriteria.getValue().toString().toLowerCase() + "%");
			}
		}
		else if (searchCriteria.getKey().startsWith("contact.")) {
			Join<Purchasedetail, User> receiver = root.join("contact");
			if (searchCriteria.getOperation().equalsIgnoreCase("like")) {
				result = builder.like(builder.lower(receiver.<String>get(searchCriteria.getKey().replace("contact.", ""))),"%" + searchCriteria.getValue().toString().toLowerCase() + "%");
			}
		}
		else if (searchCriteria.getKey().startsWith("supplier.")) {
			// root.fetch("user", JoinType.INNER);
			Join<Purchasedetail, Supplier> responsible = root.join("supplier");
			if (searchCriteria.getOperation().equalsIgnoreCase("like")) {
				result = builder.like(builder.lower(responsible.<String>get(searchCriteria.getKey().replace("supplier.", ""))),"%" + searchCriteria.getValue().toString().toLowerCase() + "%");
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
		else if (searchCriteria.getOperation().equalsIgnoreCase("notin")) {
			List<Integer> value = new ArrayList<Integer>();
			value.addAll((List<Integer>) searchCriteria.getValue());
			result = root.get(searchCriteria.getKey()).in(value).not();
		}
        return result;
    }
}

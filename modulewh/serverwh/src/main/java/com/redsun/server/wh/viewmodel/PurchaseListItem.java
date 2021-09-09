
package com.redsun.server.wh.viewmodel;

import com.redsun.server.wh.model.Purchase;
import com.redsun.server.wh.viewmodel.ListItem;

public class PurchaseListItem implements ListItem {

	private final Object value;
	private final String label;
	
	public PurchaseListItem(Purchase purchase) {
		this.value = purchase.getId();
		//TODO : Define here the attributes to be displayed as the label
		this.label = purchase.toString();
	}

	public PurchaseListItem(Object value, String label) {
		this.value = value;
		this.label = label;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public String getLabel() {
		return label;
	}

}

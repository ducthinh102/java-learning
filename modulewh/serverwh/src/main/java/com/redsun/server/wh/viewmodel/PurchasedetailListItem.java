
package com.redsun.server.wh.viewmodel;

import com.redsun.server.wh.model.Purchasedetail;
import com.redsun.server.wh.viewmodel.ListItem;

public class PurchasedetailListItem implements ListItem {

	private final Object value;
	private final String label;
	
	public PurchasedetailListItem(Purchasedetail purchasedetail) {
		this.value = purchasedetail.getId();
		//TODO : Define here the attributes to be displayed as the label
		this.label = purchasedetail.toString();
	}

	public PurchasedetailListItem(Object value, String label) {
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

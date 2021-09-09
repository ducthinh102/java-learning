
package com.redsun.server.wh.viewmodel;

import com.redsun.server.wh.model.Supplier;
import com.redsun.server.wh.viewmodel.ListItem;

public class SupplierListItem implements ListItem {

	private final Object value;
	private final String label;
	
	public SupplierListItem(Supplier supplier) {
		this.value = supplier.getId();
		//TODO : Define here the attributes to be displayed as the label
		this.label = supplier.toString();
	}

	public SupplierListItem(Object value, String label) {
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

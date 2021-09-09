
package com.redsun.server.wh.viewmodel;

import com.redsun.server.wh.model.Store;
import com.redsun.server.wh.viewmodel.ListItem;

public class StoreListItem implements ListItem {

	private final Object value;
	private final String label;
	
	public StoreListItem(Store store) {
		this.value = store.getId();
		//TODO : Define here the attributes to be displayed as the label
		this.label = store.toString();
	}

	public StoreListItem(Object value, String label) {
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

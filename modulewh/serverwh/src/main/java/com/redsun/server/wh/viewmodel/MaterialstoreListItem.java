
package com.redsun.server.wh.viewmodel;

import com.redsun.server.wh.model.Materialstore;
import com.redsun.server.wh.viewmodel.ListItem;

public class MaterialstoreListItem implements ListItem {

	private final Object value;
	private final String label;
	
	public MaterialstoreListItem(Materialstore materialstore) {
		this.value = materialstore.getId();
		//TODO : Define here the attributes to be displayed as the label
		this.label = materialstore.toString();
	}

	public MaterialstoreListItem(Object value, String label) {
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

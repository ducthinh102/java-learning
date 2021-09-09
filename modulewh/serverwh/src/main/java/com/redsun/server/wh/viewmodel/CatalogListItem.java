
package com.redsun.server.wh.viewmodel;

import com.redsun.server.wh.model.Catalog;
import com.redsun.server.wh.viewmodel.ListItem;

public class CatalogListItem implements ListItem {

	private final Object value;
	private final String label;
	
	public CatalogListItem(Catalog catalog) {
		this.value = catalog.getId();
		//TODO : Define here the attributes to be displayed as the label
		this.label = catalog.toString();
	}

	public CatalogListItem(Object value, String label) {
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

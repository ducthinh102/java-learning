
package com.redsun.server.wh.viewmodel;

import com.redsun.server.wh.model.Material;
import com.redsun.server.wh.viewmodel.ListItem;

public class MaterialListItem implements ListItem {

	private final Object value;
	private final String label;
	
	public MaterialListItem(Material material) {
		this.value = material.getId();
		//TODO : Define here the attributes to be displayed as the label
		this.label = material.toString();
	}

	public MaterialListItem(Object value, String label) {
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

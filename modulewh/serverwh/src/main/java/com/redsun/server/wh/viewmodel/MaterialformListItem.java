
package com.redsun.server.wh.viewmodel;

import com.redsun.server.wh.model.Materialform;
import com.redsun.server.wh.viewmodel.ListItem;

public class MaterialformListItem implements ListItem {

	private final Object value;
	private final String label;
	
	public MaterialformListItem(Materialform materialform) {
		this.value = materialform.getId();
		//TODO : Define here the attributes to be displayed as the label
		this.label = materialform.toString();
	}

	public MaterialformListItem(Object value, String label) {
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

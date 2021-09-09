
package com.redsun.server.wh.viewmodel;

import com.redsun.server.wh.model.Materialimport;
import com.redsun.server.wh.viewmodel.ListItem;

public class MaterialimportListItem implements ListItem {

	private final Object value;
	private final String label;
	
	public MaterialimportListItem(Materialimport materialimport) {
		this.value = materialimport.getId();
		//TODO : Define here the attributes to be displayed as the label
		this.label = materialimport.toString();
	}

	public MaterialimportListItem(Object value, String label) {
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


package com.redsun.server.wh.viewmodel;

import com.redsun.server.wh.model.Materialbaseline;
import com.redsun.server.wh.viewmodel.ListItem;

public class MaterialbaselineListItem implements ListItem {

	private final Object value;
	private final String label;
	
	public MaterialbaselineListItem(Materialbaseline materialbaseline) {
		this.value = materialbaseline.getId();
		//TODO : Define here the attributes to be displayed as the label
		this.label = materialbaseline.toString();
	}

	public MaterialbaselineListItem(Object value, String label) {
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

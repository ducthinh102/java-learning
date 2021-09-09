
package com.redsun.server.wh.viewmodel;

import com.redsun.server.wh.model.Materialbaselinedetail;
import com.redsun.server.wh.viewmodel.ListItem;

public class MaterialbaselinedetailListItem implements ListItem {

	private final Object value;
	private final String label;
	
	public MaterialbaselinedetailListItem(Materialbaselinedetail materialbaselinedetail) {
		this.value = materialbaselinedetail.getId();
		//TODO : Define here the attributes to be displayed as the label
		this.label = materialbaselinedetail.toString();
	}

	public MaterialbaselinedetailListItem(Object value, String label) {
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

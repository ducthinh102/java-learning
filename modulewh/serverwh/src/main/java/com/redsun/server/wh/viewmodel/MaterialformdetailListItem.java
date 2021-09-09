
package com.redsun.server.wh.viewmodel;

import com.redsun.server.wh.model.Materialformdetail;
import com.redsun.server.wh.viewmodel.ListItem;

public class MaterialformdetailListItem implements ListItem {

	private final Object value;
	private final String label;
	
	public MaterialformdetailListItem(Materialformdetail materialformdetail) {
		this.value = materialformdetail.getId();
		//TODO : Define here the attributes to be displayed as the label
		this.label = materialformdetail.toString();
	}

	public MaterialformdetailListItem(Object value, String label) {
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

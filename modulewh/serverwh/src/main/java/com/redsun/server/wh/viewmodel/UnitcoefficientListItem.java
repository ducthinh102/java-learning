
package com.redsun.server.wh.viewmodel;

import com.redsun.server.wh.model.Unitcoefficient;
import com.redsun.server.wh.viewmodel.ListItem;

public class UnitcoefficientListItem implements ListItem {

	private final Object value;
	private final String label;
	
	public UnitcoefficientListItem(Unitcoefficient unitcoefficient) {
		this.value = unitcoefficient.getId();
		//TODO : Define here the attributes to be displayed as the label
		this.label = unitcoefficient.toString();
	}

	public UnitcoefficientListItem(Object value, String label) {
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

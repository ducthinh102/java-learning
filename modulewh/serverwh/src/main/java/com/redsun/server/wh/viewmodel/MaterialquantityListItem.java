
package com.redsun.server.wh.viewmodel;

import com.redsun.server.wh.model.Materialquantity;
import com.redsun.server.wh.viewmodel.ListItem;

public class MaterialquantityListItem implements ListItem {

	private final Object value;
	private final String label;
	
	public MaterialquantityListItem(Materialquantity materialquantity) {
		this.value = materialquantity.getId();
		//TODO : Define here the attributes to be displayed as the label
		this.label = materialquantity.toString();
	}

	public MaterialquantityListItem(Object value, String label) {
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

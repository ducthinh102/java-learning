
package com.redsun.server.wh.viewmodel;

import com.redsun.server.wh.model.Sign;
import com.redsun.server.wh.viewmodel.ListItem;

public class SignListItem implements ListItem {

	private final Object value;
	private final String label;
	
	public SignListItem(Sign sign) {
		this.value = sign.getId();
		//TODO : Define here the attributes to be displayed as the label
		this.label = sign.toString();
	}

	public SignListItem(Object value, String label) {
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

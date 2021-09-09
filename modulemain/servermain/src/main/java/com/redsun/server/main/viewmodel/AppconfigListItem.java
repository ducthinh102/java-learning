
package com.redsun.server.main.viewmodel;

import com.redsun.server.main.model.Appconfig;
import com.redsun.server.main.viewmodel.ListItem;

public class AppconfigListItem implements ListItem {

	private final Object value;
	private final String label;
	
	public AppconfigListItem(Appconfig appconfig) {
		this.value = appconfig.getId();
		//TODO : Define here the attributes to be displayed as the label
		this.label = appconfig.toString();
	}

	public AppconfigListItem(Object value, String label) {
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

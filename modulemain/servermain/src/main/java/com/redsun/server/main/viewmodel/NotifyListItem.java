
package com.redsun.server.main.viewmodel;

import com.redsun.server.main.model.Notify;
import com.redsun.server.main.viewmodel.ListItem;

public class NotifyListItem implements ListItem {

	private final Object value;
	private final String label;
	
	public NotifyListItem(Notify notify) {
		this.value = notify.getId();
		//TODO : Define here the attributes to be displayed as the label
		this.label = notify.toString();
	}

	public NotifyListItem(Object value, String label) {
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

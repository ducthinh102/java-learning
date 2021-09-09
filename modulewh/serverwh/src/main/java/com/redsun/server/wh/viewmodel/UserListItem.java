
package com.redsun.server.wh.viewmodel;

import com.redsun.server.wh.model.User;
import com.redsun.server.wh.viewmodel.ListItem;

public class UserListItem implements ListItem {

	private final Object value;
	private final String label;
	
	public UserListItem(User user) {
		this.value = user.getId();
		//TODO : Define here the attributes to be displayed as the label
		this.label = user.toString();
	}

	public UserListItem(Object value, String label) {
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

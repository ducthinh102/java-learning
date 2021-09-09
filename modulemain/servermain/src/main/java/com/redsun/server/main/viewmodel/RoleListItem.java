
package com.redsun.server.main.viewmodel;

import com.redsun.server.main.model.Role;
import com.redsun.server.main.viewmodel.ListItem;

public class RoleListItem implements ListItem {

	private final Object value;
	private final String label;
	
	public RoleListItem(Role role) {
		this.value = role.getId();
		//TODO : Define here the attributes to be displayed as the label
		this.label = role.toString();
	}

	public RoleListItem(Object value, String label) {
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

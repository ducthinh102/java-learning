
package com.redsun.server.main.viewmodel;

import com.redsun.server.main.model.Permission;
import com.redsun.server.main.viewmodel.ListItem;

public class PermissionListItem implements ListItem {

	private final Object value;
	private final String label;
	
	public PermissionListItem(Permission permission) {
		this.value = permission.getId();
		//TODO : Define here the attributes to be displayed as the label
		this.label = permission.toString();
	}

	public PermissionListItem(Object value, String label) {
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

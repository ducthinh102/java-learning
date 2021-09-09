
package com.redsun.server.main.viewmodel;

import com.redsun.server.main.model.Userrole;
import com.redsun.server.main.viewmodel.ListItem;

public class UserroleListItem implements ListItem {

	private final Object value;
	private final String label;
	
	public UserroleListItem(Userrole userrole) {
		this.value = userrole.getId();
		//TODO : Define here the attributes to be displayed as the label
		this.label = userrole.toString();
	}

	public UserroleListItem(Object value, String label) {
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

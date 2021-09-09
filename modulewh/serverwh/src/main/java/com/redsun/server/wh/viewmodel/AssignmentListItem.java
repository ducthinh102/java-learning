
package com.redsun.server.wh.viewmodel;

import com.redsun.server.wh.model.Assignment;
import com.redsun.server.wh.viewmodel.ListItem;

public class AssignmentListItem implements ListItem {

	private final Object value;
	private final String label;
	
	public AssignmentListItem(Assignment assignment) {
		this.value = assignment.getId();
		//TODO : Define here the attributes to be displayed as the label
		this.label = assignment.toString();
	}

	public AssignmentListItem(Object value, String label) {
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

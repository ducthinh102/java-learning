
package com.redsun.server.wh.viewmodel;

import com.redsun.server.wh.model.Workflowdefine;
import com.redsun.server.wh.viewmodel.ListItem;

public class WorkflowdefineListItem implements ListItem {

	private final Object value;
	private final String label;
	
	public WorkflowdefineListItem(Workflowdefine workflowdefine) {
		this.value = workflowdefine.getId();
		//TODO : Define here the attributes to be displayed as the label
		this.label = workflowdefine.toString();
	}

	public WorkflowdefineListItem(Object value, String label) {
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

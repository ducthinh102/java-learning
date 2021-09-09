
package com.redsun.server.wh.viewmodel;

import com.redsun.server.wh.model.Workflow;
import com.redsun.server.wh.viewmodel.ListItem;

public class WorkflowListItem implements ListItem {

	private final Object value;
	private final String label;
	
	public WorkflowListItem(Workflow workflow) {
		this.value = workflow.getId();
		//TODO : Define here the attributes to be displayed as the label
		this.label = workflow.toString();
	}

	public WorkflowListItem(Object value, String label) {
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

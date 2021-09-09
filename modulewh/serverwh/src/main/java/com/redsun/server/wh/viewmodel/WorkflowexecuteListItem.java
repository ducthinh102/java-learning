
package com.redsun.server.wh.viewmodel;

import com.redsun.server.wh.model.Workflowexecute;
import com.redsun.server.wh.viewmodel.ListItem;

public class WorkflowexecuteListItem implements ListItem {

	private final Object value;
	private final String label;
	
	public WorkflowexecuteListItem(Workflowexecute workflowexecute) {
		this.value = workflowexecute.getId();
		//TODO : Define here the attributes to be displayed as the label
		this.label = workflowexecute.toString();
	}

	public WorkflowexecuteListItem(Object value, String label) {
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


package com.redsun.server.wh.viewmodel;

import com.redsun.server.wh.model.Request;
import com.redsun.server.wh.viewmodel.ListItem;

public class RequestListItem implements ListItem {

	private final Object value;
	private final String label;
	
	public RequestListItem(Request request) {
		this.value = request.getId();
		//TODO : Define here the attributes to be displayed as the label
		this.label = request.toString();
	}

	public RequestListItem(Object value, String label) {
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

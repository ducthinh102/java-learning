
package com.redsun.server.wh.viewmodel;

import com.redsun.server.wh.model.Requestdetail;
import com.redsun.server.wh.viewmodel.ListItem;

public class RequestdetailListItem implements ListItem {

	private final Object value;
	private final String label;
	
	public RequestdetailListItem(Requestdetail requestdetail) {
		this.value = requestdetail.getId();
		//TODO : Define here the attributes to be displayed as the label
		this.label = requestdetail.toString();
	}

	public RequestdetailListItem(Object value, String label) {
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

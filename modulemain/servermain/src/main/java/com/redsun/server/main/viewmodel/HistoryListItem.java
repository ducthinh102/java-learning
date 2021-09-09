
package com.redsun.server.main.viewmodel;

import com.redsun.server.main.model.History;
import com.redsun.server.main.viewmodel.ListItem;

public class HistoryListItem implements ListItem {

	private final Object value;
	private final String label;
	
	public HistoryListItem(History history) {
		this.value = history.getId();
		//TODO : Define here the attributes to be displayed as the label
		this.label = history.toString();
	}

	public HistoryListItem(Object value, String label) {
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

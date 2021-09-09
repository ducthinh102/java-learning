
package com.redsun.server.wh.viewmodel;

import com.redsun.server.wh.model.Calendar;
import com.redsun.server.wh.viewmodel.ListItem;

public class CalendarListItem implements ListItem {

	private final Object value;
	private final String label;
	
	public CalendarListItem(Calendar calendar) {
		this.value = calendar.getId();
		//TODO : Define here the attributes to be displayed as the label
		this.label = calendar.toString();
	}

	public CalendarListItem(Object value, String label) {
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

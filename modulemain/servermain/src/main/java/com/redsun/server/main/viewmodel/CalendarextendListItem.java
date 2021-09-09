
package com.redsun.server.main.viewmodel;

import com.redsun.server.main.model.Calendarextend;
import com.redsun.server.main.viewmodel.ListItem;

public class CalendarextendListItem implements ListItem {

	private final Object value;
	private final String label;
	
	public CalendarextendListItem(Calendarextend calendarextend) {
		this.value = calendarextend.getId();
		//TODO : Define here the attributes to be displayed as the label
		this.label = calendarextend.toString();
	}

	public CalendarextendListItem(Object value, String label) {
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

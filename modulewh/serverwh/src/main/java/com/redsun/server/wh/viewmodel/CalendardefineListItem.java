
package com.redsun.server.wh.viewmodel;

import com.redsun.server.wh.model.Calendardefine;
import com.redsun.server.wh.viewmodel.ListItem;

public class CalendardefineListItem implements ListItem {

	private final Object value;
	private final String label;
	
	public CalendardefineListItem(Calendardefine calendardefine) {
		this.value = calendardefine.getId();
		//TODO : Define here the attributes to be displayed as the label
		this.label = calendardefine.toString();
	}

	public CalendardefineListItem(Object value, String label) {
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


package com.redsun.server.wh.viewmodel;

import com.redsun.server.wh.model.Materialexport;
import com.redsun.server.wh.viewmodel.ListItem;

public class MaterialexportListItem implements ListItem {

	private final Object value;
	private final String label;
	
	public MaterialexportListItem(Materialexport materialexport) {
		this.value = materialexport.getId();
		//TODO : Define here the attributes to be displayed as the label
		this.label = materialexport.toString();
	}

	public MaterialexportListItem(Object value, String label) {
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

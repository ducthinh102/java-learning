
package com.redsun.server.wh.viewmodel;

import com.redsun.server.wh.model.Materialimportdetail;
import com.redsun.server.wh.viewmodel.ListItem;

public class MaterialimportdetailListItem implements ListItem {

	private final Object value;
	private final String label;
	
	public MaterialimportdetailListItem(Materialimportdetail materialimportdetail) {
		this.value = materialimportdetail.getId();
		//TODO : Define here the attributes to be displayed as the label
		this.label = materialimportdetail.toString();
	}

	public MaterialimportdetailListItem(Object value, String label) {
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

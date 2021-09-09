
package com.redsun.server.wh.viewmodel;

import com.redsun.server.wh.model.Materialexportdetail;
import com.redsun.server.wh.viewmodel.ListItem;

public class MaterialexportdetailListItem implements ListItem {

	private final Object value;
	private final String label;
	
	public MaterialexportdetailListItem(Materialexportdetail materialexportdetail) {
		this.value = materialexportdetail.getId();
		//TODO : Define here the attributes to be displayed as the label
		this.label = materialexportdetail.toString();
	}

	public MaterialexportdetailListItem(Object value, String label) {
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

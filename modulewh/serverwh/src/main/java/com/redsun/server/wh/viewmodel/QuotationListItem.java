
package com.redsun.server.wh.viewmodel;

import com.redsun.server.wh.model.Quotation;
import com.redsun.server.wh.viewmodel.ListItem;

public class QuotationListItem implements ListItem {

	private final Object value;
	private final String label;
	
	public QuotationListItem(Quotation quotation) {
		this.value = quotation.getId();
		//TODO : Define here the attributes to be displayed as the label
		this.label = quotation.toString();
	}

	public QuotationListItem(Object value, String label) {
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

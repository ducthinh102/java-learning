
package com.redsun.server.wh.viewmodel;

import com.redsun.server.wh.model.Quotationdetail;
import com.redsun.server.wh.viewmodel.ListItem;

public class QuotationdetailListItem implements ListItem {

	private final Object value;
	private final String label;
	
	public QuotationdetailListItem(Quotationdetail quotationdetail) {
		this.value = quotationdetail.getId();
		//TODO : Define here the attributes to be displayed as the label
		this.label = quotationdetail.toString();
	}

	public QuotationdetailListItem(Object value, String label) {
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

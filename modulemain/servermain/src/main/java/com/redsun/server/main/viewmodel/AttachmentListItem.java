
package com.redsun.server.main.viewmodel;

import com.redsun.server.main.model.Attachment;
import com.redsun.server.main.viewmodel.ListItem;

public class AttachmentListItem implements ListItem {

	private final Object value;
	private final String label;
	
	public AttachmentListItem(Attachment attachment) {
		this.value = attachment.getId();
		//TODO : Define here the attributes to be displayed as the label
		this.label = attachment.toString();
	}

	public AttachmentListItem(Object value, String label) {
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

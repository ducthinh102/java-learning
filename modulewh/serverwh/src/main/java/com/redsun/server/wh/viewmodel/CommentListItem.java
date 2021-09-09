
package com.redsun.server.wh.viewmodel;

import com.redsun.server.wh.model.Comment;
import com.redsun.server.wh.viewmodel.ListItem;

public class CommentListItem implements ListItem {

	private final Object value;
	private final String label;
	
	public CommentListItem(Comment comment) {
		this.value = comment.getId();
		//TODO : Define here the attributes to be displayed as the label
		this.label = comment.toString();
	}

	public CommentListItem(Object value, String label) {
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

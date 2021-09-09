
package com.redsun.server.main.viewmodel;

import com.redsun.server.main.model.Comment;
import com.redsun.server.main.viewmodel.ListItem;

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

package com.redsun.server.wh.model.event;

import com.redsun.server.wh.model.User;
import com.redsun.server.wh.model.common.ModelEvent;

public class UserEvent implements ModelEvent<User> {
	
	private User user;

	public UserEvent(User user) {
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}

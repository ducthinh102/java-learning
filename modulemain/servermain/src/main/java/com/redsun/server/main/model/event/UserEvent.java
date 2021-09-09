package com.redsun.server.main.model.event;

import com.redsun.server.main.model.User;
import com.redsun.server.main.model.common.ModelEvent;

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

package com.redsun.server.gateway.model.event;

import com.redsun.server.gateway.model.Users;
import com.redsun.server.gateway.model.common.ModelEvent;

public class UserEvent implements ModelEvent<Users> {
	
	private Users user;
	
	public UserEvent(Users user) {
		this.user = user;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

}

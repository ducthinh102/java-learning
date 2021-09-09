package com.redsun.server.main.model.event;

import com.redsun.server.main.model.Notify;
import com.redsun.server.main.model.common.ModelEvent;

public class NotifyEvent implements ModelEvent<Notify> {
	
	private Notify notify;

	public NotifyEvent(Notify notify) {
		this.notify = notify;
	}
	
	public Notify getNotify() {
		return notify;
	}

	public void setNotify(Notify notify) {
		this.notify = notify;
	}

}

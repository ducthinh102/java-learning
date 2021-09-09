package com.redsun.server.wh.model.event;

import com.redsun.server.wh.model.Notify;
import com.redsun.server.wh.model.common.ModelEvent;

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

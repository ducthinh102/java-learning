package com.redsun.server.wh.model.event.listener;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.redsun.server.wh.common.Constant;
import com.redsun.server.wh.common.ServiceTransactionException;
import com.redsun.server.wh.model.event.UserEvent;

@Component
public class UserEventListener {
	
    private static final Logger log = LoggerFactory.getLogger(UserEventListener.class);
    public Map<Integer, Map<String, Object>> watchers = new HashMap<Integer, Map<String, Object>>();
    
	@TransactionalEventListener(phase=TransactionPhase.BEFORE_COMMIT)
    public void userTransactional(UserEvent userEvent) throws ServiceTransactionException {
		log.info("userTransactional");
		// Keep current thread.
		Thread thread = Thread.currentThread();
		Map<String, Object> watcher = new HashMap<String, Object>();
		watcher.put("thread", thread);
		watcher.put("event", userEvent);
		watcher.put("status", 1);// Success.
		Integer id = userEvent.getUser().getId();
		watchers.put(id, watcher);// check exist before add.
		// Current thread go to sleep.
		try {
			Thread.sleep(3600000);// sleep in 1h.
		} catch(InterruptedException ex) {
			log.info("Successed: " + userEvent.toString());
			watchers.remove(id);
			Integer status = (Integer) watcher.get("status");
			switch(status) {
				// Success.
				case Constant.SERVICETRANSACTION_SUCCESS: {
					return;
				}
				// Abort.
				case Constant.SERVICETRANSACTION_ABORT: {
					ServiceTransactionException serviceTransactionException = new ServiceTransactionException();
					throw serviceTransactionException; // Cause error to abort transaction.
				}
			}
		}
		
    }

	public Boolean confirmStatus(Integer id, int status) {
		Map<String, Object> watcher = watchers.get(id);
		if(null == watcher) {
			return false;
		}
		
		watcher.replace("status", status);
		Thread thread = (Thread) watcher.get("thread");
		thread.interrupt();
		
		return true;
	}
	
}

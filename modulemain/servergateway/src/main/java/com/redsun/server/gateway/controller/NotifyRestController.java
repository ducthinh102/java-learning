

package com.redsun.server.gateway.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/notify")
public class NotifyRestController {

	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	@MessageMapping("/notify")
    @SendTo("/topic/notify")
    public Map<String, Object> notify(Map<String, Object> data) throws Exception {
        Thread.sleep(1000); // simulated delay
		//Map<String, Object> result = new HashMap<String, Object>();
		return data;
    }
	
	@MessageExceptionHandler
    public String handleException(Throwable exception) {
        messagingTemplate.convertAndSend("/error", exception.getMessage());
	    return exception.getMessage();
    }
	
	@RequestMapping(value = "/broadcast", method = RequestMethod.POST)
	public ResponseEntity<?> broadcast(@RequestBody Map<String, Object> data) {
		messagingTemplate.convertAndSend("/topic/notify", data);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	
}

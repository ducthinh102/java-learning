package com.redsun.server.main.model.event.listener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.redsun.server.main.common.ServiceTransactionException;
import com.redsun.server.main.model.Notify;
import com.redsun.server.main.model.event.NotifyEvent;
import com.redsun.server.main.util.RestAPIHelper;

@Component
public class NotifyEventListener {
	
    //private static final Logger log = LoggerFactory.getLogger(NotifyEventListener.class);

	@Autowired
	private Environment evn;
    
	@TransactionalEventListener(phase=TransactionPhase.AFTER_COMMIT)
    public void notifyTransactional(NotifyEvent notifyEvent) throws ServiceTransactionException, KeyManagementException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException {
		Notify notify = notifyEvent.getNotify();
	    String serverauthUrl = evn.getProperty("serverauth.url");
	    final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("idsender", notify.getIdsender());
        data.put("idreceiver", notify.getIdreceiver());
        data.put("idref", notify.getIdref());
        data.put("reftype", notify.getReftype());
        data.put("method", notify.getMethod());
        data.put("content", notify.getContent());
        data.put("status", notify.getStatus());
        data.put("module", "servermain");
		// Call to notify server.
        RestAPIHelper.exchangeSSLService(serverauthUrl + "/notify/broadcast", HttpMethod.POST, null, null, data, new HashMap<String, Object>());

    }
	
}

package com.redsun.server.wh.util;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class RestAPIHelper {

    /**
     * post method helper
     * @param uri
     * @param postData
     * @return
     */
    public static Object post(final String uri, final Object postData){
        final RestTemplate restTemplate = new RestTemplate();
        return restTemplate.postForObject(uri, postData, postData.getClass());
    }

     /**
     * this method use for delete/put http method or any http method need to
      * pass both body & urlVariable to restful service
     * @param uri
     * @param urlVariables
     * @return
     */
    public static Object generalMethod(final String uri, final HttpMethod httpMethod, final Object bodyData, final Map<String, ?> urlVariables){
        final RestTemplate restTemplate = new RestTemplate();
        // create header of http entity
        final HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        // set body = bodyData & header to http entity
        final HttpEntity<Object> entity = new HttpEntity<Object>(bodyData, headers);
        // if urlVariable is empty or null then skip it
        if(urlVariables == null || urlVariables.isEmpty()){
            // send data to restful
            return restTemplate.exchange(uri, httpMethod, entity, Object.class);
        }
        // send data to restful
        return restTemplate.exchange(uri, httpMethod, entity, Object.class, urlVariables);
    }

    /**
     * put method helper
     * @param uri
     * @param bodyData
     * @param urlVariables
     * @return
     */
    public static Object put(final String uri, final Object bodyData, final Map<String, ?> urlVariables){
        return RestAPIHelper.generalMethod(uri, HttpMethod.PUT, bodyData, urlVariables);
    }

    /**
     * delete method helper
     * @param uri
     * @param bodyData
     * @param urlVariables
     * @return
     */
    public static Object delete(final String uri, final Object bodyData, final Map<String, ?> urlVariables){
        return RestAPIHelper.generalMethod(uri, HttpMethod.DELETE, bodyData, urlVariables);
    }


    /**
     * get method helper
     * @param uri
     * @param urlVariables
     * @return
     */
    public static ResponseEntity<?> get(final String uri, final Map<String, ?> urlVariables){
        final RestTemplate restTemplate = new RestTemplate();
        if(urlVariables == null){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return restTemplate.getForObject(uri, ResponseEntity.class, urlVariables);
    }
    
    public static ResponseEntity<Object> exchangeService(final String url, HttpMethod httpMethod, final HttpHeaders httpHeaders, final HttpEntity<?> httpEntity, final Map<String, ?> urlVariables) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, KeyManagementException, UnrecoverableKeyException{
	    RestTemplate restTemplate = new RestTemplate();
        // UrlVariable.
        if(urlVariables == null || urlVariables.isEmpty()){
            return restTemplate.exchange(url, httpMethod, httpEntity, Object.class);
        }
        return restTemplate.exchange(url, httpMethod, httpEntity, Object.class, urlVariables);
    }
    
    public static ResponseEntity<Object> exchangeSSLService(final String url, HttpMethod httpMethod, final String token, final Map<String, ?> headers, final Object bodyData, final Map<String, ?> urlVariables) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, KeyManagementException, UnrecoverableKeyException{
    	KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		//KeyStore keyStore = KeyStore.getInstance("PKCS12");
	    keyStore.load(new FileInputStream(new ClassPathResource("keystore.jks").getFile()), "authserverstorepass".toCharArray());
	    SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(
	    		SSLContexts.custom()
	                    .loadTrustMaterial(null, new TrustSelfSignedStrategy())
	                    .loadKeyMaterial(keyStore, "authserverkeypass".toCharArray())
	                    .build(),
	            NoopHostnameVerifier.INSTANCE);

	    HttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).build();
	    ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
	    RestTemplate restTemplate = new RestTemplate(requestFactory);
	    
	    HttpHeaders httpHeaders = new HttpHeaders();
	    httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    if(token != null){
		    httpHeaders.add("Authorization", "Bearer " + token);
	    }
	    if(null != headers) {
	    	for (String key : headers.keySet()) {
				if(key.toLowerCase() != "authorization") {
					httpHeaders.add(key, headers.get(key).toString());
				}
			}
	    }
        HttpEntity<Object> entity;// = new HttpEntity<Object>(bodyData, httpHeaders);
        if(null == bodyData){
        	entity = new HttpEntity<Object>(httpHeaders);
        } else {
        	entity = new HttpEntity<Object>(bodyData, httpHeaders);
        }

        if(urlVariables == null || urlVariables.isEmpty()){
            return restTemplate.exchange(url, httpMethod, entity, Object.class);
        }
        return restTemplate.exchange(url, httpMethod, entity, Object.class, urlVariables);
    }
    
    
    public static ResponseEntity<Object> getSSLForEntity(final String url) throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

		SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
		        .loadTrustMaterial(null, acceptingTrustStrategy)
		        .build();

		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

		CloseableHttpClient httpClient = HttpClients.custom()
		        .setSSLSocketFactory(csf)
		        .build();

		HttpComponentsClientHttpRequestFactory requestFactory =
		        new HttpComponentsClientHttpRequestFactory();

		requestFactory.setHttpClient(httpClient);

		RestTemplate restTemplate = new RestTemplate(requestFactory);
		
		//String url = "https://www.google.com.vn";
		ResponseEntity<Object> result = restTemplate.getForEntity(url, Object.class);
	    
	    return result;
    }
    
}

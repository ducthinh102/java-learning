package com.redsun.server.gateway.util;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redsun.server.gateway.common.Constant;

public class RestAPIHelper {
	
	@Autowired
	ObjectMapper objectMapperAutowired;
	
	static ObjectMapper objectMapper;
	
	@Autowired
	public void setObjectMapper(ObjectMapper objectMapper){
		RestAPIHelper.objectMapper = objectMapper;
	}

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
    
    public static ResponseEntity<Object> exchangeService(final String url, final HttpMethod httpMethod, final HttpHeaders httpHeaders, final HttpEntity<?> httpEntity, final Map<String, ?> urlVariables) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, KeyManagementException, UnrecoverableKeyException{
	    RestTemplate restTemplate = new RestTemplate();
        // UrlVariable.
        if(urlVariables == null || urlVariables.isEmpty()){
            return restTemplate.exchange(url, httpMethod, httpEntity, Object.class);
        }
        return restTemplate.exchange(url, httpMethod, httpEntity, Object.class, urlVariables);
    }
    
    public static List<ResponseEntity<Object>> exchangeService(final List<String> moduleNames, final String url, final HttpMethod httpMethod, final Map<String, ?> data, final Map<String, ?> urlVariables) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, KeyManagementException, UnrecoverableKeyException{
    	List<ResponseEntity<Object>> result = new ArrayList<ResponseEntity<Object>>();
	    final RestTemplate restTemplate = new RestTemplate();
	    // Get http request.
	    final HttpServletRequest httpRequest = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
	    final String userInfoStr = httpRequest.getHeader(Constant.USSER_INFO);
	    // Create http header.
	    final HttpHeaders httpHeaders = new HttpHeaders();
	    httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
	    httpHeaders.set(Constant.USSER_INFO, userInfoStr);
	    httpHeaders.set("ConId", httpRequest.getHeader("ConId"));
	    // Loop modules.
	    final Map<String, Object> userInfo = objectMapper.readValue(userInfoStr, new TypeReference<Map<String, Object>>(){});
	    final List<Map<String, Object>> modules = (List<Map<String, Object>>) userInfo.get("modules");
	    for (Map<String, Object> module: modules){
	    	String moduleName = (String) module.get("name");
	    	// check module name.
	    	if(moduleNames.contains(moduleName)){
		    	Map<String, Object> info = objectMapper.readValue(CommonUtil.decrypt(module.get("info").toString()), new TypeReference<Map<String, Object>>(){});
		    	String serverUrl = (String) info.get("url");
		    	HttpEntity<Map<String, ?>> httpEntity;
		    	if(null == data){
		    		httpEntity = new HttpEntity<>(httpHeaders);
		        } else {
		        	httpEntity = new HttpEntity<>(data, httpHeaders);
		        }
		        // Call server.
		        if(urlVariables == null || urlVariables.isEmpty()){
		        	ResponseEntity<Object> resultRemote = restTemplate.exchange(serverUrl + url, httpMethod, httpEntity, Object.class);
		        	// add to result.
		        	result.add(resultRemote);
		        } else {
		        	ResponseEntity<Object> resultRemote = restTemplate.exchange(serverUrl + url, httpMethod, httpEntity, Object.class, urlVariables);
		        	// add to result.
		        	result.add(resultRemote);
		        }
	    	}
	    }
	    // return.
	    return result;
    }
    
    public static ResponseEntity<Object> exchangeFileService(final URI uri, HttpMethod httpMethod, final HttpHeaders httpHeaders, final HttpEntity<?> httpEntity) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException, KeyManagementException, UnrecoverableKeyException{
	    RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(uri, httpMethod, httpEntity, Object.class);
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
	    httpHeaders.add("Authorization", "Bearer " + token);
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

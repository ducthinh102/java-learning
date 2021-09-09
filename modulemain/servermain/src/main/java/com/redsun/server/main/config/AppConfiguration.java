package com.redsun.server.main.config;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redsun.server.main.common.Constant;
import com.redsun.server.main.model.Appconfig;
import com.redsun.server.main.service.AppconfigService;
import com.redsun.server.main.util.CommonUtil;

@Configuration
public class AppConfiguration {

	@Autowired
	Environment env;
	
	@Autowired
	public AppconfigService appconfigService;
	
	// -------------------Appconfig-------------------------------------------
	
	private Map<String, Object> appconfigRepo;
	public void reloadAppconfigRepo() throws JsonParseException, JsonMappingException, IOException {
		appconfigRepo = new HashMap<String, Object>();
		ObjectMapper objectMapper = new ObjectMapper();
		List<Appconfig> appconfigs = appconfigService.listAll();
		for (Appconfig appconfig : appconfigs) {
			Map<String, Object> content = objectMapper.readValue(appconfig.getContent(), new TypeReference<Map<String, Object>>(){});
			appconfigRepo.put(appconfig.getScope(), content);
		}
	}
	// Instance appconfig.
	public Map<String, Object> instance() throws JsonParseException, JsonMappingException, IOException {
		if(null == appconfigRepo) {
			reloadAppconfigRepo();
		}
		// return.
		return appconfigRepo;
	}

	// -------------------Mail Server Config-------------------------------------------
	
	private JavaMailSender javaMailSender;
	public void reloadMailSenderRepo() throws JsonParseException, JsonMappingException, IOException {
		JavaMailSenderImpl result = new JavaMailSenderImpl();
		// Get data from config.
		Map<String, Object> appconfig = instance();
		@SuppressWarnings("unchecked")
		Map<String, Object> mailserver = (Map<String, Object>) appconfig.get(Constant.APPCONFIG_SCOPE_MAILSERVER);
		// Set data.
		result.setHost(mailserver.get("host").toString());
		result.setPort(Integer.parseInt(mailserver.get("port").toString()));
		result.setProtocol(mailserver.get("protocol").toString());
		result.setUsername(mailserver.get("username").toString());
		result.setPassword(CommonUtil.decrypt(mailserver.get("password").toString()));
		// Set properties.
		result.setJavaMailProperties(getMailProperties());
		javaMailSender = result;
	}
	// Get properties from config file.
	private Properties getMailProperties() throws JsonParseException, JsonMappingException, IOException {
		
		Properties properties = new Properties();
/*
		// Amazon.
		properties.setProperty("spring.mail.properties.mail.smtp.auth", env.getProperty("servermail.auth"));
		properties.setProperty("spring.mail.properties.mail.smtp.starttls.enable", env.getProperty("servermail.starttlsenable"));
		properties.setProperty("spring.mail.properties.mail.smtp.starttls.required", env.getProperty("servermail.starttlsrequired"));
		properties.setProperty("spring.mail.properties.mail.smtp.ssl.enable", env.getProperty("servermail.sslenable"));
		properties.setProperty("spring.mail.test-connection", env.getProperty("servermail.testconnection"));
		properties.setProperty("spring.mail.defaultEncoding", env.getProperty("servermail.encoding"));
*/
		// Gmail. 
		// Go to gmail->settings->Forwarding and POP/IMAP: IMAP is enabled.
		// Go to url: https://myaccount.google.com/lesssecureapps. Turn on.
		properties.setProperty("mail.smtp.auth", env.getProperty("servermail.auth"));
		properties.setProperty("mail.smtp.starttls.enable", env.getProperty("servermail.starttlsenable"));
		properties.setProperty("mail.smtp.starttls.required", env.getProperty("servermail.starttlsrequired"));
		properties.setProperty("mail.smtp.ssl.enable", env.getProperty("servermail.sslenable"));
		properties.setProperty("mail.test-connection", env.getProperty("servermail.testconnection"));
		properties.setProperty("mail.defaultEncoding", env.getProperty("servermail.encoding"));
		
		return properties;
	}
	// Instance mail server.
	private JavaMailSender instanceJavaMailSender() throws JsonParseException, JsonMappingException, IOException {
		if(null == javaMailSender) {
			reloadMailSenderRepo();
		}
		// return.
		return javaMailSender;
	}
	
	@Bean
	public JavaMailSender javaMailSender() throws JsonParseException, JsonMappingException, IOException {
		return instanceJavaMailSender();
	}
	
}

package com.redsun.server.main;

import java.io.IOException;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.redsun.server.main.util.MailHelper;

@SpringBootApplication(
		exclude = { DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class }
	)
@ComponentScan({"com.redsun.server.main"})
@EntityScan("com.redsun.server.main.model")
@EnableJpaRepositories("com.redsun.server.main.repository")
@EnableResourceServer
public class ServermainApplication extends SpringBootServletInitializer {

	private static final Logger logger = LoggerFactory.getLogger(ServermainApplication.class);

	@Autowired
	MailHelper mailHelper;
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		// TimeZone.setDefault(TimeZone.getTimeZone("GMT+00:00"));
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		System.setProperty("jasypt.encryptor.password", "@redsun");
        return application.sources(ServermainApplication.class);
    }

	public static void main(String[] args) {
		// TimeZone.setDefault(TimeZone.getTimeZone("GMT+00:00"));
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		System.setProperty("jasypt.encryptor.password", "@redsun");
		SpringApplication.run(ServermainApplication.class, args);
		logger.info("...... App started ......");
	}
	
	@EventListener(ApplicationReadyEvent.class)
	public void applicationReady() throws JsonParseException, JsonMappingException, IOException {
		logger.info("App is ready!!!");
		//mailHelper.send("vnthanh25@gmail.com", "Subject Test", "Content test.");
		
	}
}

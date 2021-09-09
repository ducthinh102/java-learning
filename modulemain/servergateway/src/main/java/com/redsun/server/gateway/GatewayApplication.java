package com.redsun.server.gateway;

import java.security.KeyPair;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

@SpringBootApplication
@ComponentScan({"com.redsun.server.gateway"})
@EntityScan("com.redsun.server.gateway.model")
@EnableJpaRepositories("com.redsun.server.gateway.repository")
@EnableZuulProxy
public class GatewayApplication extends SpringBootServletInitializer {

	private static final Logger logger = LoggerFactory.getLogger(GatewayApplication.class);
	public static KeyPair keyPair = new KeyStoreKeyFactory(new ClassPathResource("keystore.jks"), "authserverstorepass".toCharArray()).getKeyPair("authserver", "authserverkeypass".toCharArray());;
	
	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		System.setProperty("jasypt.encryptor.password", "@redsun");
        return application.sources(GatewayApplication.class);
    }

	public static void main(String[] args) {
		System.setProperty("jasypt.encryptor.password", "@redsun");
		SpringApplication.run(GatewayApplication.class, args);
		logger.info("...... App started ......");
	}
}

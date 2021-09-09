package com.redsun.server.wh.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@Order(-1)
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
	
   @Override
   protected void configure(HttpSecurity http) throws Exception {
	   http
		.csrf().disable()
		.authorizeRequests()
		.antMatchers(HttpMethod.OPTIONS, "/*").permitAll();
//		.anyRequest().authenticated();
   }
}

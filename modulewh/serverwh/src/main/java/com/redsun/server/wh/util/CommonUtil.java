package com.redsun.server.wh.util;

import java.beans.FeatureDescriptor;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CommonUtil {

	public static String[] getNullPropertyNames(Object source) {
	    final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
	    return Stream.of(wrappedSource.getPropertyDescriptors())
	            .map(FeatureDescriptor::getName)
	            .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
	            .toArray(String[]::new);
	}
	
	public static String encryptPassword(String password) {
		PasswordEncoder encoder = new BCryptPasswordEncoder(10);
		return encoder.encode(password);
	}
	
	public static String encrypt(String content) {
		StandardPBEStringEncryptor cryptor = new StandardPBEStringEncryptor();
		cryptor.setPassword("@redsun");
		return cryptor.encrypt(content);
	}
	
	public static String decrypt(String content) {
		StandardPBEStringEncryptor cryptor = new StandardPBEStringEncryptor();
		cryptor.setPassword("@redsun");
		return cryptor.decrypt(content);
	}
	
	public static String getBaseUrl(HttpServletRequest request) {
	    return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}
		
}

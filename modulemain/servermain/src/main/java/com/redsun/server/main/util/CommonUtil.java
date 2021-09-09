package com.redsun.server.main.util;

import java.beans.FeatureDescriptor;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.DateUtils;
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
	
	
	public static Integer getDurationWithCalendar(Date startDate, Date endDate, Map<String, Object> calendar) {
		if(endDate.compareTo(startDate) < 0){
			return -1;
		}
		Integer result = 0;
		Date currentDate = startDate;
		List<Date> workdays = (List<Date>) calendar.get("workdays");
		List<Date> offdays = (List<Date>) calendar.get("offdays");
		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		while(currentDate.compareTo(endDate) <= 0){
			cal.setTime(currentDate);
			int dayofweeknum = cal.get(Calendar.DAY_OF_WEEK);
			Map<String, Object> dayofweek = (Map<String, Object>) calendar.get("dayofweek");
			Map<String, Object> dayofweeklabel = (Map<String, Object>) dayofweek.get("" + dayofweeknum);
			boolean iswork = (boolean) dayofweeklabel.get("iswork");
			boolean found = false;
			if(iswork) {
				for (Date date : offdays) {
					if(dateFormat.format(date).compareTo(dateFormat.format(currentDate)) == 0){
						found = true;
						break;
					}
				}
				if(!found){
					result++;
				}
			} else {
				for (Date date : workdays) {
					if(dateFormat.format(date).compareTo(dateFormat.format(currentDate)) == 0){
						found = true;
						break;
					}
				}
				if(found){
					result++;
				}
			}
			currentDate = DateUtils.addDays(currentDate, 1);
		}
		return result;
	}
	
	public static Date getEndDateWithCalendar(Date startDate, int duration, Map<String, Object> calendar) {
		if(duration < 1){
			return null;
		}
		if(duration == 1){
			return startDate;
		}
		Date result = startDate;
		List<Date> workdays = (List<Date>) calendar.get("workdays");
		List<Date> offdays = (List<Date>) calendar.get("offdays");
		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		while(duration > 1){
			cal.setTime(result);
			int dayofweeknum = cal.get(Calendar.DAY_OF_WEEK);
			Map<String, Object> dayofweek = (Map<String, Object>) calendar.get("dayofweek");
			Map<String, Object> dayofweeklabel = (Map<String, Object>) dayofweek.get("" + dayofweeknum);
			boolean iswork = (boolean) dayofweeklabel.get("iswork");
			boolean found = false;
			if(iswork) {
				for (Date date : offdays) {
					if(dateFormat.format(date).compareTo(dateFormat.format(result)) == 0){
						found = true;
						break;
					}
				}
				if(!found){
					duration--;
				}
			} else {
				for (Date date : workdays) {
					if(dateFormat.format(date).compareTo(dateFormat.format(result)) == 0){
						found = true;
						break;
					}
				}
				if(found){
					duration--;
				}
			}
			result = DateUtils.addDays(result, 1);
		}
		return result;
	}
	
	public static Date getStartDateWithCalendar(Date endDate, int duration, Map<String, Object> calendar) {
		if(duration < 1){
			return null;
		}
		if(duration == 1){
			return endDate;
		}
		Date result = endDate;
		List<Date> workdays = (List<Date>) calendar.get("workdays");
		List<Date> offdays = (List<Date>) calendar.get("offdays");
		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		while(duration > 1){
			cal.setTime(result);
			int dayofweeknum = cal.get(Calendar.DAY_OF_WEEK);
			Map<String, Object> dayofweek = (Map<String, Object>) calendar.get("dayofweek");
			Map<String, Object> dayofweeklabel = (Map<String, Object>) dayofweek.get("" + dayofweeknum);
			boolean iswork = (boolean) dayofweeklabel.get("iswork");
			boolean found = false;
			if(iswork) {
				for (Date date : offdays) {
					if(dateFormat.format(date).compareTo(dateFormat.format(result)) == 0){
						found = true;
						break;
					}
				}
				if(!found){
					duration--;
				}
			} else {
				for (Date date : workdays) {
					if(dateFormat.format(date).compareTo(dateFormat.format(result)) == 0){
						found = true;
						break;
					}
				}
				if(found){
					duration--;
				}
			}
			result = DateUtils.addDays(result, -1);
		}
		return result;
	}
	
}

package com.redsun.server.gateway.util;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redsun.server.gateway.common.Constant; 

/**
* Utility class for Spring Security. 
*/ 
public final class SecurityUtil { 

   private SecurityUtil() { 
   } 

   /**
    * Get the login of the current user. 
    */ 
   public static String getCurrentUserLogin() { 
       SecurityContext securityContext = SecurityContextHolder.getContext(); 
       Authentication authentication = securityContext.getAuthentication(); 
       String userName = null; 
       if (authentication != null) { 
           if (authentication.getPrincipal() instanceof UserDetails) { 
               UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal(); 
               userName = springSecurityUser.getUsername(); 
           } else if (authentication.getPrincipal() instanceof String) { 
               userName = (String) authentication.getPrincipal(); 
           } 
       } 
       return userName; 
   } 

   /**
    * Check if a user is authenticated. 
    * 
    * @return true if the user is authenticated, false otherwise 
    */ 
   public static boolean isAuthenticated() { 
       SecurityContext securityContext = SecurityContextHolder.getContext(); 
       Collection<? extends GrantedAuthority> authorities = securityContext.getAuthentication().getAuthorities(); 
       if (authorities != null) { 
           for (GrantedAuthority authority : authorities) { 
               if (authority.getAuthority().equals("ANONYMOUS")) { 
                   return false; 
               } 
           } 
       } 
       return true; 
   } 

   /**
    * Return the current user, or throws an exception, if the user is not 
    * authenticated yet. 
    * 
    * @return the current user 
    */ 
   public static User getCurrentUser() { 
       SecurityContext securityContext = SecurityContextHolder.getContext(); 
       Authentication authentication = securityContext.getAuthentication(); 
       if (authentication != null) { 
           if (authentication.getPrincipal() instanceof User) { 
               return (User) authentication.getPrincipal(); 
           } 
       } 
       throw new IllegalStateException("User not found!"); 
   } 

   /**
    * If the current user has a specific authority (security role). 
    * 
    * <p>The name of this method comes from the isUserInRole() method in the Servlet API</p> 
    */ 
   public static boolean isCurrentUserInRole(String authority) { 
       SecurityContext securityContext = SecurityContextHolder.getContext(); 
       Authentication authentication = securityContext.getAuthentication(); 
       if (authentication != null) { 
           if (authentication.getPrincipal() instanceof UserDetails) { 
               UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal(); 
               return springSecurityUser.getAuthorities().contains(new SimpleGrantedAuthority(authority)); 
           } 
       } 
       return false; 
   }
   
   public static List<Map<String, Object>> getModulesInfo() throws JsonParseException, JsonMappingException, IOException {
	   List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
	   HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
	   String userInfoStr = request.getHeader(Constant.USSER_INFO);
	   if(null != userInfoStr) {
		   ObjectMapper objectMapper = new ObjectMapper();
		   Map<String, Object> userInfo = objectMapper.readValue(userInfoStr, new TypeReference<Map<String, Object>>(){});
		   result = (List<Map<String, Object>>) userInfo.get("modules");
		   for (Map<String, Object> module : result) {
			   Map<String, Object> info = objectMapper.readValue(CommonUtil.decrypt(module.get("info").toString()), new TypeReference<Map<String, Object>>(){});
			   module.put("info", info);
		   }
	   }
	   // return
	   return result;
   }
   
   public static List<Integer> listIdRoles() throws JsonParseException, JsonMappingException, IOException {
	   List<Integer> result = new ArrayList<Integer>();
	   HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
	   String userInfoStr = request.getHeader(Constant.USSER_INFO);
	   if(null != userInfoStr) {
		   ObjectMapper objectMapper = new ObjectMapper();
		   Map<String, Object> userInfo = objectMapper.readValue(userInfoStr, new TypeReference<Map<String, Object>>(){});
		   Map<String, Object> info = objectMapper.readValue(CommonUtil.decrypt(userInfo.get("info").toString()), new TypeReference<Map<String, Object>>(){});
		   result = (List<Integer>) info.get("idroles");
	   }
	   // return
	   return result;
   }
   
	public static Object stringToJSONObject(String str) {
		if (str==null)
			return null;
		ObjectMapper mapper = new ObjectMapper();
		Object obj = null;
		try {
			obj = mapper.readTree(str);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return obj;
	}

}

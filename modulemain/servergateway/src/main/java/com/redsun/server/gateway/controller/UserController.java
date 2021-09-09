package com.redsun.server.gateway.controller;

import java.security.Principal;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {

	@Autowired
    private AuthorizationServerTokenServices authorizationServerTokenServices;
	
	@Autowired
	private ConsumerTokenServices consumerTokenServices;
	
    @Autowired
    private DataSource dataSource;

    @Autowired
    private TokenStore tokenStore;
    
    private TokenStore tokenStore1() {
    	return new JdbcTokenStore(dataSource); 
    }
    
    @RequestMapping("/getUser")
	@ResponseBody
	public Principal user(Principal user) {
		return user;
	}

    @RequestMapping(value = "/oauth/revoke-token", method = RequestMethod.GET)
    public ResponseEntity<?> revokeToken(Principal user, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
    	
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){    
           new SecurityContextLogoutHandler().logout(httpServletRequest, httpServletResponse, auth);
        }
        SecurityContextHolder.getContext().setAuthentication(null);
        
      
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        if (authorizationHeader != null) {
            String token = authorizationHeader.substring("Bearer".length() + 1);
            //TokenStore tokenStore = tokenStore1();
            OAuth2AccessToken oAuth2AccessToken = tokenStore.readAccessToken(token);
            tokenStore.removeAccessToken(oAuth2AccessToken);
        }
        
    	String authorization = httpServletRequest.getHeader("Authorization");
        if (authorization != null && authorization.contains("Bearer")){
            String tokenId = authorization.substring("Bearer".length() + 1);
            consumerTokenServices.revokeToken(tokenId);
        }

        return new ResponseEntity<String>(HttpStatus.OK);
    }

}

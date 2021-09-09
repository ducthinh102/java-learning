package com.redsun.server.gateway.config;

import java.security.KeyPair;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

@EnableAuthorizationServer
@Configuration
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
	
	@Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DataSource dataSource;

	@Bean
	public JwtAccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		KeyPair keyPair = new KeyStoreKeyFactory(new ClassPathResource("keystore.jks"), "authserverstorepass".toCharArray())
				.getKeyPair("authserver", "authserverkeypass".toCharArray());
		System.out.println(new String(org.springframework.security.crypto.codec.Base64.encode(keyPair.getPublic().getEncoded())));
		converter.setKeyPair(keyPair);
		return converter;
	}
	
	@Bean
	public TokenStore tokenStore(){
	    return new JwtTokenStore(jwtAccessTokenConverter());
	}
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    	
		clients.jdbc(dataSource);
/*    	
    	//clients.inMemory()
    	clients.jdbc(dataSource)
        .withClient("acme")
        .secret("acmesecret")
        .authorizedGrantTypes("password","authorization_code", "refresh_token", "client_credentials")
        .scopes("read", "write", "openid")
        .autoApprove(true)
        .accessTokenValiditySeconds(3600 * 12);
*/   	

    	/*		
		clients.inMemory()
				.withClient("acme")
				.secret("acmesecret")
				.authorizedGrantTypes("authorization_code", "refresh_token",
						"password").scopes("openid");
*/		
	}
	
/*
    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }
*/

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints)
			throws Exception {
		endpoints.authenticationManager(authenticationManager).accessTokenConverter(
				jwtAccessTokenConverter());
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess(
				"isAuthenticated()");
	}
}

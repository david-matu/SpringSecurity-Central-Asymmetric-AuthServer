package com.dave.spring.authserver.configs;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

@Configuration
@EnableAuthorizationServer
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {

	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	//@Value("${jwt.key}")
	//private String jwtKey;
	
	@Value("${keystore.password}")
	private String password;
	
	@Value("${keystore.privateKey}")
	private String privateKey;
	
	@Value("${keystore.alias}")
	private String alias;
	
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		/*
		InMemoryClientDetailsService service = new InMemoryClientDetailsService();
		
		BaseClientDetails bcd = new BaseClientDetails();
		bcd.setClientId("client");
		bcd.setClientSecret("$2a$10$cwGkCWA6vZ4xN8SD0ofkb.hVsjhuzQihDMKEAUub3DPSgt7w0Rcfq");	//dave123
		bcd.setScope(List.of("read"));
		bcd.setAuthorizedGrantTypes(List.of("password"));		//We'll use the Password grant type
		
		service.setClientDetailsStore(Map.of("client", bcd));
		clients.withClientDetails(service);
		*/
		//An alternative cleaner -> 325 (355/562)
		
		clients.inMemory()
					.withClient("client")
					.secret("$2a$10$cwGkCWA6vZ4xN8SD0ofkb.hVsjhuzQihDMKEAUub3DPSgt7w0Rcfq") //dave123
					.authorizedGrantTypes("password", "refresh_token").scopes("read")
					.scopes("read")
					.and()
					.withClient("resourceserver")		// Add a set of credentials for the resource server to use when calling the /oauth/check_token endpoint
					.secret("$2a$10$cwGkCWA6vZ4xN8SD0ofkb.hVsjhuzQihDMKEAUub3DPSgt7w0Rcfq");	// dave123 because the password encoder is BCrypt and not a NoOps...
	}
	
	
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		endpoints.authenticationManager(authenticationManager)
		.tokenStore(tokenStore())
		.accessTokenConverter(jwtAccessTokenConverter());
	}
	
	@Bean
	public AccessTokenConverter jwtAccessTokenConverter() {
		JwtAccessTokenConverter conv = new JwtAccessTokenConverter();
		
		//conv.setSigningKey(jwtKey);
		
		//Create KeyStoreKeyFactory object to read the private key file from the classpath 
		KeyStoreKeyFactory kskf = new KeyStoreKeyFactory(new ClassPathResource(privateKey), password.toCharArray()); 
		
		//Use the KeyStoreKeyFactory to retrieve the key pair and set the key pair to the JwtAccessTokenConverter object
		conv.setKeyPair(kskf.getKeyPair(alias));
		
		return conv;
	}


	@Bean
	public TokenStore tokenStore() {
		return new JdbcTokenStore(dataSource);
	}


	public void configure(AuthorizationServerSecurityConfigurer security) {
		security.checkTokenAccess("isAuthenticated");
	}
	
	/*
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		clients.inMemory()
					.withClient("client")
					.secret("$2a$10$cwGkCWA6vZ4xN8SD0ofkb.hVsjhuzQihDMKEAUub3DPSgt7w0Rcfq") 	//dave123
					.authorizedGrantTypes("")
	}
	*/
}

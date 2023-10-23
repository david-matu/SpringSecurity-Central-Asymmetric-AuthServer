package com.dave.spring.authserver.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.dave.spring.authserver.user.AppUserDetailsService;

@Configuration
public class AppConfigs {
	
	public AppConfigs() { }
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	AppUserDetailsService getUserDetailsService() {
		return new AppUserDetailsService();
	}
	
	/*
	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
		return http.auth
	}
	*/
	/*
	public void setAuthenticationManager(AuthenticationManagerBuilder builder) throws Exception {
		builder.userDetailsService(getUserDetailsService()).passwordEncoder(passwordEncoder());
	}
	*/
	
	/*
	 * 
	 * https://github.com/spring-projects/spring-security/issues/11926
	 */
	
	@Bean
	public AuthenticationManager authenticationManager() {	//UserDetailsService userDetailsService, PasswordEncoder passwordEncoder
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		
		authProvider.setUserDetailsService(getUserDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		
		return new ProviderManager(authProvider);
	}
}
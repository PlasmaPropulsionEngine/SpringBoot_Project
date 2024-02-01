package com.contactmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class Config {

	@Bean
	public UserDetailsService getUserDetailsService()
	{
	
		return new UserDetailsServicesImpl();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder()
	{
		
		
		return new BCryptPasswordEncoder();
	}
	
	
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider()
	{
		
		
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		
		
		daoAuthenticationProvider.setUserDetailsService(this.getUserDetailsService());
		
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		
		return daoAuthenticationProvider;
		
	}

	  	@Bean
	    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

           http.authorizeHttpRequests(requests -> requests.requestMatchers("/admin/**").hasRole("ADMIN")
            .requestMatchers("/user/**").hasRole("USER").requestMatchers("/**").permitAll())
            .formLogin(form->form.loginPage("/signin")
            		.loginProcessingUrl("/dologin")
            		.defaultSuccessUrl("/user/index")
            		.failureUrl("/login-fail"))
            .csrf(csrf -> csrf.disable());
	  		http.authenticationProvider(daoAuthenticationProvider());
 
	        return http.build();
	    }
	
	
	
	
	
	
	
	
	
	
}

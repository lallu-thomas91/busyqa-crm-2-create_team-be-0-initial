package com.busyqa.coop.configuration;


import java.io.IOException;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import com.busyqa.coop.security.LoginUserDetailsService;



@SuppressWarnings("unused")
@Configuration
@ComponentScan
@EnableWebSecurity

public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired 
	private LoginUserDetailsService userDetailsService;
	
	/*
	 * JPA Authentication fetching user details from database using Custom
	 * UserDetailsService
	 */
	@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		
		auth.authenticationProvider(authenticationProvider());
				
//		auth.userDetailsService(userDetailsService)
//			.passwordEncoder(passwordEncoder());
    }
	
	/*
	 * In memory Authentication using hard coded user datails
	 */
	/*
	@Override
	protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
	    auth.inMemoryAuthentication()
	        .withUser("user1").password(passwordEncoder().encode("user1Pass")).roles("USER")
	        .and()
	        .withUser("user2").password(passwordEncoder().encode("user2Pass")).roles("USER")
	        .and()
	        .withUser("admin").password(passwordEncoder().encode("adminPass")).roles("ADMIN");
	}
	*/
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		 	.cors()
//		 		.configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())
		 		.and()
			.httpBasic()
				.and()
			.authorizeRequests()
				.antMatchers("/", "/signup", "/check/*").permitAll()
				.anyRequest().authenticated()
				.and()
                .exceptionHandling().authenticationEntryPoint(
                (request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
				.and()
			.formLogin()
//				.loginPage("/login")
//				.loginProcessingUrl("/login")
//				.failureHandler(authenticationFailureHandler())
//				.failureUrl("/login/error")
				.permitAll()
//				.successHandler(appAuthenticationSuccessHandler())
				.and()
			.logout()
				.invalidateHttpSession(true)
			    .clearAuthentication(true)
			    .deleteCookies("JSESSIONID", "SESSION")
//			    .logoutSuccessUrl("/login/logout_success")
			    .permitAll()
			    .and()
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		http
//			.addFilterBefore(new WebSecurityCorsFilter(), ChannelProcessingFilter.class)
			.csrf()
				.ignoringAntMatchers("/signup")
				.disable();
//				.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	    authProvider.setUserDetailsService(userDetailsService);
	    authProvider.setPasswordEncoder(passwordEncoder());
	         
	    return authProvider;
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	   
	   /*
	   public class AppAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler{
		    protected void handle(HttpServletRequest request, HttpServletResponse response,
		            Authentication authentication) throws IOException, ServletException {
		    }
		}
	   
	   @Bean
	   public AuthenticationSuccessHandler appAuthenticationSuccessHandler(){
	        return new AppAuthenticationSuccessHandler();
	   }
	   */
	
		/*
		 * Authentication Failure Handler
		 */
	public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler{
		
		    protected void handle(HttpServletRequest request, HttpServletResponse response,
		            AuthenticationException exception) throws IOException, ServletException {
		    	
		    	String username = request.getParameter("username");
		    	String error = exception.getMessage();
		    	System.out.println("Failed login attempt with username: " + username);
		    	System.out.println("Reason: " + error);
		    	
		    	
//		    	super.setDefaultFailureUrl("/login/error");
	            super.onAuthenticationFailure(request, response, exception);
		    }
	}
	   
	@Bean
	public AuthenticationFailureHandler authenticationFailureHandler(){
		return new AuthenticationFailureHandler();
	}   	   
	  
	
	/*
	 * CORS Configuration Source
	 */
	   @Bean
//	   @Order(Ordered.HIGHEST_PRECEDENCE)
		CorsConfigurationSource corsConfigurationSource() {
			CorsConfiguration configuration = new CorsConfiguration();
			configuration.setAllowedHeaders(Arrays.asList("*"));
			configuration.setAllowedOrigins(Arrays.asList("*"));
			configuration.setAllowedMethods(Arrays.asList("*"));
			configuration.setAllowCredentials(true);
			configuration.setMaxAge((long) 3600);
			configuration.setExposedHeaders(Arrays.asList("Authorization"));
			UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
			source.registerCorsConfiguration("/**", configuration);
			return source;
		}
	 
}



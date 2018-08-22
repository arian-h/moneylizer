package com.karen.moneylizer.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.karen.moneylizer.useraccount.UserAccountService;

@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserAccountService userAccountService;

	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		// TODO re-enable csrf after dev is done
		.csrf()
				.disable()
				// we must specify ordering for our custom filter, otherwise it
				// doesn't work
				.addFilterAfter(jwtAuthenticationFilter,
						UsernamePasswordAuthenticationFilter.class)
				// we don't need Session, as we are using jwt instead. Sessions
				// are harder to scale and manage
				.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}

	/*
	 * Ignores the authentication endpoints (signup and login)
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/api/authentication/**").and().ignoring()
				.antMatchers(HttpMethod.OPTIONS, "/**");
	}

	/*
	 * Set user details services and password encoder
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.userDetailsService(userAccountService).passwordEncoder(
				passwordEncoder());
	}

	/*
	 * By default, spring boot adds custom filters to the filter chain which
	 * affects all requests this should be disabled.
	 */
	@Bean
	public FilterRegistrationBean<JwtAuthenticationFilter> rolesAuthenticationFilterRegistrationDisable(
			JwtAuthenticationFilter filter) {
		FilterRegistrationBean<JwtAuthenticationFilter> registration = new FilterRegistrationBean<JwtAuthenticationFilter>(
				filter);
		registration.setEnabled(false);
		return registration;
	}
}

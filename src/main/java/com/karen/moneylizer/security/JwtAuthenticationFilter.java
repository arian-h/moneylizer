package com.karen.moneylizer.security;

import io.jsonwebtoken.Jwts;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.stereotype.Component;

import com.karen.moneylizer.useraccount.UserAccountService;

@Component
public class JwtAuthenticationFilter extends
		AbstractAuthenticationProcessingFilter {

	@Autowired
	private UserAccountService userAccountService;

	protected JwtAuthenticationFilter() {
		super(SecurityConstants.DEFAULT_FILTER_PROCESS_URL);
	}

	/*
	 * Parses the request header and returns authentication token if credentials
	 * are valid
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request,
			HttpServletResponse response) throws AuthenticationException,
			IOException, ServletException {
		String token = request
				.getHeader(SecurityConstants.AUTHENTICATION_HEADER);
		if (token != null) {
			// parse the token
			String username = null;
			try {
				username = Jwts
						.parser()
						.setSigningKey(SecurityConstants.JWT_SECRET)
						.parseClaimsJws(
								token.replace(SecurityConstants.BEARER, ""))
						.getBody().getSubject();
			} catch (Exception e) {
				throw new BadCredentialsException(
						"Bad username/password presented");
			}
			UserDetails user = userAccountService.loadUserByUsername(username);
			if (user != null) {
				return new UsernamePasswordAuthenticationToken(
						user.getUsername(), user.getPassword(),
						user.getAuthorities());
			}
		}
		throw new BadCredentialsException("Bad username/password presented");
	}

	/*
	 * we must set authentication manager for our custom filter, otherwise it
	 * errors out
	 */
	@Override
	@Autowired
	public void setAuthenticationManager(
			AuthenticationManager authenticationManager) {
		super.setAuthenticationManager(authenticationManager);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException failed)
			throws IOException, ServletException {
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		SecurityContextHolder.getContext().setAuthentication(authResult);
		chain.doFilter(request, response);
	}

}

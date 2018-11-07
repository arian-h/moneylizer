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
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import com.karen.moneylizer.core.entity.userAccount.UserAccountEntity;
import com.karen.moneylizer.core.service.UserAccountService;
import com.karen.moneylizer.websocket.WebSocketConfiguration;

public class JwtAuthenticationFilter extends
		AbstractAuthenticationProcessingFilter {

	private final static String TOKEN_PARAM_NAME = "token";
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
		if (token == null) {
			if (request.getRequestURI().startsWith(WebSocketConfiguration.WEBSOCKET_HANDSHAKE_ENDPOINT_URI)) {
				token = request.getParameter(TOKEN_PARAM_NAME);
			}
		} else {
			token = token.replace(SecurityConstants.BEARER, "");
		}
		if (token == null) {
			throw new BadCredentialsException("Bad username/password presented");
		}
		// parse the token
		String username = null;
		try {
			username = Jwts
					.parser()
					.setSigningKey(SecurityConstants.JWT_SECRET)
					.parseClaimsJws(token)
					.getBody().getSubject();
		} catch (Exception e) {
			throw new BadCredentialsException(
					"Bad username/password presented");
		}
		UserAccountEntity userAccount = userAccountService.loadUserByUsername(username);
		if (userAccount != null) {
			return new UsernamePasswordAuthenticationToken(
					userAccount.getUsername(), userAccount.getPassword(),
					userAccount.getAuthorities());
		}
		return null;
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

package com.karen.moneylizer.websocket;

import java.security.Principal;

import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

public class QuoteSubscriptionValidator implements SubscriptionValidator {

	@Override
	public boolean validate(StompHeaderAccessor headerAccessor, Principal userPrincipal) {
		return !limitExceeded();
	}

	@Override
	public boolean supports(StompHeaderAccessor headerAccessor) {
		return headerAccessor.getDestination().matches(WebSocketConfiguration.QUOTE_CHANNEL_URI.concat("/\\w+"));
	}

	/*
	 * If the limit on the number of subscription for each user is exceeded
	 */
	private boolean limitExceeded() {
		//TODO
		return false;
	}

}
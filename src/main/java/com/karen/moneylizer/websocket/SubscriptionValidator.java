package com.karen.moneylizer.websocket;

import java.security.Principal;

import org.springframework.messaging.simp.stomp.StompHeaderAccessor;

public interface SubscriptionValidator {

	boolean validate(StompHeaderAccessor headerAccessor, Principal userPrincipal);

	boolean supports(StompHeaderAccessor headerAccessor);

}
package com.karen.moneylizer.websocket;

import java.security.Principal;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

public class SubscriptionInterceptor implements ChannelInterceptor {

	SubscriptionValidator[] validators;

	public SubscriptionInterceptor() {
		validators = new SubscriptionValidator[] { new QuoteSubscriptionValidator() };
	}

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
	    StompHeaderAccessor headerAccessor= StompHeaderAccessor.wrap(message);
	    if (StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {
	    	Principal userPrincipal = headerAccessor.getUser();
	    	for (SubscriptionValidator validator: validators) {
	    		if (validator.supports(headerAccessor) && validator.validate(headerAccessor, userPrincipal)) {
	    			return message;
	    		}
	    	}
			// if none of the validators supported/validated the subscribe
			// request, don't let the client to subscribe
	    	throw new MessagingException(message);
	    }
	    return message;
	}

}
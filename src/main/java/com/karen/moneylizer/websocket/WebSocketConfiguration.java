package com.karen.moneylizer.websocket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

	public static final String WEBSOCKET_HANDSHAKE_ENDPOINT_URI = "/api/wsocket";
	public static final String QUOTE_CHANNEL_URI = "/quote";

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker(QUOTE_CHANNEL_URI); /*Enable a simple in-memory broker for the clients to subscribe to channels and receive messages*/
        config.setApplicationDestinationPrefixes("/app"); /*The prefix for the message mapping endpoints in the controller*/
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
    	/*websocket handshaking endpoint*/
        registry.addEndpoint(WEBSOCKET_HANDSHAKE_ENDPOINT_URI)
        	/*TODO remove this after development*/
        	.setAllowedOrigins("http://localhost:8080");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registeration) {
    	registeration.interceptors(new SubscriptionInterceptor());
    }
    
}
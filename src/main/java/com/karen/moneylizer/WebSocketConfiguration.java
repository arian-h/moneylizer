package com.karen.moneylizer;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

	public static final String WEBSOCKET_HANDSHAKE_ENDPOINT_URI = "/api/wsocket";
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); /*Enable a simple in-memory broker for the clients to subscribe to channels and receive messages*/
        config.setApplicationDestinationPrefixes("/ws"); /*The prefix for the endpoints in the controller*/
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
    	/*websocket handshaking endpoint*/
        registry.addEndpoint(WEBSOCKET_HANDSHAKE_ENDPOINT_URI)
        	/*TODO remove this after development*/
        	.setAllowedOrigins("http://localhost:8080").withSockJS();
    }

}
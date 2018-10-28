package com.karen.moneylizer;

import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); /*Enable a simple in-memory broker for the clients to subscribe to channels and receive messages*/
        config.setApplicationDestinationPrefixes("/ws"); /*The prefix for the endpoints in the controller*/
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/quote-socket").addInterceptors(new HandshakeInterceptor() {
			
			@Override
			public boolean beforeHandshake(ServerHttpRequest request,
					ServerHttpResponse response, WebSocketHandler wsHandler,
					Map<String, Object> attributes) throws Exception {
				return false;
			}
			
			@Override
			public void afterHandshake(ServerHttpRequest request,
					ServerHttpResponse response, WebSocketHandler wsHandler,
					Exception exception) {
				System.out.println("Salam");
			}
		}); /*This endpoint is used for the purpose of handshaking with the client*/
    }

}
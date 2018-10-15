package com.karen.moneylizer;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;

@Configuration
public class RabbitMQConfiguration implements RabbitListenerConfigurer {

	public final static String AUTHENTICATION_EMAILS_QUEUE = "AUTHENTICATION_EMAILS_QUEUE";
	public final static String EMAIL_MESSAGE_EXCHANGE_NAME = "EMAIL_EVENTS";

	@Bean
	public Queue authenticationEmailsQueue() {
		return new Queue(AUTHENTICATION_EMAILS_QUEUE, false);
	}

	@Bean
	public DirectExchange emailMessagesExchange() {
		return new DirectExchange(EMAIL_MESSAGE_EXCHANGE_NAME);
	}

	@Bean
	public Binding authenticationEmailMQBinding(Queue queue, DirectExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(AUTHENTICATION_EMAILS_QUEUE);
	}

	@Bean
	public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
	    final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
	    rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
	    return rabbitTemplate;
	}

	@Bean
	public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
	    return new Jackson2JsonMessageConverter();
	}

	@Bean
	public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
	   return new MappingJackson2MessageConverter();
	}
	 
	@Bean
	public DefaultMessageHandlerMethodFactory messageHandlerMethodFactory() {
	   DefaultMessageHandlerMethodFactory factory = new DefaultMessageHandlerMethodFactory();
	   factory.setMessageConverter(consumerJackson2MessageConverter());
	   return factory;
	}

	@Override
	public void configureRabbitListeners(final RabbitListenerEndpointRegistrar registrar) {
	   registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
	}
}

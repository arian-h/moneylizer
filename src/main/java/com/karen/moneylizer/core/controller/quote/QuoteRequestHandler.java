package com.karen.moneylizer.core.controller.quote;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.karen.moneylizer.RabbitMQConfiguration;

@RabbitListener(queues=RabbitMQConfiguration.QUOTE_REQUEST_QUEUE)
@Component
public class QuoteRequestHandler {

	@RabbitHandler
	public void receiveRequest(String symbol) {
		System.out.println(symbol);
	}

}

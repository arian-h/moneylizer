package com.karen.moneylizer.core.controller.quote;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
@EnableScheduling
public class QuoteController {

    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@SubscribeMapping("/quote")
	public String singleQuote() {
		return "Hello";
	}

	@Scheduled(fixedDelay=5000)
	public void sendPrice() {
		messagingTemplate.convertAndSend("/quote/salam/ss", "salam");
	}

}
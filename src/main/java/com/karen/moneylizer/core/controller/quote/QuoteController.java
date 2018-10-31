package com.karen.moneylizer.core.controller.quote;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class QuoteController {

	@MessageMapping("/quote")
	public void singleQuote() {
		System.out.println("Salam");
	}
}

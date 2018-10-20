package com.karen.moneylizer.emailServices.userAccountAuthentication;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.karen.moneylizer.RabbitMQConfiguration;
import com.karen.moneylizer.emailService.Email;
import com.karen.moneylizer.emailService.EmailHandler;

@RabbitListener(queues=RabbitMQConfiguration.AUTHENTICATION_EMAILS_QUEUE)
@Component
public class UserAccountAuthenticationEmailHandler extends EmailHandler {

	@RabbitHandler
	public void receiveMessage(Email email) {
		super.receiveMessage(email);
	}

}

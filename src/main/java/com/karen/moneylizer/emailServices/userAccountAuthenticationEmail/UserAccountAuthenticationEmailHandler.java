package com.karen.moneylizer.emailServices.userAccountAuthenticationEmail;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.karen.moneylizer.RabbitMQConfiguration;
import com.karen.moneylizer.emailServices.EmailComposer;

@RabbitListener(queues=RabbitMQConfiguration.AUTHENTICATION_EMAILS_QUEUE)
@Component
public class UserAccountAuthenticationEmailHandler {

	@Autowired
	private EmailComposer emailComposer;

	@RabbitHandler
	public void receiveMessage(UserAccountActivationEmail email) {
		emailComposer.send(email);
	}

}

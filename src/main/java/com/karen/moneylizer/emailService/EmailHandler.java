package com.karen.moneylizer.emailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class EmailHandler {

	@Autowired
	private EmailComposer emailComposer;

	/**
	 * Handle the email messages in the queue
	 * @param email
	 */
	public void receiveMessage(Email email) {
		emailComposer.send(email);
	}

}

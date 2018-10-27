package com.karen.moneylizer.emailService;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public abstract class Email {

	private String subject, htmlBody, textBody, sender;
	private String[] recipients;

}

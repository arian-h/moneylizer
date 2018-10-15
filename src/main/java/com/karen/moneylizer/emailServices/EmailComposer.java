package com.karen.moneylizer.emailServices;

import org.springframework.stereotype.Component;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.karen.moneylizer.emailServices.userAccountAuthenticationEmail.UserAccountAuthenticationEmail;

@Component
public class EmailComposer {

	public EmailComposer() {};

	public void send(UserAccountAuthenticationEmail email) {
		try {
			AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder
					.standard()
					// Replace US_WEST_2 with the AWS Region you're using for
					// Amazon SES.
					.withRegion(Regions.US_WEST_2).build();
			SendEmailRequest request = new SendEmailRequest()
					.withDestination(
							new Destination().withToAddresses(email
									.getRecipients()))
					.withMessage(
							new Message()
									.withBody(
											new Body()
													.withHtml(
															new Content()
																	.withCharset(
																			"UTF-8")
																	.withData(
																			email.getHtmlBody()))
													.withText(
															new Content()
																	.withCharset(
																			"UTF-8")
																	.withData(
																			email.getTextBody())))
									.withSubject(
											new Content().withCharset("UTF-8")
													.withData(
															email.getSubject())))
					.withSource(email.getSender());
			client.sendEmail(request);
		} catch (Exception ex) {
			// Todo add logging
		}
	}
}

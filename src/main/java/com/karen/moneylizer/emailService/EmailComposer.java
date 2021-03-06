package com.karen.moneylizer.emailService;

import lombok.NoArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

@Service
@NoArgsConstructor
public class EmailComposer {
	
	@Value("${aws.accessKeyId}")
	private String awsId;

	@Value("${aws.secretKey}")
	private String awsKey;

	@Value("${aws.region}")
	private String region;

	public void send(Email email) {
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsId, awsKey);
		AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder
				.standard()
				.withCredentials(new AWSStaticCredentialsProvider(awsCreds))
				.withRegion(region).build();
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
	}
}

package com.averta.inventory.utility;

import java.io.File;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.averta.inventory.exception.InventoryException;

@Component
public class MailUtility {

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private Environment environment;

	public void sendSimpleMessage(String to, String subject, String text) throws Exception {
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(environment.getRequiredProperty("email.from"));
			message.setTo(to);
			message.setSubject(subject);
			message.setText(text);
			emailSender.send(message);
		} catch (Exception e) {
			throw new InventoryException(ErrorConstants.INTERNAL_SERVER_ERROR, "Error sending email");
		}
	}

	public void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment)
			throws Exception {

		MimeMessage message = emailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setFrom(environment.getRequiredProperty("email.from"));
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(text);

		FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
		helper.addAttachment("Invoice", file);
		emailSender.send(message);
	}
}
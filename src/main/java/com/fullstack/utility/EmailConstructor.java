package com.fullstack.utility;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.fullstack.model.User;

@Component
public class EmailConstructor {

	@Autowired
	private Environment env;
	
	@Autowired
	private TemplateEngine templateEngine;
	
	public MimeMessagePreparator contractNewUserEmail(User user, String password) {
		Context context = new Context();
		context.setVariable("user", user);
		context.setVariable("password", password);
		String text = templateEngine.process("newUserEmailTemplate", context);
		MimeMessagePreparator messagePreparator = new MimeMessagePreparator() {
			
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
				email.setPriority(1);
				email.setTo(user.getEmail());
				email.setSubject("Welcome to the APP");
				email.setText(text, true);
				email.setFrom(new InternetAddress(env.getProperty("support.email")));
			}
		};
		return messagePreparator;
	}
	
	public MimeMessagePreparator constructUpdateUser(User user) {
		Context context = new Context();
		context.setVariable("user", user);
		String text = templateEngine.process("updateUserProfileEmailTemplate", context);
		MimeMessagePreparator message = new MimeMessagePreparator() {
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
				email.setPriority(1);
				email.setTo(user.getEmail());
				email.setSubject("Welcome To Photo Share APP");
				email.setText(text, true);
				email.setFrom(new InternetAddress(env.getProperty("support.email")));
			}
		};
		return message;
	}

	public MimeMessagePreparator constructResetPasswordEmail(User user, String password) {
		Context context = new Context();
		context.setVariable("user", user);
		context.setVariable("password", password);
		String text = templateEngine.process("resetPasswordEmailTemplate", context);
		MimeMessagePreparator message = new MimeMessagePreparator() {
			
			@Override
			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper email = new MimeMessageHelper(mimeMessage);
				email.setPriority(1);
				email.setTo(user.getEmail());
				email.setSubject("Welcome To Photo Share APP");
				email.setText(text, true);
				email.setFrom(new InternetAddress(env.getProperty("support.email")));
			}
		};
		return message;
	}
}

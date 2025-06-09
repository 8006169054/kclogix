package com.kclogix.common.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class Config {

	private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
	private static final String MAIL_DEBUG = "mail.smtp.debug";
	private static final String MAIL_CONNECTION_TIMEOUT = "mail.smtp.connectiontimeout";
	private static final String MAIL_SMTP_STARTTLS_ENABLE = "mail.smtp.starttls.enable";

	@Bean
	JavaMailSender javaMailService() {
		JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		javaMailSender.setHost("smtp.office365.com");
		javaMailSender.setUsername("");
		javaMailSender.setPassword("");
		javaMailSender.setPort(587);

		Properties properties = javaMailSender.getJavaMailProperties();
		properties.put(MAIL_SMTP_AUTH, true);
		properties.put(MAIL_DEBUG, true);
//	        properties.put(MAIL_CONNECTION_TIMEOUT, connectionTimeout);
		properties.put(MAIL_SMTP_STARTTLS_ENABLE, true);

		javaMailSender.setJavaMailProperties(properties);
		javaMailSender.setDefaultEncoding("UTF-8");

		return javaMailSender;
	}

}

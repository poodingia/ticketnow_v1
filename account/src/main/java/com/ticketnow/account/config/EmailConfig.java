package com.ticketnow.account.config;

import com.google.zxing.qrcode.QRCodeWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {
    private static final Logger log = LoggerFactory.getLogger(EmailConfig.class);
    private final EmailPropertiesConfig emailPropertiesConfig;

    public EmailConfig(EmailPropertiesConfig emailPropertiesConfig) {
        this.emailPropertiesConfig = emailPropertiesConfig;
    }

    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        log.info("Email username: {}", emailPropertiesConfig.username());
        log.info("Email password: {}", emailPropertiesConfig.password());
        mailSender.setUsername(emailPropertiesConfig.username());
        mailSender.setPassword(emailPropertiesConfig.password());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    @Bean
    public QRCodeWriter getQRCodeWriter() {
        return new QRCodeWriter();
    }
}

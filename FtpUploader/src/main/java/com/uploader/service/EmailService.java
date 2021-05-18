package com.uploader.service;

import com.uploader.config.EmailConfig;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailService {
    private EmailConfig emailConfig;
    private Properties properties;

    public EmailService(EmailConfig emailConfig) {
        this.emailConfig = emailConfig;
        properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true"); //TLS

    }

    public void sendEmail(String buildFileUrl) {

        Session session = Session.getInstance(this.properties,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(emailConfig.getUsername(), emailConfig.getPassword());
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(this.emailConfig.getFromEmail()));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(this.emailConfig.getToEmail())
            );
            message.setSubject("Your Video Has Been Uploaded! Upload URL to https://erp.daodenn.ru/");
            message.setText("Please set video URL at portal: https://erp.daodenn.ru/,"
                    + "\n\n VIDEO URL: " + buildFileUrl);
            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

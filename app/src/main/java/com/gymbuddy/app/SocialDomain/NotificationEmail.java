package com.gymbuddy.app.SocialDomain;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class NotificationEmail extends Notification {

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";

    // Better: load these from environment variables
    private static final String FROM_EMAIL = System.getenv("GYMBUDDY_EMAIL");
    private static final String APP_PASSWORD = System.getenv("GYMBUDDY_EMAIL_PASSWORD");

    public void createNotificationEmail() {
        // Email creation logic here if you need it later
    }

    public void sendEmail(String toEmail, String subject, String body) {
        try {
            if (FROM_EMAIL == null || FROM_EMAIL.isBlank()) {
                throw new IllegalStateException("GYMBUDDY_EMAIL environment variable is not set.");
            }

            if (APP_PASSWORD == null || APP_PASSWORD.isBlank()) {
                throw new IllegalStateException("GYMBUDDY_EMAIL_PASSWORD environment variable is not set.");
            }

            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
                }
            });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail)
            );
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);

            System.out.println("Email sent successfully to " + toEmail);

        } catch (Exception e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage(), e);
        }
    }
}
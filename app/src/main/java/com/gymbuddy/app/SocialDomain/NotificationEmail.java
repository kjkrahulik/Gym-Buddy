package com.gymbuddy.app.SocialDomain;

//jkarta.mail sytle
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Authenticator;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

import org.springframework.web.bind.annotation.GetMapping;

import java.util.Properties;

public class NotificationEmail  extends  Notification {
    /*#Used for the email service
    spring.mail.host=smtp.gmail.com
    spring.mail.port=587
    spring.mail.username=gymbuddy.official.app@gmail.com
    spring.mail.password=zruo spgf hfhx xjzb

    spring.mail.properties.mail.smtp.auth=true
    spring.mail.properties.mail.smtp.starttls.enable=true
    */

    // Simple test email
    /* 
    @GetMapping("/send-test-email")
        public String sendTest() {
            sendEmail("youremail@example.com");
            return "Sent!";
        }
    */
    public void createNotificationEmail() {
         // Email creation logic here
    }
    /* 
    //Using Jakarta Mail
    Properties props = new Properties();
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");

    Session session = Session.getInstance(props,
    new Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
            // Enter the email and password for the SMTP server (The Email for the gym buddy app)
        return new PasswordAuthentication("your_email@gmail.com", "your_app_password");
        }
    });

    Message message = new MimeMessage(session);
    message.setFrom(new InternetAddress("your_email@gmail.com"));
    message.setRecipients(Message.RecipientType.TO,
            InternetAddress.parse("recipient@example.com"));
    message.setSubject("Test Email");
    message.setText("Hello from Java!");

    Transport.send(message);
    */

}

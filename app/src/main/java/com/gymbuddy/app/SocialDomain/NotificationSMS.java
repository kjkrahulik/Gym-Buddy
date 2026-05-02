package com.gymbuddy.app.SocialDomain;

public class NotificationSMS extends Notification {

    private String phoneNumber;
    private String message;

    public NotificationSMS(String phoneNumber, String message) {
        this.phoneNumber = phoneNumber;
        this.message = message;
    }

    public void createNotificationSMS() {
        // build SMS notification object / validate data
        if (phoneNumber == null || phoneNumber.isBlank()) {
            throw new IllegalArgumentException("Phone number is required");
        }

        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException("Message is required");
        }

        System.out.println("SMS notification created for " + phoneNumber);
    }

    public void sendSMS() {
        createNotificationSMS();

        // SMS sending logic goes here
        System.out.println("Sending SMS to " + phoneNumber + ": " + message);
    }
}
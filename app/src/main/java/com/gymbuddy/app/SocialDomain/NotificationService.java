package com.gymbuddy.app.SocialDomain;

import java.util.List;

import com.gymbuddy.app.AccountDomain.Account;

public class NotificationService {

    private NotificationRepository notificationRepo;

    public void notify(Account recipient, String message, Notification.Type type) {
        Notification notification = new Notification(recipient, message, type);
        notificationRepo.save(notification);
    }

    public List<Notification> getUserNotifications(Account user) {
        return notificationRepo.findByRecipient(user);
    }

    public void markAsRead(Long notificationId) {
        Notification notif = notificationRepo.findById(notificationId);
        notif.markAsRead();
        notificationRepo.save(notif);
    }
}

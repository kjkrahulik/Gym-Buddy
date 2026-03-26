package com.gymbuddy.app.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gymbuddy.app.AccountDomain.Account;
import com.gymbuddy.app.Repositories.NotificationRepository;
import com.gymbuddy.app.SocialDomain.Notification;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepo;

    public void notify(Account recipient, String message, Notification.Type type) {
        Notification notification = new Notification(recipient, message, type);
        notificationRepo.save(notification);
    }

    public List<Notification> getUserNotifications(Account user) {
        return notificationRepo.findByRecipient(user);
    }

    public void markAsRead(Long notificationId) {
        Notification notif = notificationRepo.findById(notificationId).orElseThrow(()->new RuntimeException("Notification not found"));
        notif.markAsRead();
        notificationRepo.save(notif);
    }
}

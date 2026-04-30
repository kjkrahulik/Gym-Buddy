package com.gymbuddy.app.SocialDomain;

import com.gymbuddy.app.AccountDomain.Account;
import com.gymbuddy.app.SocialDomain.Notification;

public class NotificationFactory {

    public static Notification createNotification(Account recipient, String message, Notification.Type type) {
        Notification notification = new Notification(recipient, message, type);
        recipient.addNotification(notification);
        return notification;
    }

    public static Notification createFriendRequestNotification(Account recipient, Account sender) {
        String message = sender.getUsername() + " sent you a friend request";

        Notification notification = new Notification(
            recipient,
            message,
            Notification.Type.FRIEND_REQUEST
        );

        recipient.addNotification(notification);
        return notification;
    }
    public static Notification createFriendAcceptedNotification(Account recipient, Account sender) {
        String message = sender.getUsername() + " accepted your friend request";

        Notification notification = new Notification(
            recipient,
            message,
            Notification.Type.FRIEND_ACCEPTED
        );

        recipient.addNotification(notification);
        return notification;
    }
}
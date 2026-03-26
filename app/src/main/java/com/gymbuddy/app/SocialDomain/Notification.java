package com.gymbuddy.app.SocialDomain;

import java.time.LocalDateTime;

import com.gymbuddy.app.AccountDomain.Account;

public class Notification {
    private Long id;

    private Account recipient;
    private String message;

    private Type type;
    private boolean read;

    private LocalDateTime createdAt;

    public enum Type {
        FRIEND_REQUEST,
        FRIEND_ACCEPTED,
        INVITATION,
        INVITE_ACCEPTED
    }

    public Notification(Account recipient, String message, Type type) {
        this.recipient = recipient;
        this.message = message;
        this.type = type;
        this.read = false;
        this.createdAt = LocalDateTime.now();
    }

    public void markAsRead() {
        this.read = true;
    }
}

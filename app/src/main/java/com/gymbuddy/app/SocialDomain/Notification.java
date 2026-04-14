package com.gymbuddy.app.SocialDomain;

import java.time.LocalDateTime;

import com.gymbuddy.app.AccountDomain.Account;

import jakarta.persistence.*;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private Account recipient;

    private String message; //body of the notification

    private boolean read;
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    private Type type;
   
    public enum Type {
        FRIEND_REQUEST,
        FRIEND_ACCEPTED,
        INVITATION,
        INVITE_ACCEPTED
    }
 

    public Notification() {}

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
    public void setRecipient(Account recipient) {
        this.recipient = recipient;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getRead() {
        return read;
    }
    public Account getRecipient() {
        return recipient;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public String getMessage() {
        return message;
    }
}

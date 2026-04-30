package com.gymbuddy.app.SocialDomain;
import com.gymbuddy.app.AccountDomain.Account;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "friend_requests")
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Account sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Account receiver;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createdAt;

    public enum Status {
        PENDING,
        ACCEPTED,
        DECLINED
    }

    public FriendRequest() {}

    public FriendRequest(Account sender, Account receiver) {
        this.sender = sender;
        this.receiver = receiver;
        this.status = Status.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public void acceptFriendRequest() {
        this.status = Status.ACCEPTED;
    }

    public void declineFriendRequest() {
        this.status = Status.DECLINED;
    }

    public Account getSender() {
        return sender;
    }
    public Account getReceiver() {
        return receiver;
    }
    public Status getStatus() {
        return status;
    }
    public void createPendingRequest(Account sender, Account receiver) {
        this.sender = sender;
        this.receiver = receiver;
        this.status = Status.PENDING;
        this.createdAt = LocalDateTime.now();
        this.receiver.addFriendRequest(this);
        this.sender.addSentRequests(this);
    }
    public void removePendingRequest() {
        this.receiver.removeFriendRequest(this);
        this.sender.removeSentRequest(this);
    }
        


   //void toFriendship(FriendRequest request);
    // getters
}
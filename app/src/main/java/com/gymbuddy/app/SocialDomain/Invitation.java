package com.gymbuddy.app.SocialDomain;

import java.time.LocalDateTime;

import com.gymbuddy.app.AccountDomain.Account;

public class Invitation {
    private Long id;

    private Account sender;
    private Account receiver;

    private Long workoutSessionId; // or WorkoutSession object

    private Status status;
    private LocalDateTime createdAt;

    public enum Status {
        PENDING,
        ACCEPTED,
        DECLINED
    }

    public Invitation(Account sender, Account receiver, Long workoutSessionId) {
        this.sender = sender;
        this.receiver = receiver;
        this.workoutSessionId = workoutSessionId;
        this.status = Status.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public void accept() {
        this.status = Status.ACCEPTED;
    }

    public void decline() {
        this.status = Status.DECLINED;
    }
    public Account getSender() {
        return sender;
    }
    public Account getReceiver() {
        return receiver;
    }
}

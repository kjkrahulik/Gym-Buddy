package com.gymbuddy.app.SocialDomain;

import java.time.LocalDateTime;

import com.gymbuddy.app.AccountDomain.Account;

import jakarta.persistence.*;

@Entity
@Table(name = "invitations")
public class Invitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Account sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Account receiver;

    private Long workoutSessionId; // or WorkoutSession object

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createdAt;

    public enum Status {
        PENDING,
        ACCEPTED,
        DECLINED
    }

    public Invitation() {}

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

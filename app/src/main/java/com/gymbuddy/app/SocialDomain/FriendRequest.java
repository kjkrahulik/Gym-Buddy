package com.gymbuddy.app.SocialDomain;
import com.gymbuddy.app.AccountDomain.Account;
import java.time.LocalDateTime;

public class FriendRequest {
    private Long id;

    private Account sender;
    private Account receiver;

    private Status status;
    private LocalDateTime createdAt;

    public enum Status {
        PENDING,
        ACCEPTED,
        DECLINED
    }

    public FriendRequest(Account sender, Account receiver) {
        this.sender = sender;
        this.receiver = receiver;
        this.status = Status.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public void accept() {
        this.status = Status.ACCEPTED;
    }

    public void decline() {
        this.status = Status.DECLINED;
    }

    // getters
}
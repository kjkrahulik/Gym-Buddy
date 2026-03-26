package com.gymbuddy.app.SocialDomain;

import java.time.LocalDateTime;

import com.gymbuddy.app.AccountDomain.Account;

public class Friendship {
    private Long id;

    private Account user1;
    private Account user2;

    private LocalDateTime createdAt;

    public Friendship(Account user1, Account user2) {
        this.user1 = user1;
        this.user2 = user2;
        this.createdAt = LocalDateTime.now();
    }





}



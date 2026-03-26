package com.gymbuddy.app.SocialDomain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.gymbuddy.app.AccountDomain.Account;

import jakarta.persistence.Column;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "friendships")
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user1_id")
    private Account user1;

    @ManyToOne
    @JoinColumn(name = "user2_id")
    private Account user2;

    @CreatedDate
    @Column(insertable = false)
    private LocalDateTime createdAt;

    public Friendship() {}

    public Friendship(Account user1, Account user2) {
        this.user1 = user1;
        this.user2 = user2;
        this.createdAt = LocalDateTime.now();
    }





}



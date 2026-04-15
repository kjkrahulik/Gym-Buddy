package com.gymbuddy.app.SocialDomain;

import com.gymbuddy.app.AccountDomain.Account;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "friendships")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    private LocalDateTime createdAt;
}
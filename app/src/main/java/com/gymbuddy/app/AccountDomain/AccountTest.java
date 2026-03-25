package com.gymbuddy.app.AccountDomain;

import jakarta.persistence.*;
import lombok.*;

/**
 * Test entity for experimenting with database operations.
 * Can be safely used for local development without affecting real user data.
 */
@Entity
@Table(name = "accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Auto-generated primary key

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String email;
    private boolean isActive;

}

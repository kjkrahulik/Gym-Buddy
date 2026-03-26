package com.gymbuddy.app.AccountDomain;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represetn a user accoutn in the Gym lBuddy system
 * Stores account related information only (not authentication logic)
 */
@Entity
@Table(name = "accounts")
@Data
public class Account {
    /** Holds account ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final UUID accountID;
    /** Holds user email */
    private String email;
    /** Holds users username */
    private String username;
    /** Holds users password */
    private String password;
    /** Holds value if user is logged in or not */
    private boolean isLoggedIn;
    /** Object holding users Profile */
    private Profile profile;
    /** Object with users goal */
    private Goal goal;
    /** Object with users diet */
    private Diet diet;

    /** Constructor to create an account 
     * Automatically give account and accountID
    */
    public Account(String email, String username, String password) {
        this.accountID = UUID.randomUUID();
        setAccountDetails(email, username, password);
    }

    private void setAccountDetails(String email, String username, String password){
        validateAccountEmail(email);
        validateAccountUsername(username);
        validateAccountPassword(password);

        this.email = email;
        this.username = username;
        this.password = password;
    }

    private void validateAccountEmail(String email){
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Account holder email cannot be null or empty");
        }
    }

    private void validateAccountUsername(String username){
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Account holder username cannot be null or empty");
        }
    }
    private void validateAccountPassword(String password){
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Account holder password cannot be null or empty");
        }
    }
}


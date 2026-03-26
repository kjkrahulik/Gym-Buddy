package com.gymbuddy.app.AccountDomain;
import java.util.UUID;

/**
 * Represetn a user accoutn in the Gym lBuddy system
 * Stores account related information only (not authentication logic)
 */
public class Account {
    /** Holds account ID */
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

    /**
     * Returns users accountID
     */
    public UUID getAccountID() {
        return accountID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }

    public Diet getDiet() {
        return diet;
    }

    public void setDiet(Diet diet) {
        this.diet = diet;
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


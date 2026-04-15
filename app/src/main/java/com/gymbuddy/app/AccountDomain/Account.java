package com.gymbuddy.app.AccountDomain;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIgnore;

import com.gymbuddy.app.SocialDomain.Invitation;
import com.gymbuddy.app.WorkoutDomain.Workout.WorkoutSession;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.CascadeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Import notification clsss to store a list of notification on the web app
import com.gymbuddy.app.SocialDomain.Notification;
import com.gymbuddy.app.SocialDomain.FriendRequest;

/**
 * Represetn a user accoutn in the Gym lBuddy system
 * Stores account related information only (not authentication logic)
 */
@Entity
@Table(name = "accounts")
public class Account {
    /** Holds account ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID accountID;
    /** Holds user email */
    @Column(unique = true, nullable = false)
    private String email;
    /** Holds users username */
    @Column(unique = true, nullable = false)
    private String username;
    /** Holds users password */
    private String password;


    //List of friends of the user
    @ManyToMany
    @JsonIgnore
    @JoinTable(
        name = "friends",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private List<Account> friends = new ArrayList<>();

    // List of notifications received by the user
    @OneToMany(mappedBy = "recipient", cascade = CascadeType.ALL)
    private List<Notification> notifications = new ArrayList<>();

    //List of incoming friend requests
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL)
    private List<FriendRequest> incomingRequests = new ArrayList<>();

    //List of outgoing friend requests
    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    private List<FriendRequest> sentRequests = new ArrayList<>();

    /** Object holding users Profile */
    @Transient
    private Profile profile;
    /** Object with users goal */
    @Transient
    private Goal goal;
    /** Object with users diet */
    @Transient
    private Diet diet;

    @Transient
    private List<WorkoutSession> workoutSession;
    @Transient
    private List<Invitation> invitationList;
    @Transient
    private List<Account> friendList;



    /** Constructor to create an account 
     * Automatically give account and accountID
    */
    public Account(String email, String username, String password) {
        setAccountDetails(email, username, password);
        this.accountID = UUID.randomUUID();
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

    public void setEmail(String email) {
        validateAccountEmail(email);
        this.email = email;
    }

    public void setUsername(String username) {
        validateAccountUsername(username);
        this.username = username;
    }

    public void setPassword(String password) {
        validateAccountPassword(password);
        this.password = password;
    }

    public Profile getProfile() {
        return profile;
    }

    public Goal getGoal() {
        return goal;
    }

    public Diet getDiet() {
        return diet;
    }

    public String getUsername() {
        return username;
    }

    public void addNotification(Notification notification) {
        notifications.add(notification);
        notification.setRecipient(this);
    }

    public void addFriend(Account friend) {
        if (!friends.contains(friend)) {
            friends.add(friend);
            friend.getFriends().add(this); // ensure bidirectional
        }
    }
}


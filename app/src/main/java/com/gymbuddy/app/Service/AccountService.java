package com.gymbuddy.app.Service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gymbuddy.app.AccountDomain.Account;
import com.gymbuddy.app.AccountDomain.Profile;
import com.gymbuddy.app.Repositories.AccountRepository;
import com.gymbuddy.app.Repositories.FriendRequestRepository;
import com.gymbuddy.app.SocialDomain.FriendRequest;
import com.gymbuddy.app.Controller.AccountSearchResult;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FriendRequestRepository friendRequestRepo;

    public Account searchAccount(String username) {
        return accountRepo.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public void deleteAccount(UUID accountID) {
        if (!accountRepo.existsById(accountID)) {
            throw new RuntimeException("Account not found");
        }
        accountRepo.deleteById(accountID);
    }

    @Transactional
    public void addAccount(Account account) {
        if (accountRepo.existsByUsername(account.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (accountRepo.existsByEmail(account.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        account.setPassword(passwordEncoder.encode(account.getPassword()));

        Profile profile = new Profile();
        profile.setAccount(account);
        account.setProfile(profile);

        accountRepo.save(account);
    }

    // Admin-level update: can change email, username, and password by account ID
    public Account updateAccount(UUID accountID, String newEmail, String newUsername, String newPassword) {
        Account account = accountRepo.findById(accountID)
            .orElseThrow(() -> new RuntimeException("Account not found"));

        if (newEmail != null && !newEmail.isBlank()) {
            account.setEmail(newEmail);
        }
        if (newUsername != null && !newUsername.isBlank()) {
            if (!account.getUsername().equals(newUsername) && accountRepo.existsByUsername(newUsername)) {
                throw new RuntimeException("Username already taken");
            }
            account.setUsername(newUsername);
        }
        if (newPassword != null && !newPassword.isBlank()) {
            account.setPassword(passwordEncoder.encode(newPassword));
        }

        return accountRepo.save(account);
    }

    // Self-service update: logged-in user can change their own email or username
    public Account updateAccountForCurrentUser(String currentUsername, String newEmail, String newUsername) {
        Account account = accountRepo.findByUsername(currentUsername)
            .orElseThrow(() -> new RuntimeException("Account not found"));

        if (newEmail != null && !newEmail.isBlank()) {
            if (!account.getEmail().equals(newEmail) && accountRepo.existsByEmail(newEmail)) {
                throw new RuntimeException("Email already taken");
            }
            account.setEmail(newEmail);
        }
        if (newUsername != null && !newUsername.isBlank()) {
            if (!currentUsername.equals(newUsername) && accountRepo.existsByUsername(newUsername)) {
                throw new RuntimeException("Username already taken");
            }
            account.setUsername(newUsername);
        }

        return accountRepo.save(account);
    }

    public Iterable<Account> getAllAccountsExcept(Account account) {
        return accountRepo.findAll().stream()
                .filter(a -> !a.equals(account))
                .filter(a -> !account.getFriends().contains(a))
                .filter(a -> !hasPendingFriendRequest(account, a))
                .toList();
    }

    private boolean hasPendingFriendRequest(Account user, Account potentialFriend) {
        // Check if user sent a request to potentialFriend
        var sentRequest = friendRequestRepo.findBySenderAndReceiverAndStatus(user, potentialFriend, FriendRequest.Status.PENDING);
        if (sentRequest.isPresent()) {
            return true;
        }

        // Check if potentialFriend sent a request to user
        var receivedRequest = friendRequestRepo.findBySenderAndReceiverAndStatus(potentialFriend, user, FriendRequest.Status.PENDING);
        if (receivedRequest.isPresent()) {
            return true;
        }

        return false;
    }

    public Account getAccountById(UUID accountID) {
        return accountRepo.findById(accountID)
            .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    public List<AccountSearchResult> getAccountsWithFriendStatus(Account currentUser) {
        List<AccountSearchResult> results = new ArrayList<>();

        for (Account account : accountRepo.findAll()) {
            // Skip the current user
            if (account.equals(currentUser)) {
                continue;
            }

            // Skip if already friends
            if (currentUser.getFriends().contains(account)) {
                continue;
            }

            // Check for pending requests
            Optional<FriendRequest> sentRequest = friendRequestRepo.findBySenderAndReceiverAndStatus(
                currentUser, account, FriendRequest.Status.PENDING
            );
            if (sentRequest.isPresent()) {
                results.add(new AccountSearchResult(account, "sent"));
                continue;
            }

            Optional<FriendRequest> receivedRequest = friendRequestRepo.findBySenderAndReceiverAndStatus(
                account, currentUser, FriendRequest.Status.PENDING
            );
            if (receivedRequest.isPresent()) {
                results.add(new AccountSearchResult(account, "received"));
                continue;
            }

            // No relationship - can send friend request
            results.add(new AccountSearchResult(account, null));
        }

        return results;
    }
}

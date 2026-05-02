package com.gymbuddy.app.Service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gymbuddy.app.AccountDomain.Account;
import com.gymbuddy.app.AccountDomain.Profile;
import com.gymbuddy.app.Repositories.AccountRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
}

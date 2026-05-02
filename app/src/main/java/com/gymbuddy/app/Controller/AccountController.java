package com.gymbuddy.app.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gymbuddy.app.AccountDomain.Account;
import com.gymbuddy.app.Service.AccountService;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable UUID id) {
        // You'll need to add a getAccountById method in AccountService
        // For now, returning 404
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<Account> getAccountByUsername(@PathVariable String username) {
        Account account = accountService.searchAccount(username);
        if (account != null) {
            return ResponseEntity.ok(account);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping
    public ResponseEntity<?> createAccount(@RequestBody Account account) {
        try {
            accountService.addAccount(account);
            return ResponseEntity.ok("Account created");

        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Username already exists");

        } catch (RuntimeException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAccount(@PathVariable UUID id, @RequestBody Account account) {
        // You'll need to add an updateAccount method in AccountService
        // For now, returning 501 Not Implemented
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Update account endpoint not implemented yet");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable UUID id) {
        // You'll need to add a deleteAccountById method in AccountService
        // For now, returning 501 Not Implemented
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Delete account endpoint not implemented yet");
    }
}
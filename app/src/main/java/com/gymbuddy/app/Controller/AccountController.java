package com.gymbuddy.app.Controller;


import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gymbuddy.app.AccountDomain.Account;
import com.gymbuddy.app.Service.AccountService;




@RestController
public class AccountController {
    
    @Autowired
    private AccountService accountService;

    // GET account by username
    @GetMapping("/username/{username}")
    public ResponseEntity<Account> getAccountByUsername(@PathVariable String username) {
        return ResponseEntity.ok(accountService.searchAccount(username));
    }

    // CREATE account
    @PostMapping
    public ResponseEntity<String> createAccount(@RequestBody Account account) {
        accountService.addAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED).body("Account created Successfully");
    }

    // DELETE account
    @DeleteMapping("/{accountID}")
    public ResponseEntity<String> deleteAccount(@PathVariable UUID accountID) {
        accountService.deleteAccount(accountID);
        return ResponseEntity.ok("Account deleted successfully");
    }

    // ADMIN update account
    @PutMapping("/{accountID}")
    public ResponseEntity<Account> updateAccount(
            @PathVariable UUID accountID,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String password
    ) {
        Account updated = accountService.updateAccount(accountID, email, username, password);
        return ResponseEntity.ok(updated);
    }

    // CURRENT USER update (by username)
    @PutMapping("/me/{username}")
    public ResponseEntity<Account> updateCurrentUser(
            @PathVariable String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String newUsername
    ) {
        Account updated = accountService.updateAccountForCurrentUser(username, email, newUsername);
        return ResponseEntity.ok(updated);
    }
    
}

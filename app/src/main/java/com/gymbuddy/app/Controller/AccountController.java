package com.gymbuddy.app.Controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import com.gymbuddy.app.AccountDomain.Account;
import com.gymbuddy.app.AccountDomain.AuthService;
import com.gymbuddy.app.Service.AccountService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserDetailsService userDetailsService;

    // ── Account CRUD ─────────────────────────────────────────────────

    @GetMapping("/username/{username}")
    public ResponseEntity<Account> getAccountByUsername(@PathVariable String username) {
        return ResponseEntity.ok(accountService.searchAccount(username));
    }

    @PostMapping
    public ResponseEntity<String> addAccount(@RequestBody Account account) {
        accountService.addAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED).body("Account created successfully");
    }

    @DeleteMapping("/{accountID}")
    public ResponseEntity<String> deleteAccount(@PathVariable UUID accountID) {
        accountService.deleteAccount(accountID);
        return ResponseEntity.ok("Account deleted successfully");
    }

    // Admin update — change email, username, or password by account ID
    @PutMapping("/{accountID}")
    public ResponseEntity<Account> updateAccount(
            @PathVariable UUID accountID,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String password
    ) {
        return ResponseEntity.ok(accountService.updateAccount(accountID, email, username, password));
    }

    // Self-service update — logged-in user changes their own email or username
    @PutMapping("/me/{username}")
    public ResponseEntity<Account> updateCurrentUser(
            @PathVariable String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String newUsername,
            HttpServletRequest request
    ) {
        Account updated = accountService.updateAccountForCurrentUser(username, email, newUsername);

        // If the username changed, refresh the security context so the session stays valid
        if (newUsername != null && !newUsername.isBlank() && !newUsername.equals(username)) {
            UserDetails newDetails = userDetailsService.loadUserByUsername(newUsername);
            UsernamePasswordAuthenticationToken newAuth =
                new UsernamePasswordAuthenticationToken(newDetails, null, newDetails.getAuthorities());
            SecurityContext newContext = SecurityContextHolder.createEmptyContext();
            newContext.setAuthentication(newAuth);
            SecurityContextHolder.setContext(newContext);
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, newContext);
            }
        }

        return ResponseEntity.ok(updated);
    }

    // ── Password reset ───────────────────────────────────────────────

    // Step 1: send a verification code to the account's registered email
    @PostMapping("/me/{username}/password/request-reset")
    public ResponseEntity<String> requestPasswordReset(@PathVariable String username) {
        try {
            authService.requestPasswordReset(username);
            return ResponseEntity.ok("Verification code sent.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Step 2: verify the code and set a new password
    @PutMapping("/me/{username}/password/reset")
    public ResponseEntity<String> resetPassword(
            @PathVariable String username,
            @RequestParam String code,
            @RequestParam String newPassword
    ) {
        try {
            authService.resetPassword(username, newPassword, code);
            return ResponseEntity.ok("Password updated successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}

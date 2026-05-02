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
import org.springframework.web.multipart.MultipartFile;

import com.gymbuddy.app.AccountDomain.Account;
import com.gymbuddy.app.AccountDomain.AuthService;
import com.gymbuddy.app.AccountDomain.Profile;
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

    // GET account by username
    @GetMapping("/username/{username}")
    public ResponseEntity<Account> getAccountByUsername(@PathVariable String username) {
        return ResponseEntity.ok(accountService.searchAccount(username));
    }

    // CREATE account
    @PostMapping
    public ResponseEntity<String> addAccount(@RequestBody Account account) {
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

    // ==================== Profile Endpoints ====================

    // GET profile by username
    @GetMapping("/me/{username}/profile")
    public ResponseEntity<Profile> getProfile(@PathVariable String username) {
        return ResponseEntity.ok(accountService.getProfile(username));
    }

    // UPDATE bio
    @PutMapping("/me/{username}/profile/bio")
    public ResponseEntity<Profile> updateBio(
            @PathVariable String username,
            @RequestParam String bio
    ) {
        return ResponseEntity.ok(accountService.updateBio(username, bio));
    }

    // ==================== Profile Picture Endpoints ====================

    @PostMapping("/me/{username}/profile/picture")
    public ResponseEntity<String> uploadProfilePicture(
            @PathVariable String username,
            @RequestParam("file") MultipartFile imageFile
    ) {
        try {
            accountService.uploadProfilePicture(username, imageFile.getInputStream());
            return ResponseEntity.ok("Profile picture uploaded successfully for: " + username);
        } catch (Exception e) {
            throw new RuntimeException("Error uploading profile picture: " + e.getMessage(), e);
        }
    }

    @PostMapping("/me/{username}/profile/picture/base64")
    public ResponseEntity<String> uploadProfilePictureBase64(
            @PathVariable String username,
            @RequestParam("base64") String base64Image
    ) {
        accountService.uploadProfilePictureBase64(username, base64Image);
        return ResponseEntity.ok("Profile picture updated successfully for: " + username);
    }

    @GetMapping("/me/{username}/profile/picture/base64")
    public ResponseEntity<String> getProfilePictureBase64(
            @PathVariable String username
    ) {
        try {
            String base64Image = accountService.getProfilePictureBase64(username);
            return ResponseEntity.ok(base64Image);
        } catch (Exception e) {
            return ResponseEntity.status(404).body("");
        }
    }

    @DeleteMapping("/me/{username}/profile/picture")
    public ResponseEntity<String> deleteProfilePicture(
            @PathVariable String username
    ) {
        accountService.deleteProfilePicture(username);
        return ResponseEntity.ok("Profile picture deleted successfully for: " + username);
    }

    // ==================== Password Reset Endpoints ====================

    // Step 1: Send a verification code to the account's registered email
    @PostMapping("/me/{username}/password/request-reset")
    public ResponseEntity<String> requestPasswordReset(@PathVariable String username) {
        try {
            authService.requestPasswordReset(username);
            return ResponseEntity.ok("Verification code sent.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Step 2: Confirm code and set new password
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

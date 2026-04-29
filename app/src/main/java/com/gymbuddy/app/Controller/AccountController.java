package com.gymbuddy.app.Controller;


import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gymbuddy.app.AccountDomain.Account;
import com.gymbuddy.app.Service.AccountService;




@RestController
@RequestMapping("/accounts")
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
    
    // ==================== Profile Picture Endpoints ====================

    /**
     * Upload profile picture for current user (multipart file upload)
     */
    @PostMapping("/me/{username}/picture")
    public ResponseEntity<String> uploadProfilePicture(
            @PathVariable String username,
            @RequestParam("file") java.io.File imageFile
    ) {
        try {
            // Convert multipart file to InputStream
            java.io.FileInputStream fis = new java.io.FileInputStream(imageFile);
            accountService.uploadProfilePicture(username, fis);
            fis.close();
            return ResponseEntity.ok("Profile picture uploaded successfully for: " + username);
        } catch (Exception e) {
            throw new RuntimeException("Error uploading profile picture: " + e.getMessage(), e);
        }
    }

    /**
     * Upload profile picture from Base64 string (for JSON API)
     */
    @PostMapping("/me/{username}/picture/base64")
    public ResponseEntity<String> uploadProfilePictureBase64(
            @PathVariable String username,
            @RequestParam("base64") String base64Image
    ) {
        accountService.uploadProfilePictureBase64(username, base64Image);
        return ResponseEntity.ok("Profile picture updated successfully for: " + username);
    }

    /**
     * Get profile picture as Base64 string (for JSON API response)
     */
    @GetMapping("/me/{username}/picture/base64")
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

    /**
     * Delete profile picture for current user
     */
    @DeleteMapping("/me/{username}/picture")
    public ResponseEntity<String> deleteProfilePicture(
            @PathVariable String username
    ) {
        accountService.deleteProfilePicture(username);
        return ResponseEntity.ok("Profile picture deleted successfully for: " + username);
    }

    

}

package com.gymbuddy.app.Service;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gymbuddy.app.AccountDomain.Account;
import com.gymbuddy.app.AccountDomain.Profile;
import com.gymbuddy.app.Repositories.AccountRepository;
@Service
public class AccountService {
   
    @Autowired
    private AccountRepository accountRepo;
    
    
    public Account searchAccount(String name) {
        return accountRepo.findByUsername(name).orElseThrow(() 
        -> new RuntimeException("Account not found"));
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

        Profile profile = new Profile();
        profile.setAccount(account);
        account.setProfile(profile);

        accountRepo.save(account);
    }



     // This is a how a developer or admin would update an account
     public Account updateAccount(UUID accountID, String newEmail, String newUsername, String newPassword) {
        Account account = accountRepo.findById(accountID).orElseThrow(() 
        -> new RuntimeException("Account not found"));

         if(newEmail != null && !newEmail.isBlank()) {
         account.setEmail(newEmail);
        }

         if(newUsername != null && !newUsername.isBlank()) {
            // checks the uniqueness of the username if changing
            if(!account.getUsername().equals(newUsername) && accountRepo.existsByUsername(newUsername)) {
               throw new RuntimeException("Username already taken");
            }

            account.setUsername(newUsername);
        }

       if (newPassword != null && !newPassword.isBlank()) {
         account.setPassword(newPassword); // later: hash this
       }

      return accountRepo.save(account);


     }

     public Account updateAccountForCurrentUser(String currentUsername, String newEmail, String newUsername) {
         Account account = accountRepo.findByUsername(currentUsername).orElseThrow(() 
        -> new RuntimeException("Account not found"));

        if(newEmail != null && !newEmail.isBlank()) {
            if(!account.getEmail().equals(newEmail) && accountRepo.existsByEmail(newEmail)) {
                throw new RuntimeException("Email already taken");
            }
            account.setEmail(newEmail);
        }

        if(newUsername != null && !newUsername.isBlank()) {
            if(!currentUsername.equals(newUsername) && accountRepo.existsByUsername(newUsername)) {
                throw new RuntimeException("Username already taken");
            }
            account.setUsername(newUsername);
        }
        return accountRepo.save(account);

     }
     
     public Profile getProfile(String username) {
        Account account = accountRepo.findByUsername(username).orElseThrow(()
            -> new RuntimeException("Account not found"));
        if (account.getProfile() == null) {
            throw new RuntimeException("Profile not found");
        }
        return account.getProfile();
    }

    public Profile updateBio(String username, String bio) {
        Account account = accountRepo.findByUsername(username).orElseThrow(()
            -> new RuntimeException("Account not found"));
        if (account.getProfile() == null) {
            throw new RuntimeException("Profile not found");
        }
        account.getProfile().setBio(bio);
        accountRepo.save(account);
        return account.getProfile();
    }

     // ==================== Profile Picture Methods (BLOB Storage) ====================

    /**
     * Uploads a profile picture for the current user (by username)
     * Accepts image as InputStream (from file upload, multipart request, etc.)
     */
    public void uploadProfilePicture(String currentUsername, java.io.InputStream inputStream) {
        Account account = accountRepo.findByUsername(currentUsername).orElseThrow(() 
            -> new RuntimeException("Account not found"));

        // Ensure account has a profile before setting picture
        if (account.getProfile() == null) {
            throw new RuntimeException("Profile not found for this account. Create a profile first.");
        }

        Profile profile = account.getProfile();
        
        // Read InputStream into byte array
        try {
            profile.setProfilePicture(inputStream.readAllBytes());
        } catch (Exception e) {
            throw new RuntimeException("Error reading image: " + e.getMessage(), e);
        }
        
        // Save the updated profile through the cascade relationship
        accountRepo.save(account);
    }

    /**
     * Uploads a profile picture from Base64 string (for JSON API responses)
     */
    public void uploadProfilePictureBase64(String currentUsername, String base64String) {
        Account account = accountRepo.findByUsername(currentUsername).orElseThrow(() 
            -> new RuntimeException("Account not found"));

        if (account.getProfile() == null) {
            throw new RuntimeException("Profile not found for this account.");
        }

        Profile profile = account.getProfile();
        profile.setProfilePictureBase64(base64String);
        
        accountRepo.save(account);
    }

    /**
     * Deletes/removes the current user's profile picture (by username)
     */
    public void deleteProfilePicture(String currentUsername) {
        Account account = accountRepo.findByUsername(currentUsername).orElseThrow(() 
            -> new RuntimeException("Account not found"));

        if (account.getProfile() == null) {
            throw new RuntimeException("Profile not found for this account.");
        }

        Profile profile = account.getProfile();
        profile.deleteProfilePicture();
        
        accountRepo.save(account);
    }

    /**
     * Gets the current user's profile picture as InputStream (for image display)
     */
    public java.io.InputStream getProfilePictureInputStream(String currentUsername) {
        Account account = accountRepo.findByUsername(currentUsername).orElseThrow(() 
            -> new RuntimeException("Account not found"));

        if (account.getProfile() == null) {
            return null;
        }

        Profile profile = account.getProfile();
        return profile.getProfilePictureInputStream();
    }

    /**
     * Gets the current user's profile picture as Base64 string (for JSON API responses)
     */
    public String getProfilePictureBase64(String currentUsername) {
        Account account = accountRepo.findByUsername(currentUsername).orElseThrow(() 
            -> new RuntimeException("Account not found"));

        if (account.getProfile() == null) {
            return "";
        }

        Profile profile = account.getProfile();
        return profile.getProfilePictureBase64();
    }

}

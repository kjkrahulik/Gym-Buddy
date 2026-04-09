package com.gymbuddy.app.Service;
import java.util.UUID;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gymbuddy.app.AccountDomain.Account;
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

     public void addAccount(Account account) {
        accountRepo.save(account);
     }

     public void logIn(String username, String password) {

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
            if(!account.getUsername().equals(newUsername) && accountRepo.findByUsername(newUsername).isPresent()) {
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
         account.setEmail(newEmail);
        }

         if(newUsername != null && !newUsername.isBlank()) {
            // checks the uniqueness of the username if changing
            if(!currentUsername.equals(newUsername) && accountRepo.findByUsername(newUsername).isPresent()) {
               throw new RuntimeException("Username already taken");
            }

            account.setUsername(newUsername);
        }
        return accountRepo.save(account);

     }
     // Add this in a second 
     public void changePassword(String username, String newPassword, String verificationCode) {
      if(!authService.verifyCode(username, verificationCode)) {
         throw new RuntimeException("Invalid verification code");
      }

      Account account = accountRepo.findByUsername(username).orElseThrow(() 
        -> new RuntimeException("Account not found"));

        account.setPassword(newPassword);
        accountRepo.save(account);
     }
}

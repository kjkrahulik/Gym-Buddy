package com.gymbuddy.app.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.gymbuddy.app.AccountDomain.Account;
import com.gymbuddy.app.Repositories.AccountRepository;
import java.util.List;
@Service
public class AccountService {
   
    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    
    public Account searchAccount(String name) {
        return accountRepo.findByUsername(name).orElse(null);
     }

     public Account deleteAccount(Account account) {
        accountRepo.delete(account);
        return account;
     }

     public Account addAccount(Account account) {
        if (accountRepo.findByUsername(account.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        // Encrypt the password before saving
        String encryptedPassword = passwordEncoder.encode(account.getPassword());
        account.setPassword(encryptedPassword);
        return accountRepo.save(account);
     }

     public List<Account> getAllAccounts() {
        return accountRepo.findAll();
     }

     public void logIn(String username, String password) {

     }

     public void signOut(String username, String password) {

     }

     public void updateAccount(String email, String username, String password) {

     }
}

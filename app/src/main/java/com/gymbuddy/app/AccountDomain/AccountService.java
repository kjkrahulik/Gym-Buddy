package com.gymbuddy.app.AccountDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gymbuddy.app.Repositories.AccountRepository;
@Service
public class AccountService {
   
    @Autowired
    private AccountRepository accountRepo;
    
    
    public Account searchAccount(String name) {
        return accountRepo.findByUsername(name).orElse(null);
     }

     public Account deleteAccount(Account account) {
        accountRepo.delete(account);
        return account;
     }

     public void addAccount(Account account) {
        accountRepo.save(account);
     }

     public void logIn(String username, String password) {

     }

     public void signOut(String username, String password) {

     }

     public void updateAccount(String email, String username, String password) {
        
     }
}

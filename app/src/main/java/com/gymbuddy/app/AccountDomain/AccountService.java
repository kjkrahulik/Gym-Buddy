package com.gymbuddy.app.AccountDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gymbuddy.app.Repositories.WorkoutRepository;

public class AccountService {
     
    private AccountRepository accountRepo;
    
    
    public Account searchAccount(Account account) {
        accountRepo.find(account);
     }

     public Account deleteAccount(Account account) {
        accountRepo.delete(account);
     }

     public Account addAccount(Account account) {
        accountRepo.save(account);
     }

     public void logIn(String username, String password) {

     }

     public void signOut(String username, String password) {

     }

     public void updateAccount(String email, String username, String password) {
        
     }
}

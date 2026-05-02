package com.gymbuddy.app;

import com.gymbuddy.app.AccountDomain.Account;
import com.gymbuddy.app.AccountDomain.AuthService;
import com.gymbuddy.app.Repositories.AccountRepository;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AccountLoader implements CommandLineRunner {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void run(String... args) throws Exception {
        Account user = new Account();
        if (accountRepository.findByUsername("dan").isEmpty()) {

            user.setUsername("dan");
            user.setPassword("securepassword");
            // add own email to test 
            user.setEmail("email@domain");

            accountRepository.save(user);

            System.out.println("Test user created");
        } else {
            System.out.println(" Test user already exists");
        }

    }
}
package com.gymbuddy.app;

import com.gymbuddy.app.AccountDomain.AccountTest;
import com.gymbuddy.app.AccountDomain.AuthService;
import com.gymbuddy.app.Repositories.AccountTestRepository;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AccountLoader implements CommandLineRunner {

    @Autowired
    private AccountTestRepository accountTestRepository;

    // Test email service
    @Autowired
    private AuthService authService;

    @Override
    public void run(String... args) throws Exception {
        AccountTest user = new AccountTest();
        if (accountTestRepository.findByUsername("dan").isEmpty()) {

            user.setUsername("dan");
            user.setPassword("securepassword");
            // add own email to test 
            user.setEmail("email@domain");

            accountTestRepository.save(user);

            System.out.println("Test user created");
        } else {
            System.out.println(" Test user already exists");
        }

        System.out.println("User ready " + user.getUsername());
        // test email
        authService.requestPasswordReset(user.getUsername());
        

    }
}
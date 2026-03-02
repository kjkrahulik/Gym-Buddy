package com.gymbuddy.app;

import com.gymbuddy.app.AccountDomain.AccountTest;
import com.gymbuddy.app.Repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AccountLoader implements CommandLineRunner {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    public void run(String... args) throws Exception {
        AccountTest user = new AccountTest();
        user.setUsername("dan");
        user.setPassword("securepassword");
        user.setEmail("dan@example.com");

        accountRepository.save(user);

        System.out.println("Saved user: " + user);
    }
}
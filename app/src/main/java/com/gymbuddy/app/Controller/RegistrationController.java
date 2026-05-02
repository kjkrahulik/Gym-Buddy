package com.gymbuddy.app.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gymbuddy.app.AccountDomain.Account;
import com.gymbuddy.app.Service.AccountService;


@Controller
public class RegistrationController {

    private final AccountService accountService;

    public RegistrationController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    @PostMapping("/api/account/register")
    public String registerAccount(
            @RequestParam String email,
            @RequestParam String username,
            @RequestParam String password,
            Model model) {

        try {
            Account newAccount = new Account(email, username, password);
            accountService.addAccount(newAccount);
            model.addAttribute("success", "Account created successfully! You can now login.");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "register";
    }
}
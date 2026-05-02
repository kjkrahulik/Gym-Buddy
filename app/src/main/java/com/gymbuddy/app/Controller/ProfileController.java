package com.gymbuddy.app.Controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.gymbuddy.app.AccountDomain.Account;
import com.gymbuddy.app.AccountDomain.Profile;
import com.gymbuddy.app.Service.AccountService;

@Controller
public class ProfileController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        String username = principal.getName();
        Account account = accountService.searchAccount(username);
        Profile profile = accountService.getProfile(username);

        model.addAttribute("username", username);
        model.addAttribute("email", account.getEmail());
        model.addAttribute("bio", profile.getBio() != null ? profile.getBio() : "");
        model.addAttribute("initial", String.valueOf(username.charAt(0)).toUpperCase());

        return "profile";
    }

    @GetMapping("/account-settings")
    public String accountSettings(Principal principal, Model model) {
        String username = principal.getName();
        Account account = accountService.searchAccount(username);
        Profile profile = accountService.getProfile(username);

        model.addAttribute("username", username);
        model.addAttribute("email", account.getEmail());
        model.addAttribute("bio", profile.getBio() != null ? profile.getBio() : "");

        return "account-settings";
    }
}
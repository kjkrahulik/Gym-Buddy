package com.gymbuddy.app.Controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gymbuddy.app.AccountDomain.Account;
import com.gymbuddy.app.AccountDomain.Profile;
import com.gymbuddy.app.Repositories.AccountRepository;
import com.gymbuddy.app.Repositories.WorkoutSessionRepository;
import com.gymbuddy.app.Service.AccountService;
import com.gymbuddy.app.Service.ProfileService;

@Controller
public class PageController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private WorkoutSessionRepository workoutSessionRepository;

    // ── Auth pages ───────────────────────────────────────────────────

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/api/account/register")
    public String registerAccount(
            @RequestParam String email,
            @RequestParam String username,
            @RequestParam String password,
            Model model) {
        try {
            accountService.addAccount(new com.gymbuddy.app.AccountDomain.Account(email, username, password));
            model.addAttribute("success", "Account created successfully! You can now login.");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "register";
    }

    // ── Profile pages ────────────────────────────────────────────────

    @GetMapping("/profile")
    public String profilePage(Principal principal, Model model) {
        String username = principal.getName();
        Account account = accountService.searchAccount(username);
        Profile profile = profileService.getProfile(username);

        long friendCount = accountRepository.countFriendsByUsername(username);
        long workoutCount = workoutSessionRepository.countByAccount(account);

        model.addAttribute("username", username);
        model.addAttribute("email", account.getEmail());
        model.addAttribute("bio", profile.getBio() != null ? profile.getBio() : "");
        model.addAttribute("initial", String.valueOf(username.charAt(0)).toUpperCase());
        model.addAttribute("friendCount", friendCount);
        model.addAttribute("workoutCount", workoutCount);

        return "profile";
    }

    @GetMapping("/profile-friend-list")
    public String profileFriendListPage(Principal principal, Model model) {
        String username = principal.getName();
        Account account = accountService.searchAccount(username);

        model.addAttribute("friends", account.getFriends());

        return "profile-friend-list";
    }

    @GetMapping("/profile-invitations")
    public String profileInvitationsPage(Principal principal, Model model) {
        String username = principal.getName();
        Account account = accountService.searchAccount(username);

        model.addAttribute("invitations", account.getIncomingRequests()); 
        // temporary placeholder if you don't have InvitationService yet

        return "profile-invitations";
    }



    

    @GetMapping("/account-settings")
    public String accountSettingsPage(Principal principal, Model model) {
        String username = principal.getName();
        Account account = accountService.searchAccount(username);
        Profile profile = profileService.getProfile(username);

        model.addAttribute("username", username);
        model.addAttribute("email", account.getEmail());
        model.addAttribute("bio", profile.getBio() != null ? profile.getBio() : "");

        return "account-settings";
    }

    // ── Workout pages ────────────────────────────────────────────────

    @GetMapping("/workout-templates")
    public String workoutTemplatesPage() {
        return "my-workout-templates";
    }

    @GetMapping("/create-workout-template")
    public String createWorkoutTemplatePage() {
        return "create-workout-template";
    }

    @GetMapping("/workout-session")
    public String workoutSessionPage() {
        return "workout-session";
    }

    @GetMapping("/view-workouts")
    public String viewWorkoutsPage() {
        return "view-workouts";
    }

    @GetMapping("/leaderboard")
    public String leaderboardPage() {
        return "leaderboard";
    }
}
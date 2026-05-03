package com.gymbuddy.app.Controller;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gymbuddy.app.AccountDomain.Account;
import com.gymbuddy.app.AccountDomain.Profile;
import com.gymbuddy.app.Repositories.AccountRepository;
import com.gymbuddy.app.Repositories.InvitationRepository;
import com.gymbuddy.app.Repositories.WorkoutSessionRepository;
import com.gymbuddy.app.Service.AccountService;
import com.gymbuddy.app.Service.FriendService;
import com.gymbuddy.app.Service.InvitationService;
import com.gymbuddy.app.Service.ProfileService;
import com.gymbuddy.app.SocialDomain.Invitation;
import com.gymbuddy.app.WorkoutDomain.Workout.WorkoutSession;

@Controller
public class PageController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private FriendService friendService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private InvitationService invitationService;

    @Autowired
    private InvitationRepository invitationRepository;

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

        List<com.gymbuddy.app.SocialDomain.Invitation> invitations = invitationService.getPendingInvitationsFor(account)
            .stream()
            .filter(inv -> inv.getWorkoutSession() != null)
            .toList();

        model.addAttribute("invitations", invitations);
        return "profile-invitations";
    }

    @GetMapping("/profile-find-friends")
    public String profileFindFriendsPage(Principal principal, Model model) {
        String currentUsername = principal.getName();

        Account currentAccount = accountService.searchAccount(currentUsername);

        model.addAttribute("accounts", accountService.getAccountsWithFriendStatus(currentAccount));

        return "profile-find-friends";
    }

    @PostMapping("/profile-find-friends/send-request")
    public String sendFriendRequest(@RequestParam UUID receiverId, Principal principal) {
        String username = principal.getName();

        Account originator = accountService.searchAccount(username);

        Account targetAccount = accountRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Target account not found"));

        friendService.sendFriendRequest(originator, targetAccount);

        return "redirect:/profile-find-friends";
    }

    @GetMapping("/profile-friend-pending")
    public String pendingFriendRequestsPage(Principal principal, Model model) {
        String username = principal.getName();
        Account account = accountService.searchAccount(username);

        var pendingRequests = account.getIncomingRequests().stream()
            .filter(req -> req.getStatus() == com.gymbuddy.app.SocialDomain.FriendRequest.Status.PENDING)
            .toList();
        model.addAttribute("requests", pendingRequests);

        return "profile-friend-pending";
    }

    @PostMapping("/friend-request/accept")
    public String acceptFriendRequest(@RequestParam Long requestId) {
        friendService.acceptRequest(requestId);

        return "redirect:/profile-friend-pending";
    }

    @PostMapping("/friend-request/decline")
    public String declineFriendRequest(@RequestParam Long requestId) {
        friendService.removeFriendRequest(requestId);

        return "redirect:/profile-friend-pending";
    }

    @PostMapping("/profile-friend-list/remove-friend")
    public String removeFriend(@RequestParam UUID friendId, Principal principal) {
        String username = principal.getName();

        Account account = accountService.searchAccount(username);
        Account friend = accountService.getAccountById(friendId);

        friendService.removeFriend(account, friend);

        return "redirect:/profile-friend-list";
    }

    @PostMapping("/profile-friend-list/invite-friend")
    public String inviteFriendToWorkout(@RequestParam UUID friendId, Principal principal) {
        Account sender = accountService.searchAccount(principal.getName());
        Account receiver = accountService.getAccountById(friendId);

        invitationService.sendWorkoutInvitation(sender, receiver);

        return "redirect:/profile-friend-list";
    }

    @GetMapping("/profile-view-friend/{friendId}")
    public String viewFriendProfile(@PathVariable UUID friendId,
                                    Principal principal,
                                    Model model) {
        Account currentAccount = accountService.searchAccount(principal.getName());
        Account friend = accountService.getAccountById(friendId);

        if (!currentAccount.getFriends().contains(friend)) {
            throw new RuntimeException("You can only view profiles of your friends.");
        }

        Profile friendProfile = profileService.getProfile(friend.getUsername());

        model.addAttribute("friend", friend);
        model.addAttribute("friendProfile", friendProfile);
        model.addAttribute("initial", String.valueOf(friend.getUsername().charAt(0)).toUpperCase());

        return "profile-view-friend";
    }

    // ── Friend API endpoints (AJAX) ──────────────────────────

    @PostMapping("/api/friend-request/send")
    @ResponseBody
    public ResponseEntity<?> sendFriendRequestApi(@RequestParam UUID receiverId, Principal principal) {
        try {
            String username = principal.getName();
            Account originator = accountService.searchAccount(username);
            Account targetAccount = accountRepository.findById(receiverId)
                    .orElseThrow(() -> new RuntimeException("Target account not found"));

            friendService.sendFriendRequest(originator, targetAccount);
            return ResponseEntity.ok(new ApiResponse(true, "Friend request sent successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/api/friend-request/accept")
    @ResponseBody
    public ResponseEntity<?> acceptFriendRequestApi(@RequestParam Long requestId) {
        try {
            friendService.acceptRequest(requestId);
            return ResponseEntity.ok(new ApiResponse(true, "Friend request accepted"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/api/friend-request/decline")
    @ResponseBody
    public ResponseEntity<?> declineFriendRequestApi(@RequestParam Long requestId) {
        try {
            friendService.removeFriendRequest(requestId);
            return ResponseEntity.ok(new ApiResponse(true, "Friend request declined"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/api/friend/remove")
    @ResponseBody
    public ResponseEntity<?> removeFriendApi(@RequestParam UUID friendId, Principal principal) {
        try {
            String username = principal.getName();
            Account account = accountService.searchAccount(username);
            Account friend = accountService.getAccountById(friendId);

            friendService.removeFriend(account, friend);
            return ResponseEntity.ok(new ApiResponse(true, "Friend removed successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    @PostMapping("/api/invitations/{invitationId}/decline")
    @ResponseBody
    public ResponseEntity<?> declineWorkoutInvitation(@PathVariable Long invitationId) {
        try {
            invitationService.declineInvitation(invitationId);
            return ResponseEntity.ok(new ApiResponse(true, "Invitation declined"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
    }

    // Simple response class for API
    public static class ApiResponse {
        public boolean success;
        public String message;

        public ApiResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
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

    // Invite Friends to a Workout Session
    /*
    @GetMapping("/workout-session/{sessionId}/invite-friends")
    public String inviteFriendsPage(@PathVariable Long sessionId,
                                    Principal principal,
                                    Model model) {
        Account account = accountService.searchAccount(principal.getName());
        WorkoutSession workoutSession = workoutSessionService.getWorkoutSessionById(sessionId);

        model.addAttribute("friends", account.getFriends());
        model.addAttribute("workoutSession", workoutSession);

        return "workout-invite-friends";
    }
    @PostMapping("/workout-session/{sessionId}/invite-friend")
    public String inviteFriendToWorkout(@PathVariable Long sessionId,
                                        @RequestParam UUID friendId,
                                        Principal principal) {
        Account sender = accountService.searchAccount(principal.getName());
        Account receiver = accountService.getAccountById(friendId);
        WorkoutSession workoutSession = workoutSessionService.getWorkoutSessionById(sessionId);

        invitationService.sendWorkoutInvitation(sender, receiver, workoutSession);

        return "redirect:/workout-session/" + sessionId + "/invite-friends";
    }
     */


}
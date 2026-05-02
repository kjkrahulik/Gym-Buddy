package com.gymbuddy.app.Controller;

import com.gymbuddy.app.AccountDomain.Account;
import com.gymbuddy.app.AccountDomain.Profile;
import com.gymbuddy.app.Service.AccountService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Only loads the web layer — no database, no full Spring context
@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Replace the real AccountService with a mock for all web-layer tests
    @MockBean
    private AccountService accountService;

    private Account testAccount;
    private Profile testProfile;
    private UUID testId;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testAccount = new Account("test@test.com", "testuser", "password123");
        testProfile = new Profile(testAccount);
        testAccount.setProfile(testProfile);
    }

    // ==================== GET /accounts/username/{username} ====================

    @Test
    void getAccountByUsername_found_returns200WithUsername() throws Exception {
        when(accountService.searchAccount("testuser")).thenReturn(testAccount);

        mockMvc.perform(get("/accounts/username/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@test.com"));
    }

    @Test
    void getAccountByUsername_notFound_returns500() throws Exception {
        when(accountService.searchAccount("unknown"))
                .thenThrow(new RuntimeException("Account not found"));

        mockMvc.perform(get("/accounts/username/unknown"))
                .andExpect(status().is5xxServerError());
    }

    // ==================== POST /accounts ====================

    @Test
    void addAccount_validBody_returns201() throws Exception {
        doNothing().when(accountService).addAccount(any(Account.class));

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@test.com\",\"username\":\"testuser\",\"password\":\"password123\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Account created Successfully"));
    }

    @Test
    void addAccount_duplicateUsername_returns500() throws Exception {
        doThrow(new RuntimeException("Username already exists"))
                .when(accountService).addAccount(any(Account.class));

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@test.com\",\"username\":\"testuser\",\"password\":\"password123\"}"))
                .andExpect(status().is5xxServerError());
    }

    // ==================== DELETE /accounts/{accountID} ====================

    @Test
    void deleteAccount_exists_returns200() throws Exception {
        doNothing().when(accountService).deleteAccount(testId);

        mockMvc.perform(delete("/accounts/" + testId))
                .andExpect(status().isOk())
                .andExpect(content().string("Account deleted successfully"));
    }

    @Test
    void deleteAccount_notFound_returns500() throws Exception {
        doThrow(new RuntimeException("Account not found"))
                .when(accountService).deleteAccount(testId);

        mockMvc.perform(delete("/accounts/" + testId))
                .andExpect(status().is5xxServerError());
    }

    // ==================== GET /accounts/me/{username}/profile ====================

    @Test
    void getProfile_found_returns200() throws Exception {
        when(accountService.getProfile("testuser")).thenReturn(testProfile);

        mockMvc.perform(get("/accounts/me/testuser/profile"))
                .andExpect(status().isOk());
    }

    @Test
    void getProfile_notFound_returns500() throws Exception {
        when(accountService.getProfile("unknown"))
                .thenThrow(new RuntimeException("Account not found"));

        mockMvc.perform(get("/accounts/me/unknown/profile"))
                .andExpect(status().is5xxServerError());
    }

    // ==================== PUT /accounts/me/{username}/profile/bio ====================

    @Test
    void updateBio_success_returns200WithUpdatedBio() throws Exception {
        // Set the bio on the profile so it appears in the response
        testProfile.setBio("new bio");
        when(accountService.updateBio(eq("testuser"), eq("new bio"))).thenReturn(testProfile);

        mockMvc.perform(put("/accounts/me/testuser/profile/bio")
                        .param("bio", "new bio"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bio").value("new bio"));
    }

    @Test
    void updateBio_accountNotFound_returns500() throws Exception {
        when(accountService.updateBio(eq("unknown"), any()))
                .thenThrow(new RuntimeException("Account not found"));

        mockMvc.perform(put("/accounts/me/unknown/profile/bio")
                        .param("bio", "some bio"))
                .andExpect(status().is5xxServerError());
    }

    // ==================== PUT /accounts/me/{username} ====================

    @Test
    void updateCurrentUser_newEmail_returns200() throws Exception {
        when(accountService.updateAccountForCurrentUser("testuser", "new@test.com", null))
                .thenReturn(testAccount);

        mockMvc.perform(put("/accounts/me/testuser")
                        .param("email", "new@test.com"))
                .andExpect(status().isOk());
    }
}

package com.gymbuddy.app.Controller;

import com.gymbuddy.app.AccountDomain.Account;
import com.gymbuddy.app.AccountDomain.AuthService;
import com.gymbuddy.app.SecurityConfig;
import com.gymbuddy.app.Service.AccountService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Only loads the web layer — no database, no full Spring context
@WebMvcTest(AccountController.class)
@Import(SecurityConfig.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountService accountService;

    @MockitoBean
    private AuthService authService;

    // Required by AccountController and by Spring Security's auth mechanism
    @MockitoBean
    private UserDetailsService userDetailsService;

    private Account testAccount;
    private UUID testId;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testAccount = new Account("test@test.com", "testuser", "password123");
    }

    // ==================== GET /accounts/username/{username} ====================

    @Test
    @WithMockUser
    void getAccountByUsername_found_returns200WithAccount() throws Exception {
        when(accountService.searchAccount("testuser")).thenReturn(testAccount);

        mockMvc.perform(get("/accounts/username/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@test.com"));
    }

    @Test
    @WithMockUser
    void getAccountByUsername_notFound_returns500() throws Exception {
        when(accountService.searchAccount("unknown"))
                .thenThrow(new RuntimeException("Account not found"));

        mockMvc.perform(get("/accounts/username/unknown"))
                .andExpect(status().is5xxServerError());
    }

    // ==================== POST /accounts ====================

    @Test
    @WithMockUser
    void addAccount_validBody_returns201() throws Exception {
        doNothing().when(accountService).addAccount(any(Account.class));

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"test@test.com\",\"username\":\"testuser\",\"password\":\"password123\"}"))
                .andExpect(status().isCreated())
                .andExpect(content().string("Account created successfully"));
    }

    @Test
    @WithMockUser
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
    @WithMockUser
    void deleteAccount_exists_returns200() throws Exception {
        doNothing().when(accountService).deleteAccount(testId);

        mockMvc.perform(delete("/accounts/" + testId))
                .andExpect(status().isOk())
                .andExpect(content().string("Account deleted successfully"));
    }

    @Test
    @WithMockUser
    void deleteAccount_notFound_returns500() throws Exception {
        doThrow(new RuntimeException("Account not found"))
                .when(accountService).deleteAccount(testId);

        mockMvc.perform(delete("/accounts/" + testId))
                .andExpect(status().is5xxServerError());
    }

    // ==================== PUT /accounts/me/{username} ====================

    @Test
    @WithMockUser
    void updateCurrentUser_newEmail_returns200() throws Exception {
        when(accountService.updateAccountForCurrentUser("testuser", "new@test.com", null))
                .thenReturn(testAccount);

        mockMvc.perform(put("/accounts/me/testuser")
                        .param("email", "new@test.com"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void updateCurrentUser_takenEmail_returns500() throws Exception {
        when(accountService.updateAccountForCurrentUser(eq("testuser"), eq("taken@test.com"), any()))
                .thenThrow(new RuntimeException("Email already taken"));

        mockMvc.perform(put("/accounts/me/testuser")
                        .param("email", "taken@test.com"))
                .andExpect(status().is5xxServerError());
    }

    // ==================== POST /accounts/me/{username}/password/request-reset ====================

    @Test
    @WithMockUser
    void requestPasswordReset_success_returns200() throws Exception {
        doNothing().when(authService).requestPasswordReset("testuser");

        mockMvc.perform(post("/accounts/me/testuser/password/request-reset"))
                .andExpect(status().isOk())
                .andExpect(content().string("Verification code sent."));
    }

    @Test
    @WithMockUser
    void requestPasswordReset_accountNotFound_returns400() throws Exception {
        doThrow(new RuntimeException("Account not found"))
                .when(authService).requestPasswordReset("unknown");

        mockMvc.perform(post("/accounts/me/unknown/password/request-reset"))
                .andExpect(status().isBadRequest());
    }

    // ==================== PUT /accounts/me/{username}/password/reset ====================

    @Test
    @WithMockUser
    void resetPassword_validCode_returns200() throws Exception {
        doNothing().when(authService).resetPassword("testuser", "newpass123", "123456");

        mockMvc.perform(put("/accounts/me/testuser/password/reset")
                        .param("code", "123456")
                        .param("newPassword", "newpass123"))
                .andExpect(status().isOk())
                .andExpect(content().string("Password updated successfully."));
    }

    @Test
    @WithMockUser
    void resetPassword_invalidCode_returns400() throws Exception {
        doThrow(new RuntimeException("Invalid verification code"))
                .when(authService).resetPassword("testuser", "newpass123", "000000");

        mockMvc.perform(put("/accounts/me/testuser/password/reset")
                        .param("code", "000000")
                        .param("newPassword", "newpass123"))
                .andExpect(status().isBadRequest());
    }
}

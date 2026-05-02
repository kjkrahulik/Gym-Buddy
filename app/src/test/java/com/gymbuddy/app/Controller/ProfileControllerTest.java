package com.gymbuddy.app.Controller;

import com.gymbuddy.app.AccountDomain.Account;
import com.gymbuddy.app.AccountDomain.Profile;
import com.gymbuddy.app.SecurityConfig;
import com.gymbuddy.app.Service.AccountService;
import com.gymbuddy.app.Service.ProfileService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Only loads the web layer — no database, no full Spring context
@WebMvcTest(ProfileController.class)
@Import(SecurityConfig.class)
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProfileService profileService;

    @MockitoBean
    private AccountService accountService;

    // Required by Spring Security's auth mechanism
    @MockitoBean
    private UserDetailsService userDetailsService;

    private Account testAccount;
    private Profile testProfile;

    @BeforeEach
    void setUp() {
        // Username must match @WithMockUser default ("user") for page route tests
        testAccount = new Account("test@test.com", "user", "password123");
        testProfile = new Profile(testAccount);
    }

    // ==================== Page routes ====================

    @Test
    @WithMockUser
    void profilePage_authenticatedUser_returns200() throws Exception {
        when(accountService.searchAccount("user")).thenReturn(testAccount);
        when(profileService.getProfile("user")).thenReturn(testProfile);

        mockMvc.perform(get("/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"));
    }

    @Test
    @WithMockUser
    void accountSettingsPage_authenticatedUser_returns200() throws Exception {
        when(accountService.searchAccount("user")).thenReturn(testAccount);
        when(profileService.getProfile("user")).thenReturn(testProfile);

        mockMvc.perform(get("/account-settings"))
                .andExpect(status().isOk())
                .andExpect(view().name("account-settings"));
    }

    // ==================== GET /profile/{username} ====================

    @Test
    @WithMockUser
    void getProfile_found_returns200WithProfile() throws Exception {
        when(profileService.getProfile("testuser")).thenReturn(testProfile);

        mockMvc.perform(get("/profile/testuser"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void getProfile_notFound_returns500() throws Exception {
        when(profileService.getProfile("unknown"))
                .thenThrow(new RuntimeException("Profile not found"));

        mockMvc.perform(get("/profile/unknown"))
                .andExpect(status().is5xxServerError());
    }

    // ==================== PUT /profile/{username}/bio ====================

    @Test
    @WithMockUser
    void updateBio_success_returns200() throws Exception {
        when(profileService.updateBio("testuser", "I love lifting")).thenReturn(testProfile);

        mockMvc.perform(put("/profile/testuser/bio")
                        .param("bio", "I love lifting"))
                .andExpect(status().isOk());
    }

    // ==================== POST /profile/{username}/picture ====================

    @Test
    @WithMockUser
    void uploadPicture_success_returns200() throws Exception {
        doNothing().when(profileService).uploadProfilePicture(eq("testuser"), any());

        MockMultipartFile file = new MockMultipartFile(
                "file", "photo.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[]{1, 2, 3});

        mockMvc.perform(multipart("/profile/testuser/picture").file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("Profile picture uploaded."));
    }

    // ==================== GET /profile/{username}/picture ====================

    @Test
    @WithMockUser
    void getPicture_hasPicture_returns200WithBase64() throws Exception {
        when(profileService.getProfilePictureBase64("testuser")).thenReturn("abc123base64==");

        mockMvc.perform(get("/profile/testuser/picture"))
                .andExpect(status().isOk())
                .andExpect(content().string("abc123base64=="));
    }

    @Test
    @WithMockUser
    void getPicture_noPictureSet_returns404() throws Exception {
        when(profileService.getProfilePictureBase64("testuser")).thenReturn("");

        mockMvc.perform(get("/profile/testuser/picture"))
                .andExpect(status().isNotFound());
    }

    // ==================== DELETE /profile/{username}/picture ====================

    @Test
    @WithMockUser
    void deletePicture_success_returns200() throws Exception {
        doNothing().when(profileService).deleteProfilePicture("testuser");

        mockMvc.perform(delete("/profile/testuser/picture"))
                .andExpect(status().isOk())
                .andExpect(content().string("Profile picture deleted."));
    }
}

package com.gymbuddy.app.Service;

import com.gymbuddy.app.AccountDomain.Account;
import com.gymbuddy.app.AccountDomain.Profile;
import com.gymbuddy.app.Repositories.ProfileRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Pure unit tests — no Spring context, no database, just Mockito
@ExtendWith(MockitoExtension.class)
class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepo;

    @InjectMocks
    private ProfileService profileService;

    private Account testAccount;
    private Profile testProfile;

    @BeforeEach
    void setUp() {
        testAccount = new Account("test@test.com", "testuser", "password123");
        testProfile = new Profile(testAccount);
    }

    // ==================== getProfile ====================

    @Test
    void getProfile_found_returnsProfile() {
        when(profileRepo.findByAccount_Username("testuser")).thenReturn(Optional.of(testProfile));

        Profile result = profileService.getProfile("testuser");

        assertSame(testProfile, result);
    }

    @Test
    void getProfile_notFound_throwsException() {
        when(profileRepo.findByAccount_Username("unknown")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> profileService.getProfile("unknown"));
    }

    // ==================== updateBio ====================

    @Test
    void updateBio_success_setsValueAndSaves() {
        when(profileRepo.findByAccount_Username("testuser")).thenReturn(Optional.of(testProfile));
        when(profileRepo.save(testProfile)).thenReturn(testProfile);

        profileService.updateBio("testuser", "I love lifting");

        assertEquals("I love lifting", testProfile.getBio());
        verify(profileRepo).save(testProfile);
    }

    // ==================== uploadProfilePicture ====================

    @Test
    void uploadProfilePicture_success_savesCalled() {
        byte[] imageBytes = new byte[]{1, 2, 3};
        when(profileRepo.findByAccount_Username("testuser")).thenReturn(Optional.of(testProfile));

        profileService.uploadProfilePicture("testuser", new ByteArrayInputStream(imageBytes));

        assertArrayEquals(imageBytes, testProfile.getProfilePicture());
        verify(profileRepo).save(testProfile);
    }

    @Test
    void uploadProfilePicture_ioError_throwsRuntimeExceptionAndSkipsSave() throws Exception {
        InputStream brokenStream = mock(InputStream.class);
        when(brokenStream.readAllBytes()).thenThrow(new IOException("disk full"));
        when(profileRepo.findByAccount_Username("testuser")).thenReturn(Optional.of(testProfile));

        assertThrows(RuntimeException.class,
                () -> profileService.uploadProfilePicture("testuser", brokenStream));
        verify(profileRepo, never()).save(any());
    }

    // ==================== deleteProfilePicture ====================

    @Test
    void deleteProfilePicture_success_pictureNulledAndSaved() {
        testProfile.setProfilePicture(new byte[]{1, 2, 3});
        when(profileRepo.findByAccount_Username("testuser")).thenReturn(Optional.of(testProfile));

        profileService.deleteProfilePicture("testuser");

        assertNull(testProfile.getProfilePicture());
        verify(profileRepo).save(testProfile);
    }

    // ==================== getProfilePictureBase64 ====================

    @Test
    void getProfilePictureBase64_hasPicture_returnsBase64String() {
        byte[] imageBytes = "fake-image".getBytes();
        testProfile.setProfilePicture(imageBytes);
        when(profileRepo.findByAccount_Username("testuser")).thenReturn(Optional.of(testProfile));

        String result = profileService.getProfilePictureBase64("testuser");

        assertEquals(Base64.getEncoder().encodeToString(imageBytes), result);
    }

    @Test
    void getProfilePictureBase64_noProfile_returnsEmptyString() {
        when(profileRepo.findByAccount_Username("unknown")).thenReturn(Optional.empty());

        assertEquals("", profileService.getProfilePictureBase64("unknown"));
    }
}

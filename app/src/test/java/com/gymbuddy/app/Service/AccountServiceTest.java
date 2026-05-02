package com.gymbuddy.app.Service;

import com.gymbuddy.app.AccountDomain.Account;
import com.gymbuddy.app.AccountDomain.Profile;
import com.gymbuddy.app.Repositories.AccountRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Pure unit tests — no Spring context, no database, just Mockito
@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AccountService accountService;

    private Account testAccount;
    private UUID testId;

    @BeforeEach
    void setUp() {
        testId = UUID.randomUUID();
        testAccount = new Account("test@test.com", "testuser", "password123");
        testAccount.setProfile(new Profile(testAccount));
    }

    // ==================== searchAccount ====================

    @Test
    void searchAccount_found_returnsAccount() {
        when(accountRepo.findByUsername("testuser")).thenReturn(Optional.of(testAccount));

        Account result = accountService.searchAccount("testuser");

        assertEquals("testuser", result.getUsername());
    }

    @Test
    void searchAccount_notFound_throwsException() {
        when(accountRepo.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> accountService.searchAccount("unknown"));
    }

    // ==================== addAccount ====================

    @Test
    void addAccount_success_callsSave() {
        when(accountRepo.existsByUsername("testuser")).thenReturn(false);
        when(accountRepo.existsByEmail("test@test.com")).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("$2a$hashed");

        accountService.addAccount(testAccount);

        verify(accountRepo).save(testAccount);
    }

    @Test
    void addAccount_duplicateUsername_throwsAndSkipsSave() {
        when(accountRepo.existsByUsername("testuser")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> accountService.addAccount(testAccount));
        verify(accountRepo, never()).save(any());
    }

    @Test
    void addAccount_duplicateEmail_throwsAndSkipsSave() {
        when(accountRepo.existsByUsername("testuser")).thenReturn(false);
        when(accountRepo.existsByEmail("test@test.com")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> accountService.addAccount(testAccount));
        verify(accountRepo, never()).save(any());
    }

    // ==================== deleteAccount ====================

    @Test
    void deleteAccount_exists_callsDeleteById() {
        when(accountRepo.existsById(testId)).thenReturn(true);

        accountService.deleteAccount(testId);

        verify(accountRepo).deleteById(testId);
    }

    @Test
    void deleteAccount_notFound_throwsAndSkipsDelete() {
        when(accountRepo.existsById(testId)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> accountService.deleteAccount(testId));
        verify(accountRepo, never()).deleteById(any());
    }

    // ==================== updateAccount ====================

    @Test
    void updateAccount_newEmail_updatesAndSaves() {
        when(accountRepo.findById(testId)).thenReturn(Optional.of(testAccount));
        when(accountRepo.save(any())).thenReturn(testAccount);

        accountService.updateAccount(testId, "new@test.com", null, null);

        assertEquals("new@test.com", testAccount.getEmail());
        verify(accountRepo).save(testAccount);
    }

    @Test
    void updateAccount_takenUsername_throwsException() {
        when(accountRepo.findById(testId)).thenReturn(Optional.of(testAccount));
        when(accountRepo.existsByUsername("taken")).thenReturn(true);

        assertThrows(RuntimeException.class, () ->
                accountService.updateAccount(testId, null, "taken", null));
    }

    @Test
    void updateAccount_newPassword_encodesBeforeSaving() {
        when(accountRepo.findById(testId)).thenReturn(Optional.of(testAccount));
        when(accountRepo.save(any())).thenReturn(testAccount);
        when(passwordEncoder.encode("newpass")).thenReturn("$2a$hashed");

        accountService.updateAccount(testId, null, null, "newpass");

        verify(passwordEncoder).encode("newpass");
        verify(accountRepo).save(testAccount);
    }

    @Test
    void updateAccount_accountNotFound_throwsException() {
        when(accountRepo.findById(testId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
                accountService.updateAccount(testId, null, null, null));
    }

    // ==================== updateAccountForCurrentUser ====================

    @Test
    void updateCurrentUser_takenEmail_throwsException() {
        when(accountRepo.findByUsername("testuser")).thenReturn(Optional.of(testAccount));
        when(accountRepo.existsByEmail("taken@test.com")).thenReturn(true);

        assertThrows(RuntimeException.class, () ->
                accountService.updateAccountForCurrentUser("testuser", "taken@test.com", null));
    }

    @Test
    void updateCurrentUser_sameEmail_doesNotCheckUniqueness() {
        when(accountRepo.findByUsername("testuser")).thenReturn(Optional.of(testAccount));
        when(accountRepo.save(any())).thenReturn(testAccount);

        // Same email the account already has — should not throw
        assertDoesNotThrow(() ->
                accountService.updateAccountForCurrentUser("testuser", "test@test.com", null));
    }

    @Test
    void updateCurrentUser_takenUsername_throwsException() {
        when(accountRepo.findByUsername("testuser")).thenReturn(Optional.of(testAccount));
        when(accountRepo.existsByUsername("taken")).thenReturn(true);

        assertThrows(RuntimeException.class, () ->
                accountService.updateAccountForCurrentUser("testuser", null, "taken"));
    }
}

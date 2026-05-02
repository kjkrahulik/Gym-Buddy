package com.gymbuddy.app.AccountDomain;

import com.gymbuddy.app.Repositories.AccountRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Pure unit tests — no Spring context, no real email server
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AccountRepository accountRepo;

    // Mock the mail sender so no real emails are sent during tests
    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private AuthService authService;

    private Account testAccount;

    @BeforeEach
    void setUp() {
        testAccount = new Account("test@test.com", "testuser", "password123");
    }

    // ==================== requestPasswordReset ====================

    @Test
    void requestPasswordReset_success_sendsOneEmail() {
        when(accountRepo.findByUsername("testuser")).thenReturn(Optional.of(testAccount));

        authService.requestPasswordReset("testuser");

        // Verify an email was actually sent
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void requestPasswordReset_emailSentToCorrectAddress() {
        when(accountRepo.findByUsername("testuser")).thenReturn(Optional.of(testAccount));

        // Capture the message that gets passed to mailSender.send()
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        authService.requestPasswordReset("testuser");
        verify(mailSender).send(captor.capture());

        // The email should go to the account's email address
        assertEquals("test@test.com", captor.getValue().getTo()[0]);
    }

    @Test
    void requestPasswordReset_accountNotFound_throwsAndSkipsEmail() {
        when(accountRepo.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.requestPasswordReset("unknown"));
        // Email must never be sent if account doesn't exist
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    // ==================== verifyCode ====================

    @Test
    void verifyCode_correctCode_returnsTrue() {
        when(accountRepo.findByUsername("testuser")).thenReturn(Optional.of(testAccount));

        // Capture the email so we can extract the real generated code
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        authService.requestPasswordReset("testuser");
        verify(mailSender).send(captor.capture());

        // Pull the 6-digit code out of the email body
        String emailText = captor.getValue().getText();
        String code = emailText.replace("Your verification code is: ", "").trim();

        assertTrue(authService.verifyCode("testuser", code));
    }

    @Test
    void verifyCode_wrongCode_returnsFalse() {
        when(accountRepo.findByUsername("testuser")).thenReturn(Optional.of(testAccount));
        authService.requestPasswordReset("testuser");

        // "000000" will never be the real code in practice — treat it as a wrong code
        assertFalse(authService.verifyCode("testuser", "000000"));
    }

    @Test
    void verifyCode_noCodeStored_returnsFalse() {
        // No requestPasswordReset was called so there is nothing in the map
        assertFalse(authService.verifyCode("testuser", "123456"));
    }

    @Test
    void verifyCode_codeConsumedAfterUse_secondCallReturnsFalse() {
        when(accountRepo.findByUsername("testuser")).thenReturn(Optional.of(testAccount));

        // Capture the real code
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        authService.requestPasswordReset("testuser");
        verify(mailSender).send(captor.capture());
        String code = captor.getValue().getText()
                .replace("Your verification code is: ", "").trim();

        // First use succeeds, second use with the same code must fail (single-use)
        assertTrue(authService.verifyCode("testuser", code));
        assertFalse(authService.verifyCode("testuser", code));
    }

    // ==================== resetPassword ====================

    @Test
    void resetPassword_validCode_updatesPasswordAndSaves() {
        when(accountRepo.findByUsername("testuser")).thenReturn(Optional.of(testAccount));

        // Capture the real code from the reset email
        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        authService.requestPasswordReset("testuser");
        verify(mailSender).send(captor.capture());
        String code = captor.getValue().getText()
                .replace("Your verification code is: ", "").trim();

        authService.resetPassword("testuser", "newpassword123", code);

        // Account should be saved with the new password
        verify(accountRepo).save(testAccount);
    }

    @Test
    void resetPassword_invalidCode_throwsException() {
        // No code was ever requested, so any code is invalid
        assertThrows(RuntimeException.class, () ->
                authService.resetPassword("testuser", "newpassword", "000000"));
    }
}

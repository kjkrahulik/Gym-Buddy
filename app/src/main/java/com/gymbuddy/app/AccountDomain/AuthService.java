package com.gymbuddy.app.AccountDomain;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.gymbuddy.app.Repositories.AccountRepository;

/**
 * Handles authentication-related operations such as login,
 * logout, password reset, and account creation.
 */
@Service
public class AuthService {

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private JavaMailSender mailSender;

    private final SecureRandom secureRandom = new SecureRandom();
    private final Map<String, String> verificationCodes = new ConcurrentHashMap<>();
    private final Map<String, Long> verificationExpiry = new ConcurrentHashMap<>();

    private static final long CODE_EXPIRY_MS = 15 * 60 * 1000;

    public void requestPasswordReset(String username) {
        Account account = accountRepo.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Account not found"));

        String code = generateCode();
        verificationCodes.put(username, code);
        verificationExpiry.put(username, System.currentTimeMillis() + CODE_EXPIRY_MS);
        sendVerificationEmail(account.getEmail(), code);
    }

    private void sendVerificationEmail(String toEmail, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("GymBuddy Password Reset");
        message.setText("Your verification code is: " + code);
        mailSender.send(message);
    }

    public boolean verifyCode(String username, String code) {
        if (!verificationCodes.containsKey(username)) {
            return false;
        }

        Long expiry = verificationExpiry.get(username);
        if (expiry == null || System.currentTimeMillis() > expiry) {
            verificationCodes.remove(username);
            verificationExpiry.remove(username);
            return false;
        }

        String storedCode = verificationCodes.get(username);
        if (!storedCode.equals(code)) {
            return false;
        }

        verificationCodes.remove(username);
        verificationExpiry.remove(username);
        return true;
    }

    public void resetPassword(String username, String newPassword, String code) {
        if (!verifyCode(username, code)) {
            throw new RuntimeException("Invalid verification code");
        }

        Account account = accountRepo.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setPassword(newPassword);
        accountRepo.save(account);
    }

    private String generateCode() {
        int code = secureRandom.nextInt(900000) + 100000;
        return String.valueOf(code);
    }

}
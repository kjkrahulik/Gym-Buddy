package com.gymbuddy.app.AccountDomain;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Map<String, String> verificationCodes = new HashMap<>();



    /**
     * Sends a password reset request to the given email.
     */
    public boolean requestPasswordReset(String username) {
        Account account = accountRepo.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("Account not found"));
    
        String code = generateCode();
        verificationCodes.put(username, code);
        sendVerificationEmail(account.getEmail(), code);

        return true;
    }

    /**
     * Send email
     */
    private void sendVerificationEmail(String toEmail, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("GymBuddy Password Reset");
        message.setText("Your verification code is: " + code);

        mailSender.send(message);
    }

    /**
     * Verifies a password reset code.
     */
    public boolean verifyCode(String username, String code) {
        if (!verificationCodes.containsKey(username)) {
            return false;
        }

        String storedCode = verificationCodes.get(username);
        if(!storedCode.equals(code)) {
            return false;
        }

        verificationCodes.remove(username);
        return true;
    }

    /**
     * Resets the user's password.
     */
    public void resetPassword(String username, String newPassword, String code) {
        if(!verifyCode(username, code)) {
            throw new RuntimeException("Invalid verfication code");
        }

        Account account = accountRepo.findByUsername(username)
        .orElseThrow(() -> new RuntimeException("Account not found"));

        account.setPassword(newPassword);
        accountRepo.save(account);

    }

    private String generateCode() {
        int code = (int)(Math.random() * 900000) + 100000;
        return String.valueOf(code);
    }


    public void check() {
        Account test = accounts.get(0);
       test.getAccountID();
    }


    // Test method 
    public void testEmail(String toEmail) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("GymBuddy Test Email");
        message.setText("If you see this, email is working!");

        mailSender.send(message);

        System.out.println("Email sent successfully");
    }

}
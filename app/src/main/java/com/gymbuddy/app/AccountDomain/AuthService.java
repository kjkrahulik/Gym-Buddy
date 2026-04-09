package com.gymbuddy.app.AccountDomain;
import java.util.List;

/**
 * Handles authentication-related operations such as login,
 * logout, password reset, and account creation.
 */
public class AuthService {

    // Stores all registered accounts (temporary storage example)
    private List<Account> accounts;


    /**
     * Sends a password reset request to the given email.
     */
    public boolean requestPasswordReset(String email) {
        return false;
    }

    /**
     * Verifies a password reset code.
     */
    public boolean verifyCode(String code) {
        return false;
    }

    /**
     * Resets the user's password.
     */
    public void resetPassword(Account account, String newPassword) {
    }


    public void check() {
        Account test = accounts.get(0);
       test.getAccountID();
    }

}
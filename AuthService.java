import java.util.List;

/**
 * Handles authentication-related operations such as login,
 * logout, password reset, and account creation.
 */
public class AuthService implements Authenticatable {

    // Stores all registered accounts (temporary storage example)
    private List<Account> accounts;

    /**
     * Attempts to authenticate a user using username and password.
     */
    @Override
    public boolean signIn(String username, String password) {
        return false;
    }

    /**
     * Logs a user out of the system.
     */
    @Override
    public void signOut(Account account) {
    }

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

    /**
     * Creates a new account in the system.
     */
    public void createAccount(Account account) {
    }

    /**
     * Deletes an existing account from the system.
     */
    public void deleteAccount(Account account) {
    }
}
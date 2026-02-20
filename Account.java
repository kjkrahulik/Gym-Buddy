import java.util.List;

/**
 * Represetn a user accoutn in the Gym lBuddy system
 * Stores account related information only (not authentication logic)
 */
public class Account {
    /** Static counter to generate unique account IDS */
    private static int idCounter = 1;
    /** Holds account ID */
    private int accountID;
    /** Holds user email */
    private String email;
    /** Holds users username */
    private String username;
    /** Holds users password */
    private String password;
    /** Holds value if user is logged in or not */
    private boolean isLoggedIn;
    /** Object holding users Profile */
    private Profile profile;
    /** Object with users goal */
    private Goal goal;
    /** Object with users diet */
    private Diet diet;

    /** Constructor to create an account 
     * Automatically give account and accountID
    */
    public Account(String email, String username, String password) {
        this.accountID = idCounter++;
        this.email = email;
        this.username = username;
        this.password = password;
        this.isLoggedIN = false;
    }

    /**
     * Returns users accountID
     */
    public int getAccountID() {
        return accountID;
    }
}
package com.gymbuddy.app.AccountDomain;
/**
 * Interface that defines authentication behavior
 * Any class implementing this must provide login and logout functionality
 */
public interface Authentication {

    /**
     * Attemps to sign user into the systme
     * 
     * @param username The users username
     * @param passowrod The users password
     * @return true if login successful otherwise false
     */
    boolean signIn(String username, String password);

    /**
     * Signs a user out of the system
     * 
     * @param account The account to be logged out
     */
    void signOut (Account account);
}
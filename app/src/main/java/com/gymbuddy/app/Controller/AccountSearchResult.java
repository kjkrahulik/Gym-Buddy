package com.gymbuddy.app.Controller;

import com.gymbuddy.app.AccountDomain.Account;

public class AccountSearchResult {
    private Account account;
    private String requestStatus; // null, "sent", or "received"

    public AccountSearchResult(Account account, String requestStatus) {
        this.account = account;
        this.requestStatus = requestStatus;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }
}

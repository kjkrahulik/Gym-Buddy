package com.gymbuddy.app.AccountDomain;

/**
 * This holds the pre-defined Goals with their tips included
 */
public enum GoalType {
    
    /**
     * Bulking Goal with tip message
     */
    BULKING(" Message"),
    /**
     * Cutting goal with tip message
     */
    CUTTING("Less Calories");

    private final String tipMessage;

    GoalType(String tipMessage) {
        this.tipMessage = tipMessage;
    }

    public String getTipMessage() {
        return tipMessage;
    }
}

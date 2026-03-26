package com.gymbuddy.app.AccountDomain;
import java.time.DayOfWeek;
import java.util.Set;
/**
 * 
 */
public class Goal {
    /**
     * Time boolean less than 45 minutes is false
     * Greater than 45 is true
     */
    private boolean timeOfWorkout;
    /**
     * Days this needs to be talked about still
     */
    private Set<DayOfWeek> daysOFWeek;

    /**
     * Sets the goaltype
     */
    private GoalType goalType;

    /**
     * Default constructor
     */
    public Goal(GoalType goalType, Set<DayOfWeek> daysOFWeek, boolean timeOfWorkout){
        this.timeOfWorkout = timeOfWorkout;
        this.goalType = goalType;
    }

    /**
     * Gets tip message based on goaltype
     */
    public String getTipMessage() {
        return goalType.getTipMessage();
    }

    /**
     * 
     */
    public String getTimeCategory() {
        return timeOfWorkout
            ? "Workout is less than 45 minutes"
            : "Workout is 45 minutes or more";
    }
    /**
     * String message
     */
    @Override
    public String toString() {
        return "Goal Type: " + goalType +
                "\nTime: " + getTimeCategory() +
                "\nWorkoutdays " + daysOFWeek +
                "\nTip Message: " + goalType.getTipMessage();
        

    }

}

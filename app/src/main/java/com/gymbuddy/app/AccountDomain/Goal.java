package com.gymbuddy.app.AccountDomain;
/**
 * 
 */
public class Goal {

    /**
     * Enum representing the type of fitness goal
     */
    public enum GoalType {
        BEGINNER("Focus on form and consistency - start with lighter weights to learn proper technique"),
        BULKING("Focus on progressive overload and caloric surplus for muscle growth"),
        CUTTING("Maintain protein intake while in caloric deficit to preserve muscle mass"),
        WEIGHT_LOSS("Combine cardio with strength training for optimal fat loss");

        private final String tipMessage;

        GoalType(String tipMessage) {
            this.tipMessage = tipMessage;
        }

        public String getTipMessage() {
            return tipMessage;
        }
    }

    /** Holds the type of goal (Beginner, Bulking, Cutting, Weight Loss) */
    private GoalType goalType;

    /**
     * Time commitment - true = more than 45 minutes per day, false = 45 minutes or less per day
     */
    private boolean timeCommitment;

    /** Holds number of workout days per week (1-6 days) */
    private int workoutDays;

    /**
     * Constructor to create a goal with all parameters
     */
    public Goal(GoalType goalType, boolean timeCommitment, int workoutDays) {
        validateWorkoutDays(workoutDays);
        this.goalType = goalType;
        this.timeCommitment = timeCommitment;
        this.workoutDays = workoutDays;
    }

    /**
     * Validates that workout days are between 1-6
     */
    private void validateWorkoutDays(int workoutDays) {
        if (workoutDays < 1 || workoutDays > 6) {
            throw new IllegalArgumentException("Workout days must be between 1 and 6");
        }
    }

    public GoalType getGoalType() {
        return goalType;
    }

    public void setGoalType(GoalType goalType) {
        this.goalType = goalType;
    }

    public boolean isTimeCommitment() {
        return timeCommitment;
    }

    public void setTimeCommitment(boolean timeCommitment) {
        this.timeCommitment = timeCommitment;
    }

    public int getWorkoutDays() {
        return workoutDays;
    }

    public void setWorkoutDays(int workoutDays) {
        validateWorkoutDays(workoutDays);
        this.workoutDays = workoutDays;
    }

    /**
     * Get the tip message for the selected goal type
     */
    public String getGoalTip() {
        return goalType.getTipMessage();
    }

    /**
     * Get time commitment as a descriptive string
     */
    public String getTimeCommitmentDescription() {
        return timeCommitment ? "More than 45 minutes per day" : "45 minutes or less per day";
    }

    /**
     * String representation of the goal
     */
    @Override
    public String toString() {
        return "Goal Type: " + goalType +
                "\nTime Commitment: " + getTimeCommitmentDescription() +
                "\nWorkout Days per Week: " + workoutDays +
                "\nTip: " + getGoalTip();
    }
}

package com.gymbuddy.app.WorkoutDomain.Exercise;

public class TimedExercise extends WorkoutExercise {
    private int durationInSeconds;

    public TimedExercise(Exercise exercise, int order) {
        super(exercise, order);
        this.durationInSeconds = 0; // Default duration of 0 seconds
    }

    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    public void setDurationInSeconds(int durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }
    
}

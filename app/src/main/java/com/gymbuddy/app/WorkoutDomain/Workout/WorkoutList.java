package com.gymbuddy.app.WorkoutDomain.Workout;

import com.gymbuddy.app.WorkoutDomain.Exercise.Exercise;

public interface WorkoutList {
    public void addExercise(Exercise exercise, boolean isWeighted);
    public boolean deleteExercise(Exercise exercise);
} 

package com.gymbuddy.app.WorkoutDomain.Workout;

import com.gymbuddy.app.WorkoutDomain.Exercise.Exercise;
import com.gymbuddy.app.WorkoutDomain.Exercise.WorkoutExercise;

public interface WorkoutList {
    public void addExercise(Exercise exercise, boolean isWeighted);
    public boolean deleteExercise(WorkoutExercise exercise);
} 

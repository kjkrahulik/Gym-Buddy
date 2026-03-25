package com.gymbuddy.app.WorkoutDomain.Workout;
import java.util.ArrayList;

import com.gymbuddy.app.WorkoutDomain.Exercise.Exercise;
import com.gymbuddy.app.WorkoutDomain.Exercise.TimedExercise;
import com.gymbuddy.app.WorkoutDomain.Exercise.WeightedExercise;
import com.gymbuddy.app.WorkoutDomain.Exercise.WorkoutExercise;

public class WorkoutTemplate {
    private String workoutName;
    private String workoutDescription;
    private ArrayList<WorkoutExercise> exercises;
    private int exerciseCount;

    public WorkoutTemplate(String workoutName, String workoutDescription) {
        this.workoutName = workoutName;
        this.workoutDescription = workoutDescription;
        this.exercises = new ArrayList<WorkoutExercise>(5); // Initial capacity for 5 exercises
        this.exerciseCount = 0;
    }

    public void addExercise(Exercise exercise, boolean isWeighted) {
        WorkoutExercise workoutExercise;
        if (isWeighted) {
            workoutExercise = new WeightedExercise(exercise, exerciseCount);
        } else {
            workoutExercise = new TimedExercise(exercise, exerciseCount); // Default duration of 0 seconds
        }
        exercises.add(workoutExercise);
        exerciseCount++;
    }

    public boolean deleteExercise(WorkoutExercise exercise) {
        if (exercises.remove(exercise)) {
            exerciseCount--;
            return true;
        } else {
            return false; // Exercise not found in the list
        }
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public String getWorkoutDescription() {
        return workoutDescription;
    }

    public ArrayList<WorkoutExercise> getExercises() {
        return exercises;
    }
}
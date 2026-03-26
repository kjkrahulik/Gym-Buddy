package com.gymbuddy.app.WorkoutDomain.Workout;

import java.util.ArrayList;

import com.gymbuddy.app.WorkoutDomain.Exercise.Exercise;
import com.gymbuddy.app.WorkoutDomain.Exercise.WorkoutExercise;

public class WorkoutSession {
    private String sessionName;
    private String sessionDescription;
    private ArrayList<WorkoutExercise> exercises;
    private int exerciseCount;

    /*public WorkoutSession(String sessionName, String sessionDescription) {
        this.sessionName = sessionName;
        this.sessionDescription = sessionDescription;
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
        if (exerciseCount > 0) {
            if (exercises.remove(exercise)) {
                exerciseCount--;
                return true;
            } 
        }
        return false; // Exercise not found in the list or no exercises to remove
        
    }

    public String getSessionName() {
        return sessionName;
    }

    public String getSessionDescription() {
        return sessionDescription;
    }

    public ArrayList<WorkoutExercise> getExercises() {
        return exercises;
    }*/
}
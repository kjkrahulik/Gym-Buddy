package com.gymbuddy.app.WorkoutDomain.Exercise;

import com.gymbuddy.app.WorkoutDomain.Workout.WorkoutSession;
import com.gymbuddy.app.WorkoutDomain.Workout.WorkoutTemplate;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public abstract class WorkoutExercise {
    private Exercise exercise;
    private int listOrder;
    @ManyToOne
    @JoinColumn(name = "workout_session_id")
    private WorkoutSession workoutSession;
    
    @ManyToOne
    @JoinColumn(name = "workout_template_id")
    private WorkoutTemplate workoutTemplate;

    public WorkoutExercise(Exercise exercise, int order) {
        this.exercise = exercise;
        this.listOrder = order;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public int getListOrder() {
        return listOrder;
    }

    public void setListOrder(int listOrder) {
        this.listOrder = listOrder;
    }
}

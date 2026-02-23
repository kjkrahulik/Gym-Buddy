package com.gymbuddy.app.WorkoutDomain.Exercise;

public abstract class WorkoutExercise {
    private Exercise exercise;
    private int listOrder;

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

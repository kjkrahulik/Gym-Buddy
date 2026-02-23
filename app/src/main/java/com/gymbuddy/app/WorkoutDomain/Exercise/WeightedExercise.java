package com.gymbuddy.app.WorkoutDomain.Exercise;

public class WeightedExercise extends WorkoutExercise {
    private int sets;
    private int reps;
    private double weight;

    public WeightedExercise(Exercise exercise, int order) {
        super(exercise, order);
        this.weight = 0.0;
        this.sets = 0;
        this.reps = 0;
    }

    public int getSets() {
        return sets;
    }   

    public int getReps() {
        return reps;
    }  

    public double getWeight() {
        return weight;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }   

    public void setReps(int reps) {
        this.reps = reps;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
    
}

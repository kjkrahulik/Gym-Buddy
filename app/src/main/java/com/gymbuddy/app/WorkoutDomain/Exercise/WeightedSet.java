package com.gymbuddy.app.WorkoutDomain.Exercise;

import jakarta.persistence.Entity;

@Entity
public class WeightedSet extends Set {
    private int sets;
    private int reps;
    private double weight;
    
}

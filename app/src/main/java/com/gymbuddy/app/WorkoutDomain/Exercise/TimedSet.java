package com.gymbuddy.app.WorkoutDomain.Exercise;

import jakarta.persistence.Entity;

@Entity
public class TimedSet extends Set {

    private int durationInSeconds;
    
}

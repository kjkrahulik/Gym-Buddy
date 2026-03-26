package com.gymbuddy.app.WorkoutDomain.Exercise;

import com.gymbuddy.app.WorkoutDomain.Exercise.WorkoutExercise;
import lombok.*;
import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
public abstract class Set {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long setID;

    private int order;
    
    @ManyToOne
    @JoinColumn(name = "workout_exercise_id")
    private WorkoutExercise workoutExercise;
}

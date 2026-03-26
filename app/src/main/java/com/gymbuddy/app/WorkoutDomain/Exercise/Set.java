package com.gymbuddy.app.WorkoutDomain.Exercise;

import com.gymbuddy.app.WorkoutDomain.Exercise.WorkoutExercise;
import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "workout_sets")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@AllArgsConstructor
public abstract class Set {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long setID;

    @Column(name = "order_index")
    private int order_index;

    @ManyToOne
    @JoinColumn(name = "workout_exercise_id")
    private WorkoutExercise workoutExercise;
}

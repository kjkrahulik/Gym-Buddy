package com.gymbuddy.app.WorkoutDomain.Exercise;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "exercises")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Exercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long exerciseID;
    private String exerciseName;
    private String muscleGroup;
    private String exerciseDescription;
}



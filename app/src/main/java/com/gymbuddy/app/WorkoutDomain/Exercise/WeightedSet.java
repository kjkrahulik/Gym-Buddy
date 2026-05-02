package com.gymbuddy.app.WorkoutDomain.Exercise;

import jakarta.persistence.*;
import lombok.*;

/**
 * A set defined by a weight and a rep count (e.g., "3 sets of 8 at 100 lb").
 */
@Entity
@Table(name = "weighted_sets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WeightedSet extends Set {

    /** Weight lifted. Units (kg/lb) are the caller's convention. */
    @Column(nullable = false)
    private float weight;

    /** Number of repetitions performed. */
    @Column(nullable = false)
    private int reps;
}

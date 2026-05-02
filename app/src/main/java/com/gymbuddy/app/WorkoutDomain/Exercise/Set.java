package com.gymbuddy.app.WorkoutDomain.Exercise;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

/**
 * Abstract base for all set types (weighted, timed, etc.).
 *
 * Uses JOINED inheritance so each subclass gets its own table with only
 * its extra columns, joined back to workout_sets for the shared fields.
 */
@Entity
@Table(name = "workout_sets")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Set {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long setID;

    /**
     * 0-based position of this set within its exercise's set list.
     * Managed automatically by WorkoutSession#addSet.
     */
    @Column(name = "order_index", nullable = false)
    private int orderIndex;

    /**
     * Wall-clock time the set was performed.
     * Optional — null when not tracked.
     */
    @Column(name = "performed_at")
    private LocalTime time;
}

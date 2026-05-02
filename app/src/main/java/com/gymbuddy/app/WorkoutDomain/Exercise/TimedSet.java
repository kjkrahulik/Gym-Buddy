package com.gymbuddy.app.WorkoutDomain.Exercise;

import jakarta.persistence.*;
import lombok.*;

/**
 * A set defined by a duration rather than a rep count
 * (e.g., a 60-second plank).
 */
@Entity
@Table(name = "timed_sets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TimedSet extends Set {

    /** How long the set lasted, in seconds. */
    @Column(name = "duration_seconds", nullable = false)
    private int durationInSeconds;
}

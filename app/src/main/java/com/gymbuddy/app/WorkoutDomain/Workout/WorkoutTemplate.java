package com.gymbuddy.app.WorkoutDomain.Workout;

import jakarta.persistence.*;
import lombok.*;

/**
 * A reusable workout plan — a named list of exercises without any set data.
 *
 * WorkoutTemplate inherits all exercise management from AbstractWorkoutList.
 * Templates can be used as the basis for creating WorkoutSessions.
 */
@Entity
@Table(name = "workout_templates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WorkoutTemplate extends WorkoutList {

    // listName and notes from AbstractWorkoutList cover workoutName / workoutDescription.
    // Add template-specific fields here as the domain grows, e.g.:

    /** Optional difficulty rating for the template (1–5). */
    @Column(name = "difficulty_rating")
    private Integer difficultyRating;

    /** Estimated duration in minutes for planning purposes. */
    @Column(name = "estimated_duration_minutes")
    private Integer estimatedDurationMinutes;
}

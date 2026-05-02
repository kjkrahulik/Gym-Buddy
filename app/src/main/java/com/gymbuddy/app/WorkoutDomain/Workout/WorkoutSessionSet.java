package com.gymbuddy.app.WorkoutDomain.Workout;

import java.time.LocalTime;

/**
 * Defines set-level operations for a workout session.
 * Implemented by WorkoutSession (and any future session variants).
 *
 * Each method targets a specific exercise slot by its order-key
 * (the integer used as the key in AbstractWorkoutList#exercises).
 */
public interface WorkoutSessionSet {

    /**
     * Records a new set for the given exercise.
     *
     * @param exerciseKey the order-key of the target exercise
     * @param weight      weight used (kg or lb — caller's convention); 0 for bodyweight
     * @param reps        repetitions performed; 0 for duration-only sets
     * @param time        wall-clock time the set was performed, or null if not tracked
     */
    void addSet(int exerciseKey, float weight, int reps, LocalTime time);

    /**
     * Removes a set from the given exercise by its position in that exercise's set list.
     *
     * @param exerciseKey the order-key of the target exercise
     * @param order       0-based index of the set to remove within that exercise's list
     * @return true if the set was found and removed, false otherwise
     */
    boolean removeSet(int exerciseKey, int order);

    /**
     * Replaces the data of an existing set.
     *
     * @param exerciseKey the order-key of the target exercise
     * @param order       0-based index of the set to update within that exercise's list
     * @param weight      new weight value
     * @param reps        new rep count
     * @param time        new wall-clock time, or null to clear it
     * @return true if the set was found and updated, false otherwise
     */
    boolean updateSet(int exerciseKey, int order, float weight, int reps, LocalTime time);
}

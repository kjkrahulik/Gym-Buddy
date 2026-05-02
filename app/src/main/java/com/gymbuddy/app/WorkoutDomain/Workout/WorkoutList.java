package com.gymbuddy.app.WorkoutDomain.Workout;

import com.gymbuddy.app.WorkoutDomain.Exercise.Exercise;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashMap;

/**
 * Abstract base class for all workout list types (sessions, templates, etc.).
 * Uses @MappedSuperclass so JPA maps fields to each concrete subclass table
 * rather than creating a separate abstract table.
 */
@MappedSuperclass
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class WorkoutList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long listID;

    @Column(nullable = false)
    protected String listName;

    @Column(columnDefinition = "TEXT")
    protected String notes;

    /**
     * Tracks the number of exercises currently in this list.
     * Kept in sync by addExercise / deleteExercise.
     */
    @Column(nullable = false)
    protected int numExercises = 0;

    /**
     * Maps an integer order-key to each Exercise.
     * Stored as a separate join table per concrete subclass.
     * Key   = position/order index (0-based)
     * Value = the Exercise entity
     */
    @ManyToMany
    @MapKeyColumn(name = "exercise_order")
    protected HashMap<Integer, Exercise> exercises = new HashMap<>();

    // ------------------------------------------------------------------
    // Shared exercise management — available to all subclasses
    // ------------------------------------------------------------------

    /**
     * Appends an exercise to the end of the list.
     *
     * @param exercise the exercise to add
     */
    public void addExercise(Exercise exercise) {
        exercises.put(numExercises, exercise);
        numExercises++;
    }

    /**
     * Removes an exercise by its order key.
     *
     * @param orderKey the integer key used when the exercise was added
     * @return true if the exercise was present and removed, false otherwise
     */
    public boolean deleteExercise(int orderKey) {
        if (exercises.remove(orderKey) != null) {
            numExercises--;
            return true;
        }
        return false;
    }

    /**
     * Returns the Exercise at the given order key, or null if absent.
     */
    public Exercise getExercise(int orderKey) {
        return exercises.get(orderKey);
    }
}

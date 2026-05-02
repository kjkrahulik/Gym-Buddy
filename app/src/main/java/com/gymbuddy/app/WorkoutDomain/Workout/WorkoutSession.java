package com.gymbuddy.app.WorkoutDomain.Workout;

import com.gymbuddy.app.WorkoutDomain.Exercise.Set;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Represents a live or completed workout session.
 *
 * Inherits exercise management from AbstractWorkoutList and adds:
 *  - start/end timestamps
 *  - per-exercise set tracking via the WorkoutSessionSet interface
 *
 * 'sets' maps the same exercise order-key used in AbstractWorkoutList#exercises
 * to an ordered list of Set entities recorded during the session.
 */
@Entity
@Table(name = "workout_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WorkoutSession extends WorkoutList implements WorkoutSessionSet {

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    /**
     * Key   = exercise order-key (matches AbstractWorkoutList#exercises key)
     * Value = ordered list of sets performed for that exercise
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @MapKeyColumn(name = "exercise_order")
    private HashMap<Integer, List<Set>> sets = new HashMap<>();

    // ------------------------------------------------------------------
    // WorkoutSessionSet implementation
    // ------------------------------------------------------------------

    /**
     * {@inheritDoc}
     *
     * Creates a new WeightedSet and appends it to the exercise's set list.
     * If no list exists yet for this exercise key, one is created automatically.
     */
    @Override
    public void addSet(int exerciseKey, float weight, int reps, LocalTime time) {
        if (!exercises.containsKey(exerciseKey)) {
            throw new IllegalArgumentException(
                "No exercise found at order-key " + exerciseKey + ". Add the exercise first."
            );
        }

        com.gymbuddy.app.WorkoutDomain.Exercise.WeightedSet newSet =
            new com.gymbuddy.app.WorkoutDomain.Exercise.WeightedSet();
        newSet.setWeight(weight);
        newSet.setReps(reps);
        newSet.setTime(time);

        // Auto-assign the order index within this exercise's set list
        List<Set> exerciseSets = sets.computeIfAbsent(exerciseKey, k -> new ArrayList<>());
        newSet.setOrderIndex(exerciseSets.size());
        exerciseSets.add(newSet);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeSet(int exerciseKey, int order) {
        List<Set> exerciseSets = sets.get(exerciseKey);
        if (exerciseSets == null || order < 0 || order >= exerciseSets.size()) {
            return false;
        }
        exerciseSets.remove(order);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean updateSet(int exerciseKey, int order, float weight, int reps, LocalTime time) {
        List<Set> exerciseSets = sets.get(exerciseKey);
        if (exerciseSets == null || order < 0 || order >= exerciseSets.size()) {
            return false;
        }

        Set target = exerciseSets.get(order);

        // Update fields common to WeightedSet; extend here if TimedSet is also used
        if (target instanceof com.gymbuddy.app.WorkoutDomain.Exercise.WeightedSet ws) {
            ws.setWeight(weight);
            ws.setReps(reps);
            ws.setTime(time);
        }
        return true;
    }
}

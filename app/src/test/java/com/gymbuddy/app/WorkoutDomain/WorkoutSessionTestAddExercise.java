package com.gymbuddy.app.WorkoutDomain;

import com.gymbuddy.app.WorkoutDomain.Workout.WorkoutSession;
import com.gymbuddy.app.WorkoutDomain.Exercise.Exercise;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Basis-path tests for Abstract Class WorkoutList.
 * Contains 4 paths.
 * Path 1: Add a single exercise to an empty session.
 * Path 2: Add multiple exercises sequentially.
 * Path 3: Retrieval post-condition via getExercise(key).
 * Path 4: Null exercise input (documents current behaviour of no null guard).
 */

class WorkoutSessionTestAddExercise {

    private WorkoutSession session;
    private Exercise chestPress;
    private Exercise squat;

    @BeforeEach
    void setUp() {
        session = new WorkoutSession();

        chestPress = new Exercise();
        chestPress.setExerciseID(1L);
        chestPress.setExerciseName("Chest Press");
        chestPress.setMuscleGroup("Chest");
        chestPress.setExerciseDescription("Barbell bench press");

        squat = new Exercise();
        squat.setExerciseID(2L);
        squat.setExerciseName("Squat");
        squat.setMuscleGroup("Legs");
        squat.setExerciseDescription("Back squat");
    }

    // BP-1: Add a single exercise to an empty session
    @Test
    @DisplayName("BP-1: Adding one exercise to an empty session stores it at key 0 and increments numExercises")
    void addExercise_toEmptySession_storesAtKeyZeroAndIncrementsCount() {
        session.addExercise(chestPress);

        // Assert
        assertEquals(1, session.getNumExercises(),
                "numExercises should be 1 after adding one exercise");
        assertSame(chestPress, session.getExercises().get(0),
                "Exercise should be stored under order-key 0");
    }

    // BP-2: Add multiple exercises sequentially
    @Test
    @DisplayName("BP-2: Adding multiple exercises assigns sequential keys and maintains correct count")
    void addExercise_multipleExercises_assignsSequentialKeysAndUpdatesCount() {

        session.addExercise(chestPress);
        session.addExercise(squat);

        assertEquals(2, session.getNumExercises(),
                "numExercises should be 2 after adding two exercises");

        // Assert – key assignment
        assertSame(chestPress, session.getExercises().get(0),
                "First exercise should be at key 0");
        assertSame(squat, session.getExercises().get(1),
                "Second exercise should be at key 1");
    }

    // BP-3: Retrieval post-condition via getExercise(key)
    @Test
    @DisplayName("BP-3: getExercise returns the exact object that was added")
    void addExercise_thenGetExercise_returnsSameReference() {
        session.addExercise(chestPress);

        // Assert
        Exercise retrieved = session.getExercise(0);
        assertSame(chestPress, retrieved,
                "getExercise(0) should return the exact Exercise reference that was added");
    }

    // BP-4: Null exercise input
    @Test
    @DisplayName("BP-4: Adding a null exercise stores null at key 0 (no null guard — documents current behaviour)")
    void addExercise_nullExercise_storesNullWithoutException() {
        // Act — no exception expected under current implementation
        session.addExercise(null);

        // Assert — count still increments
        assertEquals(1, session.getNumExercises(),
                "numExercises should increment even when null is added");

        // Assert — null is stored (documents current behaviour)
        assertNull(session.getExercise(0),
                "getExercise(0) should return null when null was added");
    }
}
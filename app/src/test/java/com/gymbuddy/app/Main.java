package com.gymbuddy.app;

import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;
import java.util.Scanner;

import com.gymbuddy.app.AccountDomain.Goal;
import com.gymbuddy.app.AccountDomain.GoalType;

/**
 * Test my code and methods
 */
public class Main {
    public static void main(String[] args) {
         Scanner scanner = new Scanner(System.in);

        // Workout days input
        System.out.println("Enter workout days (e.g. Monday,Wednesday,Friday):");
        String dayInput = scanner.nextLine();

        Set<DayOfWeek> workoutDays = new HashSet<>();
        for (String day : dayInput.split(",")) {
            workoutDays.add(
                    DayOfWeek.valueOf(day.trim().toUpperCase())
            );
        }

        // Goal input
        System.out.println("Enter goal (BULKING, CUTTING, WEIGHT_LOSS, MAINTENANCE, MUSCLE_GROWTH, BEGINNER):");
        GoalType goalType = GoalType.valueOf(scanner.nextLine().toUpperCase());

        // Duration input
        System.out.println("Is workout under 45 minutes? (true/false):");
        boolean timeOfWorkout = scanner.nextBoolean();

        // Create Goal object
        Goal goal = new Goal(goalType, workoutDays, timeOfWorkout);

        // Test object output
        System.out.println(goal);
    }
    }


package com.gymbuddy.app;

import com.gymbuddy.app.WorkoutDomain.Exercise.Exercise;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.gymbuddy.app.Repositories.ExerciseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ExerciseLoader implements CommandLineRunner {
    @Autowired
    private ExerciseRepository exerciseRepository;


    public void run(String... args) throws Exception {
        System.out.println("ExerciseLoader: Checking if exercises need to be loaded...");
        long count = exerciseRepository.count();
        System.out.println("ExerciseLoader: Current exercise count in DB: " + count);

        if (count == 0) {
            System.out.println("ExerciseLoader: Loading exercises from JSON file...");
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = getClass().getClassLoader().getResourceAsStream("static/jsons/exercises.json");
            if (is == null) {
                System.err.println("ExerciseLoader: ERROR - Could not find exercises.json file");
                throw new RuntimeException("Could not find exercises.json file");
            }
            System.out.println("ExerciseLoader: Successfully opened exercises.json file");
            List<Exercise> exercises =
                Arrays.asList(mapper.readValue(is, Exercise[].class));
            System.out.println("ExerciseLoader: Parsed " + exercises.size() + " exercises from JSON");

            // Save all exercises
            exerciseRepository.saveAll(exercises);
            System.out.println("ExerciseLoader: Successfully saved " + exercises.size() + " exercises to database");
        } else {
            System.out.println("ExerciseLoader: Database already has exercises, skipping load");
        }
    }
}



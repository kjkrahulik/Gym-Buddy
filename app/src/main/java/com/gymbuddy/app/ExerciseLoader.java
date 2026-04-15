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
        if (exerciseRepository.count() == 0) {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = getClass().getResourceAsStream("/static/jsons/exercises.json");
            if (is == null) {
                throw new RuntimeException("Could not find exercises.json file");
            }
            List<Exercise> exercises =
                Arrays.asList(mapper.readValue(is, Exercise[].class));
            exerciseRepository.saveAll(exercises);
        }
    }
}



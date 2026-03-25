package com.gymbuddy.app.Service;

import com.gymbuddy.app.WorkoutDomain.Exercise.Exercise;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gymbuddy.app.Repositories.ExerciseRepository;

@Service
public class ExerciseService {
    
    @Autowired
    private ExerciseRepository exerciseRepository;

    public void createExercise(Exercise exercise) {
        exerciseRepository.save(exercise);
    }

    public List<Exercise> getExercises() {
        return exerciseRepository.findAll();
    }

}

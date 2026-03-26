package com.gymbuddy.app.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gymbuddy.app.Service.ExerciseService;
import com.gymbuddy.app.WorkoutDomain.Exercise.Exercise;
import java.util.List;


@RestController
public class ExerciseController {
    
    @Autowired
    private ExerciseService exerciseService;

    @GetMapping("/exercises")
    public List<Exercise> getExercises() {
        return exerciseService.getExercises();
    }

    @GetMapping("/exercise/{name}")
    public Exercise getExercise(@PathVariable String name) {
        return exerciseService.getExerciseByName(name);
    }

    @PostMapping("/exercise")
    public ResponseEntity<String> createExercise(@RequestBody Exercise exercise) {
        exerciseService.createExercise(exercise);
        return ResponseEntity.status(HttpStatus.CREATED).body("Exercise created successfully");
    }
    
    


}

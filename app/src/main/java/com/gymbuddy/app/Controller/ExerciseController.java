package com.gymbuddy.app.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.gymbuddy.app.Service.ExerciseService;
import com.gymbuddy.app.WorkoutDomain.Exercise.Exercise;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@Controller
public class ExerciseController {
    
    @Autowired
    private ExerciseService exerciseService;

    @GetMapping("/exercises")
    public List<Exercise> getExercises() {
        return exerciseService.getExercises();
    }

    @PostMapping("/exercise")
    public String createExercise(@RequestBody Exercise exercise) {
        exerciseService.createExercise(exercise);
        return "Exercise created successfully";
    }
    
    


}

package com.gymbuddy.app.Service;

import com.gymbuddy.app.WorkoutDomain.Workout.WorkoutTemplate;
import com.gymbuddy.app.WorkoutDomain.Exercise.Exercise;
import com.gymbuddy.app.WorkoutDomain.Exercise.WorkoutExercise;
import com.gymbuddy.app.Repositories.WorkoutTemplateRepository;
import com.gymbuddy.app.Repositories.ExerciseRepository;
import com.gymbuddy.app.Repositories.WorkoutExerciseRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WorkoutTemplateService {

    @Autowired
    private WorkoutTemplateRepository workoutTemplateRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private WorkoutExerciseRepository workoutExerciseRepository;

    public WorkoutTemplate createWorkoutTemplate(String workoutName, String workoutDescription) {
        // Check if template with same name already exists
        Optional<WorkoutTemplate> existingTemplate = workoutTemplateRepository.findByWorkoutName(workoutName);
        if (existingTemplate.isPresent()) {
            throw new IllegalArgumentException("Workout template with name '" + workoutName + "' already exists");
        }

        WorkoutTemplate template = new WorkoutTemplate(workoutName, workoutDescription);
        return workoutTemplateRepository.save(template);
    }

    public WorkoutTemplate addExerciseToTemplate(Long templateId, Long exerciseId, int listOrder) {
        WorkoutTemplate template = workoutTemplateRepository.findById(templateId)
                .orElseThrow(() -> new IllegalArgumentException("Workout template not found with ID: " + templateId));

        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new IllegalArgumentException("Exercise not found with ID: " + exerciseId));

        // Create and save WorkoutExercise first
        WorkoutExercise workoutExercise = new WorkoutExercise();
        workoutExercise.setExercise(exercise);
        workoutExercise.setListOrder(listOrder);
        workoutExercise.setWorkoutTemplate(template);

        // Save the WorkoutExercise first
        WorkoutExercise savedWorkoutExercise = workoutExerciseRepository.save(workoutExercise);

        // Add to template's list and update count
        template.getExercises().add(savedWorkoutExercise);
        template.setExerciseCount(template.getExercises().size());

        // Save the template
        return workoutTemplateRepository.save(template);
    }

    public WorkoutTemplate getWorkoutTemplateById(Long id) {
        return workoutTemplateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Workout template not found with ID: " + id));
    }

    public List<WorkoutTemplate> getAllWorkoutTemplates() {
        return workoutTemplateRepository.findAll();
    }

    public List<WorkoutTemplate> searchWorkoutTemplates(String keyword) {
        return workoutTemplateRepository.findByWorkoutNameContainingIgnoreCase(keyword);
    }

    public WorkoutTemplate updateWorkoutTemplate(Long id, String workoutName, String workoutDescription) {
        WorkoutTemplate template = getWorkoutTemplateById(id);
        template.setWorkoutName(workoutName);
        template.setWorkoutDescription(workoutDescription);
        return workoutTemplateRepository.save(template);
    }

    public void deleteWorkoutTemplate(Long id) {
        WorkoutTemplate template = getWorkoutTemplateById(id);

        // Delete all related WorkoutExercise records first
        // since we removed cascade from the relationship
        if (template.getExercises() != null && !template.getExercises().isEmpty()) {
            workoutExerciseRepository.deleteAll(template.getExercises());
        }

        // Now delete the template
        workoutTemplateRepository.delete(template);
    }
}
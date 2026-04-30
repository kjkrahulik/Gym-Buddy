package com.gymbuddy.app.Controller;

import com.gymbuddy.app.WorkoutDomain.Workout.WorkoutTemplate;
import com.gymbuddy.app.Service.WorkoutTemplateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/workout-templates")
public class WorkoutTemplateController {

    @Autowired
    private WorkoutTemplateService workoutTemplateService;

    @PostMapping
    public ResponseEntity<?> createWorkoutTemplate(@RequestBody Map<String, String> request) {
        try {
            String workoutName = request.get("workoutName");
            String workoutDescription = request.get("workoutDescription");

            if (workoutName == null || workoutName.trim().isEmpty()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Workout name is required");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            WorkoutTemplate template = workoutTemplateService.createWorkoutTemplate(workoutName, workoutDescription);
            return ResponseEntity.status(HttpStatus.CREATED).body(template);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error creating workout template: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/{templateId}/exercises")
    public ResponseEntity<?> addExerciseToTemplate(
            @PathVariable Long templateId,
            @RequestBody Map<String, Object> request) {
        try {
            Long exerciseId = Long.parseLong(request.get("exerciseId").toString());
            int listOrder = request.get("listOrder") != null ? Integer.parseInt(request.get("listOrder").toString()) : 0;

            WorkoutTemplate template = workoutTemplateService.addExerciseToTemplate(templateId, exerciseId, listOrder);
            return ResponseEntity.ok(template);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error adding exercise to template: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping
    public ResponseEntity<List<WorkoutTemplate>> getAllWorkoutTemplates() {
        List<WorkoutTemplate> templates = workoutTemplateService.getAllWorkoutTemplates();
        return ResponseEntity.ok(templates);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getWorkoutTemplateById(@PathVariable Long id) {
        try {
            WorkoutTemplate template = workoutTemplateService.getWorkoutTemplateById(id);
            return ResponseEntity.ok(template);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<WorkoutTemplate>> searchWorkoutTemplates(@RequestParam String keyword) {
        List<WorkoutTemplate> templates = workoutTemplateService.searchWorkoutTemplates(keyword);
        return ResponseEntity.ok(templates);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateWorkoutTemplate(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        try {
            String workoutName = request.get("workoutName");
            String workoutDescription = request.get("workoutDescription");

            WorkoutTemplate template = workoutTemplateService.updateWorkoutTemplate(id, workoutName, workoutDescription);
            return ResponseEntity.ok(template);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error updating workout template: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteWorkoutTemplate(@PathVariable Long id) {
        try {
            workoutTemplateService.deleteWorkoutTemplate(id);
            Map<String, String> successResponse = new HashMap<>();
            successResponse.put("message", "Workout template deleted successfully");
            return ResponseEntity.ok(successResponse);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Error deleting workout template: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
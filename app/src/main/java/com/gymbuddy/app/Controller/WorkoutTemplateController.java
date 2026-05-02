package com.gymbuddy.app.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.gymbuddy.app.AccountDomain.Goal;
import com.gymbuddy.app.Service.WorkoutTemplateService;
import com.gymbuddy.app.WorkoutDomain.Workout.WorkoutTemplate;

@RestController
@RequestMapping("/api/workout-templates")
public class WorkoutTemplateController {

    @Autowired
    private WorkoutTemplateService workoutTemplateService;

    // ─── Generate Workout ───────────────────────────────────────────────

    @PostMapping("/generate")
    public ResponseEntity<?> generateWorkout(@RequestBody Map<String, Object> request) {
        try {
            String goalTypeStr  = (String) request.getOrDefault("goalType", "BEGINNER");
            int workoutDays     = Integer.parseInt(request.getOrDefault("workoutDays", 3).toString());
            boolean timeCommit  = Boolean.parseBoolean(request.getOrDefault("timeCommitment", false).toString());

            Goal.GoalType goalType = Goal.GoalType.valueOf(goalTypeStr.toUpperCase());
            Goal goal = new Goal(goalType, timeCommit, workoutDays);

            List<WorkoutTemplate> saved = workoutTemplateService.generateAndSave(goal);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Workout generated successfully");
            response.put("templatesCreated", saved.size());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to generate workout: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // ─── Create Template ───────────────────────────────────────────────

    @PostMapping
    public ResponseEntity<?> createWorkoutTemplate(@RequestBody Map<String, String> request) {
        try {
            String listName = request.get("listName");
            String notes = request.get("notes");

            if (listName == null || listName.trim().isEmpty()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Workout name is required");
                return ResponseEntity.badRequest().body(errorResponse);
            }

            WorkoutTemplate template = workoutTemplateService
                    .createWorkoutTemplate(listName, notes);

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

    // ─── Add Exercise ──────────────────────────────────────────────────

    @PostMapping("/{templateId}/exercises")
    public ResponseEntity<?> addExerciseToTemplate(
            @PathVariable Long templateId,
            @RequestBody Map<String, Object> request) {

        try {
            Long exerciseId = Long.parseLong(request.get("exerciseId").toString());
            int listOrder = request.get("listOrder") != null
                    ? Integer.parseInt(request.get("listOrder").toString())
                    : 0;

            WorkoutTemplate template = workoutTemplateService
                    .addExerciseToTemplate(templateId, exerciseId, listOrder);

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

    // ─── Read ──────────────────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<List<WorkoutTemplate>> getAllWorkoutTemplates() {
        return ResponseEntity.ok(workoutTemplateService.getAllWorkoutTemplates());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getWorkoutTemplateById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(workoutTemplateService.getWorkoutTemplateById(id));
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<WorkoutTemplate>> searchWorkoutTemplates(@RequestParam String keyword) {
        return ResponseEntity.ok(workoutTemplateService.searchWorkoutTemplates(keyword));
    }

    // ─── Update ────────────────────────────────────────────────────────

    @PutMapping("/{id}")
    public ResponseEntity<?> updateWorkoutTemplate(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {

        try {
            String listName = request.get("listName");
            String notes = request.get("notes");

            WorkoutTemplate template = workoutTemplateService
                    .updateWorkoutTemplate(id, listName, notes);

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

    // ─── Delete ────────────────────────────────────────────────────────

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
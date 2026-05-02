package com.gymbuddy.app.Controller;

import com.gymbuddy.app.AccountDomain.Goal;
import com.gymbuddy.app.AccountDomain.StoredGoal;
import com.gymbuddy.app.Service.GoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/goals")
public class GoalController {

    @Autowired
    private GoalService goalService;

    @PostMapping
    public ResponseEntity<?> saveGoal(Principal principal, @RequestBody Map<String, Object> body) {
        try {
            String goalTypeStr = (String) body.getOrDefault("goalType", "BEGINNER");
            int workoutDays = Integer.parseInt(body.getOrDefault("workoutDays", 3).toString());
            boolean timeCommitment = Boolean.parseBoolean(body.getOrDefault("timeCommitment", false).toString());

            Goal.GoalType goalType = Goal.GoalType.valueOf(goalTypeStr.toUpperCase());
            StoredGoal saved = goalService.saveGoal(principal.getName(), goalType, workoutDays, timeCommitment);

            return ResponseEntity.ok(Map.of(
                "message", "Goals saved successfully",
                "goalType", saved.getGoalType().name(),
                "workoutDays", saved.getWorkoutDays(),
                "timeCommitment", saved.isTimeCommitment()
            ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to save goals: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getGoal(Principal principal) {
        Optional<StoredGoal> goal = goalService.getGoal(principal.getName());
        if (goal.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "No goals saved"));
        }
        StoredGoal g = goal.get();
        return ResponseEntity.ok(Map.of(
            "goalType", g.getGoalType().name(),
            "workoutDays", g.getWorkoutDays(),
            "timeCommitment", g.isTimeCommitment()
        ));
    }
}

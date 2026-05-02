package com.gymbuddy.app.Service;

import com.gymbuddy.app.Repositories.WorkoutSessionRepository;
import com.gymbuddy.app.WorkoutDomain.Exercise.Set;
import com.gymbuddy.app.WorkoutDomain.Exercise.WeightedSet;
import com.gymbuddy.app.WorkoutDomain.Exercise.Exercise;
import com.gymbuddy.app.WorkoutDomain.Workout.WorkoutSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class LeaderboardService {

    @Autowired
    private WorkoutSessionRepository workoutSessionRepository;

    public record LeaderboardEntry(String username, float weight, int reps) {}

    public Map<String, List<LeaderboardEntry>> getLeaderboard() {
        List<WorkoutSession> allSessions = workoutSessionRepository.findAll();

        // category -> username -> [bestWeight, repsAtBestWeight]
        Map<String, Map<String, float[]>> best = new LinkedHashMap<>();
        best.put("bench", new HashMap<>());
        best.put("squat", new HashMap<>());
        best.put("deadlift", new HashMap<>());

        for (WorkoutSession session : allSessions) {
            if (session.getAccount() == null) continue;
            String username = session.getAccount().getUsername();

            for (Set set : session.getSets()) {
                if (!(set instanceof WeightedSet ws)) continue;
                Exercise exercise = session.getExercises().get(set.getExerciseOrder());
                if (exercise == null) continue;

                String name = exercise.getExerciseName().toLowerCase();
                String category;
                if (name.contains("bench")) category = "bench";
                else if (name.contains("squat")) category = "squat";
                else if (name.contains("deadlift")) category = "deadlift";
                else continue;

                float[] current = best.get(category).get(username);
                if (current == null || ws.getWeight() > current[0]) {
                    best.get(category).put(username, new float[]{ws.getWeight(), ws.getReps()});
                }
            }
        }

        Map<String, List<LeaderboardEntry>> result = new LinkedHashMap<>();
        for (var entry : best.entrySet()) {
            List<LeaderboardEntry> entries = entry.getValue().entrySet().stream()
                .map(e -> new LeaderboardEntry(e.getKey(), e.getValue()[0], (int) e.getValue()[1]))
                .sorted(Comparator.comparingDouble((LeaderboardEntry e) -> e.weight()).reversed())
                .limit(10)
                .toList();
            result.put(entry.getKey(), entries);
        }
        return result;
    }
}

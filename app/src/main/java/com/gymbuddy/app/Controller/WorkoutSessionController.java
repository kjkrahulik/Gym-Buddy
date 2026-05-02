package com.gymbuddy.app.Controller;

import com.gymbuddy.app.AccountDomain.Account;
import com.gymbuddy.app.Repositories.ExerciseRepository;
import com.gymbuddy.app.Repositories.WorkoutSessionRepository;
import com.gymbuddy.app.Repositories.WorkoutTemplateRepository;
import com.gymbuddy.app.Service.AccountService;
import com.gymbuddy.app.WorkoutDomain.Exercise.Exercise;
import com.gymbuddy.app.WorkoutDomain.Exercise.TimedSet;
import com.gymbuddy.app.WorkoutDomain.Exercise.WeightedSet;
import com.gymbuddy.app.WorkoutDomain.Workout.WorkoutSession;
import com.gymbuddy.app.WorkoutDomain.Workout.WorkoutSessionDTO;
import com.gymbuddy.app.WorkoutDomain.Workout.WorkoutTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutSessionController {

    @Autowired
    private WorkoutSessionRepository workoutSessionRepository;

    @Autowired
    private WorkoutTemplateRepository workoutTemplateRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @GetMapping
    public ResponseEntity<List<WorkoutSessionDTO>> getUserWorkouts(Principal principal) {
        try {
            String username = principal.getName();
            Account account = accountService.searchAccount(username);

            if (account == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            List<WorkoutSession> sessions = workoutSessionRepository.findByAccountOrderBySessionDateDesc(account);

            List<WorkoutSessionDTO> dtos = sessions.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> saveWorkout(
            @RequestBody SaveWorkoutRequest request,
            Principal principal) {
        try {
            String username = principal.getName();
            Account account = accountService.searchAccount(username);

            if (account == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            WorkoutSession session = new WorkoutSession();
            String sessionName = request.getSessionName() != null ? request.getSessionName() : "Untitled Workout";
            session.setSessionName(sessionName);
            session.setListName(sessionName);
            session.setNotes(request.getNotes());
            session.setSessionDate(LocalDateTime.now());
            session.setAccount(account);

            // Add exercises and sets
            int exerciseOrder = 0;
            if (request.getExercises() != null) {
                for (SaveWorkoutRequest.ExerciseRequest exReq : request.getExercises()) {
                    // Fetch exercise from database
                    Exercise exercise = exerciseRepository.findById(exReq.getExerciseID()).orElse(null);
                    if (exercise != null) {
                        session.addExercise(exercise);

                        // Add sets for this exercise
                        if (exReq.getSets() != null) {
                            for (int setIdx = 0; setIdx < exReq.getSets().size(); setIdx++) {
                                SaveWorkoutRequest.SetRequest setReq = exReq.getSets().get(setIdx);

                                if ("WEIGHTED".equals(setReq.getType())) {
                                    WeightedSet ws = new WeightedSet();
                                    ws.setWeight(setReq.getWeight() != null ? setReq.getWeight() : 0);
                                    ws.setReps(setReq.getReps() != null ? setReq.getReps() : 0);
                                    ws.setExerciseOrder(exerciseOrder);
                                    ws.setOrderIndex(setIdx);
                                    session.getSets().add(ws);
                                } else if ("TIMED".equals(setReq.getType())) {
                                    TimedSet ts = new TimedSet();
                                    ts.setDurationInSeconds(setReq.getDuration() != null ? setReq.getDuration() : 0);
                                    ts.setExerciseOrder(exerciseOrder);
                                    ts.setOrderIndex(setIdx);
                                    session.getSets().add(ts);
                                }
                            }
                        }
                        exerciseOrder++;
                    }
                }
            }

            WorkoutSession saved = workoutSessionRepository.save(session);

            Map<String, Object> response = new HashMap<>();
            response.put("sessionID", saved.getListID());
            response.put("message", "Workout saved successfully");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to save workout: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @PostMapping("/from-template/{templateId}")
    public ResponseEntity<Map<String, Object>> createSessionFromTemplate(
            @PathVariable Long templateId,
            Principal principal) {
        try {
            String username = principal.getName();
            Account account = accountService.searchAccount(username);

            if (account == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            WorkoutTemplate template = workoutTemplateRepository.findById(templateId)
                    .orElseThrow(() -> new IllegalArgumentException("Template not found"));

            WorkoutSession session = new WorkoutSession();
            session.setSessionName(template.getListName());
            session.setListName(template.getListName());
            session.setNotes(template.getNotes());
            session.setSessionDate(LocalDateTime.now());
            session.setAccount(account);

            // Copy exercises from template (no sets)
            template.getExercises().forEach((order, exercise) -> session.addExercise(exercise));

            WorkoutSession saved = workoutSessionRepository.save(session);

            Map<String, Object> response = new HashMap<>();
            response.put("sessionID", saved.getListID());
            response.put("message", "Workout session created from template");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, Object> error = new HashMap<>();
            error.put("error", "Failed to create session from template: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<WorkoutSessionDTO> getWorkout(
            @PathVariable Long sessionId,
            Principal principal) {
        try {
            String username = principal.getName();
            Account account = accountService.searchAccount(username);

            if (account == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            WorkoutSession session = workoutSessionRepository.findById(sessionId).orElse(null);

            if (session == null || !session.getAccount().equals(account)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            WorkoutSessionDTO dto = convertToDTO(session);
            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private WorkoutSessionDTO convertToDTO(WorkoutSession session) {
        WorkoutSessionDTO dto = new WorkoutSessionDTO();
        dto.setSessionID(session.getListID());
        dto.setSessionName(session.getSessionName());
        dto.setSessionDate(session.getSessionDate());
        dto.setNotes(session.getNotes());
        dto.setExerciseCount(session.getNumExercises());

        // Calculate total sets and volume
        int totalSets = session.getSets().size();
        float totalVolume = 0;

        for (com.gymbuddy.app.WorkoutDomain.Exercise.Set set : session.getSets()) {
            if (set instanceof com.gymbuddy.app.WorkoutDomain.Exercise.WeightedSet) {
                com.gymbuddy.app.WorkoutDomain.Exercise.WeightedSet ws =
                        (com.gymbuddy.app.WorkoutDomain.Exercise.WeightedSet) set;
                totalVolume += ws.getWeight() * ws.getReps();
            }
        }

        dto.setTotalSets(totalSets);
        dto.setTotalVolume(totalVolume);

        // Build exercises list
        List<WorkoutSessionDTO.ExerciseDTO> exercises = session.getExercises().entrySet()
                .stream()
                .map(entry -> {
                    Exercise exercise = entry.getValue();
                    List<com.gymbuddy.app.WorkoutDomain.Exercise.Set> exerciseSets =
                            session.getSetsByExercise(entry.getKey());
                    return new WorkoutSessionDTO.ExerciseDTO(exercise, exerciseSets);
                })
                .collect(Collectors.toList());

        dto.setExercises(exercises);

        return dto;
    }

    // Request DTOs
    public static class SaveWorkoutRequest {
        private String sessionName;
        private String notes;
        private List<ExerciseRequest> exercises;

        public String getSessionName() {
            return sessionName;
        }

        public void setSessionName(String sessionName) {
            this.sessionName = sessionName;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public List<ExerciseRequest> getExercises() {
            return exercises;
        }

        public void setExercises(List<ExerciseRequest> exercises) {
            this.exercises = exercises;
        }

        public static class ExerciseRequest {
            private Long exerciseID;
            private String exerciseName;
            private String muscleGroup;
            private List<SetRequest> sets;

            public Long getExerciseID() {
                return exerciseID;
            }

            public void setExerciseID(Long exerciseID) {
                this.exerciseID = exerciseID;
            }

            public String getExerciseName() {
                return exerciseName;
            }

            public void setExerciseName(String exerciseName) {
                this.exerciseName = exerciseName;
            }

            public String getMuscleGroup() {
                return muscleGroup;
            }

            public void setMuscleGroup(String muscleGroup) {
                this.muscleGroup = muscleGroup;
            }

            public List<SetRequest> getSets() {
                return sets;
            }

            public void setSets(List<SetRequest> sets) {
                this.sets = sets;
            }
        }

        public static class SetRequest {
            private String type;
            private Float weight;
            private Integer reps;
            private Integer duration;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public Float getWeight() {
                return weight;
            }

            public void setWeight(Float weight) {
                this.weight = weight;
            }

            public Integer getReps() {
                return reps;
            }

            public void setReps(Integer reps) {
                this.reps = reps;
            }

            public Integer getDuration() {
                return duration;
            }

            public void setDuration(Integer duration) {
                this.duration = duration;
            }
        }
    }
}

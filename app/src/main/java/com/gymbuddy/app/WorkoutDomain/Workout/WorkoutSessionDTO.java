package com.gymbuddy.app.WorkoutDomain.Workout;

import com.gymbuddy.app.WorkoutDomain.Exercise.Exercise;
import com.gymbuddy.app.WorkoutDomain.Exercise.Set;
import com.gymbuddy.app.WorkoutDomain.Exercise.TimedSet;
import com.gymbuddy.app.WorkoutDomain.Exercise.WeightedSet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutSessionDTO {
    private Long sessionID;
    private String sessionName;
    private LocalDateTime sessionDate;
    private String notes;
    private Long duration; // in seconds
    private List<ExerciseDTO> exercises;
    private Integer exerciseCount;
    private Integer totalSets;
    private Float totalVolume;
    private List<String> invitedParticipants;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExerciseDTO {
        private Long exerciseID;
        private String exerciseName;
        private String muscleGroup;
        private List<SetDTO> sets;

        public ExerciseDTO(Exercise exercise, List<Set> exerciseSets) {
            this.exerciseID = exercise.getExerciseID();
            this.exerciseName = exercise.getExerciseName();
            this.muscleGroup = exercise.getMuscleGroup();
            this.sets = exerciseSets.stream()
                    .map(SetDTO::new)
                    .collect(Collectors.toList());
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SetDTO {
        private String type; // "WEIGHTED" or "TIMED"
        private Float weight; // for weighted sets
        private Integer reps; // for weighted sets
        private Integer duration; // for timed sets

        public SetDTO(Set set) {
            if (set instanceof WeightedSet) {
                WeightedSet ws = (WeightedSet) set;
                this.type = "WEIGHTED";
                this.weight = ws.getWeight();
                this.reps = ws.getReps();
            } else if (set instanceof TimedSet) {
                TimedSet ts = (TimedSet) set;
                this.type = "TIMED";
                this.duration = ts.getDurationInSeconds();
            }
        }
    }
}

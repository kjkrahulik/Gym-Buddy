package com.gymbuddy.app.WorkoutDomain.Workout;

import java.util.ArrayList;
import java.util.List;

import com.gymbuddy.app.WorkoutDomain.Exercise.Exercise;
import com.gymbuddy.app.WorkoutDomain.Exercise.WorkoutExercise;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "workout_templates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workoutTemplateID;
    @Column(name = "workout_name")
    private String workoutName;

    @Column(name = "workout_description")
    private String workoutDescription;

    @Column(name = "exercise_count")
    private Integer exerciseCount = 0;

    @OneToMany(mappedBy = "workoutTemplate")
    private List<WorkoutExercise> exercises = new ArrayList<>();

    public WorkoutTemplate(String workoutName, String workoutDescription) {
        this.workoutName = workoutName;
        this.workoutDescription = workoutDescription;
        this.exerciseCount = 0;
    }

}
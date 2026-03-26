package com.gymbuddy.app.WorkoutDomain.Exercise;

import com.gymbuddy.app.WorkoutDomain.Workout.WorkoutSession;
import com.gymbuddy.app.WorkoutDomain.Workout.WorkoutTemplate;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "workout_exercises")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutExercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long workoutExerciseID;
    @ManyToOne
    @JoinColumn(name = "exercise_id")
    private Exercise exercise;
    private int listOrder;
    @ManyToOne
    @JoinColumn(name = "workout_session_id")
    private WorkoutSession workoutSession;
    
    @ManyToOne
    @JoinColumn(name = "workout_template_id")
    private WorkoutTemplate workoutTemplate;
    
}

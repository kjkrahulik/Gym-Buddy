package com.gymbuddy.app.Repositories;

import com.gymbuddy.app.WorkoutDomain.Exercise.WorkoutExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, Long> {
}
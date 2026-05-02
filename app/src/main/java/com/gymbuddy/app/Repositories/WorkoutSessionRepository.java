package com.gymbuddy.app.Repositories;

import com.gymbuddy.app.WorkoutDomain.Workout.WorkoutSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutSessionRepository extends JpaRepository<WorkoutSession, Long> {
}

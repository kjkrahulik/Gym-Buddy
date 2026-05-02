package com.gymbuddy.app.Repositories;

import com.gymbuddy.app.WorkoutDomain.Workout.WorkoutTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutTemplateRepository extends JpaRepository<WorkoutTemplate, Long> {
    Optional<WorkoutTemplate> findByListName(String listName);
    List<WorkoutTemplate> findByListNameContainingIgnoreCase(String keyword);
}
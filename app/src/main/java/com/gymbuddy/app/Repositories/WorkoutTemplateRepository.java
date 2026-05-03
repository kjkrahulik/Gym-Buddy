package com.gymbuddy.app.Repositories;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gymbuddy.app.AccountDomain.Account;
import com.gymbuddy.app.WorkoutDomain.Workout.WorkoutTemplate;

@Repository
public interface WorkoutTemplateRepository extends JpaRepository<WorkoutTemplate, Long> {
    Optional<WorkoutTemplate> findByListNameAndAccount(String listName, Account account);
    List<WorkoutTemplate> findByAccount(Account account);
    List<WorkoutTemplate> findByListNameContainingIgnoreCaseAndAccount(String keyword, Account account);
}
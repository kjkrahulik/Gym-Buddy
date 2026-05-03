package com.gymbuddy.app.Repositories;

import com.gymbuddy.app.AccountDomain.Account;
import com.gymbuddy.app.WorkoutDomain.Workout.WorkoutSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutSessionRepository extends JpaRepository<WorkoutSession, Long> {
    List<WorkoutSession> findByAccountOrderBySessionDateDesc(Account account);
    List<WorkoutSession> findByAccount(Account account);
    long countByAccount(Account account);
}

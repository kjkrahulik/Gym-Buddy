package com.gymbuddy.app.Repositories;

import com.gymbuddy.app.AccountDomain.Account;
import com.gymbuddy.app.AccountDomain.StoredGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoredGoalRepository extends JpaRepository<StoredGoal, Long> {
    Optional<StoredGoal> findByAccount(Account account);
}

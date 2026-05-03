package com.gymbuddy.app.Service;

import com.gymbuddy.app.AccountDomain.Account;
import com.gymbuddy.app.AccountDomain.Goal;
import com.gymbuddy.app.AccountDomain.StoredGoal;
import com.gymbuddy.app.Repositories.StoredGoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class GoalService {

    @Autowired
    private StoredGoalRepository storedGoalRepository;

    @Autowired
    private AccountService accountService;

    @Transactional
    public StoredGoal saveGoal(String username, Goal.GoalType goalType, int workoutDays, boolean timeCommitment) {
        Account account = accountService.searchAccount(username);
        StoredGoal goal = storedGoalRepository.findByAccount(account)
                .orElse(new StoredGoal());
        goal.setGoalType(goalType);
        goal.setWorkoutDays(workoutDays);
        goal.setTimeCommitment(timeCommitment);
        goal.setAccount(account);
        return storedGoalRepository.save(goal);
    }

    public Optional<StoredGoal> getGoal(String username) {
        Account account = accountService.searchAccount(username);
        return storedGoalRepository.findByAccount(account);
    }
}

package com.gymbuddy.app.AccountDomain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stored_goals")
@Data
@NoArgsConstructor
public class StoredGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Goal.GoalType goalType;

    @Column(nullable = false)
    private int workoutDays;

    @Column(nullable = false)
    private boolean timeCommitment;

    @OneToOne
    @JoinColumn(name = "account_id", unique = true)
    @JsonIgnore
    private Account account;

    public StoredGoal(Goal.GoalType goalType, int workoutDays, boolean timeCommitment, Account account) {
        this.goalType = goalType;
        this.workoutDays = workoutDays;
        this.timeCommitment = timeCommitment;
        this.account = account;
    }
}

package com.gymbuddy.app.WorkoutDomain.Workout;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gymbuddy.app.AccountDomain.Account;
import com.gymbuddy.app.WorkoutDomain.Exercise.Set;
import com.gymbuddy.app.WorkoutDomain.Exercise.WeightedSet;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "workout_sessions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WorkoutSession extends WorkoutList implements WorkoutSessionSet {

    @Column(name = "session_name")
    private String sessionName;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "session_date")
    private LocalDateTime sessionDate;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonIgnore
    private Account account;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status = Status.LIVE;

    public enum Status {
        LIVE,
        FINISHED,
        DISCARDED
    }

    @ManyToMany
    @JoinTable(
        name = "workout_session_participants",
        joinColumns = @JoinColumn(name = "workout_session_id"),
        inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    @JsonIgnore
    private List<Account> invitedParticipants = new ArrayList<>();

    /**
     * Flat list of all sets in this session.
     * Each Set carries its own exerciseOrder so you always know
     * which exercise it belongs to.
     * Cascade = sets are persisted/deleted automatically with the session.
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "workout_session_id")
    @OrderBy("exerciseOrder ASC, orderIndex ASC")
    private List<Set> sets = new ArrayList<>();
 
    // ------------------------------------------------------------------
    // Convenience — get all sets for one exercise as an ordered list
    // ------------------------------------------------------------------
 
    public List<Set> getSetsByExercise(int exerciseKey) {
        return sets.stream()
            .filter(s -> s.getExerciseOrder() == exerciseKey)
            .collect(Collectors.toList());
    }
 
    // ------------------------------------------------------------------
    // WorkoutSessionSet implementation
    // ------------------------------------------------------------------
 
    @Override
    public void addSet(int exerciseKey, float weight, int reps, LocalTime time) {
        if (!exercises.containsKey(exerciseKey)) {
            throw new IllegalArgumentException(
                "No exercise at order-key " + exerciseKey + ". Add the exercise first."
            );
        }
 
        WeightedSet newSet = new WeightedSet();
        newSet.setWeight(weight);
        newSet.setReps(reps);
        newSet.setTime(time);
        newSet.setExerciseOrder(exerciseKey);
 
        // Order index = how many sets already exist for this exercise
        long existingCount = sets.stream()
            .filter(s -> s.getExerciseOrder() == exerciseKey)
            .count();
        newSet.setOrderIndex((int) existingCount);
 
        sets.add(newSet);
    }
 
    @Override
    public boolean removeSet(int exerciseKey, int order) {
        List<Set> exerciseSets = getSetsByExercise(exerciseKey);
        if (order < 0 || order >= exerciseSets.size()) return false;
        return sets.remove(exerciseSets.get(order));
    }
 
    @Override
    public boolean updateSet(int exerciseKey, int order, float weight, int reps, LocalTime time) {
        List<Set> exerciseSets = getSetsByExercise(exerciseKey);
        if (order < 0 || order >= exerciseSets.size()) return false;

        Set target = exerciseSets.get(order);
        if (target instanceof WeightedSet ws) {
            ws.setWeight(weight);
            ws.setReps(reps);
            ws.setTime(time);
            return true;
        }
        return false;
    }

    public List<Account> getInvitedParticipants() {
        return invitedParticipants;
    }

    public void addParticipant(Account account) {
        if (account != null && !invitedParticipants.contains(account)) {
            invitedParticipants.add(account);
        }
    }

    public void removeParticipant(Account account) {
        if (account != null) {
            invitedParticipants.remove(account);
        }
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
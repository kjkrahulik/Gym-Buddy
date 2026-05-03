package com.gymbuddy.app.WorkoutDomain.Workout;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gymbuddy.app.AccountDomain.Account;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "workout_templates")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WorkoutTemplate extends WorkoutList {
    
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    @JsonIgnore
    private Account account;

    public WorkoutTemplate(String listName, String notes) {
        super();
        this.listName = listName;
        this.notes = notes;
        this.numExercises = 0;
    }

}
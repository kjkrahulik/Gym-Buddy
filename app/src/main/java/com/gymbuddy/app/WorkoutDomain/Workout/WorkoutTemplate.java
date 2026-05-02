package com.gymbuddy.app.WorkoutDomain.Workout;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "workout_templates")
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class WorkoutTemplate extends WorkoutList {

    public WorkoutTemplate(String listName, String notes) {
        super();
        this.listName = listName;
        this.notes = notes;
        this.numExercises = 0;
    }

}
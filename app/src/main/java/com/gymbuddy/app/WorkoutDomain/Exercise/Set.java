package com.gymbuddy.app.WorkoutDomain.Exercise;
 
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;
 
@Entity
@Table(name = "workout_sets")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Set {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long setID;
 
    /** 0-based position within this exercise's set list. */
    @Column(name = "order_index", nullable = false)
    private int orderIndex;
 
    /** Wall-clock time the set was performed. Null if not tracked. */
    @Column(name = "performed_at")
    private LocalTime time;
 
    @Column(name = "exercise_order", nullable = false)
    private int exerciseOrder;
}
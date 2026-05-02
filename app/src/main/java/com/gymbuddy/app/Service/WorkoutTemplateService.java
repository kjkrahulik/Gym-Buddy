package com.gymbuddy.app.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gymbuddy.app.AccountDomain.Goal;
import com.gymbuddy.app.Repositories.ExerciseRepository;
import com.gymbuddy.app.Repositories.WorkoutExerciseRepository;
import com.gymbuddy.app.Repositories.WorkoutTemplateRepository;
import com.gymbuddy.app.WorkoutDomain.Exercise.Exercise;
import com.gymbuddy.app.WorkoutDomain.Exercise.WorkoutExercise;
import com.gymbuddy.app.WorkoutDomain.Workout.WorkoutTemplate;

@Service
public class WorkoutTemplateService {

    @Autowired
    private WorkoutTemplateRepository workoutTemplateRepository;

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private WorkoutExerciseRepository workoutExerciseRepository;

    // ─── Template CRUD ────────────────────────────────────────────────────────

    public WorkoutTemplate createWorkoutTemplate(String workoutName, String workoutDescription) {
        Optional<WorkoutTemplate> existingTemplate = workoutTemplateRepository.findByWorkoutName(workoutName);
        if (existingTemplate.isPresent()) {
            throw new IllegalArgumentException("Workout template with name '" + workoutName + "' already exists");
        }
        WorkoutTemplate template = new WorkoutTemplate(workoutName, workoutDescription);
        return workoutTemplateRepository.save(template);
    }

    public WorkoutTemplate addExerciseToTemplate(Long templateId, Long exerciseId, int listOrder) {
        WorkoutTemplate template = workoutTemplateRepository.findById(templateId)
                .orElseThrow(() -> new IllegalArgumentException("Workout template not found with ID: " + templateId));

        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new IllegalArgumentException("Exercise not found with ID: " + exerciseId));

        WorkoutExercise workoutExercise = new WorkoutExercise();
        workoutExercise.setExercise(exercise);
        workoutExercise.setListOrder(listOrder);
        workoutExercise.setWorkoutTemplate(template);

        WorkoutExercise savedWorkoutExercise = workoutExerciseRepository.save(workoutExercise);

        template.getExercises().add(savedWorkoutExercise);
        template.setExerciseCount(template.getExercises().size());

        return workoutTemplateRepository.save(template);
    }

    public WorkoutTemplate getWorkoutTemplateById(Long id) {
        return workoutTemplateRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Workout template not found with ID: " + id));
    }

    public List<WorkoutTemplate> getAllWorkoutTemplates() {
        return workoutTemplateRepository.findAll();
    }

    public List<WorkoutTemplate> searchWorkoutTemplates(String keyword) {
        return workoutTemplateRepository.findByWorkoutNameContainingIgnoreCase(keyword);
    }

    public WorkoutTemplate updateWorkoutTemplate(Long id, String workoutName, String workoutDescription) {
        WorkoutTemplate template = getWorkoutTemplateById(id);
        template.setWorkoutName(workoutName);
        template.setWorkoutDescription(workoutDescription);
        return workoutTemplateRepository.save(template);
    }

    public void deleteWorkoutTemplate(Long id) {
        WorkoutTemplate template = getWorkoutTemplateById(id);
        if (template.getExercises() != null && !template.getExercises().isEmpty()) {
            workoutExerciseRepository.deleteAll(template.getExercises());
        }
        workoutTemplateRepository.delete(template);
    }

    // ─── Workout Generation ───────────────────────────────────────────────────

    public List<WorkoutTemplate> generateAndSave(Goal goal) {
        List<WorkoutTemplate> generated = generateSplit(goal);
        return saveGeneratedTemplates(generated);
    }

    public ArrayList<WorkoutTemplate> generateSplit(Goal goal) {
        int days           = goal.getWorkoutDays();
        Goal.GoalType type = goal.getGoalType();

        ArrayList<WorkoutTemplate> templates = new ArrayList<>();

        switch (days) {

            case 1: // FULL BODY
                switch (type) {
                    case BULKING: {
                        WorkoutTemplate t = newTemplate("Full Body – Bulking", "High volume compound lifts for muscle growth");
                        addExercise(t, "Barbell Squat",       "Legs",      1);
                        addExercise(t, "Barbell Bench Press", "Chest",     2);
                        addExercise(t, "Barbell Deadlift",    "Back",      3);
                        addExercise(t, "Overhead Press",      "Shoulders", 4);
                        addExercise(t, "Barbell Row",         "Back",      5);
                        templates.add(t); break;
                    }
                    case CUTTING: {
                        WorkoutTemplate t = newTemplate("Full Body – Cutting", "Moderate volume full body in a caloric deficit");
                        addExercise(t, "Goblet Squat",      "Legs",      1);
                        addExercise(t, "Dumbbell Press",    "Chest",     2);
                        addExercise(t, "Romanian Deadlift", "Legs",      3);
                        addExercise(t, "Dumbbell Row",      "Back",      4);
                        addExercise(t, "Lateral Raise",     "Shoulders", 5);
                        templates.add(t); break;
                    }
                    case WEIGHT_LOSS: {
                        WorkoutTemplate t = newTemplate("Full Body – Weight Loss", "Circuit-style full body for maximum calorie burn");
                        addExercise(t, "Bodyweight Squat", "Legs",  1);
                        addExercise(t, "Push-Up",          "Chest", 2);
                        addExercise(t, "Dumbbell Row",     "Back",  3);
                        addExercise(t, "Lunge",            "Legs",  4);
                        addExercise(t, "Plank",            "Core",  5);
                        templates.add(t); break;
                    }
                    case BEGINNER: default: {
                        WorkoutTemplate t = newTemplate("Full Body – Beginner", "Simple full body routine to build a strength baseline");
                        addExercise(t, "Goblet Squat",      "Legs",      1);
                        addExercise(t, "Push-Up",           "Chest",     2);
                        addExercise(t, "Dumbbell Row",      "Back",      3);
                        addExercise(t, "Overhead Press",    "Shoulders", 4);
                        addExercise(t, "Romanian Deadlift", "Legs",      5);
                        templates.add(t); break;
                    }
                }
                break;

            case 2: // UPPER / LOWER
                switch (type) {
                    case BULKING: {
                        WorkoutTemplate upper = newTemplate("Upper – Bulking", "Heavy upper body push and pull");
                        addExercise(upper, "Bench Press",            "Chest",     1);
                        addExercise(upper, "Incline Dumbbell Press", "Chest",     2);
                        addExercise(upper, "Barbell Row",            "Back",      3);
                        addExercise(upper, "Pull-Up",                "Back",      4);
                        addExercise(upper, "Overhead Press",         "Shoulders", 5);
                        addExercise(upper, "Tricep Pushdown",        "Triceps",   6);
                        WorkoutTemplate lower = newTemplate("Lower – Bulking", "Heavy squat and hinge focus");
                        addExercise(lower, "Squat",      "Legs", 1);
                        addExercise(lower, "RDL",        "Legs", 2);
                        addExercise(lower, "Leg Press",  "Legs", 3);
                        addExercise(lower, "Leg Curl",   "Legs", 4);
                        addExercise(lower, "Calf Raise", "Legs", 5);
                        templates.add(upper); templates.add(lower); break;
                    }
                    case CUTTING: {
                        WorkoutTemplate upper = newTemplate("Upper – Cutting", "Upper body with supersets for density");
                        addExercise(upper, "Dumbbell Bench Press", "Chest",     1);
                        addExercise(upper, "Cable Row",            "Back",      2);
                        addExercise(upper, "Overhead Press",       "Shoulders", 3);
                        addExercise(upper, "Lat Pulldown",         "Back",      4);
                        addExercise(upper, "Tricep Extension",     "Triceps",   5);
                        addExercise(upper, "Bicep Curl",           "Biceps",    6);
                        WorkoutTemplate lower = newTemplate("Lower – Cutting", "Lower body with cardio finisher");
                        addExercise(lower, "Goblet Squat",      "Legs", 1);
                        addExercise(lower, "Romanian Deadlift", "Legs", 2);
                        addExercise(lower, "Leg Press",         "Legs", 3);
                        addExercise(lower, "Leg Curl",          "Legs", 4);
                        addExercise(lower, "Calf Raise",        "Legs", 5);
                        templates.add(upper); templates.add(lower); break;
                    }
                    case WEIGHT_LOSS: {
                        WorkoutTemplate upper = newTemplate("Upper – Weight Loss", "Upper body circuit training");
                        addExercise(upper, "Push-Up",        "Chest",     1);
                        addExercise(upper, "Dumbbell Row",   "Back",      2);
                        addExercise(upper, "Overhead Press", "Shoulders", 3);
                        addExercise(upper, "Lat Pulldown",   "Back",      4);
                        addExercise(upper, "Tricep Dip",     "Triceps",   5);
                        addExercise(upper, "Hammer Curl",    "Biceps",    6);
                        WorkoutTemplate lower = newTemplate("Lower – Weight Loss", "Lower body circuit training");
                        addExercise(lower, "Bodyweight Squat", "Legs", 1);
                        addExercise(lower, "Lunge",            "Legs", 2);
                        addExercise(lower, "Glute Bridge",     "Legs", 3);
                        addExercise(lower, "Leg Curl",         "Legs", 4);
                        addExercise(lower, "Calf Raise",       "Legs", 5);
                        templates.add(upper); templates.add(lower); break;
                    }
                    case BEGINNER: default: {
                        WorkoutTemplate upper = newTemplate("Upper – Beginner", "Basic upper body push and pull");
                        addExercise(upper, "Dumbbell Bench Press",    "Chest",     1);
                        addExercise(upper, "Dumbbell Row",            "Back",      2);
                        addExercise(upper, "Dumbbell Shoulder Press", "Shoulders", 3);
                        addExercise(upper, "Lat Pulldown",            "Back",      4);
                        addExercise(upper, "Tricep Extension",        "Triceps",   5);
                        addExercise(upper, "Bicep Curl",              "Biceps",    6);
                        WorkoutTemplate lower = newTemplate("Lower – Beginner", "Basic squat, hinge, and carry");
                        addExercise(lower, "Goblet Squat",      "Legs", 1);
                        addExercise(lower, "Romanian Deadlift", "Legs", 2);
                        addExercise(lower, "Leg Press",         "Legs", 3);
                        addExercise(lower, "Leg Curl",          "Legs", 4);
                        addExercise(lower, "Calf Raise",        "Legs", 5);
                        templates.add(upper); templates.add(lower); break;
                    }
                }
                break;

            case 3: // PUSH / PULL / LEGS
                switch (type) {
                    case BULKING: {
                        WorkoutTemplate push = newTemplate("Push – Bulking", "Chest, shoulders, triceps – heavy");
                        addExercise(push, "Barbell Bench Press",    "Chest",     1);
                        addExercise(push, "Incline Dumbbell Press", "Chest",     2);
                        addExercise(push, "Overhead Press",         "Shoulders", 3);
                        addExercise(push, "Lateral Raise",          "Shoulders", 4);
                        addExercise(push, "Tricep Pushdown",        "Triceps",   5);
                        addExercise(push, "Skull Crusher",          "Triceps",   6);
                        WorkoutTemplate pull = newTemplate("Pull – Bulking", "Back and biceps – heavy");
                        addExercise(pull, "Barbell Row",  "Back",      1);
                        addExercise(pull, "Pull-Up",      "Back",      2);
                        addExercise(pull, "Lat Pulldown", "Back",      3);
                        addExercise(pull, "Face Pull",    "Shoulders", 4);
                        addExercise(pull, "Barbell Curl", "Biceps",    5);
                        addExercise(pull, "Hammer Curl",  "Biceps",    6);
                        WorkoutTemplate legs = newTemplate("Legs – Bulking", "Quads, hamstrings, calves – heavy");
                        addExercise(legs, "Barbell Squat",     "Legs", 1);
                        addExercise(legs, "Romanian Deadlift", "Legs", 2);
                        addExercise(legs, "Leg Press",         "Legs", 3);
                        addExercise(legs, "Leg Curl",          "Legs", 4);
                        addExercise(legs, "Leg Extension",     "Legs", 5);
                        addExercise(legs, "Calf Raise",        "Legs", 6);
                        templates.add(push); templates.add(pull); templates.add(legs); break;
                    }
                    case CUTTING: {
                        WorkoutTemplate push = newTemplate("Push – Cutting", "Chest, shoulders, triceps – higher reps");
                        addExercise(push, "Dumbbell Bench Press", "Chest",     1);
                        addExercise(push, "Cable Fly",            "Chest",     2);
                        addExercise(push, "Overhead Press",       "Shoulders", 3);
                        addExercise(push, "Lateral Raise",        "Shoulders", 4);
                        addExercise(push, "Tricep Extension",     "Triceps",   5);
                        addExercise(push, "Tricep Dip",           "Triceps",   6);
                        WorkoutTemplate pull = newTemplate("Pull – Cutting", "Back and biceps – higher reps");
                        addExercise(pull, "Dumbbell Row", "Back",      1);
                        addExercise(pull, "Lat Pulldown", "Back",      2);
                        addExercise(pull, "Cable Row",    "Back",      3);
                        addExercise(pull, "Face Pull",    "Shoulders", 4);
                        addExercise(pull, "Bicep Curl",   "Biceps",    5);
                        addExercise(pull, "Hammer Curl",  "Biceps",    6);
                        WorkoutTemplate legs = newTemplate("Legs – Cutting", "Quads, hamstrings, calves – higher reps");
                        addExercise(legs, "Goblet Squat",      "Legs", 1);
                        addExercise(legs, "Romanian Deadlift", "Legs", 2);
                        addExercise(legs, "Leg Press",         "Legs", 3);
                        addExercise(legs, "Leg Curl",          "Legs", 4);
                        addExercise(legs, "Leg Extension",     "Legs", 5);
                        addExercise(legs, "Calf Raise",        "Legs", 6);
                        templates.add(push); templates.add(pull); templates.add(legs); break;
                    }
                    case WEIGHT_LOSS: {
                        WorkoutTemplate push = newTemplate("Push – Weight Loss", "Push muscles with cardio bursts");
                        addExercise(push, "Push-Up",                 "Chest",     1);
                        addExercise(push, "Dumbbell Shoulder Press", "Shoulders", 2);
                        addExercise(push, "Lateral Raise",           "Shoulders", 3);
                        addExercise(push, "Tricep Dip",              "Triceps",   4);
                        addExercise(push, "Chest Fly",               "Chest",     5);
                        WorkoutTemplate pull = newTemplate("Pull – Weight Loss", "Pull muscles with cardio bursts");
                        addExercise(pull, "Dumbbell Row", "Back",      1);
                        addExercise(pull, "Lat Pulldown", "Back",      2);
                        addExercise(pull, "Bicep Curl",   "Biceps",    3);
                        addExercise(pull, "Face Pull",    "Shoulders", 4);
                        addExercise(pull, "Hammer Curl",  "Biceps",    5);
                        WorkoutTemplate legs = newTemplate("Legs – Weight Loss", "Leg circuit with cardio finisher");
                        addExercise(legs, "Bodyweight Squat", "Legs", 1);
                        addExercise(legs, "Lunge",            "Legs", 2);
                        addExercise(legs, "Glute Bridge",     "Legs", 3);
                        addExercise(legs, "Leg Curl",         "Legs", 4);
                        addExercise(legs, "Calf Raise",       "Legs", 5);
                        templates.add(push); templates.add(pull); templates.add(legs); break;
                    }
                    case BEGINNER: default: {
                        WorkoutTemplate push = newTemplate("Push – Beginner", "Chest, shoulders, triceps – light to moderate");
                        addExercise(push, "Push-Up",                 "Chest",     1);
                        addExercise(push, "Dumbbell Shoulder Press", "Shoulders", 2);
                        addExercise(push, "Lateral Raise",           "Shoulders", 3);
                        addExercise(push, "Tricep Extension",        "Triceps",   4);
                        addExercise(push, "Chest Fly",               "Chest",     5);
                        WorkoutTemplate pull = newTemplate("Pull – Beginner", "Back and biceps – light to moderate");
                        addExercise(pull, "Dumbbell Row", "Back",      1);
                        addExercise(pull, "Lat Pulldown", "Back",      2);
                        addExercise(pull, "Bicep Curl",   "Biceps",    3);
                        addExercise(pull, "Face Pull",    "Shoulders", 4);
                        addExercise(pull, "Hammer Curl",  "Biceps",    5);
                        WorkoutTemplate legs = newTemplate("Legs – Beginner", "Quads, hamstrings, glutes – light to moderate");
                        addExercise(legs, "Goblet Squat",      "Legs", 1);
                        addExercise(legs, "Romanian Deadlift", "Legs", 2);
                        addExercise(legs, "Lunge",             "Legs", 3);
                        addExercise(legs, "Leg Curl",          "Legs", 4);
                        addExercise(legs, "Calf Raise",        "Legs", 5);
                        templates.add(push); templates.add(pull); templates.add(legs); break;
                    }
                }
                break;

            case 4: // UPPER / LOWER x2
                switch (type) {
                    case BULKING: {
                        WorkoutTemplate upperA = newTemplate("Upper A – Bulking", "Heavy press focus");
                        addExercise(upperA, "Barbell Bench Press",    "Chest",     1);
                        addExercise(upperA, "Incline Dumbbell Press", "Chest",     2);
                        addExercise(upperA, "Overhead Press",         "Shoulders", 3);
                        addExercise(upperA, "Lateral Raise",          "Shoulders", 4);
                        addExercise(upperA, "Tricep Pushdown",        "Triceps",   5);
                        WorkoutTemplate lowerA = newTemplate("Lower A – Bulking", "Heavy squat focus");
                        addExercise(lowerA, "Barbell Squat", "Legs", 1);
                        addExercise(lowerA, "Leg Press",     "Legs", 2);
                        addExercise(lowerA, "Leg Extension", "Legs", 3);
                        addExercise(lowerA, "Leg Curl",      "Legs", 4);
                        addExercise(lowerA, "Calf Raise",    "Legs", 5);
                        WorkoutTemplate upperB = newTemplate("Upper B – Bulking", "Heavy row focus");
                        addExercise(upperB, "Barbell Row",  "Back",      1);
                        addExercise(upperB, "Pull-Up",      "Back",      2);
                        addExercise(upperB, "Lat Pulldown", "Back",      3);
                        addExercise(upperB, "Face Pull",    "Shoulders", 4);
                        addExercise(upperB, "Barbell Curl", "Biceps",    5);
                        WorkoutTemplate lowerB = newTemplate("Lower B – Bulking", "Heavy hinge focus");
                        addExercise(lowerB, "Barbell Deadlift",  "Back", 1);
                        addExercise(lowerB, "Romanian Deadlift", "Legs", 2);
                        addExercise(lowerB, "Glute Bridge",      "Legs", 3);
                        addExercise(lowerB, "Leg Curl",          "Legs", 4);
                        addExercise(lowerB, "Calf Raise",        "Legs", 5);
                        templates.add(upperA); templates.add(lowerA);
                        templates.add(upperB); templates.add(lowerB); break;
                    }
                    case CUTTING: {
                        WorkoutTemplate upperA = newTemplate("Upper A – Cutting", "Press with supersets");
                        addExercise(upperA, "Dumbbell Bench Press", "Chest",     1);
                        addExercise(upperA, "Cable Fly",            "Chest",     2);
                        addExercise(upperA, "Overhead Press",       "Shoulders", 3);
                        addExercise(upperA, "Lateral Raise",        "Shoulders", 4);
                        addExercise(upperA, "Tricep Extension",     "Triceps",   5);
                        WorkoutTemplate lowerA = newTemplate("Lower A – Cutting", "Squat with supersets");
                        addExercise(lowerA, "Goblet Squat",  "Legs", 1);
                        addExercise(lowerA, "Leg Press",     "Legs", 2);
                        addExercise(lowerA, "Leg Extension", "Legs", 3);
                        addExercise(lowerA, "Leg Curl",      "Legs", 4);
                        addExercise(lowerA, "Calf Raise",    "Legs", 5);
                        WorkoutTemplate upperB = newTemplate("Upper B – Cutting", "Row with supersets");
                        addExercise(upperB, "Dumbbell Row", "Back",      1);
                        addExercise(upperB, "Lat Pulldown", "Back",      2);
                        addExercise(upperB, "Cable Row",    "Back",      3);
                        addExercise(upperB, "Face Pull",    "Shoulders", 4);
                        addExercise(upperB, "Bicep Curl",   "Biceps",    5);
                        WorkoutTemplate lowerB = newTemplate("Lower B – Cutting", "Hinge with supersets");
                        addExercise(lowerB, "Romanian Deadlift", "Legs", 1);
                        addExercise(lowerB, "Glute Bridge",      "Legs", 2);
                        addExercise(lowerB, "Leg Curl",          "Legs", 3);
                        addExercise(lowerB, "Lunge",             "Legs", 4);
                        addExercise(lowerB, "Calf Raise",        "Legs", 5);
                        templates.add(upperA); templates.add(lowerA);
                        templates.add(upperB); templates.add(lowerB); break;
                    }
                    case WEIGHT_LOSS: {
                        WorkoutTemplate upperA = newTemplate("Upper A – Weight Loss", "Upper circuit day 1");
                        addExercise(upperA, "Push-Up",                 "Chest",     1);
                        addExercise(upperA, "Dumbbell Shoulder Press", "Shoulders", 2);
                        addExercise(upperA, "Lateral Raise",           "Shoulders", 3);
                        addExercise(upperA, "Tricep Dip",              "Triceps",   4);
                        addExercise(upperA, "Chest Fly",               "Chest",     5);
                        WorkoutTemplate lowerA = newTemplate("Lower A – Weight Loss", "Lower circuit day 1");
                        addExercise(lowerA, "Bodyweight Squat", "Legs", 1);
                        addExercise(lowerA, "Lunge",            "Legs", 2);
                        addExercise(lowerA, "Leg Extension",    "Legs", 3);
                        addExercise(lowerA, "Leg Curl",         "Legs", 4);
                        addExercise(lowerA, "Calf Raise",       "Legs", 5);
                        WorkoutTemplate upperB = newTemplate("Upper B – Weight Loss", "Upper circuit day 2");
                        addExercise(upperB, "Dumbbell Row", "Back",      1);
                        addExercise(upperB, "Lat Pulldown", "Back",      2);
                        addExercise(upperB, "Bicep Curl",   "Biceps",    3);
                        addExercise(upperB, "Face Pull",    "Shoulders", 4);
                        addExercise(upperB, "Hammer Curl",  "Biceps",    5);
                        WorkoutTemplate lowerB = newTemplate("Lower B – Weight Loss", "Lower circuit day 2");
                        addExercise(lowerB, "Glute Bridge",      "Legs", 1);
                        addExercise(lowerB, "Romanian Deadlift", "Legs", 2);
                        addExercise(lowerB, "Leg Curl",          "Legs", 3);
                        addExercise(lowerB, "Step-Up",           "Legs", 4);
                        addExercise(lowerB, "Calf Raise",        "Legs", 5);
                        templates.add(upperA); templates.add(lowerA);
                        templates.add(upperB); templates.add(lowerB); break;
                    }
                    case BEGINNER: default: {
                        WorkoutTemplate upperA = newTemplate("Upper A – Beginner", "Basic upper – press focus");
                        addExercise(upperA, "Dumbbell Bench Press",    "Chest",     1);
                        addExercise(upperA, "Dumbbell Shoulder Press", "Shoulders", 2);
                        addExercise(upperA, "Lateral Raise",           "Shoulders", 3);
                        addExercise(upperA, "Tricep Extension",        "Triceps",   4);
                        addExercise(upperA, "Chest Fly",               "Chest",     5);
                        WorkoutTemplate lowerA = newTemplate("Lower A – Beginner", "Basic lower – squat focus");
                        addExercise(lowerA, "Goblet Squat",  "Legs", 1);
                        addExercise(lowerA, "Leg Press",     "Legs", 2);
                        addExercise(lowerA, "Leg Extension", "Legs", 3);
                        addExercise(lowerA, "Leg Curl",      "Legs", 4);
                        addExercise(lowerA, "Calf Raise",    "Legs", 5);
                        WorkoutTemplate upperB = newTemplate("Upper B – Beginner", "Basic upper – row focus");
                        addExercise(upperB, "Dumbbell Row", "Back",      1);
                        addExercise(upperB, "Lat Pulldown", "Back",      2);
                        addExercise(upperB, "Bicep Curl",   "Biceps",    3);
                        addExercise(upperB, "Face Pull",    "Shoulders", 4);
                        addExercise(upperB, "Hammer Curl",  "Biceps",    5);
                        WorkoutTemplate lowerB = newTemplate("Lower B – Beginner", "Basic lower – hinge focus");
                        addExercise(lowerB, "Romanian Deadlift", "Legs", 1);
                        addExercise(lowerB, "Glute Bridge",      "Legs", 2);
                        addExercise(lowerB, "Lunge",             "Legs", 3);
                        addExercise(lowerB, "Leg Curl",          "Legs", 4);
                        addExercise(lowerB, "Calf Raise",        "Legs", 5);
                        templates.add(upperA); templates.add(lowerA);
                        templates.add(upperB); templates.add(lowerB); break;
                    }
                }
                break;

            case 5: // PUSH / PULL / LEGS / UPPER / LOWER
                switch (type) {
                    case BULKING: {
                        WorkoutTemplate push = newTemplate("Push – Bulking", "Chest, shoulders, triceps");
                        addExercise(push, "Barbell Bench Press",    "Chest",     1);
                        addExercise(push, "Incline Dumbbell Press", "Chest",     2);
                        addExercise(push, "Overhead Press",         "Shoulders", 3);
                        addExercise(push, "Lateral Raise",          "Shoulders", 4);
                        addExercise(push, "Skull Crusher",          "Triceps",   5);
                        WorkoutTemplate pull = newTemplate("Pull – Bulking", "Back and biceps");
                        addExercise(pull, "Barbell Row",  "Back",      1);
                        addExercise(pull, "Pull-Up",      "Back",      2);
                        addExercise(pull, "Lat Pulldown", "Back",      3);
                        addExercise(pull, "Face Pull",    "Shoulders", 4);
                        addExercise(pull, "Barbell Curl", "Biceps",    5);
                        WorkoutTemplate legs = newTemplate("Legs – Bulking", "Full leg session");
                        addExercise(legs, "Barbell Squat",     "Legs", 1);
                        addExercise(legs, "Romanian Deadlift", "Legs", 2);
                        addExercise(legs, "Leg Press",         "Legs", 3);
                        addExercise(legs, "Leg Curl",          "Legs", 4);
                        addExercise(legs, "Calf Raise",        "Legs", 5);
                        WorkoutTemplate upper = newTemplate("Upper – Bulking", "Upper body volume day");
                        addExercise(upper, "Incline Barbell Press", "Chest",     1);
                        addExercise(upper, "Cable Fly",             "Chest",     2);
                        addExercise(upper, "Seated Row",            "Back",      3);
                        addExercise(upper, "Face Pull",             "Shoulders", 4);
                        addExercise(upper, "Tricep Pushdown",       "Triceps",   5);
                        WorkoutTemplate lower = newTemplate("Lower – Bulking", "Lower body volume day");
                        addExercise(lower, "Front Squat",   "Legs", 1);
                        addExercise(lower, "Leg Press",     "Legs", 2);
                        addExercise(lower, "Leg Curl",      "Legs", 3);
                        addExercise(lower, "Leg Extension", "Legs", 4);
                        addExercise(lower, "Calf Raise",    "Legs", 5);
                        templates.add(push); templates.add(pull); templates.add(legs);
                        templates.add(upper); templates.add(lower); break;
                    }
                    case CUTTING: {
                        WorkoutTemplate push = newTemplate("Push – Cutting", "Push muscles – higher reps");
                        addExercise(push, "Dumbbell Bench Press", "Chest",     1);
                        addExercise(push, "Cable Fly",            "Chest",     2);
                        addExercise(push, "Overhead Press",       "Shoulders", 3);
                        addExercise(push, "Lateral Raise",        "Shoulders", 4);
                        addExercise(push, "Tricep Extension",     "Triceps",   5);
                        WorkoutTemplate pull = newTemplate("Pull – Cutting", "Pull muscles – higher reps");
                        addExercise(pull, "Dumbbell Row", "Back",      1);
                        addExercise(pull, "Lat Pulldown", "Back",      2);
                        addExercise(pull, "Cable Row",    "Back",      3);
                        addExercise(pull, "Face Pull",    "Shoulders", 4);
                        addExercise(pull, "Bicep Curl",   "Biceps",    5);
                        WorkoutTemplate legs = newTemplate("Legs – Cutting", "Leg day – higher reps");
                        addExercise(legs, "Goblet Squat",      "Legs", 1);
                        addExercise(legs, "Romanian Deadlift", "Legs", 2);
                        addExercise(legs, "Leg Press",         "Legs", 3);
                        addExercise(legs, "Leg Curl",          "Legs", 4);
                        addExercise(legs, "Calf Raise",        "Legs", 5);
                        WorkoutTemplate upper = newTemplate("Upper – Cutting", "Upper volume with cardio");
                        addExercise(upper, "Dumbbell Bench Press", "Chest",    1);
                        addExercise(upper, "Cable Fly",            "Chest",    2);
                        addExercise(upper, "Lat Pulldown",         "Back",     3);
                        addExercise(upper, "Cable Row",            "Back",     4);
                        addExercise(upper, "Tricep Extension",     "Triceps",  5);
                        WorkoutTemplate lower = newTemplate("Lower – Cutting", "Lower volume with cardio");
                        addExercise(lower, "Goblet Squat",  "Legs", 1);
                        addExercise(lower, "Lunge",         "Legs", 2);
                        addExercise(lower, "Leg Curl",      "Legs", 3);
                        addExercise(lower, "Leg Extension", "Legs", 4);
                        addExercise(lower, "Calf Raise",    "Legs", 5);
                        templates.add(push); templates.add(pull); templates.add(legs);
                        templates.add(upper); templates.add(lower); break;
                    }
                    case WEIGHT_LOSS: {
                        WorkoutTemplate push = newTemplate("Push – Weight Loss", "Push circuit");
                        addExercise(push, "Push-Up",                 "Chest",     1);
                        addExercise(push, "Dumbbell Shoulder Press", "Shoulders", 2);
                        addExercise(push, "Lateral Raise",           "Shoulders", 3);
                        addExercise(push, "Tricep Dip",              "Triceps",   4);
                        addExercise(push, "Chest Fly",               "Chest",     5);
                        WorkoutTemplate pull = newTemplate("Pull – Weight Loss", "Pull circuit");
                        addExercise(pull, "Dumbbell Row", "Back",      1);
                        addExercise(pull, "Lat Pulldown", "Back",      2);
                        addExercise(pull, "Bicep Curl",   "Biceps",    3);
                        addExercise(pull, "Face Pull",    "Shoulders", 4);
                        addExercise(pull, "Hammer Curl",  "Biceps",    5);
                        WorkoutTemplate legs = newTemplate("Legs – Weight Loss", "Leg circuit");
                        addExercise(legs, "Bodyweight Squat", "Legs", 1);
                        addExercise(legs, "Lunge",            "Legs", 2);
                        addExercise(legs, "Glute Bridge",     "Legs", 3);
                        addExercise(legs, "Leg Curl",         "Legs", 4);
                        addExercise(legs, "Calf Raise",       "Legs", 5);
                        WorkoutTemplate upper = newTemplate("Upper – Weight Loss", "Upper full circuit");
                        addExercise(upper, "Push-Up",          "Chest",     1);
                        addExercise(upper, "Dumbbell Row",     "Back",      2);
                        addExercise(upper, "Lateral Raise",    "Shoulders", 3);
                        addExercise(upper, "Tricep Extension", "Triceps",   4);
                        addExercise(upper, "Bicep Curl",       "Biceps",    5);
                        WorkoutTemplate lower = newTemplate("Lower – Weight Loss", "Lower full circuit");
                        addExercise(lower, "Bodyweight Squat", "Legs", 1);
                        addExercise(lower, "Glute Bridge",     "Legs", 2);
                        addExercise(lower, "Leg Curl",         "Legs", 3);
                        addExercise(lower, "Lunge",            "Legs", 4);
                        addExercise(lower, "Calf Raise",       "Legs", 5);
                        templates.add(push); templates.add(pull); templates.add(legs);
                        templates.add(upper); templates.add(lower); break;
                    }
                    case BEGINNER: default: {
                        WorkoutTemplate push = newTemplate("Push – Beginner", "Push muscles intro");
                        addExercise(push, "Push-Up",                 "Chest",     1);
                        addExercise(push, "Dumbbell Shoulder Press", "Shoulders", 2);
                        addExercise(push, "Lateral Raise",           "Shoulders", 3);
                        addExercise(push, "Tricep Extension",        "Triceps",   4);
                        addExercise(push, "Chest Fly",               "Chest",     5);
                        WorkoutTemplate pull = newTemplate("Pull – Beginner", "Pull muscles intro");
                        addExercise(pull, "Dumbbell Row", "Back",      1);
                        addExercise(pull, "Lat Pulldown", "Back",      2);
                        addExercise(pull, "Bicep Curl",   "Biceps",    3);
                        addExercise(pull, "Face Pull",    "Shoulders", 4);
                        addExercise(pull, "Hammer Curl",  "Biceps",    5);
                        WorkoutTemplate legs = newTemplate("Legs – Beginner", "Leg day intro");
                        addExercise(legs, "Goblet Squat",      "Legs", 1);
                        addExercise(legs, "Romanian Deadlift", "Legs", 2);
                        addExercise(legs, "Lunge",             "Legs", 3);
                        addExercise(legs, "Leg Curl",          "Legs", 4);
                        addExercise(legs, "Calf Raise",        "Legs", 5);
                        WorkoutTemplate upper = newTemplate("Upper – Beginner", "Full upper intro");
                        addExercise(upper, "Dumbbell Bench Press",    "Chest",     1);
                        addExercise(upper, "Dumbbell Row",            "Back",      2);
                        addExercise(upper, "Dumbbell Shoulder Press", "Shoulders", 3);
                        addExercise(upper, "Lateral Raise",           "Shoulders", 4);
                        addExercise(upper, "Tricep Extension",        "Triceps",   5);
                        WorkoutTemplate lower = newTemplate("Lower – Beginner", "Full lower intro");
                        addExercise(lower, "Goblet Squat",      "Legs", 1);
                        addExercise(lower, "Romanian Deadlift", "Legs", 2);
                        addExercise(lower, "Leg Extension",     "Legs", 3);
                        addExercise(lower, "Leg Curl",          "Legs", 4);
                        addExercise(lower, "Calf Raise",        "Legs", 5);
                        templates.add(push); templates.add(pull); templates.add(legs);
                        templates.add(upper); templates.add(lower); break;
                    }
                }
                break;

            case 6: // PUSH / PULL / LEGS x2
                switch (type) {
                    case BULKING: {
                        WorkoutTemplate pushA = newTemplate("Push A – Bulking", "Heavy push day");
                        addExercise(pushA, "Barbell Bench Press",    "Chest",     1);
                        addExercise(pushA, "Overhead Press",         "Shoulders", 2);
                        addExercise(pushA, "Incline Dumbbell Press", "Chest",     3);
                        addExercise(pushA, "Lateral Raise",          "Shoulders", 4);
                        addExercise(pushA, "Skull Crusher",          "Triceps",   5);
                        WorkoutTemplate pullA = newTemplate("Pull A – Bulking", "Heavy pull day");
                        addExercise(pullA, "Barbell Row",  "Back",      1);
                        addExercise(pullA, "Pull-Up",      "Back",      2);
                        addExercise(pullA, "Barbell Curl", "Biceps",    3);
                        addExercise(pullA, "Face Pull",    "Shoulders", 4);
                        addExercise(pullA, "Hammer Curl",  "Biceps",    5);
                        WorkoutTemplate legsA = newTemplate("Legs A – Bulking", "Heavy leg day");
                        addExercise(legsA, "Barbell Squat",     "Legs", 1);
                        addExercise(legsA, "Romanian Deadlift", "Legs", 2);
                        addExercise(legsA, "Leg Press",         "Legs", 3);
                        addExercise(legsA, "Leg Curl",          "Legs", 4);
                        addExercise(legsA, "Calf Raise",        "Legs", 5);
                        WorkoutTemplate pushB = newTemplate("Push B – Bulking", "Volume push day");
                        addExercise(pushB, "Incline Barbell Press",   "Chest",     1);
                        addExercise(pushB, "Cable Fly",               "Chest",     2);
                        addExercise(pushB, "Dumbbell Shoulder Press", "Shoulders", 3);
                        addExercise(pushB, "Lateral Raise",           "Shoulders", 4);
                        addExercise(pushB, "Tricep Pushdown",         "Triceps",   5);
                        WorkoutTemplate pullB = newTemplate("Pull B – Bulking", "Volume pull day");
                        addExercise(pullB, "Lat Pulldown", "Back",      1);
                        addExercise(pullB, "Cable Row",    "Back",      2);
                        addExercise(pullB, "Face Pull",    "Shoulders", 3);
                        addExercise(pullB, "Bicep Curl",   "Biceps",    4);
                        addExercise(pullB, "Hammer Curl",  "Biceps",    5);
                        WorkoutTemplate legsB = newTemplate("Legs B – Bulking", "Volume leg day");
                        addExercise(legsB, "Front Squat",   "Legs", 1);
                        addExercise(legsB, "Leg Press",     "Legs", 2);
                        addExercise(legsB, "Leg Extension", "Legs", 3);
                        addExercise(legsB, "Leg Curl",      "Legs", 4);
                        addExercise(legsB, "Calf Raise",    "Legs", 5);
                        templates.add(pushA); templates.add(pullA); templates.add(legsA);
                        templates.add(pushB); templates.add(pullB); templates.add(legsB); break;
                    }
                    case CUTTING: {
                        WorkoutTemplate pushA = newTemplate("Push A – Cutting", "Heavy push – caloric deficit");
                        addExercise(pushA, "Barbell Bench Press", "Chest",     1);
                        addExercise(pushA, "Overhead Press",      "Shoulders", 2);
                        addExercise(pushA, "Lateral Raise",       "Shoulders", 3);
                        addExercise(pushA, "Tricep Dip",          "Triceps",   4);
                        addExercise(pushA, "Chest Fly",           "Chest",     5);
                        WorkoutTemplate pullA = newTemplate("Pull A – Cutting", "Heavy pull – caloric deficit");
                        addExercise(pullA, "Barbell Row",  "Back",      1);
                        addExercise(pullA, "Lat Pulldown", "Back",      2);
                        addExercise(pullA, "Face Pull",    "Shoulders", 3);
                        addExercise(pullA, "Barbell Curl", "Biceps",    4);
                        addExercise(pullA, "Hammer Curl",  "Biceps",    5);
                        WorkoutTemplate legsA = newTemplate("Legs A – Cutting", "Heavy legs – caloric deficit");
                        addExercise(legsA, "Barbell Squat",     "Legs", 1);
                        addExercise(legsA, "Romanian Deadlift", "Legs", 2);
                        addExercise(legsA, "Leg Press",         "Legs", 3);
                        addExercise(legsA, "Leg Curl",          "Legs", 4);
                        addExercise(legsA, "Calf Raise",        "Legs", 5);
                        WorkoutTemplate pushB = newTemplate("Push B – Cutting", "Volume push – higher reps");
                        addExercise(pushB, "Dumbbell Bench Press",    "Chest",     1);
                        addExercise(pushB, "Cable Fly",               "Chest",     2);
                        addExercise(pushB, "Dumbbell Shoulder Press", "Shoulders", 3);
                        addExercise(pushB, "Lateral Raise",           "Shoulders", 4);
                        addExercise(pushB, "Tricep Extension",        "Triceps",   5);
                        WorkoutTemplate pullB = newTemplate("Pull B – Cutting", "Volume pull – higher reps");
                        addExercise(pullB, "Dumbbell Row", "Back",      1);
                        addExercise(pullB, "Cable Row",    "Back",      2);
                        addExercise(pullB, "Face Pull",    "Shoulders", 3);
                        addExercise(pullB, "Bicep Curl",   "Biceps",    4);
                        addExercise(pullB, "Hammer Curl",  "Biceps",    5);
                        WorkoutTemplate legsB = newTemplate("Legs B – Cutting", "Volume legs – higher reps");
                        addExercise(legsB, "Goblet Squat",  "Legs", 1);
                        addExercise(legsB, "Lunge",         "Legs", 2);
                        addExercise(legsB, "Leg Extension", "Legs", 3);
                        addExercise(legsB, "Leg Curl",      "Legs", 4);
                        addExercise(legsB, "Calf Raise",    "Legs", 5);
                        templates.add(pushA); templates.add(pullA); templates.add(legsA);
                        templates.add(pushB); templates.add(pullB); templates.add(legsB); break;
                    }
                    case WEIGHT_LOSS: {
                        WorkoutTemplate pushA = newTemplate("Push A – Weight Loss", "Push circuit day 1");
                        addExercise(pushA, "Push-Up",                 "Chest",     1);
                        addExercise(pushA, "Dumbbell Shoulder Press", "Shoulders", 2);
                        addExercise(pushA, "Lateral Raise",           "Shoulders", 3);
                        addExercise(pushA, "Tricep Dip",              "Triceps",   4);
                        addExercise(pushA, "Chest Fly",               "Chest",     5);
                        WorkoutTemplate pullA = newTemplate("Pull A – Weight Loss", "Pull circuit day 1");
                        addExercise(pullA, "Dumbbell Row", "Back",      1);
                        addExercise(pullA, "Lat Pulldown", "Back",      2);
                        addExercise(pullA, "Face Pull",    "Shoulders", 3);
                        addExercise(pullA, "Bicep Curl",   "Biceps",    4);
                        addExercise(pullA, "Hammer Curl",  "Biceps",    5);
                        WorkoutTemplate legsA = newTemplate("Legs A – Weight Loss", "Leg circuit day 1");
                        addExercise(legsA, "Bodyweight Squat", "Legs", 1);
                        addExercise(legsA, "Lunge",            "Legs", 2);
                        addExercise(legsA, "Glute Bridge",     "Legs", 3);
                        addExercise(legsA, "Leg Curl",         "Legs", 4);
                        addExercise(legsA, "Calf Raise",       "Legs", 5);
                        WorkoutTemplate pushB = newTemplate("Push B – Weight Loss", "Push circuit day 2");
                        addExercise(pushB, "Incline Push-Up",         "Chest",     1);
                        addExercise(pushB, "Cable Fly",               "Chest",     2);
                        addExercise(pushB, "Dumbbell Shoulder Press", "Shoulders", 3);
                        addExercise(pushB, "Lateral Raise",           "Shoulders", 4);
                        addExercise(pushB, "Tricep Extension",        "Triceps",   5);
                        WorkoutTemplate pullB = newTemplate("Pull B – Weight Loss", "Pull circuit day 2");
                        addExercise(pullB, "Cable Row",   "Back",      1);
                        addExercise(pullB, "Face Pull",   "Shoulders", 2);
                        addExercise(pullB, "Bicep Curl",  "Biceps",    3);
                        addExercise(pullB, "Hammer Curl", "Biceps",    4);
                        addExercise(pullB, "Reverse Fly", "Back",      5);
                        WorkoutTemplate legsB = newTemplate("Legs B – Weight Loss", "Leg circuit day 2");
                        addExercise(legsB, "Step-Up",            "Legs", 1);
                        addExercise(legsB, "Romanian Deadlift",  "Legs", 2);
                        addExercise(legsB, "Leg Curl",           "Legs", 3);
                        addExercise(legsB, "Leg Extension",      "Legs", 4);
                        addExercise(legsB, "Calf Raise",         "Legs", 5);
                        templates.add(pushA); templates.add(pullA); templates.add(legsA);
                        templates.add(pushB); templates.add(pullB); templates.add(legsB); break;
                    }
                    case BEGINNER: default: {
                        WorkoutTemplate pushA = newTemplate("Push A – Beginner", "Push day 1");
                        addExercise(pushA, "Push-Up",                 "Chest",     1);
                        addExercise(pushA, "Dumbbell Shoulder Press", "Shoulders", 2);
                        addExercise(pushA, "Lateral Raise",           "Shoulders", 3);
                        addExercise(pushA, "Tricep Extension",        "Triceps",   4);
                        addExercise(pushA, "Chest Fly",               "Chest",     5);
                        WorkoutTemplate pullA = newTemplate("Pull A – Beginner", "Pull day 1");
                        addExercise(pullA, "Dumbbell Row", "Back",      1);
                        addExercise(pullA, "Lat Pulldown", "Back",      2);
                        addExercise(pullA, "Bicep Curl",   "Biceps",    3);
                        addExercise(pullA, "Face Pull",    "Shoulders", 4);
                        addExercise(pullA, "Hammer Curl",  "Biceps",    5);
                        WorkoutTemplate legsA = newTemplate("Legs A – Beginner", "Leg day 1");
                        addExercise(legsA, "Goblet Squat",      "Legs", 1);
                        addExercise(legsA, "Romanian Deadlift", "Legs", 2);
                        addExercise(legsA, "Leg Extension",     "Legs", 3);
                        addExercise(legsA, "Leg Curl",          "Legs", 4);
                        addExercise(legsA, "Calf Raise",        "Legs", 5);
                        WorkoutTemplate pushB = newTemplate("Push B – Beginner", "Push day 2");
                        addExercise(pushB, "Dumbbell Bench Press",    "Chest",     1);
                        addExercise(pushB, "Incline Push-Up",         "Chest",     2);
                        addExercise(pushB, "Dumbbell Shoulder Press", "Shoulders", 3);
                        addExercise(pushB, "Lateral Raise",           "Shoulders", 4);
                        addExercise(pushB, "Tricep Pushdown",         "Triceps",   5);
                        WorkoutTemplate pullB = newTemplate("Pull B – Beginner", "Pull day 2");
                        addExercise(pullB, "Dumbbell Row", "Back",      1);
                        addExercise(pullB, "Cable Row",    "Back",      2);
                        addExercise(pullB, "Face Pull",    "Shoulders", 3);
                        addExercise(pullB, "Bicep Curl",   "Biceps",    4);
                        addExercise(pullB, "Hammer Curl",  "Biceps",    5);
                        WorkoutTemplate legsB = newTemplate("Legs B – Beginner", "Leg day 2");
                        addExercise(legsB, "Lunge",         "Legs", 1);
                        addExercise(legsB, "Glute Bridge",  "Legs", 2);
                        addExercise(legsB, "Leg Curl",      "Legs", 3);
                        addExercise(legsB, "Leg Extension", "Legs", 4);
                        addExercise(legsB, "Calf Raise",    "Legs", 5);
                        templates.add(pushA); templates.add(pullA); templates.add(legsA);
                        templates.add(pushB); templates.add(pullB); templates.add(legsB); break;
                    }
                }
                break;

            default:
                templates.add(newTemplate("Push – Beginner", "Chest, shoulders, triceps intro"));
                templates.add(newTemplate("Pull – Beginner", "Back and biceps intro"));
                templates.add(newTemplate("Legs – Beginner", "Leg day intro"));
                break;
        }

        return templates;
    }

    public List<WorkoutTemplate> saveGeneratedTemplates(List<WorkoutTemplate> templates) {
        List<WorkoutTemplate> saved = new ArrayList<>();
        for (WorkoutTemplate t : templates) {
            if (workoutTemplateRepository.findByWorkoutName(t.getWorkoutName()).isPresent()) {
                continue;
            }
            WorkoutTemplate savedTemplate = workoutTemplateRepository.save(
                new WorkoutTemplate(t.getWorkoutName(), t.getWorkoutDescription())
            );
            if (t.getExercises() != null) {
                for (WorkoutExercise we : t.getExercises()) {
                    Exercise ex = we.getExercise();
                    Exercise savedEx = exerciseRepository.findByExerciseName(ex.getExerciseName())
                        .orElseGet(() -> exerciseRepository.save(
                            new Exercise(null, ex.getExerciseName(), ex.getMuscleGroup(), "")
                        ));
                    WorkoutExercise newWe = new WorkoutExercise();
                    newWe.setExercise(savedEx);
                    newWe.setListOrder(we.getListOrder());
                    newWe.setWorkoutTemplate(savedTemplate);
                    workoutExerciseRepository.save(newWe);
                }
                savedTemplate.setExerciseCount(t.getExercises().size());
                workoutTemplateRepository.save(savedTemplate);
            }
            saved.add(savedTemplate);
        }
        return saved;
    }

    // ─── Private helpers ──────────────────────────────────────────────────────

    private WorkoutTemplate newTemplate(String name, String description) {
        return new WorkoutTemplate(name, description);
    }

    private void addExercise(WorkoutTemplate template, String name, String muscleGroup, int order) {
        Exercise exercise = new Exercise(null, name, muscleGroup, "");
        WorkoutExercise we = new WorkoutExercise(null, exercise, order, null, template);
        template.getExercises().add(we);
        template.setExerciseCount(template.getExerciseCount() + 1);
    }
}

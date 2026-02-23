package com.gymbuddy.app.WorkoutDomain.Exercise;

public class Exercise {
    private int exerciseID;
    private String exerciseName;
    private String muscleGroup;
    private String exerciseDescription;
    private static int exerciseCounter = 0;

public Exercise(String exerciseName, String muscleGroup, String exerciseDescription) {
    this.exerciseID = exerciseCounter++;
    this.exerciseName = exerciseName;
    this.muscleGroup = muscleGroup;
    this.exerciseDescription = exerciseDescription;
}

public String getExerciseName() {
    return exerciseName;
}


public void setExerciseName(String exerciseName) {
    this.exerciseName = exerciseName;
}


public String getMuscleGroup() {
    return muscleGroup;
}


public void setMuscleGroup(String muscleGroup) {
    this.muscleGroup = muscleGroup;
}


public String getExerciseDescription() {
    return exerciseDescription;
}


public void setExerciseDescription(String exerciseDescription) {
    this.exerciseDescription = exerciseDescription;
}


}



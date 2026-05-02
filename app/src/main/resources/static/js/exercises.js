// Exercise-related functionality
function initializeExerciseForm() {
    const exerciseForm = document.getElementById('exercise-form');

    if (exerciseForm) {
        exerciseForm.addEventListener('submit', handleExerciseSubmit);
    }
}

function handleExerciseSubmit(event) {
    event.preventDefault();

    const exerciseData = {
        exerciseName: document.getElementById('exerciseName').value,
        muscleGroup: document.getElementById('muscleGroup').value,
        exerciseDescription: document.getElementById('exerciseDescription').value
    };

    console.log('Submitting exercise data:', exerciseData);

    // Using api.js function
    submitExercise(exerciseData)
        .then(data => {
            alert('Exercise created successfully!');
            console.log('Success:', data);

            // Clear the form
            event.target.reset();

            // Optionally redirect to home page after 2 seconds
            setTimeout(() => {
                window.location.href = '/';
            }, 2000);
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Failed to create exercise: ' + error.message);
        });
}

// Initialize on DOM ready
document.addEventListener('DOMContentLoaded', initializeExerciseForm);

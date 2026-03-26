
function goToCreate() {
    window.location.href = '/create-exercise.html';
}

// Handle exercise form submission
document.addEventListener('DOMContentLoaded', function() {
    const getStartedBtn = document.getElementById('get-started-btn');
    const learnMoreBtn = document.getElementById('learn-more-btn');
    const exerciseForm = document.getElementById('exercise-form');

    if (getStartedBtn) {
        getStartedBtn.addEventListener('click', function() {
            alert('Get Started functionality coming soon!');
        });
    }

    if (learnMoreBtn) {
        learnMoreBtn.addEventListener('click', function() {
            alert('Learn More functionality coming soon!');
        });
    }

    // Add event listener for exercise form submission
    if (exerciseForm) {
        exerciseForm.addEventListener('submit', function(event) {
            event.preventDefault(); // Prevent default form submission

            const exerciseData = {
                exerciseName: document.getElementById('exerciseName').value,
                muscleGroup: document.getElementById('muscleGroup').value,
                exerciseDescription: document.getElementById('exerciseDescription').value
            };

            console.log('Submitting exercise data:', exerciseData);

            // Make POST request to the exercise API
            fetch('/exercise', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(exerciseData)
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                return response.text(); // Use text() since the controller returns a string
            })
            .then(data => {
                alert('Exercise created successfully!');
                console.log('Success:', data);

                // Clear the form or redirect
                exerciseForm.reset();
                // Optionally redirect to home page after 2 seconds
                setTimeout(() => {
                    window.location.href = '/';
                }, 2000);
            })
            .catch(error => {
                console.error('Error:', error);
                alert('Failed to create exercise: ' + error.message);
            });
        });
    }
});
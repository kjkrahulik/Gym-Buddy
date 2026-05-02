// Navigation and UI interaction functions
function goToCreate() {
    window.location.href = '/pages/create-exercise.html';
}

function initializeNavigation() {
    const getStartedBtn = document.getElementById('get-started-btn');
    const learnMoreBtn = document.getElementById('learn-more-btn');
    const goToCreateBtn = document.getElementById('go-to-create-btn');

    if (learnMoreBtn) {
        learnMoreBtn.addEventListener('click', function() {
            alert('Learn More functionality coming soon!');
        });
    }

    if (goToCreateBtn) {
        goToCreateBtn.addEventListener('click', goToCreate);
    }
}

// Initialize on DOM ready
document.addEventListener('DOMContentLoaded', initializeNavigation);

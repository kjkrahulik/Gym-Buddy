document.addEventListener('DOMContentLoaded', function() {
    const getStartedBtn = document.getElementById('get-started-btn');
    const learnMoreBtn = document.getElementById('learn-more-btn');

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
});
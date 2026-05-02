// Authentication related functions
function initializeAuthButtons() {
    const loginBtn = document.getElementById('login-btn');
    const createAccountBtn = document.getElementById('create-account-btn');

    if (loginBtn) {
        loginBtn.addEventListener('click', redirectToLogin);
    }

    if (createAccountBtn) {
        createAccountBtn.addEventListener('click', redirectToRegister);
    }
}

function redirectToLogin() {
    window.location.href = '/login';
}

function redirectToRegister() {
    window.location.href = '/register';
}

// Initialize auth on DOM ready
document.addEventListener('DOMContentLoaded', initializeAuthButtons);

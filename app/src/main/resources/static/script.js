
function goToCreate() {
    window.location.href = '/create-exercise.html';
}

// Handle exercise form submission
document.addEventListener('DOMContentLoaded', function() {
    const getStartedBtn = document.getElementById('get-started-btn');
    const learnMoreBtn = document.getElementById('learn-more-btn');
    const createAccountBtn = document.getElementById('create-account-btn');
    const loginBtn = document.getElementById('login-btn');
    const goToCreateBtn = document.getElementById('go-to-create-btn');
    const exerciseForm = document.getElementById('exercise-form');

    if (learnMoreBtn) {
        learnMoreBtn.addEventListener('click', function() {
            alert('Learn More functionality coming soon!');
        });
    }

    if (loginBtn) {
        loginBtn.addEventListener('click', function() {
            // Create a modal dialog for login
            const modalHtml = `
                <div id="login-modal" style="
                    position: fixed;
                    top: 0;
                    left: 0;
                    width: 100%;
                    height: 100%;
                    background: rgba(0, 0, 0, 0.8);
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    z-index: 1000;
                ">
                    <div style="
                        background: var(--concrete);
                        padding: 40px;
                        border-radius: 0;
                        border-top: 4px solid var(--rust);
                        border-bottom: 1px solid var(--steel);
                        max-width: 500px;
                        width: 90vw;
                        position: relative;
                        clip-path: polygon(0 0, calc(100% - 24px) 0, 100% 24px, 100% 100%, 0 100%);
                    ">
                        <h2 style="
                            font-family: 'Barlow Condensed', sans-serif;
                            font-weight: 900;
                            color: var(--chalk);
                            font-size: 28px;
                            margin-bottom: 12px;
                        ">Login to GymBuddy</h2>
                        <p style="color: var(--chalk-dim); margin-bottom: 24px;">Enter your credentials to access your account</p>

                        <form id="login-form">
                            <label for="login-username">Username or Email</label>
                            <input type="text" id="login-username" placeholder="Enter username or email" required style="margin-bottom: 16px;">

                            <label for="login-password">Password</label>
                            <input type="password" id="login-password" placeholder="Enter your password" required style="margin-bottom: 24px;">

                            <div style="display: flex; gap: 12px;">
                                <button type="submit" class="cta-button" style="flex: 1;">Login</button>
                                <button type="button" id="cancel-login-btn" class="cta-button secondary" style="flex: 1;">Cancel</button>
                            </div>
                        </form>

                        <div id="login-success" style="display: none; margin-top: 20px; padding: 16px; background: var(--smoke); border-left: 4px solid var(--rust);">
                            <p style="color: var(--chalk); font-weight: 600;">Login successful!</p>
                            <p style="color: var(--chalk-dim); font-size: 14px;">Redirecting to profile...</p>
                        </div>
                    </div>
                </div>
            `;

            // Add modal to page
            document.body.insertAdjacentHTML('beforeend', modalHtml);

            const modal = document.getElementById('login-modal');
            const loginForm = document.getElementById('login-form');
            const cancelBtn = document.getElementById('cancel-login-btn');
            const successMessage = document.getElementById('login-success');

            // Handle form submission
            if (loginForm) {
                loginForm.addEventListener('submit', function(event) {
                    event.preventDefault();

                    const usernameOrEmail = document.getElementById('login-username').value;
                    const password = document.getElementById('login-password').value;

                    // In a real application, you would send this to your backend
                    // For now, simulate login and redirect to profile page
                    console.log('Login attempt:', { usernameOrEmail, password });

                    // Show success message and hide form
                    loginForm.style.display = 'none';
                    successMessage.style.display = 'block';

                    // Clear form
                    loginForm.reset();

                    // Redirect to profile page after 2 seconds
                    setTimeout(() => {
                        modal.remove();
                        window.location.href = '/profile.html'; // Will create this page
                    }, 2000);
                });
            }

            // Handle cancel button
            if (cancelBtn) {
                cancelBtn.addEventListener('click', function() {
                    modal.remove();
                });
            }
        });
    }

    if (createAccountBtn) {
        createAccountBtn.addEventListener('click', function() {
            // Create a modal dialog for account creation
            const modalHtml = `
                <div id="account-modal" style="
                    position: fixed;
                    top: 0;
                    left: 0;
                    width: 100%;
                    height: 100%;
                    background: rgba(0, 0, 0, 0.8);
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    z-index: 1000;
                ">
                    <div style="
                        background: var(--concrete);
                        padding: 40px;
                        border-radius: 0;
                        border-top: 4px solid var(--rust);
                        border-bottom: 1px solid var(--steel);
                        max-width: 500px;
                        width: 90vw;
                        position: relative;
                        clip-path: polygon(0 0, calc(100% - 24px) 0, 100% 24px, 100% 100%, 0 100%);
                    ">
                        <h2 style="
                            font-family: 'Barlow Condensed', sans-serif;
                            font-weight: 900;
                            color: var(--chalk);
                            font-size: 28px;
                            margin-bottom: 12px;
                        ">Create Account</h2>
                        <p style="color: var(--chalk-dim); margin-bottom: 24px;">Fill out the form below to create your account</p>

                        <form id="account-form">
                            <label for="account-username">Username</label>
                            <input type="text" id="account-username" placeholder="Enter your username" required style="margin-bottom: 16px;">

                            <label for="account-email">Email Address</label>
                            <input type="email" id="account-email" placeholder="Enter your email" required style="margin-bottom: 16px;">

                            <label for="account-password">Password</label>
                            <input type="password" id="account-password" placeholder="Create a secure password (min 6 chars)" required style="margin-bottom: 16px;">

                            <label for="account-confirm-password">Confirm Password</label>
                            <input type="password" id="account-confirm-password" placeholder="Confirm your password" required style="margin-bottom: 24px;">

                            <div style="display: flex; gap: 12px;">
                                <button type="submit" class="cta-button" style="flex: 1;">Create Account</button>
                                <button type="button" id="cancel-account-btn" class="cta-button secondary" style="flex: 1;">Cancel</button>
                            </div>
                        </form>

                        <div id="account-success" style="display: none; margin-top: 20px; padding: 16px; background: var(--smoke); border-left: 4px solid var(--rust);">
                            <p style="color: var(--chalk); font-weight: 600;">Account created successfully!</p>
                        </div>
                    </div>
                </div>
            `;

            // Add modal to page
            document.body.insertAdjacentHTML('beforeend', modalHtml);

            const modal = document.getElementById('account-modal');
            const accountForm = document.getElementById('account-form');
            const cancelBtn = document.getElementById('cancel-account-btn');
            const successMessage = document.getElementById('account-success');

            // Handle form submission
            if (accountForm) {
                accountForm.addEventListener('submit', function(event) {
                    event.preventDefault();

                    const username = document.getElementById('account-username').value;
                    const email = document.getElementById('account-email').value;
                    const password = document.getElementById('account-password').value;
                    const confirmPassword = document.getElementById('account-confirm-password').value;

                    // Basic validation
                    if (password !== confirmPassword) {
                        alert('Passwords do not match!');
                        return;
                    }

                    if (password.length < 6) {
                        alert('Password must be at least 6 characters long!');
                        return;
                    }

                    const accountData = {
                        username: username,
                        email: email,
                        password: password
                    };

                    console.log('Creating account:', accountData);

                    // Make POST request to create account
                    fetch('/api/accounts', {
                        method: 'POST',
                        headers: {
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify(accountData)
                    })
                    .then(response => {
                        if (!response.ok) {
                            throw new Error(`HTTP error! status: ${response.status}`);
                        }
                        return response.text();
                    })
                    .then(data => {
                        console.log('Account created:', data);

                        // Show success message and hide form
                        accountForm.style.display = 'none';
                        successMessage.style.display = 'block';

                        // Clear form
                        accountForm.reset();

                        // Close modal after 2 seconds
                        setTimeout(() => {
                            modal.remove();
                            alert('Account created successfully!');
                        }, 2000);
                    })
                    .catch(error => {
                        console.error('Error:', error);
                        alert('Failed to create account: ' + error.message);
                    });
                });
            }

            // Handle cancel button
            if (cancelBtn) {
                cancelBtn.addEventListener('click', function() {
                    modal.remove();
                });
            }
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
// Authentication related functions
function initializeAuthButtons() {
    const loginBtn = document.getElementById('login-btn');
    const createAccountBtn = document.getElementById('create-account-btn');

    if (loginBtn) {
        loginBtn.addEventListener('click', showLoginModal);
    }

    if (createAccountBtn) {
        createAccountBtn.addEventListener('click', showRegistrationModal);
    }
}

function showLoginModal() {
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
        loginForm.addEventListener('submit', handleLoginSubmit);
    }

    // Handle cancel button
    if (cancelBtn) {
        cancelBtn.addEventListener('click', function() {
            modal.remove();
        });
    }
}

function showRegistrationModal() {
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
        accountForm.addEventListener('submit', handleRegistrationSubmit);
    }

    // Handle cancel button
    if (cancelBtn) {
        cancelBtn.addEventListener('click', function() {
            modal.remove();
        });
    }
}

function handleLoginSubmit(event) {
    event.preventDefault();

    const modal = document.getElementById('login-modal');
    const loginForm = document.getElementById('login-form');
    const successMessage = document.getElementById('login-success');

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
        window.location.href = '/profile'; // Will create this page
    }, 2000);
}

function handleRegistrationSubmit(event) {
    event.preventDefault();

    const modal = document.getElementById('account-modal');
    const accountForm = document.getElementById('account-form');
    const successMessage = document.getElementById('account-success');

    const username = document.getElementById('account-username').value;
    const email = document.getElementById('account-email').value;
    const password = document.getElementById('account-password').value;
    const confirmPassword = document.getElementById('account-confirm-password').value;

    try {
        validatePassword(password, confirmPassword);
    } catch (error) {
        alert(error.message);
        return;
    }

    const accountData = {
        username: username,
        email: email,
        password: password
    };

    console.log('Creating account:', accountData);

    createAccount(accountData)
        .then(data => {
            accountForm.style.display = 'none';
            successMessage.style.display = 'block';
            accountForm.reset();

            setTimeout(() => {
                modal.remove();
                alert('Account created successfully!');
            }, 2000);
        })
        .catch(error => {
            console.error('Error:', error);
            alert(error.message); // now shows "Username already exists"
        });
}

// Initialize auth on DOM ready
document.addEventListener('DOMContentLoaded', initializeAuthButtons);

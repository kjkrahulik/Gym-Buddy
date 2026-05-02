// API utility functions
function createAccount(accountData) {
    return fetch('/api/accounts', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(accountData)
    })
    .then(async response => {
        const text = await response.text();
        if (!response.ok) {
            throw new Error(text);
        }
        return text;
    });
}

function validatePassword(password, confirmPassword) {
    if (password !== confirmPassword) {
        throw new Error('Passwords do not match!');
    }

    if (password.length < 6) {
        throw new Error('Password must be at least 6 characters long!');
    }

    return true;
}

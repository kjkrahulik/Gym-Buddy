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

// Toast notification
function showToast(message, type = 'success') {
    const toast = document.createElement('div');
    toast.className = `toast toast-${type}`;
    toast.textContent = message;
    document.body.appendChild(toast);

    // Trigger animation
    setTimeout(() => toast.classList.add('show'), 10);

    // Auto remove
    setTimeout(() => {
        toast.classList.remove('show');
        setTimeout(() => toast.remove(), 300);
    }, 3000);
}

// Friend request functions
function sendFriendRequest(receiverId, button) {
    button.disabled = true;
    button.textContent = 'Sending...';

    fetch('/api/friend-request/send', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `receiverId=${receiverId}`
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            showToast('Friend request sent!', 'success');
            button.textContent = 'Request Sent';
            button.disabled = true;
        } else {
            showToast(data.message || 'Failed to send request', 'error');
            button.disabled = false;
            button.textContent = 'Send Friend Request';
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showToast('An error occurred', 'error');
        button.disabled = false;
        button.textContent = 'Send Friend Request';
    });
}

function acceptFriendRequest(requestId, element) {
    const container = element.closest('.friend-request-item');
    container.style.opacity = '0.6';
    element.disabled = true;

    fetch('/api/friend-request/accept', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `requestId=${requestId}`
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            showToast('Friend request accepted!', 'success');
            container.remove();
            // Optionally refresh the pending list
            refreshPendingRequests();
        } else {
            showToast(data.message || 'Failed to accept request', 'error');
            container.style.opacity = '1';
            element.disabled = false;
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showToast('An error occurred', 'error');
        container.style.opacity = '1';
        element.disabled = false;
    });
}

function declineFriendRequest(requestId, element) {
    const container = element.closest('.friend-request-item');
    container.style.opacity = '0.6';
    element.disabled = true;

    fetch('/api/friend-request/decline', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `requestId=${requestId}`
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            showToast('Friend request declined', 'success');
            container.remove();
            refreshPendingRequests();
        } else {
            showToast(data.message || 'Failed to decline request', 'error');
            container.style.opacity = '1';
            element.disabled = false;
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showToast('An error occurred', 'error');
        container.style.opacity = '1';
        element.disabled = false;
    });
}

function removeFriend(friendId, element) {
    if (!confirm('Are you sure you want to remove this friend?')) {
        return;
    }

    const container = element.closest('.friend-card');
    container.style.opacity = '0.6';
    element.disabled = true;

    fetch('/api/friend/remove', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `friendId=${friendId}`
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            showToast('Friend removed', 'success');
            container.remove();
            refreshFriendsList();
        } else {
            showToast(data.message || 'Failed to remove friend', 'error');
            container.style.opacity = '1';
            element.disabled = false;
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showToast('An error occurred', 'error');
        container.style.opacity = '1';
        element.disabled = false;
    });
}

function refreshFriendsList() {
    // Placeholder for future: could fetch updated list via API
    // For now, the page reload or item removal is enough
}

function refreshPendingRequests() {
    // Placeholder for future: could fetch updated pending requests via API
}

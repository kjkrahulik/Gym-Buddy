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
    console.log('acceptFriendRequest called with requestId:', requestId);

    const container = element.closest('.friend-request-item');
    if (!container) {
        console.error('Could not find friend-request-item container');
        showToast('Error: Could not find request element', 'error');
        return;
    }

    container.style.opacity = '0.6';
    element.disabled = true;

    const params = new URLSearchParams();
    params.append('requestId', requestId);

    fetch('/api/friend-request/accept', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: params.toString()
    })
    .then(response => {
        console.log('Response status:', response.status);
        if (!response.ok) {
            throw new Error(`HTTP ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        console.log('Response data:', data);
        if (data.success) {
            showToast('Friend request accepted!', 'success');
            container.remove();
            refreshPendingRequests();
        } else {
            showToast(data.message || 'Failed to accept request', 'error');
            container.style.opacity = '1';
            element.disabled = false;
        }
    })
    .catch(error => {
        console.error('Error accepting request:', error);
        showToast('Error: ' + error.message, 'error');
        container.style.opacity = '1';
        element.disabled = false;
    });
}

function declineFriendRequest(requestId, element) {
    console.log('declineFriendRequest called with requestId:', requestId);

    const container = element.closest('.friend-request-item');
    if (!container) {
        console.error('Could not find friend-request-item container');
        showToast('Error: Could not find request element', 'error');
        return;
    }

    container.style.opacity = '0.6';
    element.disabled = true;

    const params = new URLSearchParams();
    params.append('requestId', requestId);

    fetch('/api/friend-request/decline', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: params.toString()
    })
    .then(response => {
        console.log('Response status:', response.status);
        if (!response.ok) {
            throw new Error(`HTTP ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        console.log('Response data:', data);
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
        console.error('Error declining request:', error);
        showToast('Error: ' + error.message, 'error');
        container.style.opacity = '1';
        element.disabled = false;
    });
}

function removeFriend(friendId, element) {
    console.log('removeFriend called with friendId:', friendId, 'element:', element);

    if (!confirm('Are you sure you want to remove this friend?')) {
        console.log('User cancelled removal');
        return;
    }

    const container = element.closest('.friend-card');
    if (!container) {
        console.error('Could not find friend-card container');
        showToast('Error: Could not find friend element', 'error');
        return;
    }

    container.style.opacity = '0.6';
    element.disabled = true;

    const params = new URLSearchParams();
    params.append('friendId', friendId);

    console.log('Sending request to /api/friend/remove with params:', params.toString());

    fetch('/api/friend/remove', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: params.toString()
    })
    .then(response => {
        console.log('Response status:', response.status);
        if (!response.ok) {
            throw new Error(`HTTP ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        console.log('Response data:', data);
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
        console.error('Error removing friend:', error);
        showToast('Error: ' + error.message, 'error');
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

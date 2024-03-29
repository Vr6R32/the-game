function createAvatarWrapper(userAvatarUrl) {
    const avatarWrapper = document.createElement('div');
    avatarWrapper.classList.add('avatar-wrapper');
    const avatarImg = document.createElement('img');
    avatarImg.classList.add('user-avatar');
    avatarImg.src = userAvatarUrl;
    avatarWrapper.appendChild(avatarImg);
    return avatarWrapper;
}


function createUserDetailsDiv(username, userEmail) {
    const detailsDiv = document.createElement('div');
    detailsDiv.classList.add('conversation-details');
    const nameSpan = document.createElement('span');
    nameSpan.classList.add('user-name');
    nameSpan.textContent = username;
    const emailSpan = document.createElement('span');
    emailSpan.classList.add('email-address');
    emailSpan.textContent = userEmail;
    detailsDiv.appendChild(nameSpan);
    detailsDiv.appendChild(emailSpan);
    return detailsDiv;
}


function createStatusDot(userStatus) {
    const statusDot = document.createElement('span');
    statusDot.classList.add('status-dot');
    if (userStatus === "OFFLINE") {
        statusDot.classList.add('offline');
    } else if (userStatus === "ONLINE") {
        statusDot.classList.add('online');
    }
    return statusDot;
}


function createActivityStatusSpan(userStatus, userLogoutDate) {
    const activitySpan = document.createElement('span');
    activitySpan.classList.add('activity-status');
    if (userStatus === "OFFLINE") {
        activitySpan.textContent = getActivityElapsedTime(userLogoutDate);
    }
    return activitySpan;
}

function setMessageOwnerClass(message, loggedUserId, newMessage) {
    if (message.senderId === loggedUserId) {
        newMessage.classList.add('message-sent');
    } else {
        newMessage.classList.add('message-received');
    }
}
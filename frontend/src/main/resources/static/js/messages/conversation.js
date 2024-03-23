let currentConversationId;
let conversationsDivs = [];
document.addEventListener("DOMContentLoaded", function () {
    
    let contactsListContainer = document.getElementById("contactsList");
    fetchConversations().then(conversationsDivs => {
        Object.values(conversationsDivs).forEach(div => {
            contactsListContainer.appendChild(div);
        });
    });
    
    let searchInput = document.getElementById("searchInput");
    searchInput.addEventListener("input", function() {
        let searchTerm = searchInput.value.toLowerCase();
        contactsListContainer.innerHTML = "";
        adjustLinesInterval(100, 1000);
        Object.values(conversationsDivs).forEach(div => {
            if (div.textContent.toLowerCase().includes(searchTerm)) {
                contactsListContainer.appendChild(div);
            }
        });
    });
});

function fetchConversations() {
    return fetch('/api/v1/conversation')
        .then(response => response.json())
        .then(data => {
            let conversationMessagesContainer = document.getElementById("conversationMessages");

            conversationMessagesContainer.addEventListener('scroll', () => {
                const atTop = conversationMessagesContainer.scrollTop === 0;
                if (atTop) {
                    conversationMessagesContainer.classList.add('pull-down-refresh');
                    conversationMessagesContainer.addEventListener('animationend', () => {
                        // loadMoreMessagesAtTop();
                        conversationMessagesContainer.scrollTop = 1;
                        conversationMessagesContainer.classList.remove('pull-down-refresh');
                    }, { once: true });
                }
            });
            data.forEach(conversation => {
                createConversationDiv(conversation);
            });
            return conversationsDivs;
        });
}


function getActivityElapsedTime(logoutDateString) {
    const logoutDate = new Date(logoutDateString);
    const now = new Date();

    const nowUtc = new Date(Date.UTC(now.getFullYear(), now.getMonth(), now.getDate(), now.getHours(), now.getMinutes(), now.getSeconds()));

    const diffMs = nowUtc - logoutDate;

    const diffMins = Math.max(0, Math.floor(diffMs / 60000));
    const diffHrs = Math.max(0, Math.floor(diffMins / 60));
    const diffDays = Math.max(0, Math.floor(diffHrs / 24));

    if (diffDays > 0) {
        return `left ${diffDays} day${diffDays > 1 ? 's' : ''} ago`;
    } else if (diffHrs > 0) {
        return `left ${diffHrs} hour${diffHrs > 1 ? 's' : ''} ago`;
    } else if (diffMins > 0) {
        return `left ${diffMins} minute${diffMins > 1 ? 's' : ''} ago`;
    } else {
        return `left just now`;
    }
}


function createConversationDiv(conversation) {
    const conversationDiv = document.createElement('div');
    conversationDiv.classList.add('conversation-container');
    conversationDiv.id = conversation.id;


    const avatarWrapper = document.createElement('div');
    avatarWrapper.classList.add('avatar-wrapper');

    const avatarImg = document.createElement('img');
    avatarImg.classList.add('user-avatar');
    avatarImg.src = conversation.userAvatarUrl;
    avatarWrapper.appendChild(avatarImg);

    const statusDot = document.createElement('span');
    statusDot.classList.add('status-dot');

    const detailsDiv = document.createElement('div');
    // detailsDiv.classList.add('conversation-details');

    const nameSpan = document.createElement('span');
    nameSpan.classList.add('user-name');
    nameSpan.textContent = "Vincent Porter";

    const emailSpan = document.createElement('span');
    emailSpan.classList.add('email-address');
    emailSpan.textContent = conversation.userEmail;

    detailsDiv.appendChild(nameSpan);
    detailsDiv.appendChild(emailSpan);

    if (conversation.userStatus === "OFFLINE") {
        statusDot.classList.add('offline');
        const activitySpan = document.createElement('span');
        activitySpan.classList.add('activity-status');
        activitySpan.textContent = getActivityElapsedTime(conversation.userLogoutDate);

        detailsDiv.appendChild(activitySpan);
    } else if (conversation.userStatus === "ONLINE") {
        statusDot.classList.add('online');
    }
    avatarWrapper.appendChild(statusDot);


    conversationDiv.appendChild(avatarWrapper);
    conversationDiv.appendChild(detailsDiv);

    conversationDiv.onclick = () => {
        loadMessages(conversation.id).then(messages => {
            adjustLinesInterval(100, 1000);
            const conversationMessagesContainer = document.getElementById("conversationMessages");
            conversationMessagesContainer.innerHTML = "";
            conversationMessagesContainer.append(...messages);
            currentConversationId = conversation.id;
            conversationMessagesContainer.scrollTop = conversationMessagesContainer.scrollHeight;
        }).catch(error => {
            console.error('Error loading messages:', error);
        });
    };

    conversationsDivs[conversation.id] = conversationDiv;
    return conversationDiv;
}


function loadMessages(conversationId) {
    return fetch('/api/v1/conversation/messages/' + conversationId)
        .then(response => response.json())
        .then(data => {
            let loggedUser = document.getElementById('username').value;
            setMessageInputListener();

            return data.map(message => {
                const div = document.createElement('div');
                div.textContent = message.payload;
                div.classList.add('conversation-message');
                if (message.sender === loggedUser) {
                    div.classList.add('message-sent');
                } else {
                    div.classList.add('message-received');
                }
                return div;
            });
        });
}

function setMessageInputListener() {
    let messageInput = document.getElementById("message");
    messageInput.addEventListener("input", function () {
        adjustLinesInterval(100, 1000);
    });
}
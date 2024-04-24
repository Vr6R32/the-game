function updateConversationStatus(conversationId, conversationStatus) {
    conversationsDivs[conversationId].conversationStatus = conversationStatus;
    conversationsDivs[conversationId].acceptFlag = false;
    appendSpecifiedTypeConversations(currentContactsTab);
}

function removeAcceptationDiv(acceptDiv, conversationContainer) {
    acceptDiv.classList.add('fade-out-remove');
    setTimeout(() => {
        acceptDiv.style.display = 'none';
        conversationContainer.style.height = '69%';
    }, 500);
}

function submitConversationStatusUpdateEvent(acceptDiv,conversationId, isAccepted, conversationContainer) {

    const conversationStatusUpdateRequest = {
        conversationId: conversationId,
        isAccepted: isAccepted
    };

    fetch('api/v1/conversations/status/update', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(conversationStatusUpdateRequest)
    })
        .then(response => {
            return response.json();
        })
        .then(data => {
            updateConversationStatus(conversationId,data.conversationStatus);
            removeAcceptationDiv(acceptDiv, conversationContainer);
        })
        .catch(error => {
            console.error('Error:', error);
        });
}


function updateStatusElements(statusDot, activityStatusSpan, status, eventDate) {
    statusDot.classList.remove('online', 'offline');
    statusDot.classList.add(status.toLowerCase());
    activityStatusSpan.textContent = status === 'OFFLINE' ? getActivityElapsedTime(eventDate) : '';
}

function handleFriendConversationSessionUpdate(payload) {
    let conversationId = payload.conversationId;
    const conversationDiv = conversationsDivs[conversationId].div;

    updateStatusElements(
        conversationDiv.querySelector('.status-dot'),
        conversationDiv.querySelector('.activity-status'),
        payload.status,
        payload.eventDate
    );

    conversationsDivs[conversationId].userStatus = payload.status;
    if (payload.status === "OFFLINE") {
        conversationsDivs[conversationId].userLogoutDate = payload.eventDate;
    }

    if (currentConversationId === conversationId) {
        const openConversationDiv = document.getElementById('secondUserConversationInfo');
        if (openConversationDiv) {
            updateStatusElements(
                openConversationDiv.querySelector('.status-dot'),
                openConversationDiv.querySelector('.activity-status'),
                payload.status,
                payload.eventDate
            );
        }
    }
}

function handleNewConversationInvitation(payload) {

    let newConversation = payload;
    // invitationsCount++;

    updateContactButtonValues(document.getElementById('normal-contacts'),document.getElementById('invite-contacts'));
    createConversationDiv(newConversation);
    if(currentContactsTab==="INVITATION") appendSpecifiedTypeConversations(currentContactsTab);

}

function handleNotificationEvent(notificationMessage) {
    if (notificationMessage.type === 'FRIEND_SESSION_UPDATE') {
        handleFriendConversationSessionUpdate(notificationMessage.payload);
    }
    if (notificationMessage.type === 'CONVERSATION_INVITATION') {
        handleNewConversationInvitation(notificationMessage.payload);
    }
    if (notificationMessage.type === 'CONVERSATION_STATUS_UPDATE') {
        updateConversationStatus(notificationMessage.payload.conversationId,notificationMessage.payload.status);
    }
}

function handleNewMessageEvent(message) {
    const conversationMessages = document.getElementById('conversationMessages');

    let newMessage = document.createElement('div');
    newMessage.classList.add('conversation-message');
    newMessage.textContent = `${message.payload}`;

    const userId = loggedUser.id;

    setMessageOwnerClass(message, userId, newMessage);

    let conversationDiv = conversationsDivs[message.conversationId].div;
    conversationsDivs[message.conversationId].lastMessageDate = message.messageDate;

    if (conversationDiv) {
        const conversationsList = conversationDiv.parentNode;

        if (conversationsList) {
            conversationsList.prepend(conversationDiv);
        }

        if (currentConversationId !== message.conversationId) {
            conversationDiv.classList.add('unread-message');
        }
    }


    if (currentConversationId === message.conversationId) {
        const isScrolledToBottom = conversationMessages.scrollHeight - conversationMessages.clientHeight <= conversationMessages.scrollTop + 1;
        conversationMessages.append(newMessage);

        if (isScrolledToBottom) {
            conversationMessages.scrollTop = conversationMessages.scrollHeight;
        }
    }
}
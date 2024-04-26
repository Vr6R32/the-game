let currentConversationId;
let acceptedCount = 0;
let invitationsCount = 0;
let conversationsDivs = [];
let currentContactsTab = 'ACCEPTED';


function checkIfUserAlreadyLogged() {
    fetch('/api/v1/users')
        .then(response => {
            if (response.status === 403) {
                createSelfWriterLandingPage(welcomeText);
                return;
            }
            return response.json();
        })
        .then(data => {
            if (!data) return;
            loggedUser = data;
            setTimeout(createContactsContainer,300);
            stabilizeWebSocketConnection();
        })
        .catch(error => {
            console.log(error);
        });
}
document.addEventListener("DOMContentLoaded", function () {

    createMainContainer();

    setTimeout(paralaxHover,1001);

    checkIfUserAlreadyLogged();

});




function createMainContainer() {
    let chatWrapper = document.getElementById('chatWrapper');
    let messageContainer = document.createElement('div');
    messageContainer.setAttribute('id', 'messageContainer')
    chatWrapper.appendChild(messageContainer);
}

function createNewContactDiv() {
    let messageContainer = document.getElementById('messageContainer');
    messageContainer.innerHTML = '';

    const form = document.createElement('form');

    const loginFormWrapper = document.createElement('div');
    loginFormWrapper.classList.add('form-wrapper');
    loginFormWrapper.style.width = '100%';
    loginFormWrapper.style.height = '100%';
    // loginFormWrapper.style.marginTop = '15%';

    const invitationHeading = document.createElement('h2');
    invitationHeading.textContent = 'Send an Invitation for';
    loginFormWrapper.appendChild(invitationHeading);

    const {divInput: divEmail, inputElement: inputEmail} =
        createFormInput('userEmail', 'text');
    createFormLabel(divEmail, inputEmail, form,'Email');

    const contactDetailsHeading = document.createElement('h2');
    contactDetailsHeading.textContent = 'Save as';
    form.appendChild(contactDetailsHeading);

    const {divInput: divUsername, inputElement: inputUsername} =
        createFormInput('username', 'text');
    createFormLabel(divUsername, inputUsername, form,'Username');

    createFormButton(form, submitNewContactInvitation, 'invite');


    loginFormWrapper.appendChild(form);
    messageContainer.appendChild(loginFormWrapper);

}

function handleNewConversationResponse(conversationResponse) {



    let messageContainer = document.getElementById('messageContainer');
    messageContainer.innerHTML = '';


    const responseHeader = document.createElement('h2');
    responseHeader.textContent = conversationResponse.message;
    responseHeader.style.marginTop = '40%';
    messageContainer.appendChild(responseHeader);


    let newConversation = conversationResponse.conversation;
    invitationsCount++;
    createConversationDiv(newConversation);

    updateContactButtonValues(document.getElementById('normal-contacts'),document.getElementById('invite-contacts'));

    if(currentContactsTab==="INVITATION") appendSpecifiedTypeConversations(currentContactsTab);

}

function submitNewContactInvitation() {
    fetch('api/v1/conversations', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            secondUserEmail: document.getElementById('userEmail').value,
            secondUserContactName: document.getElementById('username').value
        })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.json();
        })
        .then(data => {
            handleNewConversationResponse(data);
        })
        .catch((error) => {
            console.error('Error:', error);
        });
}

function updateContactButtonValues(normalContactsButton, invitesContactsButton) {
    acceptedCount = 0;
    invitationsCount = 0;
    Object.values(conversationsDivs).forEach(conversationInfo => {
        if (conversationInfo.conversationStatus === 'ACCEPTED') {
            acceptedCount++;
        } else if (conversationInfo.conversationStatus === 'INVITATION') {
            invitationsCount++;
        }
    });
    normalContactsButton.textContent = 'Contacts (' + acceptedCount + ')';
    invitesContactsButton.textContent = 'Invites (' + invitationsCount + ')';
}

function sortConversations(acceptedConversations) {
    acceptedConversations.sort((a, b) => {
        const dateA = a.lastMessageDate ? new Date(a.lastMessageDate) : null;
        const dateB = b.lastMessageDate ? new Date(b.lastMessageDate) : null;

        if (dateA === null && dateB === null) {
            return 0;
        } else if (dateA === null) {
            return 1;
        } else if (dateB === null) {
            return -1;
        } else {
            return dateB - dateA;
        }
    });
}

function appendSpecifiedTypeConversations(statusType) {

    const acceptedConversations = Object.values(conversationsDivs).filter(div => div.conversationStatus === statusType);

    updateContactButtonValues(document.getElementById('normal-contacts'),document.getElementById('invite-contacts'));
    sortConversations(acceptedConversations);


    let contactsList = document.getElementById('contactsList');
    contactsList.innerHTML = '';
    currentContactsTab = statusType;
    acceptedConversations.forEach(div => {
        contactsList.appendChild(div.div);
    });


    let searchInput = document.getElementById("searchInput");
    if (searchInput && searchInput.value.trim() !== '') {
        let searchTerm = searchInput.value.toLowerCase();
        contactsList.innerHTML = "";

        let filteredConversations = Object.values(conversationsDivs).filter(div =>
            div.conversationStatus === currentContactsTab && div.div.textContent.toLowerCase().includes(searchTerm)
        );

        filteredConversations.forEach(div => {
            contactsList.appendChild(div.div);
        });
    }

}

function createContactsContainer() {


    document.getElementById('messageContainer').innerHTML = '';

    let contactsContainer = document.createElement('div');
    contactsContainer.id = 'contactsContainer';

    let contactsSearch = document.createElement('div');
    contactsSearch.id = 'contactsSearch';

    let inputGroup = document.createElement('div');
    inputGroup.className = 'input-group';

    let label = document.createElement('label');
    label.className = 'input-group__label';
    label.setAttribute('for', 'searchInput');
    label.textContent = 'Search';

    let input = document.createElement('input');
    input.type = 'text';
    input.id = 'searchInput';
    input.className = 'input-group__input';
    input.placeholder = 'Loop through contacts...';

    inputGroup.appendChild(label);
    inputGroup.appendChild(input);
    contactsSearch.appendChild(inputGroup);

    let contactsList = document.createElement('div');
    contactsList.id = 'contactsList';


    fetchConversations().then(()  => {
        appendSpecifiedTypeConversations(currentContactsTab);
    });



    let contactsPanel = document.createElement('div');
    contactsPanel.id = 'contactsPanel'


    let acceptedContactsButton = document.createElement('button');
    acceptedContactsButton.className = 'space-btn';
    acceptedContactsButton.id = 'normal-contacts';
    acceptedContactsButton.style.whiteSpace = 'nowrap';
    acceptedContactsButton.onclick = () => appendSpecifiedTypeConversations('ACCEPTED');


    let invitesContactsButton = document.createElement('button');
    invitesContactsButton.className = 'space-btn';
    invitesContactsButton.id = 'invite-contacts';
    invitesContactsButton.style.whiteSpace = 'nowrap';
    invitesContactsButton.onclick = () => appendSpecifiedTypeConversations('INVITATION');



    contactsPanel.appendChild(acceptedContactsButton);
    contactsPanel.appendChild(invitesContactsButton);


    let createContact = document.createElement('div');
    createContact.id = 'createContact';

    let createContactButton = document.createElement('button');
    createContactButton.className = 'glowing-btn';
    createContactButton.id = 'createContactButton';


    let faultyLetter = document.createElement('span');
    faultyLetter.className = 'faulty-letter';
    faultyLetter.textContent = '🞧';


    let buttonSpan = document.createElement('span');
    buttonSpan.className = 'glowing-txt';
    buttonSpan.appendChild(faultyLetter);
    buttonSpan.append('CONTACT');
    createContactButton.appendChild(buttonSpan);
    createContact.appendChild(createContactButton);

    contactsContainer.appendChild(contactsSearch);
    contactsContainer.appendChild(contactsPanel);
    contactsContainer.appendChild(contactsList);
    contactsContainer.appendChild(createContact);

    let contactsWrapperContainer = document.createElement('div');
    contactsWrapperContainer.setAttribute('id', 'contactsWrapper');
    contactsWrapperContainer.appendChild(contactsContainer);


    let chatWrapper = document.getElementById('chatWrapper');

    let paralaxHoverWrapper = document.getElementById('paralax-hover');
    paralaxHoverWrapper.insertBefore(contactsWrapperContainer,chatWrapper);




    let searchInput = document.getElementById("searchInput");
    searchInput.addEventListener("input", function() {
        let searchTerm = searchInput.value.toLowerCase();
        contactsList.innerHTML = "";
        adjustLinesInterval(100, 1000);

        let filteredConversations = Object.values(conversationsDivs).filter(div =>
            div.conversationStatus === currentContactsTab && div.div.textContent.toLowerCase().includes(searchTerm)
        );

        filteredConversations.forEach(div => {
            contactsList.appendChild(div.div);
        });
    });

    createContactButton.addEventListener('click', function() {
        currentConversationId = null;
        createNewContactDiv();
    });


    setTimeout(removeContactsContainerAnimation, 1000);
}

function removeContactsContainerAnimation(){
    let contactsContainer = document.getElementById('contactsContainer');
    contactsContainer.style.animation = 'none'
}

function fetchConversations() {
    return fetch('/api/v1/conversations')
        .then(response => response.json())
        .then(data => {
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

function updateLogoutTimes() {
    Object.keys(conversationsDivs).forEach(conversationId => {
        const conversationInfo = conversationsDivs[conversationId];
        if (conversationInfo.userStatus === 'OFFLINE' && conversationInfo.userLogoutDate) {
            const elapsedTime = getActivityElapsedTime(conversationInfo.userLogoutDate);
            const conversationDiv = conversationInfo.div;
            const elapsedTimeElement = conversationDiv.querySelector('.activity-status');
            if (elapsedTimeElement) {
                elapsedTimeElement.textContent = elapsedTime;
            }
            if(currentConversationId === conversationId) {
                const openConversationDiv = document.getElementById('secondUserConversationInfo');
                let activityStatusOpenedConversation = openConversationDiv.querySelector('.activity-status');
                activityStatusOpenedConversation.textContent = elapsedTime;
            }
        }
    });
}

setInterval(updateLogoutTimes, 61000);

function handleConversationAwaitingAccept(conversationId, messageContainer, conversationMessagesContainer) {
    if (conversationsDivs[conversationId].acceptFlag === true) {
        let acceptConversationDiv = document.createElement('div');
        acceptConversationDiv.setAttribute('id', 'acceptConversationDiv');

        let acceptationText = document.createElement('span');
        acceptationText.textContent = 'Accept that conversation?'
        acceptationText.style.marginRight = '10px';
        acceptationText.style.fontSize = '1.5vh';

        let acceptRejectButtons = document.createElement('div');
        acceptRejectButtons.className = 'buttons';

        let acceptButton = document.createElement('button');
        acceptButton.textContent = 'YES';
        acceptButton.addEventListener('click', function() {
            submitConversationStatusUpdateEvent(acceptConversationDiv,conversationId,true,conversationMessagesContainer);
        });

        let rejectButton = document.createElement('button');
        rejectButton.textContent = 'NO';
        rejectButton.addEventListener('click', function() {
            submitConversationStatusUpdateEvent(acceptConversationDiv,conversationId,false,conversationMessagesContainer);
        });

        acceptRejectButtons.appendChild(acceptButton);
        acceptRejectButtons.appendChild(rejectButton);

        acceptConversationDiv.appendChild(acceptationText);
        acceptConversationDiv.appendChild(acceptRejectButtons);


        messageContainer.appendChild(acceptConversationDiv);
        conversationMessagesContainer.style.height = '64%';
    }
}

function createSpecifiedConversationMessagesBox(conversation, conversationDiv) {
    loadMessages(conversation.id).then(messages => {

        if (conversationDiv.classList.contains('unread-message')) {
            conversationDiv.classList.remove('unread-message');
        }

        adjustLinesInterval(100, 1000);
        let messageContainer = document.getElementById('messageContainer');
        messageContainer.innerHTML = '';

        let conversationSecondUserDiv = document.createElement('div');
        conversationSecondUserDiv.setAttribute('id', 'secondUserConversationInfo');

        const avatarWrapper = createAvatarWrapper(conversation.userAvatarUrl);
        const detailsDiv = createUserDetailsDiv(conversation.username, conversation.userEmail);

        let userStatus = conversationsDivs[conversation.id].userStatus;

        const statusDot = createStatusDot(userStatus);

        const conversationInfo = conversationsDivs[conversation.id];
        const activitySpan = createActivityStatusSpan(conversationInfo.userStatus, conversationInfo.userLogoutDate);

        detailsDiv.style.width = '50%';
        detailsDiv.appendChild(activitySpan);
        avatarWrapper.style.width = '10%'
        avatarWrapper.appendChild(statusDot);

        conversationSecondUserDiv.appendChild(avatarWrapper);
        conversationSecondUserDiv.appendChild(detailsDiv);
        messageContainer.appendChild(conversationSecondUserDiv);


        let conversationMessagesContainer = document.createElement("div");
        conversationMessagesContainer.setAttribute('id', 'conversationMessages');
        conversationMessagesContainer.addEventListener('scroll', () => {
            let atTop = conversationMessagesContainer.scrollTop === 0;
            if (atTop) {
                conversationMessagesContainer.classList.add('pull-down-refresh');
                conversationMessagesContainer.addEventListener('animationend', () => {
                    // loadMoreMessagesAtTop();
                    conversationMessagesContainer.scrollTop = 1;
                    conversationMessagesContainer.classList.remove('pull-down-refresh');
                }, {once: true});
            }
        });
        conversationMessagesContainer.innerHTML = "";
        conversationMessagesContainer.append(...messages);
        currentConversationId = conversation.id;

        handleConversationAwaitingAccept(currentConversationId, messageContainer, conversationMessagesContainer);

        messageContainer.appendChild(conversationMessagesContainer);

        conversationMessagesContainer.scrollTop = conversationMessagesContainer.scrollHeight;

        createLinuxInputMessageDiv(messageContainer);

    }).catch(error => {
        console.error('Error loading messages:', error);
    });
}

function createConversationDiv(conversation) {

    const conversationDiv = document.createElement('div');
    conversationDiv.classList.add('conversation-container');
    conversationDiv.id = conversation.id;

    if(conversation.isUnread === true) {
        conversationDiv.classList.add('unread-message');
    }

    const avatarWrapper = createAvatarWrapper(conversation.userAvatarUrl);
    const detailsDiv = createUserDetailsDiv(conversation.username, conversation.userEmail);
    const statusDot = createStatusDot(conversation.userStatus);
    const activitySpan = createActivityStatusSpan(conversation.userStatus, conversation.userLogoutDate);

    avatarWrapper.appendChild(statusDot);
    detailsDiv.appendChild(activitySpan);

    conversationDiv.appendChild(avatarWrapper);
    conversationDiv.appendChild(detailsDiv);

    conversationDiv.onclick = () => {
        createSpecifiedConversationMessagesBox(conversation, conversationDiv);
    }

    conversationsDivs[conversation.id] = {
        div: conversationDiv,
        lastMessageDate: conversation.lastMessageDate,
        userStatus: conversation.userStatus,
        userLogoutDate: conversation.userLogoutDate,
        conversationStatus: conversation.status,
        acceptFlag: conversation.awaitAcceptFlag
    };

    return conversationDiv;
}

function loadMessages(conversationId) {
    return fetch('/api/v1/conversations/messages/' + conversationId)
        .then(response => response.json())
        .then(data => {
            return data.map(message => {
                const newMessage = document.createElement('div');
                newMessage.textContent = message.payload;
                newMessage.classList.add('conversation-message');
                setMessageOwnerClass(message, loggedUser.id, newMessage);
                return newMessage;
            });
        });
}

let currentConversationId;
let conversationsDivs = [];


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
            setTimeout(createContactsContainer,1000);
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



function createSelfWriterLandingPage() {

    let messageContainer = document.getElementById('messageContainer');
    messageContainer.innerHTML = '';
    let typeWriterWelcomeContainer = document.createElement('div');
    typeWriterWelcomeContainer.setAttribute('id', 'typewriter');

    let paragraphQuote = document.createElement('p');
    paragraphQuote.setAttribute('id', 'quote');
    typeWriterWelcomeContainer.appendChild(paragraphQuote);

    let indexButtonsWrapper = document.createElement('div');
    indexButtonsWrapper.className = 'index-button-wrapper';

    const loginButton = document.createElement('button');

    loginButton.innerHTML = '<span></span><span></span><span></span><span></span>Login';
    loginButton.onclick = createLoginForm;

    indexButtonsWrapper.appendChild(loginButton);


    const registerButton = document.createElement('button');
    registerButton.innerHTML = '<span></span><span></span><span></span><span></span>Register';
    registerButton.onclick;

    indexButtonsWrapper.appendChild(registerButton);

    typeWriterWelcomeContainer.appendChild(indexButtonsWrapper);
    messageContainer.appendChild(typeWriterWelcomeContainer);
    selfWriter();
}



function createNewContactDiv() {
    let messageContainer = document.getElementById('messageContainer');
    messageContainer.innerHTML = '';

    const form = document.createElement('form');

    const loginBox = document.createElement('div');
    loginBox.setAttribute('id', 'form-wrapper');

    const invitationHeading = document.createElement('h2');
    invitationHeading.textContent = 'Send an Invitation for';
    loginBox.appendChild(invitationHeading);

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


    loginBox.appendChild(form);
    messageContainer.appendChild(loginBox);

}

function submitNewContactInvitation() {

    let newContactEmail = document.getElementById('userEmail').value;
    let newContactName = document.getElementById('username').value;

    console.log(newContactEmail);
    console.log(newContactName);
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
    input.placeholder = 'Search for contacts';

    inputGroup.appendChild(label);
    inputGroup.appendChild(input);
    contactsSearch.appendChild(inputGroup);

    let contactsList = document.createElement('div');
    contactsList.id = 'contactsList';

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
    contactsContainer.appendChild(contactsList);
    contactsContainer.appendChild(createContact);

    let contactsWrapperContainer = document.createElement('div');
    contactsWrapperContainer.setAttribute('id', 'contactsWrapper');
    contactsWrapperContainer.appendChild(contactsContainer);


    let chatWrapper = document.getElementById('chatWrapper');

    let paralaxHoverWrapper = document.getElementById('paralax-hover');
    paralaxHoverWrapper.insertBefore(contactsWrapperContainer,chatWrapper);


    fetchConversations().then(conversationsDivs => {
        Object.values(conversationsDivs).forEach(div => {
            contactsList.appendChild(div.div);
        });
    });

    let searchInput = document.getElementById("searchInput");
    searchInput.addEventListener("input", function() {
        let searchTerm = searchInput.value.toLowerCase();
        contactsList.innerHTML = "";
        adjustLinesInterval(100, 1000);
        Object.values(conversationsDivs).forEach(div => {
            if (div.div.textContent.toLowerCase().includes(searchTerm)) {
                contactsList.appendChild(div.div);
            }
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
            const sortedData = data.sort((a, b) => new Date(b.lastMessageDate) - new Date(a.lastMessageDate));
            sortedData.forEach(conversation => {
                createConversationDiv(conversation);
            });
            return conversationsDivs;
        });
}


function getActivityElapsedTime(logoutDateString) {
    const logoutDate = new Date(logoutDateString);
    const now = new Date();

    const nowUtc = new Date(Date.UTC(now.getFullYear(), now.getMonth(), now.getDate(), now.getHours(), now.getMinutes(), now.getSeconds()));

    console.log(logoutDate);
    console.log(nowUtc);

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

        if (conversationInfo.status === 'OFFLINE' && conversationInfo.userLogoutDate) {
            const elapsedTime = getActivityElapsedTime(conversationInfo.userLogoutDate);
            const conversationDiv = conversationInfo.div;
            const elapsedTimeElement = conversationDiv.querySelector('.activity-status');
            if (elapsedTimeElement) {
                elapsedTimeElement.textContent = elapsedTime;
            }
        }
    });
}

setInterval(updateLogoutTimes, 61000);

function createConversationDiv(conversation) {

    const conversationDiv = document.createElement('div');
    conversationDiv.classList.add('conversation-container');
    conversationDiv.id = conversation.id;

    if(conversation.isUnread === true) {
        conversationDiv.classList.add('unread-message');
    }


    const avatarWrapper = document.createElement('div');
    avatarWrapper.classList.add('avatar-wrapper');

    const avatarImg = document.createElement('img');
    avatarImg.classList.add('user-avatar');
    avatarImg.src = conversation.userAvatarUrl;
    avatarWrapper.appendChild(avatarImg);

    const statusDot = document.createElement('span');
    statusDot.classList.add('status-dot');

    const detailsDiv = document.createElement('div');
    detailsDiv.classList.add('conversation-details');

    const nameSpan = document.createElement('span');
    nameSpan.classList.add('user-name');
    nameSpan.textContent = "Vincent Porter";

    const emailSpan = document.createElement('span');
    emailSpan.classList.add('email-address');
    emailSpan.textContent = conversation.userEmail;

    detailsDiv.appendChild(nameSpan);
    detailsDiv.appendChild(emailSpan);

    const activitySpan = document.createElement('span');
    activitySpan.classList.add('activity-status');

    if (conversation.userStatus === "OFFLINE") {
        statusDot.classList.add('offline');
        activitySpan.textContent = getActivityElapsedTime(conversation.userLogoutDate);
    } else if (conversation.userStatus === "ONLINE") {
        activitySpan.textContent = '';
        statusDot.classList.add('online');
    }

    detailsDiv.appendChild(activitySpan);
    avatarWrapper.appendChild(statusDot);


    conversationDiv.appendChild(avatarWrapper);
    conversationDiv.appendChild(detailsDiv);

    conversationDiv.onclick = () => {
        loadMessages(conversation.id).then(messages => {

            if (conversationDiv.classList.contains('unread-message')) {
                conversationDiv.classList.remove('unread-message');
            }

            adjustLinesInterval(100, 1000);
            let messageContainer = document.getElementById('messageContainer');
            messageContainer.innerHTML = '';


            let conversationSecondUserDiv = document.createElement('div');
            conversationSecondUserDiv.setAttribute('id', 'secondUserConversationInfo');




/////////////////////////////////////////////////////////////////////////////////////
            //TODO NEED TO BE REFACTORED TO AVOID CODE DUPLICATION




            const avatarWrapper = document.createElement('div');
            avatarWrapper.classList.add('avatar-wrapper');
            avatarWrapper.style.width = '10%'

            const avatarImg = document.createElement('img');
            avatarImg.classList.add('user-avatar');
            avatarImg.src = conversation.userAvatarUrl;
            avatarWrapper.appendChild(avatarImg);

            const statusDot = document.createElement('span');
            statusDot.classList.add('status-dot');

            const detailsDiv = document.createElement('div');
            detailsDiv.classList.add('conversation-details');
            detailsDiv.style.width = '50%';

            const nameSpan = document.createElement('span');
            nameSpan.classList.add('user-name');
            nameSpan.textContent = "Vincent Porter";

            const emailSpan = document.createElement('span');
            emailSpan.classList.add('email-address');
            emailSpan.textContent = conversation.userEmail;

            detailsDiv.appendChild(nameSpan);
            detailsDiv.appendChild(emailSpan);

            const activitySpan = document.createElement('span');
            activitySpan.classList.add('activity-status');

            if (conversation.userStatus === "OFFLINE") {
                statusDot.classList.add('offline');
                activitySpan.textContent = getActivityElapsedTime(conversation.userLogoutDate);
            } else if (conversation.userStatus === "ONLINE") {
                activitySpan.textContent = '';
                statusDot.classList.add('online');
            }

            detailsDiv.appendChild(activitySpan);
            avatarWrapper.appendChild(statusDot);


            conversationSecondUserDiv.appendChild(avatarWrapper);
            conversationSecondUserDiv.appendChild(detailsDiv);


            messageContainer.appendChild(conversationSecondUserDiv);






/////////////////////////////////////////////////////////////////////////////////////





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
                    }, { once: true });
                }
            });
            conversationMessagesContainer.innerHTML = "";
            conversationMessagesContainer.append(...messages);
            currentConversationId = conversation.id;

            messageContainer.appendChild(conversationSecondUserDiv);
            messageContainer.appendChild(conversationMessagesContainer);

            conversationMessagesContainer.scrollTop = conversationMessagesContainer.scrollHeight;

            createLinuxInputMessageDiv(messageContainer);

        }).catch(error => {
            console.error('Error loading messages:', error);
        });

    }

    // conversationsDivs[conversation.id] = conversationDiv;
    conversationsDivs[conversation.id] = {
        div: conversationDiv,
        userStatus: conversation.userStatus,
        userLogoutDate: conversation.userLogoutDate
    };

    return conversationDiv;
}

function createNormalMessageInputDiv() {
    if(document.getElementById('messageForm')===null) {
        const messageContainer = document.getElementById('messageContainer');

        const messageFormDiv = document.createElement('div');
        messageFormDiv.id = 'messageForm';

        const messageInput = document.createElement('input');
        messageInput.type = 'text';
        messageInput.id = 'message';
        messageInput.className = 'message-input';
        messageInput.placeholder = 'Type your message';
        
        messageInput.addEventListener('keypress', function (e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                sendMessageNormal();
            }
        });
        messageInput.addEventListener("input", function () {
            adjustLinesInterval(100, 1000);
        });

        const sendButton = document.createElement('button');
        sendButton.onclick = sendMessageNormal;
        sendButton.className = 'hover-button';
        sendButton.textContent = '⮉';

        messageFormDiv.appendChild(messageInput);
        messageFormDiv.appendChild(sendButton);
        messageContainer.appendChild(messageFormDiv);
    }
}


function setMessageOwnerClass(message, loggedUserId, newMessage) {
    if (message.senderId === loggedUserId) {
        newMessage.classList.add('message-sent');
    } else {
        newMessage.classList.add('message-received');
    }
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

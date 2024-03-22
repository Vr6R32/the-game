let currentConversationId;
let conversationsDivs = [];
document.addEventListener("DOMContentLoaded", function () {
    let contactsContainer = document.getElementById("contactsContainer");
    fetchConversations().then(conversationsDivs => {
        Object.values(conversationsDivs).forEach(div => {
            contactsContainer.appendChild(div);
        });
    });
});

function fetchConversations() {
    return fetch('/api/v1/conversation')
        .then(response => response.json())
        .then(data => {
            let conversationMessagesContainer = document.getElementById("conversationMessages");
            data.forEach(conversation => {
                const div = document.createElement('div');
                div.textContent = `Konwersacja ${conversation.id}`;
                div.classList.add('conversation-container');
                div.onclick = () => loadMessages(conversation.id).then(messages => {
                    conversationMessagesContainer.innerHTML = "";
                    currentConversationId = conversation.id;
                    conversationMessagesContainer.append(...messages);
                    conversationMessagesContainer.scrollTop = conversationMessagesContainer.scrollHeight;

                }).catch(error => {
                    console.error('Error loading messages:', error);
                });
                conversationsDivs[conversation.id] = div;
            });
            return conversationsDivs;
        });
}

function loadMessages(conversationId) {
    return fetch('/api/v1/conversation/messages/' + conversationId)
        .then(response => response.json())
        .then(data => {
            let loggedUser = document.getElementById('username').value;
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
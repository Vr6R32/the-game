let currentConversationId;

document.addEventListener("DOMContentLoaded", function () {
    let messageContainer = document.getElementById("messageContainer");
    fetchConversations().then(conversationsDivs => {
        messageContainer.append(...conversationsDivs);
    });
});

function fetchConversations() {
    return fetch('/api/v1/conversation')
        .then(response => response.json())
        .then(data => {
            const conversationsDivs = [];
            let messageContainer = document.getElementById("messageContainer");
            data.forEach(conversation => {
                const div = document.createElement('div');
                div.textContent = `Konwersacja ${conversation.id}`;
                div.classList.add('conversation-container');
                div.onclick = () => loadMessages(conversation.id).then(messages => {
                    currentConversationId = conversation.id;
                    messageContainer.innerHTML = "";
                    messageContainer.append(...messages);
                });
                conversationsDivs.push(div);
            });
            return conversationsDivs;
        });
}

function loadMessages(conversationId) {
    return fetch('/api/v1/conversation/messages/' + conversationId)
        .then(response => response.json())
        .then(data => {
            const conversationsDivs = [];
            let loggedUser = document.getElementById('username').value;
            data.forEach(message => {
                const div = document.createElement('div');
                div.textContent = `${message.sender}:${message.payload}`;
                div.classList.add('conversation-message');

                console.log(message.sender);
                console.log(loggedUser);
                
                if(message.sender === loggedUser){
                    div.classList.add('message-sent');
                } else {
                    div.classList.add('message-received');
                }

                conversationsDivs.push(div);
            });
            return conversationsDivs;
        });
}
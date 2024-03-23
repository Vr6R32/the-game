let stompClient;
let blockUntil = 0;
let messageTimes = [];

document.addEventListener("DOMContentLoaded", function () {
    connectSocket();

    setInterval(function() {
        console.log('Reconnecting...');
        connectSocket();
    }, 110000);
});

function connectSocket(){
    if (stompClient && stompClient.connected) {
        stompClient.disconnect(function() {
            console.log('Disconnected');
        });
    }

    let socketUrl = document.getElementById('websocketUrl').value;
    const socket = new SockJS(socketUrl);
    stompClient = Stomp.over(socket);

    const username = document.getElementById('username').value;
    const userId = document.getElementById('userId').value;

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);

        stompClient.subscribe('/user/'+ userId +'/errors', function (error) {
            const errorMessage = JSON.parse(error.body);
            console.log(errorMessage)
        });

        // stompClient.subscribe('/user/'+ userId +'/notifications', function (notification) {
        //     const notificationMessage = JSON.parse(notification.body);
        //     console.log(notificationMessage)
        // });

        stompClient.subscribe('/user/' + userId + '/messages', function (message) {
            const chatMessage = JSON.parse(message.body);
            appendMessage(chatMessage);
        });
    }, function(error) {
        console.log('Connection error: ', error);
    });
}

function appendMessage(message) {
    const conversationMessages = document.getElementById('conversationMessages');
    const isScrolledToBottom = conversationMessages.scrollHeight - conversationMessages.clientHeight <= conversationMessages.scrollTop + 1;

    let loggedUser = document.getElementById('username').value;
    let newMessage = document.createElement('div');

    if (currentConversationId === message.conversationId) {
        newMessage.classList.add('conversation-message');
        newMessage.textContent = `${message.payload}`;
        conversationMessages.append(newMessage)

        if (message.sender === loggedUser) {
            newMessage.classList.add('message-sent');
        } else {
            newMessage.classList.add('message-received');
        }

        if (isScrolledToBottom) {
            conversationMessages.scrollTop = conversationMessages.scrollHeight;
        }
    } else {
        let conversationDiv = conversationsDivs[message.conversationId];
        if (conversationDiv) {

            conversationDiv.style.backgroundColor = 'red';

            // conversationDiv.classList.add('new-message');
        }
    }
}

function spamCheck() {
    const now = Date.now();
    loadSpamProtectionData();

    if (now < blockUntil) {
        const remainingTime = ((blockUntil - now) / 1000).toFixed(0);
        alert(`You are blocked from sending messages. Please wait ${remainingTime} seconds.`);
        return false;
    }

    messageTimes.push(now);

    if (messageTimes.length > 4) {
        if (now - messageTimes[messageTimes.length - 5] < 2000) {
            blockUntil = now + 120000;
            alert("You're sending messages too quickly. You have been blocked for 2 minutes.");
            messageTimes = [];
            localStorage.setItem('blockUntil', blockUntil.toString());
            localStorage.setItem('messageTimes', JSON.stringify(messageTimes));
            return false;
        }
    }

    if (messageTimes.length > 4) {
        messageTimes.shift();
    }

    localStorage.setItem('blockUntil', blockUntil.toString());
    localStorage.setItem('messageTimes', JSON.stringify(messageTimes));
    return true;
}

function loadSpamProtectionData() {
    const storedBlockUntil = localStorage.getItem('blockUntil');
    const storedMessageTimes = localStorage.getItem('messageTimes');
    blockUntil = storedBlockUntil ? parseInt(storedBlockUntil, 10) : 0;
    messageTimes = storedMessageTimes ? JSON.parse(storedMessageTimes) : [];
    const now = Date.now();
    messageTimes = messageTimes.filter(time => now - time < 2000);
}

function sendMessage() {

    adjustLinesInterval(100, 2000);

    if(!spamCheck()){
        return;
    }

    const usernameInput = document.getElementById('username').value;
    const messageInput = document.getElementById('message');
    const trimmedMessage = messageInput.value.trim();

    if (trimmedMessage === '') {
        alert("Message cannot be empty.");
        return;
    }
    const message = {
        sender: usernameInput,
        receiver: usernameInput,
        payload: trimmedMessage
    };
    stompClient.send("/chat/private/message/"+ currentConversationId , {}, JSON.stringify(message));
    messageInput.value = '';
    messageInput.focus();

}

document.addEventListener("DOMContentLoaded", function () {

    createChatIcon();
    let chatIcon = document.getElementById('chatIcon');
    dragElement(chatIcon);
    chatIcon.addEventListener('click', function(event) {
        if (!chatIcon.wasDragged) {
            maximizeChat();
        }
        chatIcon.wasDragged = false;
    });

    let chatWrapper = document.getElementById('chatWrapper');
    chatWrapper.style.display = 'block';

});

function createChatIcon() {
    const chatIcon = document.createElement('div');
    chatIcon.id = 'chatIcon';
    chatIcon.textContent = 'C';
    document.body.appendChild(chatIcon);
}

function maximizeChat() {
    document.getElementById('chatWrapper').style.display = 'block';
    document.getElementById('chatIcon').style.display = 'none';
}

function minimizeChat() {
    document.getElementById('chatWrapper').style.display = 'none';
    document.getElementById('chatIcon').style.display = 'flex';
}

function dragElement(element) {
    let pos1 = 0, pos2 = 0, pos3 = 0, pos4 = 0;
    element.onmousedown = dragMouseDown;

    function dragMouseDown(e) {
        e = e || window.event;
        e.preventDefault();
        pos3 = e.clientX;
        pos4 = e.clientY;
        document.onmouseup = closeDragElement;
        document.onmousemove = elementDrag;
        element.wasDragged = false;
    }

    function elementDrag(e) {
        e = e || window.event;
        e.preventDefault();
        pos1 = pos3 - e.clientX;
        pos2 = pos4 - e.clientY;
        pos3 = e.clientX;
        pos4 = e.clientY;
        element.style.top = (element.offsetTop - pos2) + "px";
        element.style.left = (element.offsetLeft - pos1) + "px";
        element.wasDragged = true; // Set the flag to indicate dragging
    }

    function closeDragElement() {
        document.onmouseup = null;
        document.onmousemove = null;
    }
}
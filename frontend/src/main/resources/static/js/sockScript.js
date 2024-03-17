let stompClient;
let blockUntil = 0;
let messageTimes = [];

document.addEventListener("DOMContentLoaded", function () {
    connectSocket(); // Initial connection

    // Set interval for reconnecting every minute
    setInterval(function() {
        console.log('Reconnecting...');
        connectSocket();
    }, 11000); // 60000 milliseconds = 1 minute

    document.getElementById('message').addEventListener('keypress', function (e) {
        if (e.key === 'Enter') {
            e.preventDefault();
            sendMessage();
        }
    });
});

function connectSocket(){
    // Disconnect existing connection if any
    if (stompClient && stompClient.connected) {
        stompClient.disconnect(function() {
            console.log('Disconnected');
        });
    }

    // Create a new connection
    let socketUrl = document.getElementById('websocketUrl').value;
    const socket = new SockJS(socketUrl);
    stompClient = Stomp.over(socket);

    const username = document.getElementById('username').value;
    const userId = document.getElementById('userId').value;

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);

        // stompClient.subscribe('/user/'+ usernameInput +'/errors', function (error) {
        //     alert("Otrzymano błąd: " + error.body);
        // });

        stompClient.subscribe('/user/'+ userId +'/errors', function (message) {
            const chatMessage = JSON.parse(message.body);
            appendMessage(chatMessage);
            // console.log(chatMessage)
        });

        stompClient.subscribe('/conversation/1', function (message) {
            const chatMessage = JSON.parse(message.body);
            appendMessage(chatMessage);
        });
    }, function(error) {
        console.log('Connection error: ', error);
    });
}

function appendMessage(chatMessage) {
    const messageContainer = document.getElementById('messageContainer');
    const isScrolledToBottom = messageContainer.scrollHeight - messageContainer.clientHeight <= messageContainer.scrollTop + 1;

    messageContainer.innerHTML += `<p><strong>${chatMessage.sender}:</strong> ${chatMessage.payload}</p>`;

    if (isScrolledToBottom) {
        messageContainer.scrollTop = messageContainer.scrollHeight;
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
        payload: trimmedMessage
    };
    stompClient.send("/chat/sendMessage/1", {}, JSON.stringify(message));
    messageInput.value = '';
    messageInput.focus();
}

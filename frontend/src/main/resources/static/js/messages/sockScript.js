let stompClient;
let blockUntil = 0;
let messageTimes = [];


function stabilizeWebSocketConnection() {
    connectSocket();
    setInterval(function() {
        console.log('Reconnecting...');
        connectSocket();
    }, 110000);
}

function connectSocket(){
    if (stompClient && stompClient.connected) {
        stompClient.disconnect(function() {
            console.log('Disconnected');
        });
    }

    let socketUrl = document.getElementById('websocketUrl').value;

    const socket = new SockJS(socketUrl);
    stompClient = Stomp.over(socket);

    const userId = loggedUser.id;

    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);

        stompClient.subscribe('/user/'+ userId +'/errors', function (error) {
            const errorMessage = JSON.parse(error.body);
            console.log(errorMessage)
        });

        stompClient.subscribe('/user/'+ userId +'/notifications', function (notification) {
            const notificationMessage = JSON.parse(notification.body);
            handleNotificationEvent(notificationMessage);
        });

        stompClient.subscribe('/user/' + userId + '/messages', function (message) {
            const chatMessage = JSON.parse(message.body);
            handleNewMessageEvent(chatMessage);
        });
    }, function(error) {
        console.log('Connection error: ', error);
    });
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
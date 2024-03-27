
function focusInputArea() {
    const input = document.getElementById('message');
    input.focus();
}


function updateCursor() {
    
    let terminalBody = document.getElementById('terminal__body');
    const cursor = document.getElementById('terminal__prompt--cursor');
    const selection = window.getSelection();
    if (!selection.rangeCount) return;
    const range = selection.getRangeAt(0);
    const rect = range.getBoundingClientRect();

    if (rect.width === 0 && rect.height === 0) {
        const lastChild = terminalBody.lastChild;
        if (lastChild && lastChild.getBoundingClientRect) {
            cursor.style.transform = ``;
        }
    } else {

        const containerRect = terminalBody.getBoundingClientRect();
        const cursorLeft = rect.left - containerRect.left - 128 ;
        
        // let userEmail = document.getElementById('userEmail').value;
        // let numberOfCharacters = userEmail.length;
        // const cursorLeft = rect.left - containerRect.left - (numberOfCharacters * 10) ;
        const cursorTop = rect.top - containerRect.top;

        // const messageInput = document.getElementById('message');
        // if(messageInput.textContent.trim() === '') {
        //     const resetpos = 0;
        //     cursor.style.transform = `translate(${resetpos}px, ${resetpos}px)`;
        // } else {
        //     cursor.style.transform = `translate(${cursorLeft}px, ${cursorTop}px)`;
        // }
        cursor.style.transform = `translate(${cursorLeft}px, ${cursorTop}px)`;
    }
}


function linuxCmdEntryAnimation() {
    const observer = new IntersectionObserver((entries, observer) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('visible');
                setTimeout(() => {
                    entry.target.classList.remove('visible');
                }, 400);
            }
        });
    }, {threshold: 0.1});

    document.querySelectorAll('.glow-box-shadow').forEach(box => {
        observer.observe(box);
    });
}

function sendMessageLinux() {

    adjustLinesInterval(100, 2000);
    if(!spamCheck()){
        return;
    }

    const messageInput = document.getElementById('message');
    const trimmedMessage = messageInput.textContent.trim();
    if (trimmedMessage === '') {
        alert("Message cannot be empty.");
        return;
    }
    const message = {
        payload: trimmedMessage
    };
    stompClient.send("/chat/private/message/"+ currentConversationId , {}, JSON.stringify(message));
    messageInput.textContent = '';
    messageInput.focus();
}



function createLinuxInputMessageDiv(messageContainer) {

    let existingTerminal = document.getElementById('terminal');

    if(existingTerminal===null) {


        const messageFormDiv = document.createElement('div');
        messageFormDiv.id = 'messageForm';

        const terminal = document.createElement('div');
        terminal.id = 'terminal';
        terminal.className = 'glow-box-shadow';

        const terminalBar = document.createElement('section');
        terminalBar.id = 'terminal__bar';

        const barButtons = document.createElement('div');
        barButtons.id = 'bar__buttons';

        const closeButton = document.createElement('button');
        closeButton.className = 'bar__button';
        closeButton.id = 'bar__button--exit';
        closeButton.innerHTML = '&#10005;';

        const minimizeButton = document.createElement('button');
        minimizeButton.className = 'bar__button';
        minimizeButton.innerHTML = '&#9472;';

        const maximizeButton = document.createElement('button');
        maximizeButton.className = 'bar__button';
        maximizeButton.innerHTML = '&#9723;';

        barButtons.appendChild(closeButton);
        barButtons.appendChild(minimizeButton);
        barButtons.appendChild(maximizeButton);


        const userEmailUbuntuLogin = document.createElement('p');
        userEmailUbuntuLogin.id = 'bar__user';
        userEmailUbuntuLogin.textContent = loggedUser.email;

        terminalBar.appendChild(barButtons);
        terminalBar.appendChild(userEmailUbuntuLogin);

        // Tworzenie ciała terminala
        const terminalBody = document.createElement('section');
        terminalBody.id = 'terminal__body';
        terminalBody.addEventListener('click', focusInputArea);

        const terminalPrompt = document.createElement('div');
        terminalPrompt.id = 'terminal__prompt';

        // const userSpan = document.createElement('span');
        // userSpan.id = 'terminal__prompt--user';
        // userSpan.textContent = 'fobabs@ubuntu' + ':';
        // userSpan.textContent = 'karacz@hitman.pl' + ':';

        const userSpan = document.createElement('span');
        userSpan.id = 'terminal__prompt--user';
        // userSpan.textContent = userEmail + ':';
        userSpan.textContent = 'e-chat@ubuntu:';

        const locationSpan = document.createElement('span');
        locationSpan.id = 'terminal__prompt--location';
        locationSpan.textContent = '~';

        const blingSpan = document.createElement('span');
        blingSpan.id = 'terminal__prompt--bling';
        blingSpan.textContent = '$';

        const cursorSpan = document.createElement('span');
        cursorSpan.id = 'terminal__prompt--cursor';

        const inputDiv = document.createElement('div');
        inputDiv.className = 'terminal__input';
        inputDiv.id = 'message';
        inputDiv.setAttribute('contenteditable', 'true');
        inputDiv.setAttribute('role', 'textbox');
        inputDiv.setAttribute('aria-multiline', 'false');
        inputDiv.addEventListener('keyup', handleKeydown);
        inputDiv.addEventListener('click', updateCursor);
        inputDiv.addEventListener('input', updateCursor)
        inputDiv.addEventListener('keypress', function (e) {
            if (e.key === 'Enter') {
                e.preventDefault();
                sendMessageLinux();
                updateCursor();
            }
        });

        terminalPrompt.appendChild(userSpan);
        terminalPrompt.appendChild(locationSpan);
        terminalPrompt.appendChild(blingSpan);
        terminalPrompt.appendChild(cursorSpan);
        terminalPrompt.appendChild(inputDiv);

        terminalBody.appendChild(terminalPrompt);

        terminal.appendChild(terminalBar);
        terminal.appendChild(terminalBody);

        messageFormDiv.appendChild(terminal);
        messageContainer.appendChild(messageFormDiv);

        
        linuxCmdEntryAnimation();

    }

}


function handleKeydown(e) {
    if (['ArrowLeft', 'ArrowRight', 'ArrowUp', 'ArrowDown'].includes(e.key)) {
        setTimeout(updateCursor(), 0);
    }
}


let loggedUser;

function createLoginForm() {

    let mainContainer = document.getElementById('messageContainer');
    mainContainer.innerHTML = '';

    let loginWrapper = document.createElement("div");
    loginWrapper.classList.add('form-wrapper');

    const form = document.createElement('form');
    form.setAttribute('id', 'loginForm');

    const h2 = document.createElement('h2');
    h2.textContent = 'Log@in';
    form.appendChild(h2);

    const {divInput: divUsername, inputElement: inputUsername} =
        createFormInput('username', 'text');
    createFormLabel(divUsername, inputUsername, form,'Username');


    const {divInput: divPassword, inputElement: inputPassword} =
        createFormInput('password', 'password');
    createFormLabel(divPassword, inputPassword, form,'Password');

    createFormButton(form, submitLoginForm,'execute');

    loginWrapper.appendChild(form);
    mainContainer.appendChild(loginWrapper);

    inputUsername.focus();
}



function submitLoginForm() {
    let username = document.getElementById("username").value;
    let password = document.getElementById("password").value;

    document.querySelectorAll('.error-message').forEach(e => e.remove());

    if (!username || !password) {
        if (!username) {
            displayErrorMessage('username', 'Please fill in your email.');
        }
        if (!password) {
            displayErrorMessage('password', 'Please fill in your password.');
        }
        return;
    }

    const data = {
        username: username,
        password: password
    };

    const requestOptions = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    };

    fetch('/api/v1/auth/login', requestOptions)
        .then(response => response.json())
        .then(data => {
            document.getElementById("username").value = '';
            document.getElementById("password").value = '';

            console.log('Odpowiedź z serwera:', data);
            if(data.message === 'AUTHENTICATED') {
                document.getElementById('messageContainer').innerHTML = '';
                loggedUser = data.user;
                stabilizeWebSocketConnection();
                createContactsContainer()
            } else {
                //TODO MAKE AUTH ERROR HANDLING
            }
        })
        .catch(error => {
            console.error('Wystąpił błąd:', error);
        });
}

function displayErrorMessage(inputId, message) {
    const input = document.getElementById(inputId);
    const errorMessage = document.createElement('div');
    errorMessage.textContent = message;
    errorMessage.classList.add('error-message');
    errorMessage.style.color = 'red';
    errorMessage.style.fontSize = '0.8rem';
    errorMessage.style.marginTop = '5px';
    input.parentElement.appendChild(errorMessage);
}


function logout() {
    const requestOptions = {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    };

    fetch('/api/v1/auth/logout', requestOptions)
        .then(response => response.json())
        .then(data => {
            console.log(data);
            stompClient.disconnect();
            acceptedCount = 0;
            invitationsCount = 0;
            conversationsDivs = [];
            handleLogoutAnimation();
        })
        .catch(error => {
            console.error('Wystąpił błąd podczas wylogowywania:', error);
        });
}

function handleLogoutAnimation() {
    let contactsWrapper = document.getElementById('contactsWrapper');
    contactsWrapper.classList.remove('expandWidth');
    void contactsWrapper.offsetWidth;
    contactsWrapper.classList.add('reduce-width');

    setTimeout(() => {
        let contactsWrapper = document.getElementById('contactsWrapper');
        if (contactsWrapper) {
            contactsWrapper.parentNode.removeChild(contactsWrapper);
        }
    }, 1007);
    textPosition = 0;
    createSelfWriterLandingPage();
}
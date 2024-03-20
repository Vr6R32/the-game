
document.addEventListener("DOMContentLoaded", function() {
    let loginWrapper = document.getElementById("login-form-wrapper");
    loginWrapper.appendChild(createLoginForm());
});


function createLoginForm() {
    const form = document.createElement('form');
    form.setAttribute('id', 'loginForm');

    const h2 = document.createElement('h2');
    h2.textContent = 'Log@in';
    form.appendChild(h2);

    const divUsername = document.createElement('div');
    divUsername.classList.add('input-box');
    const inputUsername = document.createElement('input');
    inputUsername.setAttribute('type', 'text');
    inputUsername.setAttribute('id', 'username');
    inputUsername.setAttribute('required', "true");
    inputUsername.addEventListener('click', function() {
        divUsername.querySelectorAll('.error-message').forEach(e => e.remove());
    });
    const labelUsername = document.createElement('label');
    labelUsername.textContent = 'Email';
    divUsername.appendChild(inputUsername);
    divUsername.appendChild(labelUsername);
    form.appendChild(divUsername);

    const divPassword = document.createElement('div');
    divPassword.classList.add('input-box');
    const inputPassword = document.createElement('input');
    inputPassword.setAttribute('type', 'password');
    inputPassword.setAttribute('id', 'password');
    inputPassword.setAttribute('required', "true");
    inputPassword.addEventListener('click', function() {
        divPassword.querySelectorAll('.error-message').forEach(e => e.remove());
    });
    const labelPassword = document.createElement('label');
    labelPassword.textContent = 'Password';
    divPassword.appendChild(inputPassword);
    divPassword.appendChild(labelPassword);
    form.appendChild(divPassword);

    const divButton = document.createElement('div');
    divButton.classList.add('form-button');
    const button = document.createElement('button');
    button.setAttribute('type', 'button');
    button.textContent = 'Execute';
    button.onclick = function() { submitForm(); };
    divButton.appendChild(button);
    form.appendChild(divButton);

    return form;
}






function submitForm() {
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
            console.log('Odpowiedź z serwera:', data);
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
            console.log('Odpowiedź z serwera po wylogowaniu:', data);
            window.location.href = data.location;
        })
        .catch(error => {
            console.error('Wystąpił błąd podczas wylogowywania:', error);
        });
}
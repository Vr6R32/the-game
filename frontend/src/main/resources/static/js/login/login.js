
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
    
    const {divUsername, inputUsername} = createUsernameInput();
    createUsernameLabel(divUsername, inputUsername, form);

    const {divPassword, inputPassword} = createInputPassword();
    createLabelPassword(divPassword, inputPassword, form);
    
    createFormButton(form);

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
            document.getElementById("username").value = '';
            document.getElementById("password").value = '';

            console.log('Odpowiedź z serwera:', data);
            if(data.message === 'AUTHENTICATED') {
                window.location.href = '/messages';
            } else {
                // MAKE AUTH ERROR
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
            console.log('Odpowiedź z serwera po wylogowaniu:', data);
            window.location.href = data.location;
        })
        .catch(error => {
            console.error('Wystąpił błąd podczas wylogowywania:', error);
        });
}
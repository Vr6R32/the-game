function createUsernameInput() {
    const divUsername = document.createElement('div');
    divUsername.classList.add('input-box');
    const inputUsername = document.createElement('input');
    inputUsername.setAttribute('type', 'text');
    inputUsername.setAttribute('id', 'username');
    inputUsername.setAttribute('required', "true");
    inputUsername.addEventListener('click', function () {
        divUsername.querySelectorAll('.error-message').forEach(e => e.remove());
    });
    

    inputUsername.addEventListener('input', function () {
        adjustLinesInterval(100, 1000);
    });
    return {divUsername, inputUsername};
}

function createUsernameLabel(divUsername, inputUsername, form) {
    const labelUsername = document.createElement('label');
    labelUsername.textContent = 'Email';
    divUsername.appendChild(inputUsername);
    divUsername.appendChild(labelUsername);
    form.appendChild(divUsername);
}

function createInputPassword() {
    const divPassword = document.createElement('div');
    divPassword.classList.add('input-box');
    const inputPassword = document.createElement('input');
    inputPassword.setAttribute('type', 'password');
    inputPassword.setAttribute('id', 'password');
    inputPassword.setAttribute('required', "true");
    inputPassword.addEventListener('click', function () {
        divPassword.querySelectorAll('.error-message').forEach(e => e.remove());
    });

    inputPassword.addEventListener('input', function () {
        adjustLinesInterval(100, 1000);
    });
    return {divPassword, inputPassword};
}

function createLabelPassword(divPassword, inputPassword, form) {
    const labelPassword = document.createElement('label');
    labelPassword.textContent = 'Password';
    divPassword.appendChild(inputPassword);
    divPassword.appendChild(labelPassword);
    form.appendChild(divPassword);
}

function createFormButton(form) {
    const divButton = document.createElement('div');
    divButton.classList.add('form-button');
    const button = document.createElement('button');
    button.setAttribute('type', 'button');
    button.textContent = 'Execute';
    button.onclick = function () {
        submitLoginForm();
    };
    form.addEventListener('keypress', function (e) {
        if (e.key === 'Enter') {
            e.preventDefault();
            submitLoginForm();
        }
    });
    divButton.appendChild(button);
    form.appendChild(divButton);
}
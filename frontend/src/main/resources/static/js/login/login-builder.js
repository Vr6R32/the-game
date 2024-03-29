function createFormInput(id, type) {
    const divInput = document.createElement('div');
    divInput.classList.add('input-box');

    const inputElement = document.createElement('input');
    inputElement.setAttribute('type', type);
    inputElement.setAttribute('id', id);
    inputElement.setAttribute('required', "true");
    inputElement.addEventListener('click', function () {
        divInput.querySelectorAll('.error-message').forEach(e => e.remove());
    });
    inputElement.addEventListener('input', function () {
        adjustLinesInterval(100, 1000);
    });

    return {divInput, inputElement};
}
function createFormLabel(divUsername, inputUsername, form, textContent) {
    const labelUsername = document.createElement('label');
    labelUsername.textContent = textContent;
    divUsername.appendChild(inputUsername);
    divUsername.appendChild(labelUsername);
    form.appendChild(divUsername);
}

function createFormButton(form, actionFunction, textContent) {
    const buttonWrapper = document.createElement('div');
    buttonWrapper.classList.add('index-button-wrapper');
    buttonWrapper.style.display = 'flex';
    buttonWrapper.style.justifyContent = 'center';

    const button = document.createElement('button');
    button.setAttribute('type', 'button');
    button.textContent = textContent;
    button.onclick = function () {
        actionFunction();
        adjustLinesInterval(100, 1000);
    };
    form.addEventListener('keypress', function (e) {
        if (e.key === 'Enter') {
            e.preventDefault();
            actionFunction();
            adjustLinesInterval(100, 1000);
        }
    });
    buttonWrapper.appendChild(button);
    form.appendChild(buttonWrapper);
}
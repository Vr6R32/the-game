let textPosition = 0;


let welcomeText =
    
    'Email Chatter is a backend-driven project harnessing the robust capabilities of \n' +
    'Spring Boot in conjunction with a Spring Cloud API Gateway and Spring Eureka for efficient service discovery. \n ' +
    'It utilizes WebSocket for real-time bi-directional communication and combines \n' +
    'MongoDB with PostgreSQL for a comprehensive and versatile data persistence layer. \n' +
    'Designed for scalability, the infrastructure is optimized to function as an online messaging platform, \n' +
    'Users can effortlessly write messages and add contacts using email addresses,\n' +
    'Fostering a user-friendly and accessible communication environment. \n' +
    'This innovative application caters to the growing demand for agile and responsive\n' +
    'Online communication tools that prioritize convenience and connectivity.';

let logoutText =
    'Successfully logged out';

function selfWriter() {
    let textToShow = welcomeText.substring(0, textPosition).replace(/\n/g, '<br>');
    document.querySelector("#quote").innerHTML = textToShow + '<span>\u25AE</span>';

    if (textPosition++ !== welcomeText.length) {
        setTimeout(selfWriter, 10);
    }
}

function createSelfWriterLandingPage() {

    let messageContainer = document.getElementById('messageContainer');
    messageContainer.innerHTML = '';
    let typeWriterWelcomeContainer = document.createElement('div');
    typeWriterWelcomeContainer.setAttribute('id', 'typewriter');

    let paragraphQuote = document.createElement('p');
    paragraphQuote.setAttribute('id', 'quote');
    typeWriterWelcomeContainer.appendChild(paragraphQuote);

    let indexButtonsWrapper = document.createElement('div');
    indexButtonsWrapper.className = 'index-button-wrapper';

    const loginButton = document.createElement('button');

    loginButton.innerHTML = '<span></span><span></span><span></span><span></span>Login';
    loginButton.onclick = createLoginForm;

    indexButtonsWrapper.appendChild(loginButton);


    const registerButton = document.createElement('button');
    registerButton.innerHTML = '<span></span><span></span><span></span><span></span>Register';
    registerButton.onclick;

    indexButtonsWrapper.appendChild(registerButton);

    typeWriterWelcomeContainer.appendChild(indexButtonsWrapper);
    messageContainer.appendChild(typeWriterWelcomeContainer);
    selfWriter();
}


let writeText =
    
    'Email Chatter is a backend-driven project harnessing the robust capabilities of \n' +
    'Spring Boot in conjunction with a Spring Cloud API Gateway and Spring Eureka for efficient service discovery. \n ' +
    'It utilizes WebSocket for real-time bi-directional communication and combines \n' +
    'MongoDB with PostgreSQL for a comprehensive and versatile data persistence layer. \n' +
    'Designed for scalability, the infrastructure is optimized to function as an online messaging platform, \n' +
    'Users can effortlessly write messages and add contacts using email addresses,\n' +
    'Fostering a user-friendly and accessible communication environment. \n' +
    'This innovative application caters to the growing demand for agile and responsive\n' +
    'Online communication tools that prioritize convenience and connectivity.'

let textPosition = 0;

function typewriter() {
    let textToShow = writeText.substring(0, textPosition).replace(/\n/g, '<br>');
    document.querySelector("#quote").innerHTML = textToShow + '<span>\u25AE</span>';

    if (textPosition++ !== writeText.length) {
        setTimeout(typewriter, 10);
    }
}

window.addEventListener('load', typewriter);
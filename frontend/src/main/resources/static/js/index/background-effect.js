let activeLines = 0;
let maxLines = 70;
let activeBackgroundDuration = 5;
let activeBackgroundDurationCleaner = 4000;
let activeBackgroundDurationInterval = 500;
let linesTimeout;

document.addEventListener("DOMContentLoaded", function () {
    drawBackgroundLines();
});


function drawBackgroundLines() {
    if (activeLines < maxLines) {
        let e = document.createElement("div");
        e.setAttribute("class", "circle");
        e.style.width = "20px";
        e.textContent = "@";
        e.style.textAlign = 'center';
        e.style.textShadow = '0 0 10px #ff5400, 0 0 20px #ff5400, 0 0 30px #ff5400';
        e.style.left = Math.random() * window.innerWidth + "px";
        e.style.animationDuration = activeBackgroundDuration + "s";

        let backgroundContainer = document.getElementById("background-animation");
        backgroundContainer.appendChild(e);
        activeLines++;

        setTimeout(function () {
            backgroundContainer.removeChild(e);
            activeLines--;
        }, activeBackgroundDurationCleaner);
    }

    linesTimeout = setTimeout(drawBackgroundLines, activeBackgroundDurationInterval);
}

function adjustLinesInterval(newInterval, duration) {
    clearTimeout(linesTimeout);
    activeBackgroundDurationInterval = newInterval;
    activeBackgroundDuration = 1;
    drawBackgroundLines();

    setTimeout(() => {
        activeBackgroundDurationInterval = 500;
        activeBackgroundDuration = 5;
        clearTimeout(linesTimeout);
        drawBackgroundLines();
    }, duration);
}











//
//
// let activeLines = 0;
// let maxLines = 30;
//
// function lines() {
//     if (activeLines < maxLines) {
//         let e = document.createElement("div");
//         e.setAttribute("class", "circle");
//         e.style.width = "20px";
//         e.textContent = "@";
//         e.style.justifyContent = 'center';
//         e.style.textShadow = '0 0 10px #ff5400, 0 0 20px #ff5400, 0 0 30px #ff5400';
//         e.style.position = 'absolute'; // Upewnij się, że divy są absolutnie pozycjonowane
//         e.style.left = Math.random() * window.innerWidth + "px";
//         e.style.animationDuration = "2s";
//
//         let currentTime = new Date().getTime(); // Zapisz czas utworzenia
//         e.setAttribute("data-creation-time", currentTime);
//
//         let backgroundContainer = document.getElementById("background-animation");
//         backgroundContainer.appendChild(e);
//         activeLines++;
//     }
// }
//
// setInterval(lines, 100);
//
// // Algorytm do usuwania starych linii
// function removeOldLines() {
//     let backgroundContainer = document.getElementById("background-animation");
//     let circles = backgroundContainer.getElementsByClassName("circle");
//     let currentTime = new Date().getTime();
//
//     for (let i = circles.length - 1; i >= 0; i--) {
//         let circle = circles[i];
//         let creationTime = parseInt(circle.getAttribute("data-creation-time"));
//         if (currentTime - creationTime > 2000) { // Starsze niż 2 sekundy
//             backgroundContainer.removeChild(circle);
//             activeLines--;
//         }
//     }
// }
//
// setInterval(removeOldLines, 3000); // Sprawdź i usuń stare linie co 3 sekundy
//














// function lines() {
//     let sizeW = Math.random() * 22;
//     let duration = Math.random() * 3;
//     let e = document.createElement("div");
//     e.setAttribute("class", "circle");
//     e.style.width =  sizeW + "px";
//     e.style.left = Math.random() * + innerWidth + "px";
//     e.style.animationDuration = 2 + duration + "s";
//
//     let backgroundAnimationContainer = document.getElementById("background-animation");
//     backgroundAnimationContainer.appendChild(e)
//
//
//     setTimeout(function () {
//         backgroundAnimationContainer.removeChild(e);
//         // document.body.removeChild(e);
//     }, 5000);
// }
// setInterval(function () {
//     lines();
// }, 200);
//
// // let activeSnowflakes = 0;
// // const maxSnowflakes = 50;
// //
// // function createSnowflake() {
// //     if (activeSnowflakes < maxSnowflakes) {
// //         const snowflake = document.createElement('div');
// //         snowflake.classList.add('snowflake');
// //         document.body.appendChild(snowflake);
// //
// //         setTimeout(() => {
// //             snowflake.remove();
// //             activeSnowflakes--;
// //         }, 3000);
// //
// //         activeSnowflakes++;
// //     }
// //
// //     requestAnimationFrame(createSnowflake);
// // }
// //
// // requestAnimationFrame(createSnowflake);
let activeLines = 0;
let maxLines = 40;

function lines() {
    if (activeLines < maxLines) {
        let sizeW = Math.random() * 22;
        let e = document.createElement("div");
        e.setAttribute("class", "circle");
        e.style.width = sizeW + "px";
        e.style.left = Math.random() * window.innerWidth + "px";
        e.style.animationDuration = 2 + "s";

        let backgroundContainer = document.getElementById("background-animation");
        backgroundContainer.appendChild(e)
        activeLines++;

        console.log(activeLines);
        setTimeout(function () {
            backgroundContainer.removeChild(e)
            activeLines--
        }, 2000);
    }
}

setInterval(lines, 500);






















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
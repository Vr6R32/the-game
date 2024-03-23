
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
            const lastRect = lastChild.getBoundingClientRect();
            cursor.style.transform = `translate(${lastRect.left}px, ${lastRect.bottom}px)`;
        }
    } else {
        // let userEmail = document.getElementById('userEmail').value;
        // let numberOfCharacters = userEmail.length;

        const containerRect = terminalBody.getBoundingClientRect();
        const cursorLeft = rect.left - containerRect.left - 128 ;
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


// function updateCursor() {
//     let terminalBody = document.getElementById('terminal__body');
//     const cursor = document.getElementById('terminal__prompt--cursor');
//     const selection = window.getSelection();
//
//     if (!selection.rangeCount || !terminalBody.textContent.trim()) {
//         cursor.style.transform = `translate(0px, 0px)`;
//         return;
//     }
//     const range = selection.getRangeAt(0);
//     const rect = range.getBoundingClientRect();
//     let cursorLeft, cursorTop;
//     if (rect.width === 0 && rect.height === 0) {
//         const children = terminalBody.children;
//         const firstChildRect = children.length > 0 ? children[0].getBoundingClientRect() : null;
//
//         if (firstChildRect) {
//             cursorLeft = firstChildRect.left;
//             cursorTop = firstChildRect.top;
//         } else {
//             cursorLeft = cursorTop = 0;
//         }
//     } else {
//         const containerRect = terminalBody.getBoundingClientRect();
//         cursorLeft = rect.left - containerRect.left - 128;
//         cursorTop = rect.top - containerRect.top;
//     }
//
//     cursor.style.transform = `translate(${cursorLeft}px, ${cursorTop}px)`;
// }
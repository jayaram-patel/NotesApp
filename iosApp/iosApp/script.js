window.onload = function() {
    const urlParams = new URLSearchParams(window.location.search);
    const noteText = urlParams.get('note');
    if (noteText) {
        //change heading to note name
        document.getElementById("page-title").innerText = noteText;
        document.getElementById("message").value = "Regarding my note: " + noteText;
    }
};

function submitForm() {

    let name = document.getElementById("name").value;
    let email = document.getElementById("email").value;
    let message = document.getElementById("message").value;

    if (name === "" || email === "" || message === "") {
        // On iOS, we use the message handler bridge
        if (window.webkit && window.webkit.messageHandlers && window.webkit.messageHandlers.iosBridge) {
             window.webkit.messageHandlers.iosBridge.postMessage({action: 'showError'});
        } else if (typeof Android !== 'undefined') {
             Android.showError();
        }
        return;
    }

    if (window.webkit && window.webkit.messageHandlers && window.webkit.messageHandlers.iosBridge) {
        window.webkit.messageHandlers.iosBridge.postMessage({
            action: 'submitForm',
            name: name,
            email: email,
            message: message
        });
        window.webkit.messageHandlers.iosBridge.postMessage({action: 'showSuccess'});
    } else if (typeof Android !== 'undefined') {
        Android.submitForm(name, email, message);
        Android.showSuccess();
    }

    document.getElementById("name").value = "";
    document.getElementById("email").value = "";
    document.getElementById("message").value = "";
}
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
        Android.showError();
        return;
    }

    Android.submitForm(
        name,
        email,
        message
    );

    Android.showSuccess();

    document.getElementById("name").value = "";
    document.getElementById("email").value = "";
    document.getElementById("message").value = "";
}

function populateNoteData(text) {
    if (text) {
        document.getElementById("page-title").innerText = text;
        document.getElementById("message").value = "Regarding my note: " + text;
    }
}

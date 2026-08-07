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
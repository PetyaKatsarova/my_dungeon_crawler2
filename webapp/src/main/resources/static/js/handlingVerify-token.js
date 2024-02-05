function handleSubmit(event) {
    event.preventDefault();

    // Get the form data
    const formData = new FormData(event.target);

    //verify-account post aanroepen
    const email = document.getElementById("email").value;
    const token = document.getElementById("token").value;

    fetch("/verify-account", {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded",
        },
        body: `email=${email}&token=${token}`,
    }).then(response => {
        if (response.status === 200) {
            // /als ok href = "../html/set-password.html";
            window.location.href = '../html/set-password.html';
        } else if (response.status === 400) {
            //error tonen json console -> + html error
            response.text().then((errorMessage) => {
                alert(errorMessage);
            });
        } else {
            console.error("An unexpected error occurred.");
        }
    })
        .catch(error => {
            console.error('Error:', error);
        });
}

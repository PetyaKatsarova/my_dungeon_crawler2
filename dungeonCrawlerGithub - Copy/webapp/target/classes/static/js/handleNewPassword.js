function handleNewPassword(event) {
    event.preventDefault();

    // Retrieve email and newPassword values
    const email = document.getElementById('email').value;
    const newPassword = document.getElementById('newPassword').value;

    // Construct the URL met the query parameters
    const url = `/set-password?email=${encodeURIComponent(email)}&newPassword=${encodeURIComponent(newPassword)}`;

    // Perform an HTTP PUT request
    fetch(url, {
        method: 'PUT'
    }).then(response => {
        if (response.status === 200) {
            // Password change was successfuf, show message "password change succesfull please login (login is href to  window.location.href = '/html/loginScreen.html';
            // Display a success message and provide an option to go to the login page
            const successMessage = document.getElementById('successMessage');
            successMessage.innerHTML = 'Your password has successfully changed, please ';

            // Create a link to the login page
            const loginLink = document.createElement('a');
            loginLink.textContent = 'login';
            loginLink.href = '/html/loginScreen.html';

            // Append the link to the success message
            successMessage.appendChild(loginLink);

            // Make the success message zichtbaar
            successMessage.style.display = 'block';
        } else if (response.status === 400) {
            // Password change failed door to bad request
            // Handle and display the error message from the response
            return response.text();
        } else if (response.status === 404) {
            // User not found
            // Handle and display the error message from the response
            return response.text();
        } else {
            // Handle other unexpected errors
            console.error("An unexpected error occurred.");
        }
    })
        .catch(error => {
            console.error('Error:', error);
        });
}


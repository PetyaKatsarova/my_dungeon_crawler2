const emailUser = document.querySelector('#email');
const passwordUser = document.querySelector('#password');
const toggleButton = document.querySelector('#togglePassword');
const crawlerName = document.querySelector('#crawlerName');
const errorContainer = document.querySelector('.error-container');
const textBox = document.querySelector('.error-container p');

document.querySelector('form').addEventListener('submit', function(event) {
    event.preventDefault();
    passwordUser.classList.remove('error');
    emailUser.classList.remove('error');
    crawlerName.classList.remove('error');

    const data = { email: emailUser.value, password: passwordUser.value, name: crawlerName.value};
    const urlAPI = 'http://localhost:8080/signup';

    fetch(urlAPI, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    })
        .then((response) => handleErrors(response))
        .then((data) => succeed(data))
        .catch((error) => fail(error));

    const handleErrors = function(response) {
        if (response.status === 201) {
            return response.text();
        }
        return response.text().then(errorText => {
            throw {
                status: response.status,
                statusText: response.statusText,
                message: errorText
            };
        });
    }

    const succeed = function(data) {
        errorContainer.classList.add('success');
        errorContainer.style.display = 'block';
        console.log(data);
        textBox.textContent = data;
        setTimeout(function() {
            window.location.href = '../html/loginScreen.html';
        }, 10000);
    }

    const fail = function(error) {
        errorContainer.style.display = 'block';
        passwordUser.type = 'password';
        toggleButton.textContent = 'Show';

        if (error.status === 400) {
            console.error('Bad Request:', error.message);
            passwordUser.value = '';
            textBox.textContent = error.message;
            passwordUser.classList.add('error');
        } else if (error.status === 409) {
            if (error.message.includes("A user with this email already exists. " +
                "If you've forgotten your password, you can try resetting it.")) {
                console.error('Conflict - User:', error.message);
                emailUser.value = '';
                textBox.textContent = error.message;
                emailUser.classList.add('error');
            } else if (error.message.includes("A Crawler with this name already exists. " +
                "Please choose a different name for your Crawler.")) {
                console.error('Conflict - Crawler:', error.message);
                crawlerName.value = '';
                textBox.textContent = error.message;
                crawlerName.classList.add('error');
            }
        } else if (error.status === 503) {
            console.error('Service Unavailable:', error.message);
            textBox.textContent = error.message;
        } else {
            console.error('An error occurred:', error.status, error.statusText);
            textBox.textContent = error.message;
        }
    }
});

document.addEventListener('DOMContentLoaded', function () {
    toggleButton.addEventListener('click', function () {
        if (passwordUser.type === 'password') {
            passwordUser.type = 'text';
            toggleButton.textContent = 'Hide';
        } else {
            passwordUser.type = 'password';
            toggleButton.textContent = 'Show';
        }
    });
});

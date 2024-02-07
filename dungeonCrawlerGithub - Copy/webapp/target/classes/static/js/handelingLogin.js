document.querySelector('form').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent default form submission

    const emailUser = document.querySelector('#email');
    const passwordUser = document.querySelector('#password');
    const data = { email: emailUser.value, password: passwordUser.value };
    const urlAPI = 'http://localhost:8080/login';

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
        if(!response.ok) {
            throw (response.status + ': ' + response.statusText);
        }
        return response.text();
    }

    const succeed = function(data) {
        localStorage.setItem('token', data);
        window.location.href = '/html/gameboard.html'     //'../html/main.html';
    }

    const fail = function(error) {
        document.querySelector('#error').style.display = 'block'
        emailUser.value = '';
        passwordUser.value = '';
        console.log(error);
    }

});

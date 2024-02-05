function fetchHighScores() {
    fetch('http://localhost:8080/api/highscores/top?limit=10')
        .then(response => {
            if (!response.ok) {
                throw new Error(`Network response was not ok: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            const highscoreContainer = document.getElementById('scoreList');
            highscoreContainer.innerHTML = ''; // Verwijder bestaande inhoud

            // Loop door de ontvangen gegevens en voeg ze toe aan de container
            data.forEach(score => {
                const scoreElement = document.createElement('li');
                scoreElement.textContent = `${score.email}: ${score.highscore}`;
                highscoreContainer.appendChild(scoreElement);
            });
        })
        .catch(error => {
            console.error('Error trying to fetch highscores:', error);
        });
}

window.addEventListener('DOMContentLoaded', (event) => {
    // Wacht tot de pagina volledig is geladen
    // Controleer of de huidige pagina de "victoryPage" is
    if (document.getElementById('victoryPage')) {
        // Selecteer het audio-element
        const audio = document.getElementById('victorySound');
        // Controleer of het audio-element bestaat
        if (audio) {
            // Probeer het audiofragment af te spelen
            audio.play()
                .catch(handleAudioError);
        } else {
            handleAudioElementNotFound();
        }
    }
    fetchHighScores();
});


function handleAudioError(error) {
    console.error('Error whilst playing audio:', error);
    showAlert('An error occured trying to play audio.');
}

function handleAudioElementNotFound() {
    console.error('Audio-element not found on the page.');
}

function showAlert(message) {
    alert(message);
}




// Event listeners for buttons
document.getElementById('retry').addEventListener('click', () => {
    window.location.href = '../html/game.html';
});
document.getElementById('homePage').addEventListener('click', () => {
    window.location.href = '../html/main.html';
});
document.getElementById('logOut').addEventListener('click', () => {
    window.location.href = '../html/loginScreen.html';
});

// Audio and mute button logic
const audio = document.getElementById('defeatSound');
const muteButton = document.getElementById('muteButton');
muteButton.addEventListener('click', () => {
    if (audio.paused) {
        audio.play();
        muteButton.textContent = 'Mute';
    } else {
        audio.pause();
        muteButton.textContent = 'Unmute';
    }
});

// Easter egg logic
let clickCount = 0;
let easterEgg = document.getElementById('easterEgg');
muteButton.addEventListener('click', () => {
    clickCount++;
    if (clickCount === 2) {
        easterEgg.style.display = 'block';
        clickCount = 0;

        // Load Easter egg CSS
        const link = document.createElement('link');
        link.rel = 'stylesheet';
        link.href = '../css/easterEggStyles.css';
        document.head.appendChild(link);

        // Remove document-wide click listener
        document.removeEventListener('click', handleDocumentClick);
    }
});

// Grid item color and number change logic
document.querySelectorAll('.item').forEach(item => {
    item.addEventListener('click', function() {
        // Change the colors of all grid items
        document.querySelectorAll('.item').forEach(element => {
            element.style.backgroundColor = `rgba(${Math.random() * 255}, ${Math.random() * 255}, ${Math.random() * 255}, 0.8)`;
        });
    });
});

// AJAX Functionality to Show High Scores
document.addEventListener('DOMContentLoaded', function() {
    const showHighScoresButton = document.getElementById('showHighScoresButton');
    const highScoresContainer = document.getElementById('highScoresContainer');
    const highScoresList = document.getElementById('highScoresList');

    showHighScoresButton.addEventListener('click', function() {
        // Simulating an AJAX call to fetch high scores
        fetch('../assets/simulated_high_scores.json')
            .then(response => response.json())
            .then(data => {
                // Clearing the previous high scores list
                highScoresList.innerHTML = '';

                // Displaying the high scores container
                highScoresContainer.style.display = 'block';

                // Extracting and displaying a random subset of 10 high scores
                const randomSubset = data.high_scores.sort(() => 0.5 - Math.random()).slice(0, 10);
                randomSubset.forEach(entry => {
                    const listItem = document.createElement('li');
                    listItem.textContent = `${entry.username}: ${entry.score}`;
                    highScoresList.appendChild(listItem);
                });
            })
            .catch(error => {
                console.error('Error fetching high scores:', error);
            });
    });
});

// AJAX Functionality to Show Random Monster
document.addEventListener("DOMContentLoaded", function() {
    const showRandomMonsterButton = document.getElementById('showRandomMonsterButton');
    const randomMonsterContainer = document.getElementById('randomMonsterContainer');

    showRandomMonsterButton.addEventListener('click', function() {
        // Simulating an AJAX call to fetch random monster
        fetch('../assets/simulated_monsters.json')  // Replace this with the actual URL in your project
            .then(response => response.json())
            .then(data => {
                // Displaying the random monster container
                randomMonsterContainer.style.display = 'block';

                // Selecting a random monster
                const randomIndex = Math.floor(Math.random() * data.monsters.length);
                const randomMonster = data.monsters[randomIndex];

                // Displaying the random monster information
                randomMonsterContainer.innerHTML = `<h2>Random Monster</h2><p>Name: ${randomMonster.name}</p><p>Health Points: ${randomMonster.healthpoints}</p>`;
            })
            .catch(error => {
                console.error('Error fetching random monster:', error);
            });
    });
});

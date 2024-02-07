document.getElementById('startGame').addEventListener('click', () => {
    window.location.href = '../html/game.html';
});

document.getElementById('resumeGame').addEventListener('click', () => {
    sessionStorage.setItem("resumeGame", "true");
    window.location.href = '../html/game.html';
});

// document.getElementById('Leaderboard').addEventListener('click', () => {
//     window.location.href = '../html/leaderboard.html'; // Redirect to the leaderboard page
// });


document.getElementById('logOut').addEventListener('click', () => {
    window.location.href = '../index.html';
});

document.getElementById('help').addEventListener('click', () => {
    console.log(" ----------- trla alal -----------")
    window.location.href = '../html/faq.html';
});

// deze funcite maakt het leaderboard zichtbaar

//TODO: kijken of ik dublicatie kan volkomen
fetchLeaderboards();
function fetchLeaderboards() {
    fetch('http://localhost:8080/api/highscores/top?limit=5')
        .then(response => response.json())
        .then(data => {
            populateLeaderboardView(data);
        })
        .catch(error => {
            console.error('Error fetching leaderboards:', error);
        });
}
function populateLeaderboardView(data) {
    const leaderboardView = document.getElementById('leaderboardView');
    const table = createTable(data);
    leaderboardView.innerHTML = '';
    leaderboardView.appendChild(table);
}

function createTable(data) {
    const table = document.createElement('table');
    table.className = 'centered-table';
    // Create table header
    const thead = document.createElement('thead');
    const tr = document.createElement('tr');
    tr.innerHTML = '<th>Email</th><th>Highscore</th>';
    thead.appendChild(tr);
    table.appendChild(thead);

    // Create table body
    const tbody = document.createElement('tbody');
    data.forEach(highscore => {
        const tr = document.createElement('tr');
        tr.innerHTML = `<td>${highscore.email}</td><td>${highscore.highscore}</td>`;
        tbody.appendChild(tr);
    });
    table.appendChild(tbody);

    return table;
}

document.addEventListener('DOMContentLoaded', function() {
    // Find all links
    let links = document.querySelectorAll('a');

    // Attach a click event listener to each link
    links.forEach(function(link) {
        link.addEventListener('click', function(e) {
            e.preventDefault();  // Stop the default link behavior

            // Fade out the current content
            let pageContainer = document.getElementById('page-container');
            pageContainer.classList.remove('fade-in');
            pageContainer.classList.add('fade-out');

            // Wait for the animation to finish
            setTimeout(function() {
                // Navigate to the new page (you can replace this with AJAX)
                window.location.href = link.getAttribute('href');
            }, 500);  // Wait 500ms (the duration of the fade-out animation)
        });
    });
});


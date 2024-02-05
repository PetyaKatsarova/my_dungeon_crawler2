document.getElementById('backToMenu').addEventListener('click', () => {
    window.location.href = '../html/main.html';
});

let inputRank = null;

document.getElementById('rankSearch').addEventListener('input', (event) => {
    const rank = event.target.value;
    inputRank = parseInt(rank);
    if(rank) {
        fetch(`http://localhost:8080/api/highscores/rank/${rank}`)
            .then(response => response.json())
            .then(data => {
                populateSearchResults(data, inputRank);
            })
            .catch(error => {
                console.error('Error searching by rank:', error);
            });
    } else {
        document.getElementById('searchResults').innerHTML = ''; // Clear the search results if input is empty
    }
});

document.getElementById('nameSearch').addEventListener('input', (event) => {
    const name = event.target.value;
    if(name) {
        fetch(`http://localhost:8080/api/highscores/name/${name}`)
            .then(response => response.json())
            .then(data => {
                populateSearchResults(data);
            })
            .catch(error => {
                console.error('Error searching by name:', error);
            });
    } else {
        document.getElementById('searchResults').innerHTML = ''; // Clear the search results if input is empty
    }
});

fetchLeaderboards();
function fetchLeaderboards() {
    fetch('http://localhost:8080/api/highscores/top?limit=10')
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
    const table = createTable(data, inputRank);
    leaderboardView.innerHTML = '';
    leaderboardView.appendChild(table);
}

function populateSearchResults(data, rank) {
    const searchResults = document.getElementById('searchResults');

    // If rank is defined, add the rank information to each item in the data array
    if (rank !== undefined && rank !== null) {
        data = data.map((item, index) => ({ ...item, rank: rank + index }));
    }

    // Limit the data to the top 5 results
    data = data.slice(0, 5);

    const table = createTable(data);
    searchResults.innerHTML = '';
    searchResults.appendChild(table);
}




function createTable(data, rank) {
    const table = document.createElement('table');
    table.className = 'centered-table';
    // Create table header
    const thead = document.createElement('thead');
    const tr = document.createElement('tr');
    tr.innerHTML = '<th>Rank</th><th>Email</th><th>Highscore</th>';
    thead.appendChild(tr);
    table.appendChild(thead);

    // Create table body
    const tbody = document.createElement('tbody');
    data.forEach((highscore, index) => {
        const tr = document.createElement('tr');

        // Use the rank information from the data array if present, otherwise use index + 1
        const displayRank = highscore.rank !== undefined ? highscore.rank : index + 1;

        if (highscore.rank !== undefined && index === 0) {
            tr.classList.add('highlight'); // Highlight the row if it is the first item in a rank search
        }

        tr.innerHTML = `<td>${displayRank}</td><td>${highscore.email}</td><td>${highscore.highscore}</td>`;
        tbody.appendChild(tr);
    });

    table.appendChild(tbody);
    return table;
}



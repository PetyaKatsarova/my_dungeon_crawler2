// toggle display/notDisplay answers on questions click
function toggleAnswer(element) {
    document.querySelectorAll(".faq-answer").forEach(answer => {
        if (answer !== element.nextElementSibling && answer.classList.contains('show')) {
            answer.classList.remove('show');
            answer.style.animation = 'collapse 0.5s ease forwards';
        }
    });
    const answer = element.nextElementSibling;
    if (answer.classList.contains('show')) {
        answer.classList.remove('show');
        answer.style.animation = 'collapse 0.5s ease forwards';
    } else {
        answer.classList.add('show');
        answer.style.animation = 'expand 0.5s ease forwards';
    }
}

// on question click shows the current answer and close all other answers
document.querySelectorAll(".faq-question").forEach(q => {
    q.addEventListener("click", ev => {
        toggleAnswer(q);
        ev.stopPropagation();
    });
});

// req and display monster: fetch is cleaner than ajax(http
const monster = document.getElementById("monster-display");
if (monster) {
    document.getElementById('monster-question').addEventListener('click', function () {
        fetch('http://localhost:8080/random-monster')  // Replace with your Spring Boot application's URL
            .then(response => response.json())
            .then(monster => {
                // monster object returned by the API
                const monsterInfoDiv = document.getElementById('monster-display');
                monsterInfoDiv.innerHTML = `
            <h3>Name: ${monster.name}</h3>
            <p>Health Points: ${monster.healthPoints}</p>
            <p>Gold Pieces: ${monster.gold}</p>
        `;
            })
            .catch(error => {
                document.getElementById('monster-display');
                monsterInfoDiv.innerHTML = `Oops, something went wrong. We couldn't fetch the monster...`;
            });
    });
}

// display page with transition.....todo


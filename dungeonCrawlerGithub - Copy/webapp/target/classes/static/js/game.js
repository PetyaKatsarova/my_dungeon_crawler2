const terminal = document.querySelector("#terminal");
const input = document.querySelector("#input");

function printMessage(message) {
    terminal.innerHTML += "\n" + message + "\n";
    terminal.scrollTop = terminal.scrollHeight;
}

input.addEventListener("keydown", (event) => {
    if (event.key === "Enter") {
        const command = input.value;
        input.value = "";
        executeCommand(command);
    }
});

function executeCommand(command) {
    printMessage("$ " + command);
    const {command: cmd, args} = parseCommand(command);

    switch (cmd) {
        case "help":
            handleHelp();
            break;
        case "createNewGame":
            handleCreateNewGame(args);
            break;
        case "move":
            handleMove(cmd, args);
            break;
        case "status":
            handleStatus();
            break;
        case "look":
            handleLook();
            break;
        case "open":
            handleOpen(cmd)
            break;
        case "pickup":
            handlePickup(cmd, args);
            break;
        case "drink":
            handleDrink(cmd, args);
            break;
        case "fight":
            handleFight(cmd);
            break;
        case "save":
            handleSave(cmd);
            break;
        case "cheatcode":
            handleCheatCode(args);
            break;
        default:
            printMessage("Unknown command. Type 'help' for a list of available commands.");
    }
}

function parseCommand(input) {
    const parts = input.trim().split(' ');
    const command = parts[0];
    const args = parts.slice(1);
    return {command, args};
}

function handleHelp() {
    printMessage("\nAvailable commands:");
    printMessage("- help: Display available commands");
    printMessage("- createNewGame rowsNumber columnsNumber: Create a new game with the specified rows and columns");
    printMessage("- status: Display your statistics and current location");
    printMessage("- look: Examine your surroundings in the current room");
    printMessage("- move DIRECTION: Move the player in the specified direction (e.g., 'north', 'east', 'south', 'west')");
    printMessage("- open: Open a chest in the room (if available)");
    printMessage("- pickup: Pick up an item (weapon, gold, potion) from the room");
    printMessage("- pickup holygrail: You win the game!");
    printMessage("- drink namePotion: Drink a health potion (if available)");
    printMessage("- fight: Engage in combat with a monster in the room (if available)");
    printMessage("- save: save the game your playing. You can only save your last game\n");
}

function handleCreateNewGame(args) {
    if (args.length !== 2) {
        printMessage("Usage: createNewGame rows columns");
        return;
    }
    const rows = args[0];
    const columns = args[1];
    if (!(rows >= 1 && rows <= 20 && columns >= 1 && columns <= 20)) {
        printMessage("Rows and columns should be a number from 1 to 20");
    } else {
        generateGrid(rows, columns);

        const urlGetAPI = 'http://localhost:8080/game/create?rows=' + rows + '&columns=' + columns;
        createGetRequest(urlGetAPI, succeedGame, failGettingGame);
    }
}

function handleMove(cmd, args) {
    const gameData = JSON.parse(sessionStorage.getItem('game'));
    const putUrl = urlPutAPI + "?command=" + cmd + "&argument=" + args

    if (gameData) {    // Validate input
        if (!args || args.length === 0) {
            console.log("No direction specified.");
            return;
        }

        // Logic to move the player in the specified direction
        const direction = args[0];
        const curRoom = currentRoom(gameData);
        const doors = curRoom.doors;

        // Check if there's a door in the specified direction
        let doorFound = false;
        for (const door of doors) {
            if (door.toLowerCase() === direction.toLowerCase()) { // There is a door in the specified direction
                doorFound = true;
                break;
            }

        }
        //Yes? make the move in direction
        if (doorFound) {
            createPutRequest(putUrl, succeedPut, failPut);
            printMessage('player had moved to new room');
        } else {
            printMessage('door does not exist')
        }
    }
}

function handleStatus() {
    const gameData = JSON.parse(sessionStorage.getItem('game'));
    if (gameData) {
        const player = gameData.gameOwner;
        printPlayerStatus(player);
    } else {
        printMessage("Game data not found. Make sure you have started a game.");
    }
}

function printPlayerStatus(crawler) {
    const weapon = crawler.weapon;
    const healthPotions = crawler.healthPotions;
    let displayPotion = '';

    if (healthPotions.length === 0) {
        displayPotion = 'No health potions';
    } else {
        for (const potion of healthPotions) {
            displayPotion += `${potion.itemName} (healingModifier: ${potion.healingModifier}), `;
        }
        displayPotion = displayPotion.slice(0, -2);
    }

    const statusMessage = `Player Status:
    - Name: ${crawler.name}
    - HP: ${crawler.healthPoints}
    - Weapon: ${weapon.itemName} (attackModifier: ${weapon.attackModifier})
    - Gold Points: ${crawler.gold}
    - Health Potions: ${displayPotion}`;

    printMessage(statusMessage);
}

function handleLook() {
    const gameData = JSON.parse(sessionStorage.getItem('game'));
    if (gameData) {
        const currRoom = currentRoom(gameData);
        printMessage(roomInfoMessage(currRoom));
    } else
        printMessage("Game data not found. Make sure you have started a game.");
}

function currentRoom(gameData) {
    let currentRoom = null;
    for (let i = 0; i < gameData.gameBoard.length; i++) {
        for (let j = 0; j < gameData.gameBoard[i].length; j++) {
            if (gameData.gameBoard[i][j].currentRoom === true)
                currentRoom = gameData.gameBoard[i][j];
        }
    }
    return currentRoom;
}

function roomInfoMessage(currentRoom) {
    const doors = currentRoom.doors;
    let numberOfObjects = 0;
    let msg = `You are in a room on row ${currentRoom.row} and column ${currentRoom.column}\nYou have ${doors.length} doors: `;

    for (let i = 0; i < doors.length; i++)
        msg += doors[i] + ", ";
    msg = msg.slice(0, -2) + "\n"; // removes the last ',' and space
    if (currentRoom.chest !== null)
        numberOfObjects++;
    if (currentRoom.containsGrail)
        numberOfObjects++;
    if (currentRoom.monster !== null)
        numberOfObjects++;
    if (currentRoom.droppedItem != null)
        numberOfObjects++;
    let object = (numberOfObjects > 1) ? "objects" : "object";
    numberOfObjects > 0 ? msg += `You have ${numberOfObjects} ${object} in the room:` : msg += "This room is empty.";
    return msg + itemsRoomInfo(currentRoom);
}

function itemsRoomInfo(currentRoom) {
    let msg = "";
    if (currentRoom.monster != null)
        msg += `\nMonster ${currentRoom.monster.name} with HealthPoints ${currentRoom.monster.healthPoints} and gold ${currentRoom.monster.gold}`;
    if (currentRoom.chest != null)
        msg += `\none closed chest`;
    if (currentRoom.containsGrail)
        msg += "\n and the Holy Grail!";
    if (currentRoom.droppedItem != null)
        msg += droppedItemInfo(currentRoom.droppedItem);
    return msg;
}

function droppedItemInfo(item) {
    let msg = `\n`;
    if (item.type === 'Potion') {
        msg += `A ${item.itemName} containing ${item.healingModifier} health points`;
    } else if (item.type === 'Gold') {
        msg += `${item.itemName}`; // make it to start with capital
    } else if (item.type === 'Weapon') {
        msg += `A weapon ${item.itemName} with an attack modifier of ${item.attackModifier} hit points`;
    }
    return msg;
}

function handleOpen(cmd) {
    const gameData = JSON.parse(sessionStorage.getItem('game'));
    const putUrl = urlPutAPI + "?command=" + cmd;
    if (gameData) {
        const room = currentRoom(gameData);
        if (room.chest != null) {
            const chestContent = room.chest.item;
            if (chestContent.type === "Gold") {
                const goldValue = chestContent.value;
                const message = `You opened the chest, and ${goldValue} gold drops on the floor.`;
                printMessage(message);
            } else if (chestContent.type === "Weapon") {
                const weaponName = chestContent.itemName;
                const attackModifier = chestContent.attackModifier;
                const message = `You opened the chest, and a weapon called ${weaponName} with an attack modifier of ${attackModifier} drops on the floor.`;
                printMessage(message);
            } else if (chestContent.type === "Potion") {
                const potionName = chestContent.itemName;
                const message = `You opened the chest, and a ${potionName} drops on the floor.`;
                printMessage(message);
            } else if (chestContent.type === "Trap") {
                const damageValue = chestContent.damage;
                const message = `Ouch, that's painful! There was a trap inside the chest. You got ${damageValue} damage.`;
                printMessage(message);
            } else {
                printMessage('You opened the chest, but it was empty.');
            }
            createPutRequest(putUrl, succeedPut, failPut);
            setTimeout(function() {
                const nieuweGameData = JSON.parse(sessionStorage.getItem('game'));
                if (nieuweGameData.gameOwner === null) {
                    sessionStorage.removeItem("game");
                    printMessage("You died. GAME OVER!");
                    setTimeout(function () {
                        window.location.href = '../html/defeat.html';
                    }, 3000); // 3 seconden
                }
            }, 500);
        } else {
            printMessage('There is no chest inside the room')
        }
    }
}

function handlePickup(cmd, args) {
    const gameData = JSON.parse(sessionStorage.getItem('game'));
    const putUrl = urlPutAPI + "?command=" + cmd;
    const putUrlHolyGrail = urlPutAPI + "?command=" + cmd + "&argument=" + args;
    const room = currentRoom(gameData);
    const droppedItem = room.droppedItem;

    function onHolyGrailSuccess() {
        const jwtToken = localStorage.getItem('token');

        fetch(putUrlHolyGrail, {
            method: 'PUT',
            headers: {
                'Authorization': jwtToken,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(gameData)
        })
            .then(response => response.json())
            .then(data => {
                sessionStorage.removeItem("game");
                printMessage("You won. GAME OVER!");

                setTimeout(function() {
                    window.location.href = '../html/victory.html';
                }, 3000);
            })
            .catch((error) => {
                console.error('Failed to end game:', error);
            });
    }

    function onHolyGrailFailure() {
        printMessage("Failed to pick up the Holy Grail.");
    }

    if (args[0] === "holygrail") {
        if (room.containsGrail) {
            createPutRequest(putUrlHolyGrail, onHolyGrailSuccess, onHolyGrailFailure);
        }else{
            printMessage("There is no Holy Grail in this room.")
        }
        return;
    }

    if (droppedItem) {
        if (droppedItem.type === "Potion") {
            printMessage(`You picked up a ${droppedItem.itemName}.`);
        } else if (droppedItem.type === "Weapon") {
            printMessage(`You picked up the ${droppedItem.itemName}.`);
        } else if (droppedItem.type === "Gold") {
            printMessage(`You picked up ${droppedItem.value} gold.`);
        }
        createPutRequest(putUrl, succeedPut, failPut);
    }
}

function handleDrink(cmd, args) {
    const gameData = JSON.parse(sessionStorage.getItem('game'));

    if (gameData) {
        const potionChoice = args[0];
        const potions = gameData.gameOwner.healthPotions;

        if (potions && potions.length > 0) {
            const putUrl = urlPutAPI + "?command=" + cmd + "&argument=" + potionChoice;
            createPutRequest(putUrl, succeedDrink, failDrink);
        } else {
            printMessage("You have no potions");
        }
    } else {
        printMessage("Game data not found. Make sure you have started a game.");
    }
}

function succeedDrink(data) {
    sessionStorage.setItem('game', data);
    const gameData = JSON.parse(data);
    const gameOwner = gameData.gameOwner;
    const healthPoints = gameOwner ? gameOwner.healthPoints : "unknown";
    printMessage(`Potion consumed successfully. Your current health points: ${healthPoints}`);
}

function failDrink() {
    printMessage('Failed to consume potion') ;
}

const jwtToken = localStorage.getItem('token');

const createGetRequest = function (url, succeed, fail) {
    fetch(url, {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': jwtToken
        },
    })
        .then((response) => handleErrors(response))
        .then((data) => succeed(data))
        .catch((error) => fail(error))
};


const handleErrors = function (response) {
    if (!response.ok) {
        throw (response.status + ': ' + response.statusText);
    }
    return response.text();
}

const succeedGame = function (data) {
    // Convert data to JSON object if it is a string
    const gameData = (typeof data === 'string') ? JSON.parse(data) : data;

    sessionStorage.setItem('game', JSON.stringify(gameData));
    console.log("succeed data: " + gameData);

    printMessage("New game created");
    updateGameBoard(gameData); // Call updateGameBoard to update the cell colors
}

const failGettingGame = function (error) {
    console.log(error);
    printMessage("Failed to create a game");
}


const urlPutAPI = 'http://localhost:8080/game/command';
const createPutRequest = function (url, succeed, fail) {
    const sendGame = JSON.parse(sessionStorage.getItem('game'));
    console.log('senddata:', sendGame);
    fetch(url, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': jwtToken
        },
        body: JSON.stringify(sendGame)
    })
        .then((response) => handleErrors(response))
        .then((data) => succeed(data))
        .catch((error) => fail(error))
};


const succeedPut = function (data) {
    // Convert data to JSON object if it is a string
    const gameData = (typeof data === 'string') ? JSON.parse(data) : data;
    sessionStorage.setItem('game', JSON.stringify(gameData));
    updateGameBoard(gameData);     // Call updateGameBoard to update the cell colors
}

const failPut = function (error) {
    console.log(error);
}

// look at open
function handleSave(cmd) {
    const gameData = JSON.parse(sessionStorage.getItem('game'));
    const putUrl = urlPutAPI + "?command=" + cmd;
    if (gameData) {
        createPutRequest(putUrl, succeedPut, failPut);
        printMessage("Your game was successfully saved.");
    } else {
        printMessage('Game couldnt be saved');
    }
}

function handleFight(cmd) {
    const gameData = JSON.parse(sessionStorage.getItem('game'));
    const putUrl = urlPutAPI + "?command=" + cmd;

    if (gameData) {
        const room = currentRoom(gameData);
        if (room && room.monster != null) {
            const goldEarned = room.monster.gold;
            printMessage(room.monster.name + " fights back");
            const healthMonsterBefore = room.monster.healthPoints;
            const healthPlayerBefore = gameData.gameOwner.healthPoints;
            createPutRequest(putUrl, succeedPut, failPut, cmd);
            setTimeout(function() {
                handleFightOutcome(goldEarned, healthMonsterBefore, healthPlayerBefore);
            }, 500); // halve seconde
        }
    }
}

function handleFightOutcome(goldEarned, healthMonsterBefore, healthPlayerBefore) {
    const nieuweGameData = JSON.parse(sessionStorage.getItem('game'));
    if (nieuweGameData) {
        const nieuweRoom = currentRoom(nieuweGameData);
        if (nieuweRoom && nieuweRoom.monster != null) {
            if (nieuweGameData.gameOwner === null) {
                handleGameOver();
            } else {
                handleRoundResult(nieuweRoom.monster, nieuweGameData, healthMonsterBefore, healthPlayerBefore);
            }
        } else {
            handleMonsterDefeated(goldEarned, nieuweGameData);
        }
    }
}

function handleGameOver() {
    sessionStorage.removeItem("game");
    printMessage("You died. GAME OVER!");
    setTimeout(function() {
        window.location.href = '../html/defeat.html';
    }, 3000); // 3 seconden
}

function handleRoundResult(monster, nieuweGameData, healthMonsterBefore, healthPlayerBefore) {
    const healthMonsterAfter = monster.healthPoints;
    const healthPlayerAfter = nieuweGameData.gameOwner.healthPoints;
    const monsterName = monster.name;

    if (healthMonsterAfter < healthMonsterBefore) {
        printMessage(`You won this round. ${monsterName}'s new health: ${healthMonsterAfter}`);
    } else if (healthPlayerAfter < healthPlayerBefore) {
        printMessage(`${monsterName} won this round. Your new health: ${healthPlayerAfter}`);
    } else {
        printMessage("It was a draw!");
    }
}

function handleMonsterDefeated(goldEarned, nieuweGameData) {
    printMessage('The monster has been defeated!');
    printMessage(`You earned ${goldEarned} gold. Your total gold: ${nieuweGameData.gameOwner.gold}`);
}

function logout() {
    localStorage.removeItem('token');
    sessionStorage.removeItem('game');
    window.location.href = "../index.html";
}

function handleCheatCode(args) {
    const gameData = JSON.parse(sessionStorage.getItem('game'));
    const crawler = gameData.gameOwner;
    const room = currentRoom(gameData);

    if (args == 'PaulsLifeElixer') {
        crawler.healthPoints = 100;
    }

    if (args == 'MaxsSuperiorWeapon') {
        const newWeapon = {
            type: 'Weapon',
            id: 24,
            itemName: 'Boots_of_Butt_kicking',
            attackModifier: 10
        };
        crawler.weapon = newWeapon;
    }

    if (args == 'JerrysQuickVictory'){
        room.containsGrail = true;
        sessionStorage.setItem('game', JSON.stringify(gameData));
        executeCommand('pickup holygrail')
    }

    if (args == 'MariosMoneyBag') {
        crawler.gold += 100;
    }

    // Add a cheat for Petya to reveal the entire game map
    if (args == 'PetyasMapReveal') {
        let maxRooms = 0;
        for (let i = 0; i < gameData.gameBoard.length; i++) {
            for (let j = 0; j < gameData.gameBoard[i].length; j++) {
                gameData.gameBoard[i][j].visited = true;
                maxRooms += 1;
            }
        }
        crawler.roomsVisited = maxRooms;
    }

    if (args == 'DennisTouchOfDeath') {
        room.monster.healthPoints = 5;
    }

    sessionStorage.setItem('game', JSON.stringify(gameData));
}

function generateGrid(rows, columns) {
    const gridContainer = document.querySelector('.grid-container');

    // Clear any existing grid items
    gridContainer.innerHTML = '';

    // Update grid container styles dynamically
    gridContainer.style.gridTemplateRows = `repeat(${rows}, 1fr)`;
    gridContainer.style.gridTemplateColumns = `repeat(${columns}, 1fr)`;

    for (let i = 0; i < rows; i++) {
        for (let j = 0; j < columns; j++) {
            const cell = document.createElement('div');
            cell.className = 'grid-item';
            cell.dataset.row = i;
            cell.dataset.col = j;
            gridContainer.appendChild(cell);
        }
    }
}



function updateGameBoard(gameData) {
    // Step 1: Fetch Current Room
    const current = currentRoom(gameData);

    // Remove blinking class from all cells
    const allCells = document.querySelectorAll('.grid-item');
    allCells.forEach(cell => {
        cell.classList.remove('blinking');
        cell.style.backgroundColor = '';  // Clear the background color
    });

    // Step 2: Color the Cells for Visited Rooms
    for (let i = 0; i < gameData.gameBoard.length; i++) {
        for (let j = 0; j < gameData.gameBoard[i].length; j++) {
            const room = gameData.gameBoard[i][j];
            if (room.visited) {
                const visitedCell = document.querySelector(`.grid-item[data-row='${i}'][data-col='${j}']`);
                if (visitedCell) {
                    visitedCell.style.backgroundColor = 'blue';
                }
            }
        }
    }

    // Step 3: Add blinking to Current Room
    if (current) {
        const row = current.row;
        const col = current.column;
        const currentCell = document.querySelector(`.grid-item[data-row='${row}'][data-col='${col}']`);
        if (currentCell) {
            currentCell.classList.add('blinking');
        }
    }
}

const resumeGameValue = sessionStorage.getItem("resumeGame");

if (resumeGameValue === "true") {
    printMessage("Welcome back Crawler. Lets continue your journey to find the Holy Grail.");
    const urlResume = 'http://localhost:8080/game/resume';
    createGetRequest(urlResume, succeedGame, failGettingGame);
    sessionStorage.removeItem("resumeGame");
} else {
    printMessage("Welcome to HVA dungeonCrawler!\nif you want to see the available commands type help.\n" +
        "To start a new game type createNewGame {number of rows} {number of columns}");
}

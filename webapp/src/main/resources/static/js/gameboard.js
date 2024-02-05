function createBoard(rows, cols, specialRow, specialCol) {
    const board = document.getElementById("gameboard");
    board.style.width = `${cols * 52}px`; // cell width + left border + right border = 50px + 1px + 1px = 52px

    for (let i = 0; i < rows; i++) {
        for (let j = 0; j < cols; j++) {
            const cell = document.createElement("div");
            cell.className = "cell";
            if (i === specialRow && j === specialCol) {
                cell.classList.add("special");
                // cell.innerHTML = "\uD83E\uDE99 \uD83D\uDC09 \uD83C\uDFC6";  // 🪙 🐉 🏆
                const divMonster = cell.appendChild(document.createElement("div"));
                const divChest = cell.appendChild(document.createElement("div"));
                const divGrail = cell.appendChild(document.createElement("div"));
                divMonster.innerHTML = "\uD83D\uDC09";
                divChest.innerHTML = "🎁";
                divGrail.innerHTML = "\uD83C\uDFC6";
            }

            board.appendChild(cell);
        }
    }
}

// Create an 8x8 board with a special cell at row 2, column 3
createBoard(8, 8, 2, 3);

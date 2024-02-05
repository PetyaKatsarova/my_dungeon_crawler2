document.getElementById('fight-button').addEventListener('click', function () {
    console.log("clicked on fight-button");
    handleFight('fight');
})

document.getElementById('look-button').addEventListener('click', function () {
    console.log("clicked on look-button");
    handleLook();
})

document.getElementById('status-button').addEventListener('click', function () {
    console.log("clicked on status-button");
    handleStatus();
})

document.getElementById('open-button').addEventListener('click', function () {
    console.log("clicked on open-button");
    handleOpen('open');
})

document.getElementById('pickup-button').addEventListener('click', function () {
    console.log("clicked on pickup-button");
    handlePickup('pickup', 'item');
})

const moveUpButtonImage = document.getElementById('move-up');
moveUpButtonImage.addEventListener('click', function () {
    console.log("clicked on up-arrow");
    const direction = 'NORTH';
    handleMove('move', [direction]);
})
const moveLeftButtonImage = document.getElementById('move-left');
moveLeftButtonImage.addEventListener('click', function () {
    console.log("clicked on left-arrow");
    const direction = 'WEST';
    handleMove('move', [direction]);
})

const moveRightButtonImage = document.getElementById('move-right');
moveRightButtonImage.addEventListener('click', function () {
    console.log("clicked on right-arrow");
    const direction = 'EAST';
    handleMove('move', [direction]);
})

const moveDownButtonImage = document.getElementById('move-down');
moveDownButtonImage.addEventListener('click', function () {
    console.log("clicked on down-arrow");
    const direction = 'SOUTH';
    handleMove('move', [direction]);
})

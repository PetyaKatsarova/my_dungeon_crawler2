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

document.getElementById('move-up').addEventListener('click', function () {
    console.log("clicked on up-arrow");
    const direction = 'NORTH';
    handleMove('move', [direction]);
})
document.getElementById('move-left').addEventListener('click', function () {
    console.log("clicked on left-arrow");
    const direction = 'WEST';
    handleMove('move', [direction]);
})

document.getElementById('move-right').addEventListener('click', function () {
    console.log("clicked on right-arrow");
    console.log("clicked on right-arrow");
    const direction = 'EAST';
    handleMove('move', [direction]);
})
document.getElementById('move-down').addEventListener('click', function () {
    console.log("clicked on down-arrow");
    const direction = 'SOUTH';
    handleMove('move', [direction]);
})

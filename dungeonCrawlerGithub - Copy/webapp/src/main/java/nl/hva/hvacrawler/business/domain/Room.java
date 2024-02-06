package nl.hva.hvacrawler.business.domain;
/**
 * Authors: Micha Marsman and Petya Katsarova
 */

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.Random;

public class Room {
    private int         id;
    private int         row;
    private int         column;
    @JsonIgnore
    private Game        game;
    private Chest       chest;
    private Monster     monster;
    @JsonIgnore
    private int         healthMonster;
    @JsonIgnore
    private int         goldMonster;
    private Item        droppedItem;
    private boolean     visited;
    private boolean     currentRoom;
    private boolean     containsGrail;
    private List<Door>  doors;
    private final int   NUMBER_OPTIONS_FOR_ROOM = 4; // empty, has monster, has chest, has both

    private static final Random random = new Random();

    public Room() {
        this(null, null);
    }

    public Room(Game game, List<Door> doors) {
        this.id = 0; // auto increment, to be set later
        this.game = game;
        this.currentRoom = false;
        this.visited = false;
        this.containsGrail = false;
        this.droppedItem = null;
        this.doors = doors;
        this.row = 0;
        this.column = 0;
        randomlyAllocateContents();
    }

    private void randomlyAllocateContents() { // can have 1 monster or 1 chest, or 1 monster and 1 chest, or be empty
        int option = random.nextInt(NUMBER_OPTIONS_FOR_ROOM);

        switch (option) {
            case 0:
                Monster currMonster = new Monster();
                this.setMonster(currMonster);
                // TODO zijn deze 2 hieronder wel nodig
//                this.setGoldMonster(currMonster.getGold());
//                this.setHealthMonster(currMonster.getHealthPoints());
                break;
            case 1:
                this.setChest(new Chest());
                break;
            case 2:
                this.setMonster(new Monster());
                this.setChest(new Chest());
                break;
        }
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(boolean currentRoom) {
        this.currentRoom = currentRoom;
        if (currentRoom)
            this.setVisited(true);
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }


    public boolean isContainsGrail() {
        return containsGrail;
    }

    public void setContainsGrail(boolean containsGrail) {
        this.containsGrail = containsGrail;
    }

    public Chest getChest() {
        return chest;
    }

    public void setChest(Chest chest) {
        this.chest = chest;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public Monster getMonster() {
        return monster;
    }

    public void setMonster(Monster monster) {
        this.monster = monster;
    }

    public List<Door> getDoors() {
        return doors;
    }

    public void setDoors(List<Door> doors) {
        this.doors = doors;
    }

    public Item getDroppedItem() {
        return droppedItem;
    }

    public void setDroppedItem(Item droppedItem) {
        this.droppedItem = droppedItem;
    }

    public int getHealthMonster() {
        return healthMonster;
    }

    public void setHealthMonster(int healthMonster) {
        this.healthMonster = healthMonster;
    }

    public int getGoldMonster() {
        return goldMonster;
    }

    public void setGoldMonster(int goldMonster) {
        this.goldMonster = goldMonster;
    }

    @Override public String toString() {
        return "Room{" +
                "id=" + id +
                ", row=" + row +
                ", column=" + column +
                ", game=" + game +
                ", chest=" + chest +
                ", monster=" + monster +
                ", droppedItem=" + droppedItem +
                ", visited=" + visited +
                ", currentRoom=" + currentRoom +
                ", containsGrail=" + containsGrail +
                ", doors=" + doors +
                ", NUMBER_OPTIONS_FOR_ROOM=" + NUMBER_OPTIONS_FOR_ROOM +
                '}';
    }
}

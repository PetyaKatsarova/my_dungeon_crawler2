package nl.hva.hvacrawler.communication.dto;

import nl.hva.hvacrawler.business.domain.Chest;
import nl.hva.hvacrawler.business.domain.Door;
import nl.hva.hvacrawler.business.domain.Item;
import nl.hva.hvacrawler.business.domain.Monster;

import java.util.List;

public class GameRoomDTO {

    private int     id;
    private int     row;
    private int     column;
    private Chest   chest;
    private Item    droppedItem;
    private Monster monster;
    private boolean visited;
    private boolean currentRoom;
    private boolean containsGrail;
    private List<Door> doors;

    public GameRoomDTO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Chest getChest() {
        return chest;
    }

    public void setChest(Chest chest) {
        this.chest = chest;
    }

    public Item getDroppedItem() {
        return droppedItem;
    }

    public void setDroppedItem(Item droppedItem) {
        this.droppedItem = droppedItem;
    }

    public Monster getMonster() {
        return monster;
    }

    public void setMonster(Monster monster) {
        this.monster = monster;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(boolean currentRoom) {
        this.currentRoom = currentRoom;
    }

    public boolean isContainsGrail() {
        return containsGrail;
    }

    public List<Door> getDoors() {
        return doors;
    }

    public void setDoors(List<Door> doors) {
        this.doors = doors;
    }
    public boolean hasDoor(String door) {
        for (Door doorEnum : Door.values()) {
            if (doorEnum.name().equalsIgnoreCase(door)) {
                return true; // Door exists
            }
        }
        return false; // Door does not exist
    }

    @Override public String toString() {
        return "GameRoomDTO{" +
                "id=" + id +
                ", row=" + row +
                ", column=" + column +
                ", chest=" + chest +
                ", droppedItem=" + droppedItem +
                ", monster=" + monster +
                ", visited=" + visited +
                ", currentRoom=" + currentRoom +
                ", containsGrail=" + containsGrail +
                ", doors=" + doors +
                '}';
    }
}

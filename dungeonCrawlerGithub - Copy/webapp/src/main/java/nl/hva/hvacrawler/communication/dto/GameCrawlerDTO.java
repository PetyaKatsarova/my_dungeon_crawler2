package nl.hva.hvacrawler.communication.dto;

import nl.hva.hvacrawler.business.domain.Potion;
import nl.hva.hvacrawler.business.domain.Weapon;

import java.util.List;

public class GameCrawlerDTO {

    private int idCharacter;
    private String name;
    private int healthPoints;
    private int gold;
    private int kills;
    private int roomsVisited;
    private List<Potion> healthPotions;
    private Weapon weapon;

    public GameCrawlerDTO() {
    }

    public int getIdCharacter() {
        return idCharacter;
    }

    public void setIdCharacter(int idCharacter) {
        this.idCharacter = idCharacter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHealthPoints() {
        return healthPoints;
    }
    public void incrementRoomsVisited() {
        this.roomsVisited++;
    }

    public void setHealthPoints(int healthPoints) {
        this.healthPoints = healthPoints;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getRoomsVisited() {
        return roomsVisited;
    }

    public void setRoomsVisited(int roomsVisited) {
        this.roomsVisited = roomsVisited;
    }

    public List<Potion> getHealthPotions() {
        return healthPotions;
    }

    public void setHealthPotions(List<Potion> healthPotions) {
        this.healthPotions = healthPotions;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    @Override public String toString() {
        return "GameCrawlerDTO{" +
                "idCharacter=" + idCharacter +
                ", name='" + name + '\'' +
                ", healthPoints=" + healthPoints +
                ", gold=" + gold +
                ", kills=" + kills +
                ", roomsVisited=" + roomsVisited +
                ", healthPotions=" + healthPotions +
                ", weapon=" + weapon +
                '}';
    }
}

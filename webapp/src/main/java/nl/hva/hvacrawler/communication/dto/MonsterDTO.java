package nl.hva.hvacrawler.communication.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MonsterDTO {

    private final Logger logger = LoggerFactory.getLogger(MonsterDTO.class);
    private int idCharacter;
    private int healthPoints;
    private int gold;

    public MonsterDTO() {
        this(0,0,0);
    }

    public MonsterDTO(int idCharacter, int healthPoints, int gold) {
        this.idCharacter = idCharacter;
        this.healthPoints = healthPoints;
        this.gold = gold;
    }

    public int getIdCharacter() {
        return idCharacter;
    }

    public void setIdCharacter(int idCharacter) {
        this.idCharacter = idCharacter;
    }

    public int getHealthPoints() {
        return healthPoints;
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
}

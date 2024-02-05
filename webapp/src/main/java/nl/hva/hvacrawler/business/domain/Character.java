package nl.hva.hvacrawler.business.domain;

public abstract class Character {
    private int idCharacter;
    private String name;
    private int healthPoints;
    private int gold;

    public Character(String name, int healthPoints, int gold) {
        this.idCharacter = 0;
        this.name = name;
        this.healthPoints = healthPoints;
        this.gold = gold;
    }


    public String getName() {
        return name;
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

    public int getIdCharacter() {
        return idCharacter;
    }

    public void setIdCharacter(int idCharacter) {
        this.idCharacter = idCharacter;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override public String toString() {
        return "Character{" +
                "idCharacter=" + idCharacter +
                ", name='" + name + '\'' +
                ", healthPoints=" + healthPoints +
                ", gold=" + gold +
                '}';
    }
}

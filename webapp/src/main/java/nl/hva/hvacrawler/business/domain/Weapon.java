package nl.hva.hvacrawler.business.domain;


import com.fasterxml.jackson.annotation.JsonCreator;

public class Weapon extends Item{
    private int attackModifier; // between 1 - 10
    public Weapon() {
        this(1, "Weapon", 0);
    }

    @JsonCreator
    public Weapon(int id, String itemName, int attackModifier) {
        super(id, itemName);
        this.attackModifier = attackModifier;
    }

    public int getAttackModifier() {
        return attackModifier;
    }

    public void setAttackModifier(int attackModifier) {
        this.attackModifier = attackModifier;
    }
}

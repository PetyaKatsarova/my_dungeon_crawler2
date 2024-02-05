package nl.hva.hvacrawler.business.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Description:
 * Author: Petya Katsarova
 * Email: pskpetya@gmail.com
 * Created on: 10/08/2023 15:59
 */

// NB: Elke Health Potion geeft je er 5 HP bij, maar je komt nooit boven je begin waarde uit.
public class Potion extends Item {

    private int healingModifier;

    public Potion() {
        super(0, "Potion");
        this.healingModifier = 0;
    }

    public Potion(int id, String itemName) {
        super(id, itemName);
    }

    @JsonCreator
    public Potion(int id, String itemName, int healingModifier) {
        super(id, itemName);
        this.healingModifier = healingModifier;
    }

    public int getHealingModifier() {
        return healingModifier;
    }

    public void setHealingModifier(int healingModifier) {
        this.healingModifier = healingModifier;
    }

    @Override
    public String toString() {
        return "Potion{" +
                "healingModifier=" + healingModifier +
                '}';
    }
}

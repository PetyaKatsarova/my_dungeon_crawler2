package nl.hva.hvacrawler.business.domain;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Description:
 * Author: Petya Katsarova
 * Email: pskpetya@gmail.com
 * Created on: 10/08/2023 15:59
 */

public class Trap extends Item{

    private int damage;


    public Trap() {
        this(0, "Trap", 0);
    }

    @JsonCreator
    public Trap(int id, String itemName,  int damage) {
        super(id, itemName);
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    @Override public String toString() {
        return "Trap{" +
                "damage=" + damage +
                '}';
    }
}

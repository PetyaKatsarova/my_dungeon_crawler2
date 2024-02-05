package nl.hva.hvacrawler.business.domain;

/**
 * Description:
 * Author: Petya Katsarova
 * Email: pskpetya@gmail.com
 * Created on: 10/08/2023 15:59
 */

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Objects;

public class Gold extends Item {
    private int value;

    @JsonCreator
    public Gold(int id, String itemName, int value) {
        super(id, itemName);
        this.value = value;
    }

    public Gold() {
        super(0, "Gold");
        this.value = 0;
    }


    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Gold gold)) {
            return false;
        }
        return getValue() == gold.getValue();
    }

    @Override public int hashCode() {
        return Objects.hash(getValue());
    }

    @Override public String toString() {
        return "Gold{" +
                "value=" + value +
                " id = " + getId() + '}';
    }
}

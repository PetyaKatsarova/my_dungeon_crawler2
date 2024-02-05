package nl.hva.hvacrawler.business.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Description:
 * Author: Petya Katsarova
 * Email: pskpetya@gmail.com
 * Created on: 10/08/2023 15:59
 */
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

// Game object converted into json: deserialiazition of abstract Item
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Weapon.class),
        @JsonSubTypes.Type(value = Trap.class),
        @JsonSubTypes.Type(value = Potion.class),
        @JsonSubTypes.Type(value = Gold.class),
        @JsonSubTypes.Type(value = Nothing.class)
})
public abstract class Item {
    private int     id;
    @JsonIgnore
    private String name;

    public Item() {
        this.id = 0;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public Item(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getItemName() {
        return name;
    }


    //method call when item is picked up
    public void onPickup(Crawler crawler) {
        //default behavior is to do nothing
        //subclasses can override this method to add special behavior
        //for example holy grail onPickup is to end the game
        //a better weapon onPickup should give a choice to keep or discard the current equipped weapon
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setItemName(String itemName) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
package nl.hva.hvacrawler.business.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Description:
 * Author: Petya Katsarova
 * Email: pskpetya@gmail.com
 * Created on: 09/08/2023 13:43
 */
public class Chest {
    private static final Logger logger = LoggerFactory.getLogger(Chest.class);
    int                         id;
    Item                        item; // abstract class extended by Gold, Trap, Weapon, Nothing
    private final int           TRAP_PROBABILITY = 20; // in percentage = 20%
    private static Random       random = new Random();

    private String[] TYPES_ITEMS = {"weapon", "potion", "gold", "trap", "nothing"};

    public Chest() {
        this.id = 0;
        generateChest();
        logger.info("New Chest was created.");
    }

    public void generateChest() {
        if (random.nextInt(100) <= TRAP_PROBABILITY) {    // trapProbability is an int between 0 and 100%;
            item = new Trap(0, "Trap", 0);
            logger.info("New Trap");
        } else {
            addRandomItemInChest();
        }
    }

    // for later: ok de kansen op een wapen, GP of een health potion mag je mee experimenteren. Maak het aanpasbaar in db
    private void addRandomItemInChest() {
        List<String> typesItems = new ArrayList<>(Arrays.asList(TYPES_ITEMS));
        typesItems.remove("trap");
        String randomItem = typesItems.get(random.nextInt(typesItems.size()));

        switch (randomItem) {
            case "nothing":
                logger.info("Jammer, nothing in this chest but on the bright side: it was not a trap :)");
                item = new Nothing();
                logger.info("this chest is empty");
                break;
            case "weapon":
                item = new Weapon();
                logger.info("this chest has a weapon");
                break;
            case "potion":
                item = new Potion();
                logger.info("this chest has a health potion");
                break;
            case "gold":
                item = new Gold();
                logger.info("this chest has gold pieces");
                break;
            default:
                throw new IllegalStateException("Unexpected item: " + randomItem);
        }
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @Override public String toString() {
        return "Chest{" +
                "id=" + id +
                ", item=" + item +
                '}';
    }
}



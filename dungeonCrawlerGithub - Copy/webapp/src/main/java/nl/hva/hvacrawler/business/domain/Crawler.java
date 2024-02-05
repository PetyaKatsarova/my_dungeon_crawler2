package nl.hva.hvacrawler.business.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

public class Crawler extends Character {
    private final Logger    logger = LoggerFactory.getLogger(Crawler.class);
    @Getter
    private int             kills;
    @Getter
    private int             roomsVisited;
    @Getter
    private List<Potion>    healthPotions;
    @Getter
    private Weapon          weapon;
    @Getter
    @JsonIgnore
    private User            user;
    private final int       MAX_HEALTH_POINTS = 100;
    private final int       STARTING_GOLD = 0;

    public Crawler() {
        this("John Do");
    }

    public Crawler(String name) {
        super(name, 0, 0);
        initializeCrawler();
        logger.info("new Crawler created");
    }

    private void initializeCrawler() {
        this.healthPotions = new ArrayList<>();
        this.weapon = new Weapon();
        this.kills = 0;
        this.roomsVisited = 1;
        this.user = null;
        this.setHealthPoints(MAX_HEALTH_POINTS);
        this.setGold(STARTING_GOLD);
    }

    public Crawler(String name, int healthPoints, int gold, int kills, int roomsVisited) {
        super(name, healthPoints, gold);
        this.kills = kills;
        this.roomsVisited = roomsVisited;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public void setHealthPotions(List<Potion> healthPotions) {
        this.healthPotions = healthPotions;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setRoomsVisited(int roomsVisited) {
        this.roomsVisited = roomsVisited;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Crawler{" +
                "kills=" + kills +
                ", roomsVisited=" + roomsVisited +
                ", healthPotions=" + healthPotions +
                ", weapon=" + weapon +
                ", MAX_HEALTH_POINTS=" + MAX_HEALTH_POINTS +
                ", STARTING_GOLD=" + STARTING_GOLD +
                '}';
    }
}
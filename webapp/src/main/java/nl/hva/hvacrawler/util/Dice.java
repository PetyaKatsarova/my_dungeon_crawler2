package nl.hva.hvacrawler.util;

import org.springframework.stereotype.Component;

import java.util.Random;
@Component
public class Dice {

    private final Random random;

    public Dice() {
        random = new Random();
    }

    public int roll(int sides) {
        return random.nextInt(sides) + 1;
    }
}

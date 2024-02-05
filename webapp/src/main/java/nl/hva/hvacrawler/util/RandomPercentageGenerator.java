package nl.hva.hvacrawler.util;

import java.util.concurrent.ThreadLocalRandom;

public class RandomPercentageGenerator {

    public static double randomPercentageBetweenFiftyAndHundred() {
        return ThreadLocalRandom.current().nextDouble(0.5, 1.0);
    }
}

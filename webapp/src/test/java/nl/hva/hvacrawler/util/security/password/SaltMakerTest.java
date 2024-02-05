package nl.hva.hvacrawler.util.security.password;

import nl.hva.hvacrawler.util.security.password.SaltMaker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SaltMakerTest {

    SaltMaker saltMaker = new SaltMaker();

    public static final int SALT_LENGTH_TO_LOW = 0;
    public static final int SALT_LENGTH_TO_HIGH = 21;

    @Test
    public void setSaltLengthErrorTest() {
        assertThrows(IllegalArgumentException.class,  () -> saltMaker.setSaltLength(SALT_LENGTH_TO_LOW));
        assertThrows(IllegalArgumentException.class,  () -> saltMaker.setSaltLength(SALT_LENGTH_TO_HIGH));
        assertDoesNotThrow(() -> saltMaker.setSaltLength(SaltMaker.MINIMUM_SALT_LENGTH));
        assertDoesNotThrow(() -> saltMaker.setSaltLength(SaltMaker.MAXIMUM_SALT_LENGTH));
    }

    @Test
    public void generateSaltLengthOutcomeTest() {
        int expectedLength1 = 30; // moet deze als magic number behandeld worden? moet het dan in
        // de methode gedaan worden of boven aan in de klasse?
        int expectedLength2 = 2;
        saltMaker.setSaltLength(15); // zelfde hier?
        int saltLength1 = saltMaker.generateSalt().length();
        saltMaker.setSaltLength(1);
        int saltLength2 = saltMaker.generateSalt().length();
        assertEquals(expectedLength1, saltLength1);
        assertEquals(expectedLength2, saltLength2);
    }
}
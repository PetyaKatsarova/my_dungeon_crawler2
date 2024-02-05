package nl.hva.hvacrawler.util.security.password;

import nl.hva.hvacrawler.util.security.password.HashService;
import nl.hva.hvacrawler.util.security.password.PepperService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HashServiceTest {

    PepperService pepperService = new PepperService();
    HashService hashService = new HashService(pepperService);
    public static final String WACHTWOORD = "IkWilErin!";
    public static final int ONLY_HASH_LENGTH = 64;


    @Test
    void hashAndSaltLengthTest() {
        int hashAndSalt = hashService.hash(WACHTWOORD).length();
        int expected = ONLY_HASH_LENGTH;
        assertNotEquals(expected, hashAndSalt);

    }
}
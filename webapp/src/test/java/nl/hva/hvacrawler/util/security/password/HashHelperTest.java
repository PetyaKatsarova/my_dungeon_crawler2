package nl.hva.hvacrawler.util.security.password;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HashHelperTest {

    public static final String WACHTWOORD1 = "IkWilErin!";
    public static final String WACHTWOORD2 = "Qwerty123!";
    public static final String WACHTWOORD3 = "@g3h3im";
    public static final String SALT1 = "vwf2qfqp29-f";
    public static final String SALT2 = "bwkwj4qdfq@f";
    public static final String SALT3 = "eg33g3g3g3g3";
    public static final String PEPPER = "ExtraSmaak";

    @Test
    public void testHash() {
        String hashed1 = "0e342a797e051ad8c95bbe472e0d9c72b842f900ac62e05c00a016915e5ff4f8";
        String hashed2 = "3875034e17855bac03a3cc9e107b1d28a9b44313d381c3335588525b4e70b55b";
        String hashed3 = "62dbce7281a12495af69a57750404d8ae8ae8a6300540627a040c14993fc8ca4";

        assertEquals(hashed1, HashHelper.hash(WACHTWOORD1));
        assertEquals(hashed2, HashHelper.hash(WACHTWOORD2));
        assertEquals(hashed3, HashHelper.hash(WACHTWOORD3));
    }

    @Test
    public void testSaltHash() {
        String hashed1 = "1e65c50e132ab567d1721e8cfcfdf2082cc53c9cdebe1e9aec457d22aa8d4214";
        String hashed2 = "bd0eaa6afce2576c98edff37f3f6af68dbedc6bb256c58a642f5235d4c6d96c3";
        String hashed3 = "acfca5cd6d8705cb7e8ee8aa17f1faff91d02c3f5ac2de26475331f36a3e4810";

        assertEquals(hashed1, HashHelper.hash(WACHTWOORD1, SALT1));
        assertEquals(hashed2, HashHelper.hash(WACHTWOORD2, SALT2));
        assertEquals(hashed3, HashHelper.hash(WACHTWOORD3, SALT3));
    }

    @Test
    public void testSaltAndPepperHash() {
        // even pepper lengte van 10 (5 voor en 5 achter)
        String hashed1 = "0b906b833bb81c898ad56c4c850908ce1d2d63b7bdb181257870fb7239ebe0d1";
        String hashed2 = "b1bd008ec4ef333e747ac8c4503222d1463d8aa635cd50d305534afdb1615a88";
        String hashed3 = "7997fff03b03aa72ee9b6ce686245ff6b04e9d6c237d631375364d21061ff976";

        assertEquals(hashed1, HashHelper.hash(WACHTWOORD1, SALT1, PEPPER));
        assertEquals(hashed2, HashHelper.hash(WACHTWOORD2, SALT2, PEPPER));
        assertEquals(hashed3, HashHelper.hash(WACHTWOORD3, SALT3, PEPPER));

        // oneven pepper lengte van 11 (5 voor en 6 achter)
        String hashed4 = "e34462cd923c14d0c3ece2d2bb09891595ee41318cf74b75d336322be44d35f5";
        String pepper2 = "ExtraSmaak!";
        assertEquals(hashed4, HashHelper.hash(WACHTWOORD1, SALT1, pepper2));

        // pepper lengte van 1 (pepper op het einde)
        String hashed5 = "7eeda774e24a06cb979bad84d0edaf7baffc3351775ae5e0936e1d2bcfcbf3e2";
        String pepper3 = "!";
        assertEquals(hashed5, HashHelper.hash(WACHTWOORD1, SALT1, pepper3));
    }
}
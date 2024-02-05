package nl.hva.hvacrawler.util.security.password;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordCheckerServiceTest {

    PasswordCheckerService passwordCheckerService = new PasswordCheckerService();

    public static final String PASSWORD_LENGTH_NINE = "wovg4nx84";
    public static final String PASSWORD_LENGTH_TEN = "wovg4nx84a";
    public static final String PASSWORD_LENGTH_SIXTYFOUR = "jR5sDvP9aJG2QzTc8uH6eRwCqY1iNfXo4mZlVbKtUxW9sYgMjR5sDvP9aJG2QzTc";
    public static final String PASSWORD_LENGTH_SIXTYFIVE =
            "xT7gZmL9vC1kVbNfIqY5wS6eH8uJ3aP9dR2sF1iN6fXoT7gZmL9vC1kVbNfIqY5w2";
    public static final String PASSWORD_REPEAT_FIVE = "wovggggg4nx84a";
    public static final String PASSWORD_REPEAT_FOUR = "wovgggg4nx84a";
    public static final String PASSWORD_CONSECUTIVE_NUMBERS_FOUR_ASCENDING = "wovg4nx56784a";
    public static final String PASSWORD_CONSECUTIVE_NUMBERS_FIVE_ASCENDING = "wovg4nx456784a";
    public static final String PASSWORD_CONSECUTIVE_NUMBERS_FOUR_DESCENDING = "wovg4nx8765a";
    public static final String PASSWORD_CONSECUTIVE_NUMBERS_FIVE_DESCENDING = "wovg4nx87654a";
    public static final String PASSWORD_COMMON_PASSWORD_BEGIN = "dragonwovg4nx87654a";
    public static final String PASSWORD_COMMON_PASSWORD_MIDDLE = "wovg4nsupermanx87654a";
    public static final String PASSWORD_COMMON_PASSWORD_END = "wovg4nx87654acomputer";
    public static final String PASSWORD_SEQUENCE_BEGIN = "supermanwovg4nx87654a";
    public static final String PASSWORD_SEQUENCE_MIDDLE = "wovg4nxsuperman87654a";
    public static final String PASSWORD_SEQUENCE_END = "wovg4nx87654superman";

    @Test
    void isPasswordValidLengthCheck() {
        assertFalse(passwordCheckerService.isPasswordValid(PASSWORD_LENGTH_NINE));
        assertFalse(passwordCheckerService.isPasswordValid(PASSWORD_LENGTH_SIXTYFIVE));
        assertTrue(passwordCheckerService.isPasswordValid(PASSWORD_LENGTH_TEN));
        assertTrue(passwordCheckerService.isPasswordValid(PASSWORD_LENGTH_SIXTYFOUR));
    }

    @Test
    void isPasswordValidConsecutiveRepeatsCheck() {
        assertFalse(passwordCheckerService.isPasswordValid(PASSWORD_REPEAT_FIVE));
        assertTrue(passwordCheckerService.isPasswordValid(PASSWORD_REPEAT_FOUR));
    }

    @Test
    void isPasswordValidConsecutiveNumbersCheck() {
        assertFalse(passwordCheckerService.isPasswordValid(PASSWORD_CONSECUTIVE_NUMBERS_FIVE_ASCENDING));
        assertFalse(passwordCheckerService.isPasswordValid(PASSWORD_CONSECUTIVE_NUMBERS_FIVE_DESCENDING));
        assertTrue(passwordCheckerService.isPasswordValid(PASSWORD_CONSECUTIVE_NUMBERS_FOUR_ASCENDING));
        assertTrue(passwordCheckerService.isPasswordValid(PASSWORD_CONSECUTIVE_NUMBERS_FOUR_DESCENDING));
    }

    @Test
    void isPasswordValidCommonPasswordCheck() {
        assertFalse(passwordCheckerService.isPasswordValid(PASSWORD_COMMON_PASSWORD_BEGIN));
        assertFalse(passwordCheckerService.isPasswordValid(PASSWORD_COMMON_PASSWORD_MIDDLE));
        assertFalse(passwordCheckerService.isPasswordValid(PASSWORD_COMMON_PASSWORD_END));
    }

    @Test
    void isPasswordValidSequenceCheck() {
        assertFalse(passwordCheckerService.isPasswordValid(PASSWORD_SEQUENCE_BEGIN));
        assertFalse(passwordCheckerService.isPasswordValid(PASSWORD_SEQUENCE_MIDDLE));
        assertFalse(passwordCheckerService.isPasswordValid(PASSWORD_SEQUENCE_END));
    }
}
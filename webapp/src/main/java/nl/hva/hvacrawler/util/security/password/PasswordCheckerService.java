package nl.hva.hvacrawler.util.security.password;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service
public class PasswordCheckerService {

    private final Logger logger = LoggerFactory.getLogger(PasswordCheckerService.class);
    private static final int MINIMUM_PASSWORD_LENGTH = 10;
    private static final int MAXIMUM_PASSWORD_LENGTH = 64;
    private static final int CHECK_CONSECUTIVE_REPEATS_LENGTH = 5;
    private static final int CHECK_CONSECUTIVE_NUMBERS_LENGTH = 5;
    private static final String FILE_PATH_COMMON_PASSWORDS = "src/main/resources/easyPasswords.txt";

    public PasswordCheckerService() {
        logger.info("New PasswordChecker");
    }

    public boolean isPasswordValid(String password) {
        return password.length() >= MINIMUM_PASSWORD_LENGTH &&
                password.length() <= MAXIMUM_PASSWORD_LENGTH &&
                !checkConsecutiveRepeats(password, CHECK_CONSECUTIVE_REPEATS_LENGTH) &&
                !checkConsecutiveNumbers(password, CHECK_CONSECUTIVE_NUMBERS_LENGTH) &&
                !checkCommonAndSequencePassword(password, FILE_PATH_COMMON_PASSWORDS);
    }

    private boolean checkConsecutiveNumbers(String password, int lengthNumber) {
        for (int i = 0; i <= password.length() - lengthNumber; i++) {
            String substring = password.substring(i, i + lengthNumber);
            if (isAscending(substring) || isDescending(substring)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAscending(String text) {
        return "0123456789".contains(text);
    }

    private boolean isDescending(String text) {
        return "0987654321".contains(text);
    }

    private boolean checkConsecutiveRepeats(String text, int maxRepeat) {
        char prevChar = '\0';
        int repeatCount = 1;
        for (char currentChar : text.toCharArray()) {
            if (currentChar == prevChar) {
                repeatCount++;
                if (repeatCount == maxRepeat) {
                    return true;
                }
            } else {
                repeatCount = 1;
                prevChar = currentChar;
            }
        }
        return false;
    }

    private boolean checkCommonAndSequencePassword(String password, String path) {
        String lowercaseText = password.toLowerCase();

        for (String sequence : loadQwertySequences(path)) {
            if (lowercaseText.contains(sequence)) {
                return true;
            }
        }
        return false;
    }

    private String[] loadQwertySequences(String path) {
        try (Stream<String> lines = Files.lines(Paths.get(path))) {
            return lines.toArray(String[]::new);
        } catch (IOException e) {
            e.printStackTrace();
            return new String[0];
        }
    }
}

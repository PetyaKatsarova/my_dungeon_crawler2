package nl.hva.hvacrawler.util.security.token;
import java.util.Random;
import org.springframework.stereotype.Component;

@Component
public class ResetToken {

    public String generateToken() {
        Random random = new Random();
        int randomNumber = random.nextInt(999999);
        StringBuilder output = new StringBuilder(Integer.toString(randomNumber));

        while (output.length() < 6) {
            output.insert(0, "0");
        }
        return output.toString();
    }
}
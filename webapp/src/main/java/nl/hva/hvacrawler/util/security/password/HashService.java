package nl.hva.hvacrawler.util.security.password;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HashService {

    private static final int DEFAULT_ROUNDS = 1;
    private PepperService pepperService;
    private SaltMaker saltMaker;
    private int rounds;
    @Autowired
    public HashService(PepperService pepperService) {
        this(pepperService, DEFAULT_ROUNDS);
    }

    public HashService(PepperService pepperService, int rounds) {
        this(pepperService, rounds, SaltMaker.STANDARD_SALT_LENGTH);
    }

    public HashService(PepperService pepperService, int rounds, int saltLength) {
        this.pepperService = pepperService;
        this.rounds = rounds;
        this.saltMaker = new SaltMaker(saltLength);
    }

    public String hash(String password){
        String salt = saltMaker.generateSalt();
        return hash(password, salt);
    }

    public String hash(String password, String salt){
        String pepper = pepperService.getPEPPER();
        String hash = HashHelper.hash(password, salt, pepper);
        String processedHash = processRounds(hash, numberOfRounds(rounds));
        return processedHash+salt; // De eerste reeks is 64 tekens
    }

    private String processRounds(String hash, long numberOfRounds){
        for (int i = 0; i < numberOfRounds; i++) {
            hash = HashHelper.hash(hash);
        }
        return hash;
    }

    private long numberOfRounds(int rounds){
        return rounds;
    }

}

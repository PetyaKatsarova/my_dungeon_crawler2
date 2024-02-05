package nl.hva.hvacrawler.persistence.dao;

import nl.hva.hvacrawler.util.security.token.VerificationToken;

public interface VerificationTokenDao {

    VerificationToken findByToken(String token);

    void save(VerificationToken verificationToken);

    void delete(VerificationToken token);

    int findUserIdByToken(String token);
}

package nl.hva.hvacrawler.persistence.repository;
import nl.hva.hvacrawler.business.domain.Highscore;
import nl.hva.hvacrawler.business.domain.User;
import nl.hva.hvacrawler.persistence.dao.JdbcUserDao;
import nl.hva.hvacrawler.persistence.dao.JdbcVerificationTokenDao;

import nl.hva.hvacrawler.util.security.token.VerificationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {
    private final Logger logger = LoggerFactory.getLogger(UserRepository.class);

    private final JdbcUserDao jdbcUserDao;
    private final JdbcVerificationTokenDao jdbcVerificationTokenDao;

    public UserRepository(JdbcUserDao jdbcUserDao, JdbcVerificationTokenDao jdbcVerificationTokenDao) {
        super();
        logger.info("New UserRepository");
        this.jdbcUserDao = jdbcUserDao;
        this.jdbcVerificationTokenDao = jdbcVerificationTokenDao;
    }

   public User findUserByEmail(String email) {
        return jdbcUserDao.findUserByEmail(email);
    }

    public User saveUser(User user) {
        return jdbcUserDao.save(user);
    }

    public void saveVerificationToken(VerificationToken verificationToken) {
        jdbcVerificationTokenDao.save(verificationToken);
    }

    public VerificationToken findByToken(String token) {
        VerificationToken verificationToken = jdbcVerificationTokenDao.findByToken(token);
        if (verificationToken == null) {
            return null;
        }
        int idUser = jdbcVerificationTokenDao.findUserIdByToken(token);
        verificationToken.setUser(jdbcUserDao.findUserById(idUser));
        return verificationToken;
    }

    public boolean updateResetTokenForUser(String email, String token) {
        return jdbcUserDao.updateResetTokenForUser(email, token);
    }

    public void deleteToken(VerificationToken token) {
        jdbcVerificationTokenDao.delete(token);
    }

    public User updateUser(User user) {
        return jdbcUserDao.updateUser(user);
    }

    public User finduserByJwtToken(String jwtoken) {
        return jdbcUserDao.findUserByjwtToken(jwtoken);
    }

    public boolean updateUserIdentificatieToken(User user) {
        return jdbcUserDao.updateUserIdentificatieToken(user);
    }

    public User findUserById(int idUser) {
        return jdbcUserDao.findUserById(idUser);
    }

    public List<Highscore> getHighScoresAroundName(String name) {
        return jdbcUserDao.getHighScoresAroundName(name);
    }

    public List<Highscore> getHighScoreslist(int limit) {
        return jdbcUserDao.getHighScoreslist(limit);
    }

    public List<Highscore> getHighScoresAroundRank(int rank) {
        return jdbcUserDao.getHighScoresAroundRank(rank);
    }

}


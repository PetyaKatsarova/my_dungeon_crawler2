package nl.hva.hvacrawler.business.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import nl.hva.hvacrawler.communication.dto.GameCrawlerDTO;
import nl.hva.hvacrawler.communication.dto.GameDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class User {
    private final Logger logger = LoggerFactory.getLogger(User.class);
    private String password;
    private int idUser;
    private String email;
    private boolean isVerified;
    private String salt;
    private String resetToken;
    private String jwtToken;
    private int highscore;
    private final int scorePerGoldPoint = 50;
    private final int scorePerRemainingHP = 100;
    private final int scorePerKill = 100;
    private final int scoreWining = 1000;

    public User() {
        super();
        this.idUser = 0;
        this.email = "";
        this.password = "";
        this.isVerified = false;
        this.salt = null;
        this.resetToken = null;
        this.jwtToken = null;
        this.highscore = 0;
        logger.info("New User created");
    }

    public User(String email, String password) {
        super();
        this.idUser = 0;
        this.email = email;
        this.password = password;
        this.isVerified = false;
        this.salt = null;
        this.resetToken = null;
        this.jwtToken = null;
        this.highscore = 0;
        logger.info("New User created");
    }

    public User(String email, String password, String salt) {
        super();
        this.idUser = 0;
        this.email = email;
        this.password = password;
        this.isVerified = false;
        this.salt = salt;
        this.resetToken = null;
        this.jwtToken = null;
        this.highscore = 0;
        logger.info("New User created");
    }

    public User(String email, String password, String salt, boolean isVerified,
                String resetTokentoken, String jwtToken, int highscore) {
        super();
        this.idUser = 0;
        this.email = email;
        this.password = password;
        this.isVerified = isVerified;
        this.salt = salt;
        this.resetToken = resetTokentoken;
        this.jwtToken = jwtToken;
        this.highscore = highscore;
        logger.info("New User created");
    }

    public User(int idUser, String email, String password, String salt, boolean isVerified,
                String resetToken, String jwtToken, int highscore) {
        this.idUser = idUser;
        this.password = password;
        this.email = email;
        this.isVerified = isVerified;
        this.salt = salt;
        this.resetToken = resetToken;
        this.jwtToken = jwtToken;
        this.highscore = highscore;
        logger.info("New User created");
    }

        private int calculateGameScore(GameCrawlerDTO crawler) {
        int gameScore = (crawler.getGold() * scorePerGoldPoint) + (crawler.getKills() * scorePerKill) + (crawler.getHealthPoints() * scorePerRemainingHP);
        return gameScore;
    }
    public boolean checkAndUpdateHighScore(GameDTO gamedto) {
        int gameScore = 0;
        if (gamedto.getGameStatus() == GameDTO.GameStatus.FINISHED) {
            gameScore = (calculateGameScore(gamedto.getGameOwner()) + scoreWining);
        }
        else {
            gameScore = calculateGameScore(gamedto.getGameOwner());
        }
        ;
        if (gameScore > this.highscore) {
            this.highscore = gameScore;
            return true;
        }
        return false;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setResetToken(String token) {
        this.resetToken = token;
    }

    public String getResetToken() {
        return resetToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public int getHighscore() {
        return highscore;
    }

    public void setHighscore(int highscore) {
        this.highscore = highscore;
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User user)) {
            return false;
        }
        return Objects.equals(getPassword(), user.getPassword()) &&
                Objects.equals(getEmail(), user.getEmail());
    }

    @Override public int hashCode() {
        return Objects.hash(getPassword(), getEmail());
    }

    @Override public String toString() {
        return "User{" +
                "password='" + password + '\'' +
                ", idUser=" + idUser +
                ", email='" + email + '\'' +
                ", salt='" + salt + '\'' +
                '}';
    }
}

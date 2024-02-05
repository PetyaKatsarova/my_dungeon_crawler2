package nl.hva.hvacrawler.util.security.token;

import nl.hva.hvacrawler.business.domain.User;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class VerificationToken {
    private int id;
    private String token;
    private Date expirationTime;
    private User user;

    private static final int EXPIRATION_TIME = 15;

    public VerificationToken() {
    }

    public VerificationToken(String token, User user) {
        super();
        this.token = token;
        this.user = user;
        this.expirationTime = this.getTokenExpirationTime();
    }

    public VerificationToken(String token) {
        super();
        this.token = token;
        this.expirationTime = this.getTokenExpirationTime();
    }

    public VerificationToken(String token, Date expirationTime) {
        this.token = token;
        this.expirationTime = expirationTime;
    }

    public Date getTokenExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, EXPIRATION_TIME);
        return new Date(calendar.getTime().getTime());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof VerificationToken that)) {
            return false;
        }
        return Objects.equals(getToken(), that.getToken()) &&
                Objects.equals(getUser(), that.getUser());
    }

    @Override public int hashCode() {
        return Objects.hash(getToken(), getUser());
    }

    @Override public String toString() {
        return "VerificationToken{" +
                "token='" + token + '\'' +
                ", expirationTime=" + expirationTime +
                ", user=" + user +
                '}';
    }
}

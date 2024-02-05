package nl.hva.hvacrawler.business.domain;


public class Highscore {
    private String email;
    private int highscore;

    public Highscore(String email, int highscore) {
        this.email = email;
        this.highscore = highscore;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getHighscore() {
        return highscore;
    }
    public void setHighscore(int highscore) {
        this.highscore = highscore;
    }

}

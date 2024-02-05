package nl.hva.hvacrawler.persistence.dao;

import nl.hva.hvacrawler.business.domain.Highscore;
import nl.hva.hvacrawler.business.domain.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JdbcUserDaoTest {

    @Autowired
    private JdbcUserDao jdbcUserDaoTest;
   // private final JdbcUserDao jdbcUserDaoTest;

    // in de database
    private final User gebruiker1 = new User(1, "gebruiker1@example.com", "wachtwoord123",
            "zout123", true, "123456", "jwttoken123", 100);
    private final User gebruiker2 = new User(2, "gebruiker2@example.com", "wachtwoord456",
            "zout456", true, null, null, 150);

    private final User gebruiker3 = new User(3, "gebruiker3@example.com", "wachtwoord789",
            "zout789", true, null, "234567", 200);
    private final User gebruiker4 = new User(4, "gebruiker4@example.com", "wachtwoordabc",
            "zoutabc", false, null, null, 0);

    // niet in de database
    private final User gebruiker5 = new User(0, "gebruiker0@example.com", "wachtwoord00",
            "zout00", true, null, "jwttoken789", 1000);

    @Autowired
    public JdbcUserDaoTest(JdbcTemplate jdbcTemplate) {
        this.jdbcUserDaoTest = new JdbcUserDao(jdbcTemplate);
    }

    @Test
    void findUserByEmail() {
        User foundUser = jdbcUserDaoTest.findUserByEmail("gebruiker2@example.com");

        assertNotNull(foundUser);
        assertEquals("gebruiker2@example.com", foundUser.getEmail());
    }

    @Test
    void save() {
        User savedUser = jdbcUserDaoTest.save(gebruiker5);

        assertNotNull(savedUser);
        assertTrue(savedUser.getIdUser() > 0);
    }

    @Test
    void updateUser() {
        gebruiker1.setEmail("gewijzigdegebruiker@example.com");
        gebruiker1.setPassword("gewijzigdwachtwoord");
        User updatedUser = jdbcUserDaoTest.updateUser(gebruiker1);

        assertNotNull(updatedUser);
        assertEquals("gewijzigdegebruiker@example.com", updatedUser.getEmail());
        assertEquals("gewijzigdwachtwoord", updatedUser.getPassword());
    }

    @Test
    void findUserById() {
        User foundUser = jdbcUserDaoTest.findUserById(1);

        assertNotNull(foundUser);
        assertEquals(1, foundUser.getIdUser());
    }

    @Test
    void findUserByjwtToken() {
        User foundUser = jdbcUserDaoTest.findUserByjwtToken("jwttoken123");

        assertNotNull(foundUser);
        assertEquals("jwttoken123", foundUser.getJwtToken());
    }

    @Test
    void updateUserIdentificatieToken() {
        gebruiker3.setJwtToken("nieuwjwttoken");
        boolean updated = jdbcUserDaoTest.updateUserIdentificatieToken(gebruiker3);

        assertTrue(updated);

        User updatedUser = jdbcUserDaoTest.findUserByEmail("gebruiker3@example.com");
        assertEquals("nieuwjwttoken", updatedUser.getJwtToken());
    }
    @Test
    void getHighScoreslist_ValidLimit() {
        int limit = 2;
        List<Highscore> highscores = jdbcUserDaoTest.getHighScoreslist(limit);

        assertNotNull(highscores);
        assertEquals(limit, highscores.size());

        // Verify the actual data
        assertEquals("gebruiker3@example.com", highscores.get(0).getEmail());
        assertEquals(200, highscores.get(0).getHighscore());
        assertEquals("gebruiker2@example.com", highscores.get(1).getEmail());
        assertEquals(150, highscores.get(1).getHighscore());
    }

    @Test
    void getHighScoresAroundName_ValidName() {
        String name = "gebruiker1";
        List<Highscore> highscores = jdbcUserDaoTest.getHighScoresAroundName(name);

        assertNotNull(highscores);
        assertFalse(highscores.isEmpty());

        // Verify the actual data
        assertEquals("gebruiker1@example.com", highscores.get(0).getEmail());
        assertEquals(100, highscores.get(0).getHighscore());
    }

    @Test
    void getHighScoresAroundRank_ValidRank() {
        int rank = 1; // Adjust rank as necessary based on your ranking logic
        List<Highscore> highscores = jdbcUserDaoTest.getHighScoresAroundRank(rank);

        assertNotNull(highscores);
        assertFalse(highscores.isEmpty());

        // Verify the actual data
        assertEquals("gebruiker3@example.com", highscores.get(0).getEmail());
        assertEquals(200, highscores.get(0).getHighscore());
        // Add more assertions to check other users around the rank
    }


}
package nl.hva.hvacrawler.persistence.repository;

import nl.hva.hvacrawler.business.domain.Highscore;
import nl.hva.hvacrawler.business.domain.User;
import nl.hva.hvacrawler.persistence.dao.JdbcUserDao;
import nl.hva.hvacrawler.persistence.dao.JdbcVerificationTokenDao;
import nl.hva.hvacrawler.util.security.token.VerificationToken;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserRepositoryTest {

    private UserRepository userRepositoryUnderTest;
    private JdbcUserDao jdbcUserDao;
    private JdbcVerificationTokenDao jdbcVerificationTokenDao;

    private UserRepository userRepository;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @BeforeAll
    public void setup(){
        jdbcUserDao = Mockito.mock(JdbcUserDao.class);
        jdbcVerificationTokenDao = Mockito.mock(JdbcVerificationTokenDao.class);
        userRepositoryUnderTest = new UserRepository(jdbcUserDao, jdbcVerificationTokenDao);
    }

    @Test
    void findUserByEmail_ExistingUser() {
        when(jdbcUserDao.findUserByEmail("test@example.com"))
                .thenReturn(new User("test@example.com", "TestPassword"));

        User foundUser = userRepositoryUnderTest.findUserByEmail("test@example.com");

        assertNotNull(foundUser);
        assertEquals("test@example.com", foundUser.getEmail());
        assertEquals("TestPassword", foundUser.getPassword());
    }

    @Test
    void findUserByEmail_ExistingUser_integration_test(){
        User foundUser = userRepository.findUserByEmail("gebruiker2@example.com");
        assertNotNull(foundUser);
        assertEquals("gebruiker2@example.com", foundUser.getEmail());
        assertEquals("wachtwoord456", foundUser.getPassword());
    }

    @Test
    void findUserByEmail_NonExistingUser() {
        when(jdbcUserDao.findUserByEmail("nonexistent@example.com"))
                .thenReturn(null);

        User foundUser = userRepositoryUnderTest.findUserByEmail("nonexistent@example.com");

        assertNull(foundUser);
    }

    @Test
    void saveUser_Success() {
        when(jdbcUserDao.save(any(User.class)))
                .thenReturn(new User("test@example.com", "TestPassword"));

        User newUser = new User("test@example.com", "TestPassword");
        User savedUser = userRepositoryUnderTest.saveUser(newUser);

        assertNotNull(savedUser);
        assertEquals("test@example.com", savedUser.getEmail());
        assertEquals("TestPassword", savedUser.getPassword());
    }

    @Test
    void findByToken_ExistingToken() {
        VerificationToken token = new VerificationToken("testToken");
        when(jdbcVerificationTokenDao.findByToken("testToken")).thenReturn(token);
        when(jdbcUserDao.findUserById(anyInt())).thenReturn(new User("test@example.com", "TestPassword"));
        VerificationToken foundToken = userRepositoryUnderTest.findByToken("testToken");

        assertNotNull(foundToken);
        assertEquals("testToken", foundToken.getToken());

        User associatedUser = foundToken.getUser();
        assertNotNull(associatedUser);
        assertEquals("test@example.com", associatedUser.getEmail());
    }

    @Test
    void findByToken_NonExistingToken() {
        when(jdbcVerificationTokenDao.findByToken("nonexistentToken")).thenReturn(null);
        VerificationToken foundToken = userRepositoryUnderTest.findByToken("nonexistentToken");
        assertNull(foundToken);
    }

    @Test
    void updateResetTokenForUser_UserExists() {
        when(jdbcUserDao.updateResetTokenForUser(eq("test@example.com"), anyString())).thenReturn(true);
        boolean result = userRepositoryUnderTest.updateResetTokenForUser("test@example.com", "newToken");

        assertTrue(result);
    }

    @Test
    void updateResetTokenForUser_UserDoesNotExist() {
        when(jdbcUserDao.updateResetTokenForUser(eq("nonexistent@example.com"), anyString())).thenReturn(false);
        boolean result = userRepositoryUnderTest.updateResetTokenForUser("nonexistent@example.com", "newToken");

        assertFalse(result);
    }

    @Test
    void deleteToken() {
        VerificationToken token = new VerificationToken("testToken", new User("test@example.com", "TestPassword"));
        userRepositoryUnderTest.deleteToken(token);

        verify(jdbcVerificationTokenDao, times(1)).delete(token);
    }


    @Test
    void updateUser() {
        User userToUpdate = new User("test@example.com", "UpdatedPassword");
        when(jdbcUserDao.updateUser(userToUpdate)).thenReturn(userToUpdate);
        User updatedUser = userRepositoryUnderTest.updateUser(userToUpdate);

        assertNotNull(updatedUser);
        assertEquals("test@example.com", updatedUser.getEmail());
        assertEquals("UpdatedPassword", updatedUser.getPassword());
    }

    @Test
    void finduserByJwtToken() {
        String jwtToken = "someJwtToken";
        when(jdbcUserDao.findUserByjwtToken(jwtToken)).thenReturn(new User("test@example.com", "TestPassword"));
        User foundUser = userRepositoryUnderTest.finduserByJwtToken(jwtToken);

        assertNotNull(foundUser);
        assertEquals("test@example.com", foundUser.getEmail());
        assertEquals("TestPassword", foundUser.getPassword());
    }

    @Test
    void updateUserIdentificatieToken() {
        User user = new User("test@example.com", "TestPassword");
        when(jdbcUserDao.updateUserIdentificatieToken(user)).thenReturn(true);
        boolean result = userRepositoryUnderTest.updateUserIdentificatieToken(user);

        assertTrue(result);
    }

    @Test
    void findUserById_UserExists() {
        int userId = 1;
        when(jdbcUserDao.findUserById(userId)).thenReturn(new User("test@example.com", "TestPassword"));
        User foundUser = userRepositoryUnderTest.findUserById(userId);

        assertNotNull(foundUser);
        assertEquals("test@example.com", foundUser.getEmail());
        assertEquals("TestPassword", foundUser.getPassword());
    }

    @Test
    void findUserById_UserDoesNotExist() {
        int userId = 2;
        when(jdbcUserDao.findUserById(userId)).thenReturn(null);
        User foundUser = userRepositoryUnderTest.findUserById(userId);

        assertNull(foundUser);
    }
    @Test
    void getHighScoreslist_ValidLimit() {
        List<Highscore> expectedHighscores = List.of(
                new Highscore("player1", 100),
                new Highscore("player2", 90)
        );
        when(jdbcUserDao.getHighScoreslist(2)).thenReturn(expectedHighscores);

        List<Highscore> actualHighscores = userRepositoryUnderTest.getHighScoreslist(2);

        assertEquals(expectedHighscores, actualHighscores);
    }

    @Test
    void getHighScoresAroundName_ValidName() {
        List<Highscore> expectedHighscores = List.of(
                new Highscore("player1", 100),
                new Highscore("player2", 90)
        );
        when(jdbcUserDao.getHighScoresAroundName("player1")).thenReturn(expectedHighscores);

        List<Highscore> actualHighscores = userRepositoryUnderTest.getHighScoresAroundName("player1");

        assertEquals(expectedHighscores, actualHighscores);
    }

    @Test
    void getHighScoresAroundRank_ValidRank() {
        List<Highscore> expectedHighscores = List.of(
                new Highscore("player1", 100),
                new Highscore("player2", 90),
                new Highscore("player3", 80)
        );
        when(jdbcUserDao.getHighScoresAroundRank(2)).thenReturn(expectedHighscores);

        List<Highscore> actualHighscores = userRepositoryUnderTest.getHighScoresAroundRank(2);

        assertEquals(expectedHighscores, actualHighscores);
    }

}
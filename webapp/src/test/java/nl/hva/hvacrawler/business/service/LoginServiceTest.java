package nl.hva.hvacrawler.business.service;

import nl.hva.hvacrawler.business.domain.User;
import nl.hva.hvacrawler.persistence.dao.UserDao;
import nl.hva.hvacrawler.persistence.repository.UserRepository;
import nl.hva.hvacrawler.util.HashAndSaltUtil;
import nl.hva.hvacrawler.util.security.password.HashService;
import nl.hva.hvacrawler.util.security.password.PasswordCheckerService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

//unit test
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LoginServiceTest {
    @Mock
    private HashService mockHashService = mock(HashService.class);
    @Mock
    private PasswordCheckerService mockPasswordCheckerService = mock(PasswordCheckerService.class);
    @Mock
    UserRepository mockRepo = mock(UserRepository.class);
    @Mock
    private HashAndSaltUtil hashAndSaltUtil;
    private final String salt = "e6df9d59baed164d";
    private final String hashedPW = "3f2eac66d068c382495e693c680cafc7058c3e7e6cf2f68fa8773c1447442dd6";
    @InjectMocks
    private LoginService loginServiceUnderTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        User testUser =  new User("paul@hva.nl", hashedPW, salt);
        testUser.setVerified(true);
        when(mockRepo.findUserByEmail("paul@hva.nl")).thenReturn(testUser);

        when(mockHashService.hash("Malorcahetspaanseeiland", salt)).thenReturn(hashedPW);

    }


    @Test
    void authenticateCorrectLogin() {
        //if user exist and password is correct
        User testuserCorrect = new User("paul@hva.nl", "Malorcahetspaanseeiland", salt);
        boolean actual = loginServiceUnderTest.authenticate(testuserCorrect);
        boolean expected = true;
        assertThat(actual).isEqualTo(expected);
    }
    @Test
    void authenicateIncorrectLogin() {
        //if provided email does exist but password is wrong
        User testuserIncorrect = new User("paul@hva.nl", "ibiza");
        boolean actual = loginServiceUnderTest.authenticate(testuserIncorrect);
        assertThat(actual).isFalse();
    }

}
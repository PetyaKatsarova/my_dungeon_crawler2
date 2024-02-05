package nl.hva.hvacrawler.business.service;

import nl.hva.hvacrawler.business.domain.Crawler;
import nl.hva.hvacrawler.business.domain.User;
import nl.hva.hvacrawler.communication.dto.RegistrationDTO;
import nl.hva.hvacrawler.exception.CrawlerAlreadyExistsException;
import nl.hva.hvacrawler.exception.PasswordNotValidException;
import nl.hva.hvacrawler.exception.UserAlreadyExistsException;
import nl.hva.hvacrawler.persistence.repository.CrawlerRepository;
import nl.hva.hvacrawler.persistence.repository.UserRepository;
import nl.hva.hvacrawler.util.security.password.HashService;
import nl.hva.hvacrawler.util.security.password.PasswordCheckerService;
import nl.hva.hvacrawler.util.security.token.VerificationToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RegistrationServiceTest {

    @Mock
    private static UserRepository mockUserRepo = Mockito.mock(UserRepository.class);
    @Mock
    private static CrawlerRepository mockCrawlerRepo = Mockito.mock(CrawlerRepository.class);
    @Mock
    private static HashService mockHashService = Mockito.mock(HashService.class);
    @Mock
    private static PasswordCheckerService mockPasswordCheckerService =
            Mockito.mock(PasswordCheckerService.class);

    @InjectMocks
    private RegistrationService registrationService;

    @BeforeEach
    public void testSetup(){
        MockitoAnnotations.openMocks(this);
        when(mockUserRepo.findUserByEmail("michamarsman@hotmail.com")).thenReturn(
                new User("michamarsman@hotmail.com", "Ditisookweereengeheimwachtwoord"));
    }

    @Test
    void register_NewUserAndCrawler_Success() {
        RegistrationDTO registrationDTO = new RegistrationDTO("test@example.com", "validPassword", "testName");

        when(mockUserRepo.findUserByEmail(registrationDTO.getEmail())).thenReturn(null);
        when(mockPasswordCheckerService.isPasswordValid(registrationDTO.getPassword())).thenReturn(true);
        when(mockCrawlerRepo.checkIfCrawlerAlreadyExists(registrationDTO.getName())).thenReturn(false);

        Crawler newCrawler = new Crawler(registrationDTO.getName());
        when(mockCrawlerRepo.saveOrUpdateOne(newCrawler)).thenReturn(newCrawler);

        String hashedSaltPassword =
                "45d38f2727a51933107bc9c809a2d56dac37de4f58c748cb756a44fe57425dc1a7b688ae3f1b72a1";
        when(mockHashService.hash(registrationDTO.getPassword())).thenReturn(hashedSaltPassword);

        String hashPassword = "45d38f2727a51933107bc9c809a2d56dac37de4f58c748cb756a44fe57425dc1";
        User newUser = new User(registrationDTO.getEmail(), hashPassword, "a7b688ae3f1b72a1");
        when(mockUserRepo.saveUser(newUser)).thenReturn(newUser);
        User registeredUser = registrationService.register(registrationDTO);

        verify(mockUserRepo, times(1)).saveUser(newUser);
        assertNotNull(registeredUser);
        assertEquals(registrationDTO.getEmail(), registeredUser.getEmail());
        assertEquals(hashPassword, registeredUser.getPassword());
    }

    @Test
    void register_UserAlreadyExists_Exception() throws Exception {
        RegistrationDTO registrationDTO = new RegistrationDTO("test@example.com", "validPassword", "testName");
        when(mockUserRepo.findUserByEmail(registrationDTO.getEmail())).thenReturn(new User());
        assertThrows(UserAlreadyExistsException.class, () -> registrationService.register(registrationDTO));
    }

    @Test
    void register_InvalidPassword_Exception() throws Exception {
        RegistrationDTO registrationDTO = new RegistrationDTO("test@example.com", "invalidPassword", "testName");
        when(mockUserRepo.findUserByEmail(registrationDTO.getEmail())).thenReturn(null);
        when(mockPasswordCheckerService.isPasswordValid(registrationDTO.getPassword())).thenReturn(false);
        assertThrows(PasswordNotValidException.class, () -> registrationService.register(registrationDTO));
    }

    @Test
    void register_CrawlerAlreadyExists_Exception() throws Exception {
        RegistrationDTO registrationDTO = new RegistrationDTO("test@example.com", "validPassword", "testName");
        when(mockPasswordCheckerService.isPasswordValid(registrationDTO.getPassword())).thenReturn(true);
        when(mockCrawlerRepo.checkIfCrawlerAlreadyExists(registrationDTO.getName())).thenReturn(true);
        assertThrows(CrawlerAlreadyExistsException.class, () -> registrationService.register(registrationDTO));
    }

    @Test
    void findUserByEmail_UserExists_ReturnsUser() {
        User user = registrationService.findUserByEmail("michamarsman@hotmail.com");
        verify(mockUserRepo).findUserByEmail("michamarsman@hotmail.com");
        assertNotNull(user);
        assertEquals("michamarsman@hotmail.com", user.getEmail());
        assertEquals("Ditisookweereengeheimwachtwoord", user.getPassword());
    }

    @Test
    void findUserByEmail_UserDoesNotExist_ReturnsNull() {
        when(mockUserRepo.findUserByEmail("nonexistent@example.com")).thenReturn(null);
        User user = registrationService.findUserByEmail("nonexistent@example.com");
        verify(mockUserRepo).findUserByEmail("nonexistent@example.com");
        assertNull(user);
    }

    @Test
    void saveUserVerificationToken() {
        User user = new User("test@example.com", "password");
        String token = "verificationToken";
        VerificationToken verificationToken = new VerificationToken(token, user);

        doNothing().when(mockUserRepo).saveVerificationToken(verificationToken);
        registrationService.saveUserVerificationToken(user, token);

        verify(mockUserRepo, times(1)).saveVerificationToken(verificationToken);
    }

    @Test
    void validateToken_ValidToken() {
        String validToken = "validToken";
        User user = new User("test@example.com", "password");
        VerificationToken verificationToken = new VerificationToken(validToken, user);

        when(mockUserRepo.findByToken(validToken)).thenReturn(verificationToken);
        String result = registrationService.validateToken(validToken);

        verify(mockUserRepo, times(1)).findByToken(validToken);
        assertEquals("valid", result);
        assertTrue(user.isVerified());
    }

    @Test
    void validateToken_ExpiredToken() {
        String expiredToken = "expiredToken";
        User user = new User("test@example.com", "password");
        VerificationToken verificationToken = new VerificationToken(expiredToken, user);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR, -1); // Set the expiration time to 1 hour ago
        verificationToken.setExpirationTime(new Date(calendar.getTimeInMillis()));

        when(mockUserRepo.findByToken(expiredToken)).thenReturn(verificationToken);
        String result = registrationService.validateToken(expiredToken);

        verify(mockUserRepo, times(1)).findByToken(expiredToken);
        assertEquals("Token already expired", result);
        assertFalse(user.isVerified());
    }

    @Test
    void validateToken_InvalidToken() {
        String invalidToken = "invalidToken";
        when(mockUserRepo.findByToken(invalidToken)).thenReturn(null);
        String result = registrationService.validateToken(invalidToken);

        verify(mockUserRepo, times(1)).findByToken(invalidToken);
        assertEquals("Invalid verification token", result);
    }

    @Test
    void findByToken() {
        String token = "verificationToken";
        User user = new User("test@example.com", "password");
        VerificationToken verificationToken = new VerificationToken(token, user);

        when(mockUserRepo.findByToken(token)).thenReturn(verificationToken);
        VerificationToken result = registrationService.findByToken(token);

        verify(mockUserRepo, times(1)).findByToken(token);
        assertEquals(verificationToken, result);
    }
}

package nl.hva.hvacrawler.business.service;

import nl.hva.hvacrawler.business.domain.Crawler;
import nl.hva.hvacrawler.business.domain.User;
import nl.hva.hvacrawler.communication.dto.RegistrationDTO;
import nl.hva.hvacrawler.exception.PasswordNotValidException;
import nl.hva.hvacrawler.exception.UserAlreadyExistsException;
import nl.hva.hvacrawler.exception.CrawlerAlreadyExistsException;
import nl.hva.hvacrawler.persistence.repository.CrawlerRepository;
import nl.hva.hvacrawler.persistence.repository.UserRepository;

import nl.hva.hvacrawler.util.security.password.HashService;
import nl.hva.hvacrawler.util.security.password.PasswordCheckerService;
import nl.hva.hvacrawler.util.security.token.VerificationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Calendar;

import static nl.hva.hvacrawler.util.HashAndSaltUtil.getHashFromHashSalt;
import static nl.hva.hvacrawler.util.HashAndSaltUtil.getSaltFromHashSalt;


@Service
public class RegistrationService {
    private final Logger logger = LoggerFactory.getLogger(RegistrationService.class);
    private final UserRepository userRepository;
    private final CrawlerRepository crawlerRepository;
    private final HashService hashService;
    private final PasswordCheckerService passwordCheckerService;


    public RegistrationService(UserRepository userRepository, CrawlerRepository crawlerRepository
            ,HashService hashService, PasswordCheckerService passwordCheckerService) {
        this.userRepository = userRepository;
        this.crawlerRepository = crawlerRepository;
        this.hashService = hashService;
        this.passwordCheckerService = passwordCheckerService;
        logger.info("New RegistrationService");
    }

    /**
     * Registreert een nieuwe gebruiker met de opgegeven registratiegegevens.
     *
     * @param registrationDTO De registratiegegevens van de nieuwe gebruiker.
     * @return De geregistreerde gebruiker als de registratie succesvol is voltooid.
     * @throws UserAlreadyExistsException als er al een gebruiker is met het opgegeven e-mailadres.
     * @throws PasswordNotValidException als het opgegeven wachtwoord niet geldig is.
     * @throws CrawlerAlreadyExistsException als er al een Crawler bestaat met de opgegeven naam.
     */
    public User register(RegistrationDTO registrationDTO) throws UserAlreadyExistsException,
            PasswordNotValidException, CrawlerAlreadyExistsException {
        User existingUser = findUserByEmail(registrationDTO.getEmail());
        if (existingUser != null){
            throw new UserAlreadyExistsException("User with email " + registrationDTO.getEmail() + " already exists");
        }
        String password = registrationDTO.getPassword();
        if (!passwordCheckerService.isPasswordValid(password)) {
            throw new PasswordNotValidException("Password is not valid");
        }
        if(crawlerRepository.checkIfCrawlerAlreadyExists(registrationDTO.getName())){
            throw new CrawlerAlreadyExistsException("Crawler with name " + registrationDTO.getName() + " already exists");
        }
        return createUserAndCrawler(registrationDTO.getName(), registrationDTO.getEmail(), password);
    }

    private User createUserAndCrawler(String crawlerName, String email, String password) {
        User inputUser = userWithHashedPassword(password, email);
        User returnUser = userRepository.saveUser(inputUser);
        Crawler crawler = new Crawler(crawlerName);
        crawler.setUser(returnUser);
        crawlerRepository.saveOrUpdateOne(crawler);
        return returnUser;
    }

    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }


    protected User userWithHashedPassword(String password, String email) {
        String hashedPassword = hashService.hash(password);
        String salt = getSaltFromHashSalt(hashedPassword);
        String passwordHash = getHashFromHashSalt(hashedPassword);
        return new User(email, passwordHash, salt);
    }

    public void saveUserVerificationToken(User theUser, String token) {
        VerificationToken verificationToken = new VerificationToken(token, theUser);
        userRepository.saveVerificationToken(verificationToken);
    }

    /**
     * Controleert een verificatietoken voor de verificatie van een gebruikersaccount.
     *
     * @param theToken Het verificatietoken dat moet worden goedgekeurd.
     * @return "valid" als het token geldig is en de gebruiker is geverifieerd,
     *         "Token already expired" als het token is verlopen,
     *         "Invalid verification token" als het token niet geldig is of niet bestaat.
     */
    public String validateToken(String theToken) {
        VerificationToken token = userRepository.findByToken(theToken);
        if (token == null) {
            return "Invalid verification token";
        }
        User user = token.getUser();
        Calendar calendar = Calendar.getInstance();
        if (token.getExpirationTime().getTime() - calendar.getTime().getTime() <= 0) {
            userRepository.deleteToken(token);
            return "Token already expired";
        }
        user.setVerified(true);
        userRepository.updateUser(user);
        return "valid";
    }

    public VerificationToken findByToken(String token) {
        return userRepository.findByToken(token);
    }
}

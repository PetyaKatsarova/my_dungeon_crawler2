package nl.hva.hvacrawler.communication.controller;

import jakarta.servlet.http.HttpServletRequest;
import nl.hva.hvacrawler.business.domain.User;
import nl.hva.hvacrawler.business.service.RegistrationService;
import nl.hva.hvacrawler.communication.dto.RegistrationDTO;
import nl.hva.hvacrawler.exception.CrawlerAlreadyExistsException;
import nl.hva.hvacrawler.exception.EmailSendingException;
import nl.hva.hvacrawler.exception.PasswordNotValidException;
import nl.hva.hvacrawler.exception.UserAlreadyExistsException;
import nl.hva.hvacrawler.util.event.RegistrationCompleteEvent;
import nl.hva.hvacrawler.util.security.token.VerificationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RegistrationController {

    private final Logger logger = LoggerFactory.getLogger(RegistrationController.class);
    private final RegistrationService registrationService;
    private final ApplicationEventPublisher publisher;
    public static final String SUCCESFUL_REGISTRATION_MESSAGE = "You have registered " +
            "successfully. " +
            "An email has been sent to your inbox for account verification. " +
            "Please check your email within the next 15 minutes to complete the " +
            "registration process.";
    public static final String USER_EXISTS_MESSAGE = "A user with this email already exists. " +
            "If you've forgotten your password, you can try resetting it.";
    public static final String CRAWLER_EXISTS_MESSAGE = "A Crawler with this name already exists. " +
            "Please choose a different name for your Crawler.";
    public static final String INVALID_PASSWORD_MESSAGE = """
    The password is not valid. Please ensure it meets the following criteria:
    - No consecutive repeats of the same character for a minimum of 5 characters.
    - No consecutive numbers for a minimum of 5 characters.
    """;
    public static final String FAILING_EMAIL_MESSAGE = "There was an error " +
            "sending the verification email. Please try again or return later.";
    public static final String SERVER_ERROR_MESSAGE = "An error occurred " +
            "during registration due to a server issue. Please try again later.";

    public RegistrationController(RegistrationService registrationService, ApplicationEventPublisher publisher) {
        this.registrationService = registrationService;
        this.publisher = publisher;
        logger.info("New RegistrationController");
    }

    @PostMapping("/signup")
    public ResponseEntity<String> userSignInHandler(@RequestBody RegistrationDTO registrationDTO,
                                                    final HttpServletRequest request) {
        try {
            User user = registrationService.register(registrationDTO);
            publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
            return ResponseEntity.status(HttpStatus.CREATED).body(SUCCESFUL_REGISTRATION_MESSAGE);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(USER_EXISTS_MESSAGE);
        } catch (CrawlerAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(CRAWLER_EXISTS_MESSAGE);
        }catch (PasswordNotValidException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(INVALID_PASSWORD_MESSAGE);
        } catch (EmailSendingException e) {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(FAILING_EMAIL_MESSAGE);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(SERVER_ERROR_MESSAGE);
        }
    }


    public String applicationUrl(HttpServletRequest request) {
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    @GetMapping("/verifyEmail")
    public String verifyEmail(@RequestParam("token") String token) {
        VerificationToken theToken = registrationService.findByToken(token);
        String verificationResult;
        if (theToken == null) {
            verificationResult = "This token does not exist";
        } else {
            boolean alreadyVerified = theToken.getUser().isVerified();
            verificationResult = alreadyVerified
                    ? "This account has already been verified. Please, login."
                    : registrationService.validateToken(token);
        }
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Email Verification Result</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h1>Email Verification Result</h1>\n" +
                "    <p>" + verificationResult + "</p>\n" +
                "</body>\n" +
                "</html>";
    }

}

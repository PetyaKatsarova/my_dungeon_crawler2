package nl.hva.hvacrawler.communication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.hva.hvacrawler.business.domain.User;
import nl.hva.hvacrawler.business.service.RegistrationService;
import nl.hva.hvacrawler.communication.dto.RegistrationDTO;
import nl.hva.hvacrawler.exception.CrawlerAlreadyExistsException;
import nl.hva.hvacrawler.exception.EmailSendingException;
import nl.hva.hvacrawler.exception.PasswordNotValidException;
import nl.hva.hvacrawler.exception.UserAlreadyExistsException;
import nl.hva.hvacrawler.util.security.token.VerificationToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RegistrationController.class)
class RegistrationControllerTest {

    @MockBean
    private RegistrationService registrationService;

    @MockBean
    private ApplicationEventPublisher publisher;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new RegistrationController(registrationService, publisher)).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void userSignInHandler_Success() throws Exception {
        RegistrationDTO registrationDTO = new RegistrationDTO("test@example.com", "TestPassword",
                "TestName");

        User user = new User();
        user.setEmail("test@example.com");

        Mockito.when(registrationService.register(Mockito.any(RegistrationDTO.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().string(RegistrationController.SUCCESFUL_REGISTRATION_MESSAGE));
    }

    @Test
    void userSignInHandler_UserAlreadyExists() throws Exception {
        RegistrationDTO registrationDTO = new RegistrationDTO("existing@example.com", "TestPassword",
                "TestName");

        Mockito.when(registrationService.register(Mockito.any(RegistrationDTO.class)))
                .thenThrow(new UserAlreadyExistsException("User with this email already exists"));

        mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDTO)))
                .andExpect(status().isConflict())
                .andExpect(content().string(RegistrationController.USER_EXISTS_MESSAGE));
    }

    @Test
    void userSignInHandler_PasswordNotValid() throws Exception {
        RegistrationDTO registrationDTO = new RegistrationDTO("test@example.com", "Weak",
                "TestName");

        Mockito.when(registrationService.register(Mockito.any(RegistrationDTO.class)))
                .thenThrow(new PasswordNotValidException("Password is not valid"));

        mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(RegistrationController.INVALID_PASSWORD_MESSAGE));
    }

    @Test
    void userSignInHandler_CrawlerAlreadyExists() throws Exception {
        RegistrationDTO registrationDTO = new RegistrationDTO("test@example.com", "TestPassword",
                "TestName");

        Mockito.when(registrationService.register(Mockito.any(RegistrationDTO.class)))
                .thenThrow(new CrawlerAlreadyExistsException("Crawler with this name already exists"));

        mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDTO)))
                .andExpect(status().isConflict())
                .andExpect(content().string(RegistrationController.CRAWLER_EXISTS_MESSAGE));
    }

    @Test
    void userSignInHandler_EmailSendingException() throws Exception {
        RegistrationDTO registrationDTO = new RegistrationDTO("test@example.com", "TestPassword",
                "TestName");

        Mockito.when(registrationService.register(Mockito.any(RegistrationDTO.class)))
                .thenThrow(new EmailSendingException("Error sending verification email", null));

        mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDTO)))
                .andExpect(status().isServiceUnavailable())
                .andExpect(content().string(containsString(RegistrationController.FAILING_EMAIL_MESSAGE)));
    }

    @Test
    void userSignInHandler_GenericException() throws Exception {
        RegistrationDTO registrationDTO = new RegistrationDTO("test@example.com", "TestPassword",
                "TestName");

        Mockito.when(registrationService.register(Mockito.any(RegistrationDTO.class)))
                .thenThrow(new RuntimeException("An error occurred during registration"));

        mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string(RegistrationController.SERVER_ERROR_MESSAGE));
    }

    @Test
    void verifyEmail_ValidToken() throws Exception {
        String token = "validToken";
        VerificationToken verificationToken = new VerificationToken(token, new User());
        String htmlResponse = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Email Verification Result</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h1>Email Verification Result</h1>\n" +
                "    <p>" + "valid" + "</p>\n" +
                "</body>\n" +
                "</html>";

        Mockito.when(registrationService.findByToken(token)).thenReturn(verificationToken);
        Mockito.when(registrationService.validateToken(token)).thenReturn("valid");

        mockMvc.perform(MockMvcRequestBuilders.get("/verifyEmail")
                        .param("token", token))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(htmlResponse)));
    }

    @Test
    void verifyEmail_AlreadyVerified() throws Exception {
        String token = "validToken";
        VerificationToken verificationToken = new VerificationToken(token, new User());
        verificationToken.getUser().setVerified(true);

        Mockito.when(registrationService.findByToken(token)).thenReturn(verificationToken);

        mockMvc.perform(MockMvcRequestBuilders.get("/verifyEmail")
                        .param("token", token))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Email Verification Result")))
                .andExpect(content().string(containsString("This account has already been verified. Please, login.")));
    }

    @Test
    void verifyEmail_InvalidToken() throws Exception {
        String token = "invalidToken";
        String htmlResponse = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <title>Email Verification Result</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <h1>Email Verification Result</h1>\n" +
                "    <p>" + "This token does not exist" + "</p>\n" +
                "</body>\n" +
                "</html>";

        Mockito.when(registrationService.findByToken(token)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get("/verifyEmail")
                        .param("token", token))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(htmlResponse)));
    }

}

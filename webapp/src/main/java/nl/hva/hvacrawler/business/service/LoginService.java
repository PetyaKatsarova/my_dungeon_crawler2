package nl.hva.hvacrawler.business.service;

import jakarta.mail.MessagingException;
import nl.hva.hvacrawler.business.domain.User;
import nl.hva.hvacrawler.exception.EmailSendingException;
import nl.hva.hvacrawler.util.security.EmailSenderService;
import nl.hva.hvacrawler.util.security.token.ResetToken;
import nl.hva.hvacrawler.exception.PasswordNotValidException;
import nl.hva.hvacrawler.persistence.repository.UserRepository;
import nl.hva.hvacrawler.util.security.password.PasswordCheckerService;
import nl.hva.hvacrawler.util.security.token.JWTToken;
import nl.hva.hvacrawler.util.security.password.HashService;

import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

import static nl.hva.hvacrawler.util.HashAndSaltUtil.getHashFromHashSalt;
import static nl.hva.hvacrawler.util.HashAndSaltUtil.getSaltFromHashSalt;

@Service
public class LoginService {

    private final ResetToken resetToken;
    private final UserRepository userRepository;
    private final EmailSenderService emailSenderService;
    private final HashService hashService;
    private final PasswordCheckerService passwordCheckerService;
    private JWTToken jwtToken;

    public LoginService(ResetToken resetToken, UserRepository userRepository,
                        HashService hashService, PasswordCheckerService passwordCheckerService,
                        EmailSenderService emailSenderService) {
        this.resetToken = resetToken;
        this.userRepository = userRepository;
        this.hashService = hashService;
        this.passwordCheckerService = passwordCheckerService;
        this.jwtToken = new JWTToken();
        this.emailSenderService = emailSenderService;
    }

    /**
     * Authenticeert een gebruiker op basis van de verstrekte gebruikersgegevens.
     *
     * @param user De gebruiker die moet worden geauthenticeerd, inclusief e-mailadres en wachtwoord.
     * @return true als de gebruiker succesvol is geauthenticeerd, anders false.
     */
    public boolean authenticate(User user) {
        boolean userCanEnter = false;
        User loginUser = userRepository.findUserByEmail(user.getEmail());
        if (loginUser != null) {
            String hashedPW = loginUser.getPassword();
            String salt = loginUser.getSalt();
            String toBechecked = getHashFromHashSalt(hashService.hash(user.getPassword(), salt));
            if (hashedPW.equals(toBechecked) && loginUser.isVerified()) {
                userCanEnter = true;
            }
        }
        return userCanEnter;
    }

    public String generateJWTToken(User user) {
        return jwtToken.generateJWTToken(user.getEmail());
    }

    public String resetTokenLink(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user != null) {
            if (user.getEmail().equals(email)) {
                String token = resetToken.generateToken();
                userRepository.updateResetTokenForUser(email, token);
                try {
                    emailSenderService.sendResetTokenEmail(email, token);
                } catch (MessagingException | UnsupportedEncodingException e) {
                    throw new EmailSendingException("Unable to send reset password link", e);
                }

                // Construct an HTML message with a link
                String htmlMessage = "<html><body>";
                htmlMessage += "<p>Please check your email to set a new password with your generated token.</p>";
                htmlMessage += "<p><a href=\"/html/verify-token.html\">Click here to verify your token</a></p>";
                htmlMessage += "</body></html>";

                return htmlMessage;
            }
        }

        return "Unknown user " + email;
    }

    // TODO printstatements verwijderen
    public String verifyAccount(String email, String token) {
        User user = userRepository.findUserByEmail(email);
        if (user == null || !user.getEmail().equals(email)) {
            return "User not found with this email";
        }
        if (user.getResetToken().equals(token)) {
            userRepository.updateResetTokenForUser(email, null);
            return "token verified you can login";
        } else {
            return "Please regenerate token and try again";
        }
    }

    // TODO printstatements verwijderen
    public String setNewPassword(String email, String newPassword) {
        User user = userRepository.findUserByEmail(email);
        if (!user.getEmail().equals(email)) {
            return "User not found with this email: " + email;
        }
        if (!passwordCheckerService.isPasswordValid(newPassword)) {
            throw new PasswordNotValidException("Password is not valid");
        }
        setHashedPassword(newPassword, user);
        userRepository.updateUser(user);
        return "Password update succesfully, please login with new password";
    }

    private void setHashedPassword(String password, User user) {
        String hashedPassword = hashService.hash(password);
        user.setSalt(getSaltFromHashSalt(hashedPassword));
        user.setPassword(getHashFromHashSalt(hashedPassword));
    }

    public boolean updateUserIdentificatieToken(User user) {
        return userRepository.updateUserIdentificatieToken(user);
    }
}

package nl.hva.hvacrawler.communication.controller;

import nl.hva.hvacrawler.business.service.LoginService;
import nl.hva.hvacrawler.business.service.RegistrationService;
import nl.hva.hvacrawler.exception.PasswordNotValidException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;

@RestController
public class PasswordResetController {

    @Autowired
    private LoginService loginService;


    @GetMapping("/forgot-password")
    public String forgetPasswordHandler() {
        return "../html/forgot-password";
    }

    @PostMapping("/reset-link")
    public ResponseEntity<String> resetLinkHandler(@RequestParam String  email) {
        return new ResponseEntity<>(loginService.resetTokenLink(email), HttpStatus.OK);
    }

    @PostMapping("/verify-account")
    public ResponseEntity<String> verifyAccountHandler(@RequestParam String email, @RequestParam String token) {
        String verificationResult = loginService.verifyAccount(email, token);

        return switch (verificationResult) {
            case "token verified you can login" -> new ResponseEntity<>(verificationResult, HttpStatus.OK);
            case "Please regenerate token and try again", "User not found with this email" ->
                    new ResponseEntity<>(verificationResult, HttpStatus.BAD_REQUEST);
            default -> new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        };
    }
    @PutMapping("/set-password")
    public ResponseEntity<String> setNewPasswordHandler(@RequestParam String email, @RequestParam String newPassword) {
        try {
            String resultMessage = loginService.setNewPassword(email, newPassword);
            return ResponseEntity.ok(resultMessage); // Password update was successful
        } catch (UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found with this email: " + email);
        } catch (PasswordNotValidException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Password is not valid");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }
    private class UserNotFoundException extends RuntimeException {

        public UserNotFoundException(String message) {
            super(message);
        }
    }

}

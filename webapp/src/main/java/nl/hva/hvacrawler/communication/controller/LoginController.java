package nl.hva.hvacrawler.communication.controller;

import nl.hva.hvacrawler.business.domain.User;
import nl.hva.hvacrawler.business.service.LoginService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {
    private final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
        logger.info("New LoginController");
    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUserHandler(@RequestBody User user) {
        if (loginService.authenticate(user)) {
            String jwt = loginService.generateJWTToken(user);
            user.setJwtToken(jwt);
            loginService.updateUserIdentificatieToken(user);
            return ResponseEntity.ok("Bearer " +jwt);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed");
    }

}


package nl.hva.hvacrawler.communication.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HTMLController {

    private final Logger logger = LoggerFactory.getLogger(HTMLController.class);

    public HTMLController() {
        logger.info("New HTMLLoginController");
    }

    @GetMapping("/login")
    public String showLoginPage() {
        return "/html/loginScreen.html";
    }

}

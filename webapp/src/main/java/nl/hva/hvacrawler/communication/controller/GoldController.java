package nl.hva.hvacrawler.communication.controller;

import nl.hva.hvacrawler.business.domain.Gold;
import nl.hva.hvacrawler.business.domain.Trap;
import nl.hva.hvacrawler.business.service.GoldService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;
@Controller
public class GoldController {
    private final Logger logger = LoggerFactory.getLogger(GoldController.class);
    private final GoldService goldService;

    public GoldController(GoldService goldService) {
        this.goldService = goldService;
        logger.info("New GoldController");
    }
    @GetMapping("/gold/{id}")
    @ResponseBody
    public Gold getgold(@PathVariable int id) {
        return goldService.getGoldById(id).orElse(null);
    }
    @GetMapping("/random-gold")
    @ResponseBody
    public Gold getRandomGold() {
        Optional<Gold> randomGold = goldService.getRandomGold();
        return randomGold.orElse(null);
    }
}

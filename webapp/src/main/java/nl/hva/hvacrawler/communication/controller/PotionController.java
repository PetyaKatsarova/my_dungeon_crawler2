package nl.hva.hvacrawler.communication.controller;

import nl.hva.hvacrawler.business.domain.Potion;
import nl.hva.hvacrawler.business.service.PotionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PotionController {
    private final PotionService potionService;

    public PotionController(PotionService potionService) {
        this.potionService = potionService;
    }

    @GetMapping("/potion/{id}")
    @ResponseBody
    public Potion getPotion(@PathVariable int id){
        return potionService.getPotionById(id).orElse(null);
    }

    @GetMapping("/random-potion")
    @ResponseBody
    public Potion getRandomPotion() {
        return potionService.getRandomPotion().orElse(null);
    }

}

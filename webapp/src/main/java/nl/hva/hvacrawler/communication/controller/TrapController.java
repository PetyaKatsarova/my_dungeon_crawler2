package nl.hva.hvacrawler.communication.controller;

import nl.hva.hvacrawler.business.domain.Trap;
import nl.hva.hvacrawler.business.service.TrapService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.swing.text.html.Option;
import java.util.Optional;

@Controller
public class TrapController {
    private final Logger logger = LoggerFactory.getLogger(TrapController.class);
    private final TrapService trapService;

    public TrapController(TrapService trapService) {
        this.trapService = trapService;
        logger.info("New TrapController");
    }
    @GetMapping("/trap/{id}")
    @ResponseBody
    public Trap getTrap(@PathVariable int id) {
        return trapService.getTrapById(id).orElse(null);
    }
    @GetMapping("/random-trap")
    @ResponseBody
    public Trap getRandomTrap() {
        Optional<Trap> randomTrap = trapService.getRandomTrap();
        return randomTrap.orElse(null);
    }
}

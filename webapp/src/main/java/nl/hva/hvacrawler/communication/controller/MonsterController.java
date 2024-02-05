package nl.hva.hvacrawler.communication.controller;

import nl.hva.hvacrawler.business.domain.Monster;
import nl.hva.hvacrawler.business.service.MonsterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class MonsterController {
    private final MonsterService monsterService;


    public MonsterController(MonsterService monsterService) {
        this.monsterService = monsterService;
    }


    @GetMapping("/monsters/{id}")
    @ResponseBody
    public Monster getMonster(@PathVariable int id) {
        // Retrieve the monster from the database using the MonsterDao
        return monsterService.getMonsterById(id).orElse(null);
    }

    @GetMapping("/random-monster")
    @ResponseBody
    public Monster getRandomMonster() {
        Optional<Monster> randomMonster = monsterService.getRandomMonster();
        if (randomMonster.isPresent()) {
            return randomMonster.get();
        } else {
            return new Monster("No monster",0,0,0); // Handle the case when monster is not found or already fetched
        }
    }
}

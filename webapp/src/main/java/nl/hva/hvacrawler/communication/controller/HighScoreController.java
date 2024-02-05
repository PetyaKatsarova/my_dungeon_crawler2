package nl.hva.hvacrawler.communication.controller;

import nl.hva.hvacrawler.business.domain.Highscore;
import nl.hva.hvacrawler.business.service.HighscoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

    @RestController
    @RequestMapping("/api/highscores")
    public class HighScoreController {

        @Autowired
        private HighscoreService highscoreService;

        @GetMapping("/top")
        public List<Highscore> getHighScores(@RequestParam int limit) {
            return highscoreService.getHighScoreslist(limit);
        }
        @GetMapping("/name/{name}")
        public List<Highscore> getHighScoresAroundName(@PathVariable String name) {
            return highscoreService.getHighScoresAroundName(name);
        }
        @GetMapping("/rank/{rank}")
        public List<Highscore> getHighScoresAroundRank(@PathVariable int rank) {
            return highscoreService.getHighScoresAroundRank(rank);
        }

    }

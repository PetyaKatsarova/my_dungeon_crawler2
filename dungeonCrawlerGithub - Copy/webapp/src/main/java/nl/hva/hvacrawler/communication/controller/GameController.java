package nl.hva.hvacrawler.communication.controller;

import nl.hva.hvacrawler.business.domain.Game;
import nl.hva.hvacrawler.business.domain.User;
import nl.hva.hvacrawler.business.service.AuthorizationService;
import nl.hva.hvacrawler.business.service.GameService;
import nl.hva.hvacrawler.communication.dto.GameDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequestMapping("/game")
public class GameController {
    private final GameService gameService;
    private final AuthorizationService authorizationService;
    private final Logger logger = LoggerFactory.getLogger(GameController.class);

    @Autowired
    public GameController(GameService gameService, AuthorizationService authorizationService) {
        this.gameService = gameService;
        this.authorizationService = authorizationService;
        logger.info("New GameController");
    }

    @GetMapping("/test")
    @ResponseBody
    public String GameTester() {
        return "Hello from GameController, this is a test :)";
    }

    @GetMapping("/create")
    @ResponseBody
    public ResponseEntity<Game> createGame(@RequestHeader("Authorization") String jwtToken,
                                           @RequestParam int rows, @RequestParam int columns) {
        User user = authorizationService.validateJWTToken(jwtToken);
        if (user != null) {
            Game game = gameService.createGame(user, rows, columns);
            return ResponseEntity.ok(game);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @PutMapping("/command")
    @ResponseBody
    public ResponseEntity<GameDTO> commandForGame(@RequestHeader("Authorization") String jwtToken,
                                                  @RequestBody GameDTO gameDTO, @RequestParam("command") String command,
                                                  @RequestParam(name = "argument", required = false) String arg) {
        User user = authorizationService.validateJWTToken(jwtToken);
        if (user != null) {
            if ("pickup".equalsIgnoreCase(command) && "holygrail".equalsIgnoreCase(arg)) {
                gameDTO.setGameStatus(GameDTO.GameStatus.FINISHED);
            }
            GameDTO alteredGame = gameService.commandForGame(gameDTO, command, arg);
            return ResponseEntity.ok(alteredGame);
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

        @GetMapping("/resume")
        @ResponseBody
        public ResponseEntity<Game> resumePausedGame (@RequestHeader("Authorization") String jwtToken) {
            User user = authorizationService.validateJWTToken(jwtToken);
            if (user != null) {
                Game game = gameService.resumeGame(user);
                return ResponseEntity.ok(game);
            } else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
            }
        }

        @GetMapping("/getGameById")
        @ResponseBody
        public Game getGameById ( @RequestParam("idGame") int idGame){
            return gameService.getGameById(idGame);
        }

    }
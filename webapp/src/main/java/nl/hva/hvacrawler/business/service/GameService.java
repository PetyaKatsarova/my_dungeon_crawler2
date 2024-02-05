package nl.hva.hvacrawler.business.service;

import nl.hva.hvacrawler.business.domain.*;
import nl.hva.hvacrawler.communication.dto.GameCrawlerDTO;
import nl.hva.hvacrawler.communication.dto.GameDTO;
import nl.hva.hvacrawler.communication.dto.GameRoomDTO;
import nl.hva.hvacrawler.persistence.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

@Service
public class GameService {
    private final Logger logger = LoggerFactory.getLogger(GameService.class);
    private final GameRepository gameRepository;
    private final CrawlerRepository crawlerRepository;
    private final MonsterService monsterService;
    private final PotionService potionService;
    private final TrapService trapService;
    private final WeaponService weaponService;
    private final GoldService goldService;
    private final CommandService commandService;
    private final SaveService saveService;
    private Map<String, BiFunction<GameDTO, String, GameDTO>> commandHandlers = new HashMap<>();


    public GameService(GameRepository gameRepository, CrawlerRepository crawlerRepository,
                       MonsterService monsterService, PotionService potionService,
                       TrapService trapService, WeaponService weaponService, GoldService goldService, CommandService commandService,
                       SaveService saveService) {
        this.gameRepository = gameRepository;
        this.crawlerRepository = crawlerRepository;
        this.monsterService = monsterService;
        this.potionService = potionService;
        this.trapService = trapService;
        this.weaponService = weaponService;
        this.goldService = goldService;
        this.commandService = commandService;
        this.saveService = saveService;
        initializeCommandHandlers();
        logger.info("New GameService");
    }

    private void initializeCommandHandlers() {
        commandHandlers.put("move", this::handleMove);
        commandHandlers.put("open", (gameDTO, arg) -> commandService.openChest(gameDTO));
        commandHandlers.put("pickup", commandService::pickup);
        commandHandlers.put("save", (gameDTO, arg) -> saveService.handleSave(gameDTO));
        commandHandlers.put("fight", (gameDTO, arg) -> commandService.fight(gameDTO));
        commandHandlers.put("drink", commandService::handleDrink);
    }

    public Game getGameById(int idGame) {
//        todo: need to collect all roooms, chests etc....
        return gameRepository.findGameById(idGame);
    }

    /**
     * Maakt een nieuw spel aan voor de opgegeven gebruiker met het opgegeven aantal rijen en kolommen.
     *
     * @param user    De gebruiker voor wie het spel wordt aangemaakt.
     * @param rows    Het aantal rijen in het spelbord.
     * @param columns Het aantal kolommen in het spelbord.
     * @return Het aangemaakte Game-object.
     */
    public Game createGame(User user, int rows, int columns){
        Crawler crawler = crawlerRepository.findOneByUserId(user.getIdUser());
        Crawler crawlerGame = new Crawler(crawler.getName());
        crawlerGame.setWeapon(new Weapon(1, "Fist", 0));
        crawlerGame.setIdCharacter(crawler.getIdCharacter());
        crawlerGame.setUser(crawler.getUser());
        Game game = new Game(crawlerGame, rows, columns);
        return createGameHelper(game);
    }

    private Game createGameHelper(Game game) {
        Room[][] gameBoard = game.getGameBoard();
        for (int i = 0; i < game.getRows(); i++) {
            for (int j = 0; j < game.getColumns(); j++) {
                Room room = gameBoard[i][j];
                setRandomMonster(room);
                setRandomItem(room);
            }
        }
        return game;
    }

    private void setRandomMonster(Room room) {
        if (room.getMonster() != null) {
            room.setMonster(monsterService.getRandomMonster().orElse(null));
        }
    }

    private void setRandomItem(Room room) {
        Chest chest = room.getChest();
        if (chest != null) {
            Item item = chest.getItem();
            if (item != null && !item.getName().equals("nothing")) {   // of !item instance of
                // Nothing
                Item randomItem = getRandomItem(item.getName());
                chest.setItem(randomItem);
            }
        }
    }

    private Item getRandomItem(String itemName) {
        switch (itemName) {
            case "Weapon" -> {return weaponService.getRandomWeapon().orElse(null);}
            case "Potion" -> {return potionService.getRandomPotion().orElse(null);}
            case "Trap" -> {return trapService.getRandomTrap().orElse(null);}
            case "Gold" -> {return goldService.getRandomGold().orElse(null);}
            default -> {return null;}
        }
    }

    /**
     * Verwerkt een spelcommando met een optioneel argument en voert de bijbehorende actie uit op het huidige spel.
     *
     * @param gameDTO  Het huidige GameDTO-object dat de spelstatus bevat.
     * @param command  Het commando dat moet worden uitgevoerd (bijv. "move", "open", "pickup").
     * @param arg      Het optionele argument dat bij het commando hoort (bijv. de richting bij "move").
     * @return Het bijgewerkte GameDTO-object na het verwerken van het commando.
     */
    public GameDTO commandForGame(GameDTO gameDTO, String command, String arg) {
        BiFunction<GameDTO, String, GameDTO> handler = commandHandlers.get(command);
        if (handler != null) {
            gameDTO = handler.apply(gameDTO, arg);
        }
        return gameDTO;
    }

   public GameDTO handleMove(GameDTO gameDTO, String arg) {
        GameRoomDTO[][] gameBoard = gameDTO.getGameBoard();
        boolean found = false;
        for (int i = 0; i < gameDTO.getRows() && !found; i++) {
            for (int j = 0; j < gameDTO.getColumns(); j++) {
                GameRoomDTO room = gameBoard[i][j];
                if (room.isCurrentRoom()) {
                    found = true;
                    room.setCurrentRoom(false);
                    if (!room.isVisited()) {
                        room.setVisited(true);
                        GameCrawlerDTO crawler = gameDTO.getGameOwner();
                        crawler.incrementRoomsVisited();
                    }
                        Coordinates newCoordinates = crawlerNavigator(i, j, arg);
                        if (room.hasDoor(arg.toLowerCase())) {
                            gameBoard[newCoordinates.getNewRow()][newCoordinates.getNewColumn()].setCurrentRoom(true);
                        }
                        break;
                    }
                }
            }
            return gameDTO;
        }

        private Coordinates crawlerNavigator ( int currentRow, int currentColumn, String command){
            Direction direction = getDirectionFromCommand(command);
            int newRow = currentRow;
            int newColumn = currentColumn;

            switch (direction) {
                case NORTH:
                    newRow--;
                    break;
                case SOUTH:
                    newRow++;
                    break;
                case WEST:
                    newColumn--;
                    break;
                case EAST:
                    newColumn++;
                    break;
            }
            return new Coordinates(newRow, newColumn);
        }

        // Define a Direction enum to represent the possible directions
        private enum Direction {
            NORTH, SOUTH, WEST, EAST
        }
        private class Coordinates {
            private int newRow;
            private int newColumn;

            public Coordinates(int newRow, int newColumn) {
                this.newRow = newRow;
                this.newColumn = newColumn;
            }

            public int getNewRow() {
                return newRow;
            }

            public int getNewColumn() {
                return newColumn;
            }
        }


        // Implement a method to map command strings to directions
        private Direction getDirectionFromCommand (String command){
            // Convert the command to lowercase for case-insensitive comparison
            command = command.toLowerCase();

            return switch (command) {
                case "north" -> Direction.NORTH;
                case "south" -> Direction.SOUTH;
                case "west" -> Direction.WEST;
                case "east" -> Direction.EAST;
                default ->
                    // Handle an invalid or unrecognized command
                        throw new IllegalArgumentException("Invalid or unrecognized command: " + command);
            };
        }

        public Game resumeGame(User user){
        return gameRepository.resumeGame(user);
        }
}



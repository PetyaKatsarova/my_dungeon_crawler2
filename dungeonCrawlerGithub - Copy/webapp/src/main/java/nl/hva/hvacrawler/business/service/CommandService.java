package nl.hva.hvacrawler.business.service;

import nl.hva.hvacrawler.business.domain.*;
import nl.hva.hvacrawler.communication.dto.GameCrawlerDTO;
import nl.hva.hvacrawler.communication.dto.GameDTO;
import nl.hva.hvacrawler.communication.dto.GameRoomDTO;
import nl.hva.hvacrawler.persistence.repository.CrawlerRepository;
import nl.hva.hvacrawler.persistence.repository.UserRepository;
import nl.hva.hvacrawler.util.Dice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommandService {
    private final Dice              dice;
    private final CrawlerRepository crawlerRepository;
    private final UserRepository    userRepository;


    public CommandService(Dice dice, CrawlerRepository crawlerRepository, UserRepository userRepository) {
        this.dice = dice;
        this.crawlerRepository = crawlerRepository;
        this.userRepository = userRepository;
    }

    private GameRoomDTO getCurrentRoom(GameDTO game) {
        GameRoomDTO[][] gameBoard = game.getGameBoard();
        for (int i = 0; i < game.getRows(); i++) {
            for (int j = 0; j < game.getColumns(); j++) {
                GameRoomDTO room = gameBoard[i][j];
                if (room.isCurrentRoom()) {
                    return room;
                }
            }
        }
        return null; // Als er geen huidige kamer wordt gevonden
    }
    private void fight(GameCrawlerDTO crawler, Monster monster, Dice dice) {
        int playerRoll = dice.roll(20);
        int monsterRoll = dice.roll(20);

        if (playerRoll > monsterRoll) {
            int damage = 5 + crawler.getWeapon().getAttackModifier(); // De hoeveelheid schade die wordt toegebracht aan het monster + de weapon modifier
            monster.setHealthPoints(calculateDamage(monster.getHealthPoints(), damage));
            if (monster.getHealthPoints() == 0) {
                crawler.setGold(crawler.getGold() + monster.getGold());
                crawler.setKills(crawler.getKills()+1);
            }
        } else if (playerRoll < monsterRoll) {
            int damage = 5; // De hoeveelheid schade die wordt toegebracht aan de speler (crawler)
            crawler.setHealthPoints(calculateDamage(crawler.getHealthPoints(), damage));
        }
    }

    private int calculateDamage(int healthPoints, int damage) {
        return Math.max(healthPoints - damage, 0);
    }

    public GameDTO fight(GameDTO game) {
        GameCrawlerDTO crawler = game.getGameOwner();
        GameRoomDTO currentRoom = getCurrentRoom(game);

        if (currentRoom != null) {
            Monster monster = currentRoom.getMonster();
            fight(crawler, monster, dice);

            if (monster.getHealthPoints() == 0) {
                currentRoom.setMonster(null);
                game.setGameOwner(crawler);
            } else if (crawler.getHealthPoints() == 0) {
                handleHighscore(game);
                game.setGameOwner(null);
            }
        }

        return game;
    }

    public GameDTO pickup(GameDTO game, String arg) {

        // Extra if voor als de arg holygrail is.
        if ("holygrail".equalsIgnoreCase(arg)) {
            game.setGameStatus(GameDTO.GameStatus.FINISHED);
            handleHighscore(game);
            return game;

        }

        GameRoomDTO[][] gameBoard = game.getGameBoard();
        GameCrawlerDTO crawler = game.getGameOwner();
        for (int i = 0; i < game.getRows(); i++) {
            for (int j = 0; j < game.getColumns(); j++) {
                GameRoomDTO room = gameBoard[i][j];
                if (room.isCurrentRoom()) {
                    Item droppedItem = room.getDroppedItem();
                    if (droppedItem != null) {
                        if(droppedItem instanceof Weapon weapon){
                            crawler.setWeapon(weapon);
                        } else if (droppedItem instanceof Gold gold) {
                            int amount = gold.getValue();
                            crawler.setGold(crawler.getGold() + amount);
                        } else{
                            crawler.getHealthPotions().add((Potion) droppedItem);
                        }
                        room.setDroppedItem(null);
                    }
                }
            }
        }
        game.setGameOwner(crawler);
        return game;
    }

    /**
     * Opent de kisten in de huidige kamers van het spel en verwerkt hun inhoud.
     *
     * @param gameDTO Het GameDTO-object dat de huidige staat van het spel vertegenwoordigt.
     * @return Het bijgewerkte GameDTO-object na het openen van de kisten.
     */
    public GameDTO openChest(GameDTO gameDTO) {
        GameRoomDTO[][] gameBoard = gameDTO.getGameBoard();
        for (int i = 0; i < gameDTO.getRows(); i++) {
            for (int j = 0; j < gameDTO.getColumns(); j++) {
                GameRoomDTO room = gameBoard[i][j];
                if (room.isCurrentRoom() && room.getChest() != null) {
                    handleChestContents(gameDTO, room);
                }
            }
        }
        return gameDTO;
    }

    private void handleChestContents(GameDTO gameDTO, GameRoomDTO room) {
        Item itemFromChest = room.getChest().getItem();
        if (itemFromChest instanceof Trap trap) {
            handleTrap(gameDTO, trap);
        } else if (!(itemFromChest instanceof Nothing)) {
            handleNonTrapItem(room, itemFromChest);
        }
        room.setChest(null);
    }

    private void handleTrap(GameDTO gameDTO, Trap trap) {
        int damageTrap = trap.getDamage();
        GameCrawlerDTO crawler = gameDTO.getGameOwner();
        int healthCrawler = crawler.getHealthPoints();
        crawler.setHealthPoints(healthCrawler - damageTrap);
        if (crawler.getHealthPoints() <= 0) {
            handleHighscore(gameDTO);
            gameDTO.setGameOwner(null);
        } else {
            gameDTO.setGameOwner(crawler);
        }
    }

    private void handleNonTrapItem(GameRoomDTO room, Item itemFromChest) {
        room.setDroppedItem(itemFromChest);
    }

    public GameDTO handleDrink(GameDTO gameDTO, String potionChoice) {
        GameCrawlerDTO crawler = gameDTO.getGameOwner();
        int currentHealthPoints = crawler.getHealthPoints();
        final int MAXHEALTHPOINTS = 100;
        List<Potion> potions = crawler.getHealthPotions();
        for (Potion potion : potions) {
            if (potion.getName().equals(potionChoice)) {
                int potionHealingPoints = potion.getHealingModifier();
                int healthPointsAfterDrink = currentHealthPoints + potionHealingPoints;
                if (healthPointsAfterDrink > MAXHEALTHPOINTS) {
                    crawler.setHealthPoints(MAXHEALTHPOINTS);
                } else {crawler.setHealthPoints(healthPointsAfterDrink);}
                crawler.getHealthPotions().remove(potion);
                gameDTO.setGameOwner(crawler);
                break;
            }
        }
        return gameDTO;
    }
    public void handleHighscore (GameDTO gameDTO) {
        int userID = crawlerRepository.getUserIdByCrawlerId(gameDTO.getGameOwner().getIdCharacter());
        User user = userRepository.findUserById(userID);
        if(user.checkAndUpdateHighScore(gameDTO)){
            userRepository.updateUser(user);
        }
    }
}


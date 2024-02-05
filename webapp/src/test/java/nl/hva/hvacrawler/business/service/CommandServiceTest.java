package nl.hva.hvacrawler.business.service;

import nl.hva.hvacrawler.communication.dto.GameCrawlerDTO;
import nl.hva.hvacrawler.communication.dto.GameDTO;
import nl.hva.hvacrawler.communication.dto.GameRoomDTO;
import nl.hva.hvacrawler.business.domain.Weapon;
import nl.hva.hvacrawler.business.domain.Monster;
import nl.hva.hvacrawler.util.Dice;
import nl.hva.hvacrawler.persistence.repository.CrawlerRepository;
import nl.hva.hvacrawler.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CommandServiceTest {

    @Mock
    private Dice dice;

    @Mock
    private CrawlerRepository crawlerRepository;

    @Mock
    private UserRepository userRepository;

    private CommandService commandService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        commandService = new CommandService(dice, crawlerRepository, userRepository);
    }

    @Test
    public void testFightPlayerWins() {
        // Arrange
        GameCrawlerDTO crawler = new GameCrawlerDTO();
        Weapon weapon = new Weapon();
        weapon.setAttackModifier(3);
        crawler.setWeapon(weapon);
        GameRoomDTO room = new GameRoomDTO();
        Monster monster = new Monster();
        monster.setHealthPoints(10);
        room.setMonster(monster);
        room.setCurrentRoom(true);

        GameRoomDTO[][] gameBoard = new GameRoomDTO[][]{{room}};
        GameDTO game = new GameDTO();
        game.setGameBoard(gameBoard);
        game.setRows(1);
        game.setColumns(1);
        game.setGameOwner(crawler);

        when(dice.roll(20)).thenReturn(15); // Player roll
        when(dice.roll(20)).thenReturn(10); // Monster roll

        // Act
        GameDTO updatedGame = commandService.fight(game);

        // Assert
        assertEquals(5, monster.getHealthPoints()); // Check that the monster lost some health

    }

}

package nl.hva.hvacrawler.business.service;

/**
 * Description:
 * Author: Petya Katsarova
 * Email: pskpetya@gmail.com
 * Created on: 11/09/2023 15:23
 */

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import nl.hva.hvacrawler.business.domain.*;
import nl.hva.hvacrawler.communication.dto.GameDTO;
import nl.hva.hvacrawler.persistence.repository.CrawlerRepository;
import nl.hva.hvacrawler.persistence.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class GameServiceTest {
    @Mock
    private GameRepository gameRepository;
    @Mock
    private CrawlerRepository crawlerRepository;
    @Mock
    private MonsterService monsterService;
    @Mock
    private PotionService potionService;
    @Mock
    private TrapService trapService;
    @Mock
    private WeaponService weaponService;
    @Mock
    private GoldService goldService;
    @Mock
    private CommandService commandService;
    @Mock
    private SaveService saveService;
    @InjectMocks
    private GameService gameService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetGameById() {
        Game mockGame = new Game();
        when(gameRepository.findGameById(anyInt())).thenReturn(mockGame);
        Game result = gameService.getGameById(1);
        assertEquals(mockGame, result);
    }

    @Test
    public void testCreateGame() {
        User user = new User();
        user.setIdUser(1);
        Crawler crawler = new Crawler("name");
        when(crawlerRepository.findOneByUserId(1)).thenReturn(crawler);

        Game result = gameService.createGame(user, 3, 3);

        assertEquals(3, result.getRows());
        assertEquals(3, result.getColumns());
    }

    @Test
    public void testCommandForGame() {
        GameDTO gameDTO = new GameDTO();
        gameDTO.setRows(3);
        gameDTO.setColumns(3);

        when(commandService.pickup(any(), any())).thenReturn(gameDTO);

        GameDTO result = gameService.commandForGame(gameDTO, "pickup", "");

        assertEquals(3, result.getRows());
        assertEquals(3, result.getColumns());
    }
}


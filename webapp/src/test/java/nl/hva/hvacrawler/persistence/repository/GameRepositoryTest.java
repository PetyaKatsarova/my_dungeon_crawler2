package nl.hva.hvacrawler.persistence.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.hva.hvacrawler.business.domain.Game;
import nl.hva.hvacrawler.business.domain.Room;
import nl.hva.hvacrawler.business.service.GameService;
import nl.hva.hvacrawler.communication.dto.GameDTO;
import nl.hva.hvacrawler.communication.dto.GameRoomDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameRepositoryTest {
    private GameDTO testGame;
    private GameService gameServiceTest;
    private GameRepository repoTest;
    private GameDTO gameDTOTest;

    @BeforeEach
    void setUp() {
        testGame = mock(GameDTO.class);
        repoTest = mock(GameRepository.class);

        // Load the mock game board data from the JSON file
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            InputStream jsonFile = getClass().getClassLoader().getResourceAsStream("mockGameboard.json");
            gameDTOTest = objectMapper.readValue(jsonFile, GameDTO.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void crawlerNavigator_NorthCommand() {
        // Arrange
        GameRoomDTO[][] mockGameRoomDTO = new GameRoomDTO[2][2];

        when(testGame.getRows()).thenReturn(1);
        when(testGame.getColumns()).thenReturn(1);
        when(testGame.getGameBoard()).thenReturn(mockGameRoomDTO);


        // Act
//        repoTest.crawlerNavigator(testGame, "north");

        //get actual
        GameRoomDTO[][] gameBoard = testGame.getGameBoard();
        GameRoomDTO room = null;
        for (int i = 0; i < testGame.getRows(); i++) {
            for (int j = 0; j < testGame.getColumns(); j++) {
                room = gameBoard[i][j];

            }
        }
        // Assert
        GameRoomDTO expectedRoom = gameBoard[0][1];
        assertEquals(expectedRoom, room);
        System.out.println("actual values were " + room);
        System.out.println("expected values were " + expectedRoom);
    }
}

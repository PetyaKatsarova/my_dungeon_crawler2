package nl.hva.hvacrawler.business.domain;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;

/**
 * Description: Test
 * Author: Petya Katsarova
 * Email: pskpetya@gmail.com
 * Created on: 23/08/2023 11:37
 */

public class GameTest {

    private Game game;
    private Crawler mockCrawler;

    @BeforeEach
    public void setup() {
        mockCrawler = new Crawler();
        game = new Game(mockCrawler, 3, 3);
    }

    @Test
    public void testInitGameboard() {
        Room[][] gameBoard = game.getGameBoard();

        assertNotNull(gameBoard);
        assertEquals(3, gameBoard.length);
        assertEquals(3, gameBoard[0].length);

        for (int i = 0; i < gameBoard.length; i++) {
            for (int j = 0; j < gameBoard[i].length; j++) {
                assertNotNull(gameBoard[i][j]);
            }
        }
    }

    @Test
    public void testGetDoorsForRoom_CornerCase() {
        // Test for the top-left corner (0,0)
        List<Door> doors = game.getDoorsForRoom(0, 0, 3, 3);
        assertEquals(2, doors.size());
    }

    @Test
    public void testGetDoorsForRoom_MiddleCase() {
        // Test for the middle (1,1)
        List<Door> doors = game.getDoorsForRoom(1, 1, 3, 3);
        assertEquals(4, doors.size());
    }
}



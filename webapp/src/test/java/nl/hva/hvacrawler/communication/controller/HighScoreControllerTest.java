package nl.hva.hvacrawler.communication.controller;

import nl.hva.hvacrawler.business.domain.Highscore;
import nl.hva.hvacrawler.business.service.HighscoreService;
import nl.hva.hvacrawler.communication.controller.HighScoreController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = HighScoreController.class)
public class HighScoreControllerTest {

    @Autowired
    private HighScoreController highScoreController;

    @MockBean
    private HighscoreService highscoreService;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetHighScores() {
        List<Highscore> sampleHighScores = Arrays.asList(
            new Highscore("Player1", 100),
            new Highscore("Player2", 90)
        );

        when(highscoreService.getHighScoreslist(2)).thenReturn(sampleHighScores);

        List<Highscore> result = highScoreController.getHighScores(2);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Player1", result.get(0).getEmail());
        assertEquals(100, result.get(0).getHighscore());
    }

    @Test
    public void testGetHighScoresAroundName() {
        List<Highscore> sampleHighScores = Arrays.asList(
            new Highscore("leva.thonissen@gmail.com", 80),
            new Highscore("jente.peters@gmail.com", 75)
        );

        when(highscoreService.getHighScoresAroundName("leva.thonissen@gmail.com")).thenReturn(sampleHighScores);

        List<Highscore> result = highScoreController.getHighScoresAroundName("leva.thonissen@gmail.com");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("leva.thonissen@gmail.com", result.get(0).getEmail());
        assertEquals(80, result.get(0).getHighscore());
        assertEquals("jente.peters@gmail.com", result.get(1).getEmail());
        assertEquals(75, result.get(1).getHighscore());
    }

    @Test
    public void testGetHighScoresAroundRank() {
        List<Highscore> sampleHighScores = Arrays.asList(
            new Highscore("Player3", 60),
            new Highscore("Player4", 50)
        );

        when(highscoreService.getHighScoresAroundRank(3)).thenReturn(sampleHighScores);

        List<Highscore> result = highScoreController.getHighScoresAroundRank(3);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Player3", result.get(0).getEmail());
        assertEquals(60, result.get(0).getHighscore());
    }
}

package nl.hva.hvacrawler.business.service;

import nl.hva.hvacrawler.business.domain.Highscore;
import nl.hva.hvacrawler.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HighscoreServiceTest {
    
    @Mock
    private UserRepository userRepository;

    private HighscoreService highscoreService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        highscoreService = new HighscoreService(userRepository);
    }

    @Test
    void getHighScoresList() {
        int limit = 2;
        List<Highscore> expectedHighScores = Arrays.asList(
            new Highscore("Annely", 100),
            new Highscore("Jerom", 90)
        );
        when(userRepository.getHighScoreslist(limit)).thenReturn(expectedHighScores);

        List<Highscore> result = highscoreService.getHighScoreslist(limit);

        assertNotNull(result);
        assertEquals(expectedHighScores, result);
    }

    @Test
    void getHighScoresAroundName() {
        String name = "Annely";
        List<Highscore> expectedHighScores = Arrays.asList(
            new Highscore("Annely", 100),
            new Highscore("Jerom", 90)
        );
        when(userRepository.getHighScoresAroundName(name)).thenReturn(expectedHighScores);

        List<Highscore> result = highscoreService.getHighScoresAroundName(name);

        assertNotNull(result);
        assertEquals(expectedHighScores, result);
    }

    @Test
    void getHighScoresAroundRank() {
        int rank = 1;
        List<Highscore> expectedHighScores = Arrays.asList(
            new Highscore("Annely", 100),
            new Highscore("Jerom", 90)
        );
        when(userRepository.getHighScoresAroundRank(rank)).thenReturn(expectedHighScores);

        List<Highscore> result = highscoreService.getHighScoresAroundRank(rank);

        assertNotNull(result);
        assertEquals(expectedHighScores, result);
    }
}

package nl.hva.hvacrawler.persistence.dao;

import nl.hva.hvacrawler.business.domain.Game;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import nl.hva.hvacrawler.business.domain.Crawler;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Description: Simulate the jdbcGameDao behavior with Mockito to check if works as expected
 * Author: Petya Katsarova
 * Email: pskpetya@gmail.com
 * Created on: 12/09/2023 10:51
 */

@SpringBootTest
//@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JdbcGameDaoTest {

    @Mock
    private JdbcTemplate jdbcTemplate = mock(JdbcTemplate.class);
    @Mock
    private JdbcCrawlerDao jdbcCrawlerDao;
    @InjectMocks
    private JdbcGameDao jdbcGameDao;

    @BeforeEach
    public void setUp() {
        jdbcCrawlerDao = new JdbcCrawlerDao(jdbcTemplate);
        jdbcGameDao = new JdbcGameDao(jdbcTemplate, jdbcCrawlerDao);// or use MockitoAnnotations.openMocks(this);
    }

//    not void: return sth
    @Test
    public void testSaveOrUpdateOne_NewGame() {
        Game game = new Game(new Crawler());
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

        // Mock the update method to simulate a successful database insert.
        //Capture the KeyHolder passed to it; Populate it with a simulated generated key.  Return 1 to indicate that one record was successfully inserted.
        when(jdbcTemplate.update(any(PreparedStatementCreator.class), any(KeyHolder.class)))
                .thenAnswer(invocation -> {
                    KeyHolder kh = invocation.getArgument(1, KeyHolder.class);
                    // Simulate the generated key.
                    Map<String, Object> keys = new HashMap<>();
                    keys.put("GENERATED_KEY", 1);
                    kh.getKeyList().add(keys);
                    return 1;  // Return 1 to indicate one row inserted.
                });
//get it from the db: first save and then use the dao to save and compare the expected and actual: saved and actual
        Game savedGame = jdbcGameDao.saveOrUpdateOne(game);

        assertNotNull(savedGame);
        assertEquals(1, savedGame.getId());
        assertEquals(Game.GameStatus.PAUSED, savedGame.getGameStatus());
    }

    @Test
    public void testSaveOrUpdateOne_ExistingGame() {
        Game game = new Game(new Crawler());
        game.setId(1);
        game.setGameStatus(Game.GameStatus.PAUSED);

        when(jdbcTemplate.update(anyString(), any(PreparedStatementSetter.class))).thenReturn(1);
        Game updatedGame = jdbcGameDao.saveOrUpdateOne(game);

        assertNotNull(updatedGame);
        assertEquals(1, updatedGame.getId());
        assertEquals(Game.GameStatus.PAUSED, updatedGame.getGameStatus());
    }

    // fails
//    @Test
//    public void testFindOneById_GameFound() throws SQLException {
//        ResultSet rs = mock(ResultSet.class);
//        when(rs.next()).thenReturn(true).thenReturn(false);  // Simulate one row in ResultSet
//        when(rs.getInt("idGame")).thenReturn(1);
//        when(rs.getString("status")).thenReturn("PAUSED");
//        when(rs.getInt("Crawler_idCrawler")).thenReturn(1);
//        when(rs.getInt("finalScore")).thenReturn(100);
//        when(rs.getDate("finishedAt")).thenReturn(new java.sql.Date(System.currentTimeMillis()));
//
//        Crawler mockCrawler = new Crawler();
//        when(jdbcCrawlerDao.findOneById(1)).thenReturn(Optional.of(mockCrawler));
//
//        // Mock jdbcTemplate.query to return a list containing one game.
//        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class)))
//                .thenAnswer(invocation -> {
//                    RowMapper<Game> mapper = invocation.getArgument(2, RowMapper.class);
//                    return Collections.singletonList(mapper.mapRow(rs, 1));
//                });
//
//        // Inject jdbcTemplate into jdbcGameDao if needed (uncomment if needed)
//        // jdbcGameDao.setJdbcTemplate(jdbcTemplate);
//
//        Optional<Game> result = jdbcGameDao.findOneById(1);
//
//        assertTrue(result.isPresent());  // This should pass if everything is set up correctly
//        assertEquals(1, result.get().getId());
//        assertEquals(Game.GameStatus.PAUSED, result.get().getGameStatus());
//        assertEquals(100, result.get().getFinalScore());
//        assertNotNull(result.get().getFinishedAt());
//    }

}


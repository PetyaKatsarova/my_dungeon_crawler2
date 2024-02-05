package nl.hva.hvacrawler.persistence.dao;

import nl.hva.hvacrawler.business.domain.Highscore;
import nl.hva.hvacrawler.persistence.dao.JdbcUserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JdbcUserHighscoreDaoTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private JdbcUserDao jdbcUserDao;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetHighScoresList() {
        Highscore highscore1 = new Highscore("test1@email.com", 100);
        Highscore highscore2 = new Highscore("test2@email.com", 200);

        when(jdbcTemplate.query(anyString(), any(JdbcUserDao.HighScoreRowMapper.class), any())).thenReturn(List.of(highscore1, highscore2));


        List<Highscore> result = jdbcUserDao.getHighScoreslist(2);

        assertEquals(2, result.size());
        assertEquals(highscore1, result.get(0));
        assertEquals(highscore2, result.get(1));
    }

    @Test
    public void testGetHighScoresAroundName() {
        Highscore highscore1 = new Highscore("test1@email.com", 100);
        Highscore highscore2 = new Highscore("test2@email.com", 200);

        when(jdbcTemplate.query(anyString(), any(JdbcUserDao.HighScoreRowMapper.class), any())).thenReturn(List.of(highscore1, highscore2));


        List<Highscore> result = jdbcUserDao.getHighScoresAroundName("test");

        assertEquals(2, result.size());
        assertEquals(highscore1, result.get(0));
        assertEquals(highscore2, result.get(1));
    }
}

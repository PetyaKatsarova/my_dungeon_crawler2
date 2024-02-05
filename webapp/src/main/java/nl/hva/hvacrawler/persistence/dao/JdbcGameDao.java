package nl.hva.hvacrawler.persistence.dao;

import nl.hva.hvacrawler.business.domain.Game;
import nl.hva.hvacrawler.business.domain.Crawler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Optional;

/**
 * Description:
 * Author: Petya Katsarova
 * Email: pskpetya@gmail.com
 * Created on: 14/08/2023 12:12
 */

@Repository
public class JdbcGameDao implements BaseDao<Game> {
    private final Logger logger = LoggerFactory.getLogger(JdbcUserDao.class);
    private final JdbcTemplate jdbcTemplate;
    private JdbcCrawlerDao jdbcCrawlerDao;

    public JdbcGameDao(JdbcTemplate jdbcTemplate, JdbcCrawlerDao jdbcCrawlerDao) {
        super();
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcCrawlerDao = jdbcCrawlerDao;
        logger.info("New GameDAO");
    }

    @Override
    public Game saveOrUpdateOne(Game game) {
        if (game.getId() == 0)
            return saveGame(game);
        else {
            return updateGame(game);
        }
    }

    private Game saveGame(Game game) {
        game.setGameStatus(Game.GameStatus.PAUSED);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> insertGameStatement(game, connection), keyHolder);
        if (keyHolder.getKey() == null) {
            logger.error("KeyHolder key is null. Insert operation may have failed.");
            return null;
        }
        int newKey = keyHolder.getKey().intValue();
        game.setId(newKey);
        return game;
    }

    private PreparedStatement insertGameStatement(Game game, Connection connection) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO Game (status, finalScore,finishedAt, idCrawler) " +
                            "VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, game.getGameStatus().name()); // Converts enum to string
            ps.setInt(2, game.getFinalScore());
            ps.setDate(3, game.getFinishedAt());
            ps.setInt(4, game.getGameOwner().getIdCharacter());
            return ps;
        } catch (SQLException ex) {
            logger.error("Database error in GameDao: " + ex.getMessage());
            return null;
        }
    }

    private Game updateGame(Game game) {
        // !! todo to add the logic when game is over, to change the status to 'finished'
        String updateSql = "UPDATE Game SET idCrawler = ?, status = ?, finalScore = ?, finishedAt" +
                " = ? WHERE idGame = ?";
        jdbcTemplate.update(updateSql,
                game.getGameOwner().getIdCharacter(),
                game.getGameStatus().name(),
                game.getFinalScore(),
                game.getFinishedAt(),
                game.getId()
        );
        return game;
    }

    @Override
    public Optional<Game> findOneById(int idGame) {
        List<Game> games = jdbcTemplate.query("SELECT * FROM Game WHERE idGame = ?", new GameRowMapper(), idGame);
        if (games.size() == 1)
            return Optional.of(games.get(0));
        logger.error("No game with id " + idGame + " in the db.");
        return Optional.empty();
    }

    public int getHighestPausedGameIdFromCrawlerId(int idCrawler){
        String sql = "SELECT MAX(idGame) FROM Game WHERE idCrawler = ? AND status = 'PAUSED'";
        try {
            Integer gameId = jdbcTemplate.queryForObject(sql, new Object[]{idCrawler},
                    Integer.class);
            return gameId;
        } catch (Exception e) {
            logger.error("Error finding gameId by crawlerId", e);
            return -1;
        }
    }

    private class GameRowMapper implements RowMapper<Game> {
        @Override
        public Game mapRow(ResultSet rs, int rowNum) throws SQLException {
            int idGame = rs.getInt("idGame");
            String status = rs.getString("status");
            int idCrawler = rs.getInt("Crawler_idCrawler");
            int finalScore = rs.getInt("finalScore");
            Date finishedAt = rs.getDate("finishedAt");
            Crawler crawler = jdbcCrawlerDao.findOneById(idCrawler).orElse(null);
            Game game = new Game(crawler);
            Game.GameStatus gameStatus = Game.GameStatus.valueOf(status.toUpperCase());
            game.setGameStatus(gameStatus);
            game.setId(idGame);
            return game;
        }
    }

}


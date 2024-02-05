
/**
 * Description: JdbcPlayerDao class to interact with db
 * Author: Petya Katsarova
 * Email: pskpetya@gmail.com
 * Created on: 10/08/2023 15:59
 */
package nl.hva.hvacrawler.persistence.dao;

import nl.hva.hvacrawler.business.domain.Crawler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class JdbcCrawlerDao implements BaseDao<Crawler>, CrawlerDao {

    private final Logger logger = LoggerFactory.getLogger(JdbcCrawlerDao.class);
    private final JdbcTemplate jdbcTemplate;

    public JdbcCrawlerDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Crawler saveOrUpdateOne(Crawler crawler) {
        if (crawler.getIdCharacter() == 0) {
            saveCrawler(crawler);
        } else {
            updateCrawler(crawler);
        }
        return crawler;
    }

    private Crawler saveCrawler(Crawler crawler) {
        String sql = "INSERT INTO Crawler(name, healthPoints, kills, roomsVisited, gold, weapon, idUser) VALUES (?, ?, ?, ? ,?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[]{"idCrawler"});
                ps.setString(1, crawler.getName());
                ps.setInt(2, crawler.getHealthPoints());
                ps.setInt(3, crawler.getKills());
                ps.setInt(4, crawler.getRoomsVisited());
                ps.setInt(5, crawler.getGold());
                ps.setInt(6, crawler.getWeapon().getId());
                ps.setInt(7, crawler.getUser().getIdUser());
                return ps;
            }, keyHolder);
            int newKey = keyHolder.getKey().intValue();
            crawler.setIdCharacter(newKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return crawler;
    }


    private void updateCrawler(Crawler crawler) {
        String sql = "UPDATE Crawler SET name = ?, healthPoints = ?, kills = ?, roomsVisited = ?," +
                " gold = ?, weapon = ?, idUser = ? WHERE idCrawler = ?";
        jdbcTemplate.update(sql,
                crawler.getName(),
                crawler.getHealthPoints(),
                crawler.getKills(),
                crawler.getRoomsVisited(),
                crawler.getGold(),
                crawler.getWeapon().getId(),
                crawler.getUser().getIdUser(),
                crawler.getIdCharacter()
        );
    }

    @Override
    public Optional<Crawler> findOneById(int idCrawler) {
        String sql = "SELECT * FROM Crawler WHERE idCrawler = ?";
        try {
            Crawler crawler = jdbcTemplate.queryForObject(sql, new CrawlerRowMapper(), idCrawler);
            return Optional.ofNullable(crawler);
        } catch (Exception e) {
            logger.error("Error finding crawler by id", e);
            return Optional.empty();
        }
    }

    @Override
    public Optional<Crawler> findOneByIdUser(int idUser) {
        String sql = "SELECT * FROM Crawler WHERE idUser = ?";
        try {
            Crawler crawler = jdbcTemplate.queryForObject(sql, new CrawlerRowMapper(), idUser);
            return Optional.ofNullable(crawler);
        } catch (Exception e) {
            logger.error("Error finding crawler by id", e);
            return Optional.empty();
        }
    }

    @Override
    public int getWeaponIdByCrawlerId(int idCrawler) {
        String sql = "SELECT Weapon FROM Crawler WHERE idCrawler = ?;";
        try {
            Integer weaponid = jdbcTemplate.queryForObject(sql, new Object[]{idCrawler},
                    Integer.class);
            return weaponid;
        } catch (Exception e) {
            logger.error("Error finding weaponId by crawlerId", e);
            return -1;
        }
    }

    @Override
    public int getUserIdByCrawlerId(int idCrawler) {
        String sql = "SELECT idUser FROM Crawler WHERE idCrawler = ?;";
        try {
            Integer userid = jdbcTemplate.queryForObject(sql, new Object[]{idCrawler},
                    Integer.class);
            return userid;
        } catch (Exception e) {
            logger.error("Error finding userId by crawlerId", e);
            return -1;
        }
    }

    @Override
    public boolean checkIfCrawlerAlreadyExists(String name) {
        String sql = "SELECT COUNT(*) FROM Crawler WHERE name = ?";
        try {
            int count = jdbcTemplate.queryForObject(sql, Integer.class, name);
            return count > 0;
        } catch (Exception e) {
            logger.error("Error finding userId by crawlerId", e);
            return true;
        }
    }


    private class CrawlerRowMapper implements RowMapper<Crawler> {
        @Override
        public Crawler mapRow(ResultSet rs, int rowNum) throws SQLException {
            int idCrawler = rs.getInt("idCrawler");
            String name = rs.getString("name");
            int healthPoints = rs.getInt("healthPoints");
            int kills = rs.getInt("kills");
            int roomsVisited = rs.getInt("roomsVisited");
            int gold = rs.getInt("gold");
            Crawler crawler = new Crawler(name, healthPoints, gold, kills, roomsVisited);
            crawler.setIdCharacter(idCrawler);
            return crawler;
        }
    }
}


package nl.hva.hvacrawler.persistence.dao;

import nl.hva.hvacrawler.communication.dto.HasPotionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class JdbcCrawlerHasPotionDao implements CrawlerHasPotionDao{

    private final Logger logger = LoggerFactory.getLogger(JdbcCrawlerHasPotionDao.class);
    private final JdbcTemplate jdbcTemplate;

    public JdbcCrawlerHasPotionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        logger.info("New CrawlerHasPotionDao");
    }

    @Override
    public List<HasPotionDTO> findAllPotionInfoByCrawlerId(int crawlerId) {
        String sql = "SELECT idCrawler, idItem_Potion, quantity FROM Crawler_has_Potion WHERE idCrawler = ?";
        return jdbcTemplate.query(sql, new HasPotionDTORowMapper(), crawlerId);
    }

    @Override
    public void saveOrUpdateOne(HasPotionDTO potion) {
        if (potionExists(potion.getIdCrawler(), potion.getIdItemPotion())) {
            updateCrawlerHasPotion(potion);
        } else {
            saveCrawlerHasPotion(potion);
        }
    }

    @Override
    public boolean potionExists(int idCrawler, int idItemPotion) {
        String sql = "SELECT COUNT(*) FROM Crawler_has_Potion WHERE idCrawler = ? AND idItem_Potion = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, idCrawler, idItemPotion);
        return count != null && count > 0;
    }


    public void saveCrawlerHasPotion(HasPotionDTO potion) {
        String sql = "INSERT INTO Crawler_has_Potion (idCrawler, idItem_Potion, quantity) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, potion.getIdCrawler(), potion.getIdItemPotion(), potion.getQuantity());
    }

    public void updateCrawlerHasPotion(HasPotionDTO potion) {
        String sql = "UPDATE Crawler_has_Potion SET quantity = quantity + 1 WHERE idCrawler = ? AND idItem_Potion = ?";
        jdbcTemplate.update(sql, potion.getIdCrawler(), potion.getIdItemPotion());
    }

    @Override
    public void deleteCrawlerHasPotionByCrawlerId(int idCrawler) {
        String sql = "DELETE FROM Crawler_has_Potion WHERE idCrawler = ?";
        jdbcTemplate.update(sql, idCrawler);
    }

    public class HasPotionDTORowMapper implements RowMapper<HasPotionDTO> {

        @Override
        public HasPotionDTO mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
            int idCrawler = resultSet.getInt("idCrawler");
            int idItemPotion = resultSet.getInt("idItem_Potion");
            int quantity = resultSet.getInt("quantity");

            return new HasPotionDTO(idCrawler, idItemPotion, quantity);
        }
    }

}

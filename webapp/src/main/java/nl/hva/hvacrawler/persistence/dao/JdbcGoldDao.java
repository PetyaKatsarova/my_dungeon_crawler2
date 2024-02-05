package nl.hva.hvacrawler.persistence.dao;

import nl.hva.hvacrawler.business.domain.Gold;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
@Repository
public class JdbcGoldDao implements GoldDao {
    private final JdbcTemplate jdbcTemplate;
    private final Logger logger = LoggerFactory.getLogger(JdbcGoldDao.class);

    public JdbcGoldDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        logger.info("New JdbcGoldDao");
    }

    @Override
    public Optional<Gold> getGoldById(int id) {
        String query = "SELECT g.*, i.name as itemName From Gold g  LEFT JOIN Item i ON g.idItem = i.idItem WHERE i.idItem = ?";
        List<Gold> golds = jdbcTemplate.query(query, this::mapGoldWithItemName, id);
        return golds.isEmpty() ? Optional.empty() : Optional.of(golds.get(0));
    }


    @Override
    public Optional<Gold> getRandomGold() {
        return Optional.empty();
    }
    private Gold mapGoldWithItemName(ResultSet rs, int rowNum) throws SQLException {
        int id =rs.getInt("idItem");
        String itemName = rs.getString("itemName");
        int value = rs.getInt("value");
        Gold gold = new Gold(id, itemName, value);
        return gold;
    }
}

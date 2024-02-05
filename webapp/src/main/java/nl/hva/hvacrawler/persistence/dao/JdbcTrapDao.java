package nl.hva.hvacrawler.persistence.dao;

import nl.hva.hvacrawler.business.domain.Trap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcTrapDao implements TrapDao {
    private final Logger logger = LoggerFactory.getLogger(JdbcTrapDao.class);
    private final JdbcTemplate jdbcTemplate;

    public JdbcTrapDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        logger.info("New JdbcTrapDao");
    }
    @Override
    public Optional<Trap> getTrapById(int id) {
        String query = "SELECT t.*, i.name AS itemName FROM Trap t  LEFT JOIN Item i ON t.idItem = i.idItem WHERE i.idItem = ?";
        List<Trap> traps = jdbcTemplate.query(query, this::mapTrapWithItemName, id);
        return traps.isEmpty() ? Optional.empty() : Optional.of(traps.get(0));
    }
    @Override
    public Optional<Trap> getRandomTrap() {
        return Optional.empty();
    }

    private Trap mapTrapWithItemName(ResultSet rs, int rowNum) throws SQLException {
        int id =rs.getInt("idItem");
        String itemName = rs.getString("itemName");
        int damagePoints = rs.getInt("damagePoints");

        Trap trap = new Trap(id, itemName, damagePoints);
        return trap;
    }
}

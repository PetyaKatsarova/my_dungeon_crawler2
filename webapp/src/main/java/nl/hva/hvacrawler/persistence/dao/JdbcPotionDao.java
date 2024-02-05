package nl.hva.hvacrawler.persistence.dao;

import nl.hva.hvacrawler.business.domain.Potion;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcPotionDao implements PotionDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcPotionDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Potion> getPotionById(int id) {
        String query = "SELECT p.*, i.name as itemName " +
                "FROM Potion p " +
                "LEFT JOIN Item i ON p.idItem = i.idItem " +
                "WHERE i.idItem = ?";

        List<Potion> potions = jdbcTemplate.query(query, this::mapPotionWithItemName, id);

        return potions.isEmpty() ? Optional.empty() : Optional.of(potions.get(0));
    }


    @Override
    public Optional<Potion> getRandomPotion() {
        return Optional.empty();
    }


    private Potion mapPotionWithItemName(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("idItem");
        String itemName = rs.getString("itemName");
        int healthPoints = rs.getInt("healthPoints");

        Potion potion = new Potion(id, itemName, healthPoints);

        return potion;
    }
}

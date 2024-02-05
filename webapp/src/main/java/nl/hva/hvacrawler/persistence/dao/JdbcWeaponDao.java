package nl.hva.hvacrawler.persistence.dao;

import nl.hva.hvacrawler.business.domain.Weapon;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcWeaponDao implements WeaponDao {
    private final JdbcTemplate jdbcTemplate;

    public JdbcWeaponDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Weapon> getWeaponById(int id) {
        String query = "SELECT w.*, i.name as itemName FROM Weapon w LEFT JOIN Item i ON w.idItem = i.idItem WHERE i.idItem = ?";

        List<Weapon> weapons = jdbcTemplate.query(query, this::mapWeaponWithItemName, id);

        return weapons.isEmpty() ? Optional.empty() : Optional.of(weapons.get(0));
    }

    @Override
    public Optional<Weapon> getRandomWeapon() {
        return Optional.empty();
    }

    private Weapon mapWeaponWithItemName(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("idItem");
        String itemName = rs.getString("itemName");
        int attackModifier = rs.getInt("attackModifier");

        Weapon weapon = new Weapon(id, itemName, attackModifier);
         // Set the itemId for reference to the associated item

        return weapon;
    }
}

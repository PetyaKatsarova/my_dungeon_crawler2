package nl.hva.hvacrawler.persistence.dao;

import nl.hva.hvacrawler.business.domain.Monster;
import nl.hva.hvacrawler.util.RandomPercentageGenerator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;


@Repository
public class JdbcMonsterDao implements MonsterDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcMonsterDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(Monster monster) {
        String query = "INSERT INTO Monster (name, healthpoints) VALUES (?, ?)";
        jdbcTemplate.update(query, monster.getName(), monster.getHealthPoints());
    }

    @Override
    public Optional<Monster> getMonsterById(int id) {
        String query = "SELECT * FROM Monster WHERE idMonster = ?";
        List<Monster> monsters = jdbcTemplate.query(query, new MonsterRowMapper(), id);
        return monsters.isEmpty() ? Optional.empty() : Optional.of(monsters.get(0));
    }


    private class MonsterRowMapper implements RowMapper<Monster> {
        @Override
        public Monster mapRow(ResultSet rs, int rowNum) throws SQLException {
            int idMonster = rs.getInt("idMonster");
            String name = rs.getString("name");
            int healthPoints = rs.getInt("healthpoints");
            double randomPercentage = RandomPercentageGenerator.randomPercentageBetweenFiftyAndHundred();
            int goldPieces = (int) (healthPoints * randomPercentage);

            Monster monster = new Monster(name, healthPoints, goldPieces, idMonster);
            monster.setIdCharacter(idMonster);
            return monster;
        }
    }
}

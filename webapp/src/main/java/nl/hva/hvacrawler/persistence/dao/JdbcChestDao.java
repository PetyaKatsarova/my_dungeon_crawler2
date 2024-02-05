package nl.hva.hvacrawler.persistence.dao;

import nl.hva.hvacrawler.business.domain.Chest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcChestDao implements ChestDAO<Chest> {
    private final Logger logger = LoggerFactory.getLogger(JdbcChestDao.class);
    private final JdbcTemplate jdbcTemplate;

    public JdbcChestDao(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
        logger.info("New ChestDAO");
    }

    @Override
    public Chest saveOrUpdateOne(Chest chest) {
        Chest returnChest = null;
        if (chest != null && chest.getId() == 0) {
            returnChest = saveChest(chest);
        } else returnChest = updateChest(chest);
        return returnChest;
    }

    @Override
    public Optional<Chest> findOneById(int id) {
//        todo
        return Optional.empty();
    }

    public int getIdItemByIdChest(int idChest) {
        String sql = "SELECT idItem FROM Chest WHERE idChest = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, idChest);
    }

    private Chest saveChest(Chest chest) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        if (chest != null) {
            try {
                jdbcTemplate.update(connection -> insertChestStatement(chest, connection), keyHolder);
                int newKey = keyHolder.getKey().intValue();
                chest.setId(newKey);
                return chest;
            } catch (DataAccessException e) {
                Throwable rootCause = e.getRootCause();
                if (rootCause instanceof SQLException) {
                    SQLException sqlException = (SQLException) rootCause;
//                logger.error("SQL State: " + sqlException.getSQLState());
                    logger.error("Message: " + sqlException.getMessage());
                }
            }
        }
        return null;
    }


    private PreparedStatement insertChestStatement(Chest chest, Connection connection) {
        try {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO Chest (idItem) VALUES (?)", Statement.RETURN_GENERATED_KEYS);
            if (chest.getItem() != null)
                ps.setInt(1, chest.getItem().getId());
                // this was if the chest is empty adn item id is null: now we have Nothing class for that
            else
                ps.setNull(1, Types.INTEGER);
            return ps;
        } catch (SQLException e) {
            logger.error("SQL syntax not accepted by database" + e.getMessage());
            return null;
        }
    }

    private Chest updateChest(Chest chest) {
        if (chest != null && chest.getItem() != null) {
            String updateQuerry = "UPDATE Chest SET idItem = ? WHERE IdChest = ?";

            jdbcTemplate.update(updateQuerry,
                    chest.getItem().getId(),
                    chest.getId());
        }
//         this was done to deal with empty chest: now we have Nothing class for that
//        else {
//            String updateQuerry = "UPDATE Chest SET idItem = null WHERE IdChest = ?";// we have Nothing class: maybe not needed this part ??
//            jdbcTemplate.update(updateQuerry,
//                    chest.getId());
//        }

        return chest;
    }



//    !!!! NB !!!!!!!!! THIS CODE CREATES CIRCULAR CONNECTION BETWEEN BEANS: THE ROW MAPPER BELLOW
//     SPECIFIALLY THIS LINE:    Chest chest = jdbcChestDao.findOneById(idChest).orElse(null);
//    @Override
//    public Optional<Chest> findOneById(int id) {
//        List<Chest> chests = jdbcTemplate.query("SELECT * FROM Chest WHERE idChest =?", new ChestRowMapper(), id);
//        if (chests.size() == 1) {
//            return Optional.of(chests.get(0));
//        } else {
//            logger.error("chest with with id " + id + " does not exists in database");
//            return Optional.empty();
//        }
//    }
//
//    private class ChestRowMapper implements RowMapper<Chest> {
//        @Override
//        public Chest mapRow(ResultSet rs, int rowNum) throws SQLException {
//            int idChest = rs.getInt("idChest");
//            int idItem = rs.getInt("idItem");
//            Chest chest = jdbcChestDao.findOneById(idChest).orElse(null);
//            chest.setId(idChest);
//            return chest;
//        }
//    }
}

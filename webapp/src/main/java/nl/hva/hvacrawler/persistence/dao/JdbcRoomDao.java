package nl.hva.hvacrawler.persistence.dao;

import nl.hva.hvacrawler.business.domain.Chest;
import nl.hva.hvacrawler.business.domain.Game;
import nl.hva.hvacrawler.business.domain.Monster;
import nl.hva.hvacrawler.business.domain.Room;
import nl.hva.hvacrawler.communication.dto.MonsterDTO;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;

@Repository
public class JdbcRoomDao implements BaseDao<Room> {

    private final JdbcTemplate  jdbcTemplate;
    private final Logger        logger = LoggerFactory.getLogger(JdbcRoomDao.class);
    private final JdbcChestDao  jdbcChestDao;

    public JdbcRoomDao(JdbcTemplate jdbcTemplate, JdbcChestDao jdbcChestDao1) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcChestDao = jdbcChestDao1;
        logger.info("JdbcRoomDao created");
    }

    @Override
    public Room saveOrUpdateOne(Room room) {
        Game game = room.getGame();
        if (game == null || game.getId() == 0) {
            return null; // Or throw exception
        }
        if (room.getId() == 0) {
            return saveRoom(room);
        } else
            return updateRoom(room);
    }

    private Room saveRoom(Room room) {
        // every room chest id is set in roomRepository, where is first is saved in db adn then requested the id
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> insertRoomStatement(room, connection), keyHolder);
        int newKey = keyHolder.getKey().intValue();
        room.setId(newKey);
        return room;
    }

    private PreparedStatement insertRoomStatement(Room room, Connection connection) {
        try {
            StringBuilder sql = new StringBuilder("INSERT INTO Room(`row`, `column`, holygrail, currentRoom, visitedRoom, idGame");
            if (room.getMonster() != null)
                sql.append(", idMonster, healthMonster, goldMonster");
            if (room.getChest() != null)
                sql.append(", idChest");
            if (room.getDroppedItem() != null)
                sql.append(", droppedItem");
            sql.append(") VALUES (?, ?, ?, ?, ?, ?");
            if (room.getMonster() != null)
                sql.append(", ?, ?, ?");
            if (room.getChest() != null)
                sql.append(", ?");
            if (room.getDroppedItem() != null)
                sql.append(", ?");
            sql.append(")");

            PreparedStatement ps = connection.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);

            ps.setInt(1, room.getRow());
            ps.setInt(2, room.getColumn());
            ps.setBoolean(3, room.isContainsGrail());
            ps.setBoolean(4, room.isCurrentRoom());
            ps.setBoolean(5, room.isVisited());
            ps.setInt(6, room.getGame().getId());

            // Keep track of the next parameter index
            int nextIndex = 7;
            if (room.getMonster() != null) {
                Monster currMonster = room.getMonster();
                ps.setInt(nextIndex, currMonster.getIdCharacter());;
                nextIndex++;
                ps.setInt(nextIndex, currMonster.getHealthPoints());
                nextIndex++;
                ps.setInt(nextIndex, currMonster.getGold());
                nextIndex++;
            }
            if (room.getChest() != null) {
                Chest chest = jdbcChestDao.saveOrUpdateOne(room.getChest());
                if (chest != null) {
                    room.setChest(chest);
                    ps.setInt(nextIndex, room.getChest().getId()); // If chest id is always 0, consider adding logic to auto-increment.
                    nextIndex++;
                } else {
                    logger.info("NO CHEST IN CURRENT ROOM");
                }
            }
            if (room.getDroppedItem() != null){
                ps.setInt(nextIndex, room.getDroppedItem().getId());
            }
            return ps;
        } catch (SQLException ex) {
            logger.error("Database error in RoomDao: " + ex.getMessage());
            return null;
        }
    }


//    private Room updateRoom(Room room) {
//        Monster monster = room.getMonster();
//        Chest chest = room.getChest();
//        int monsterId = (monster != null) ? monster.getIdCharacter() : 0; // Use default value if null
//        int chestId = (chest != null) ? chest.getId() : 0; // Use default value if null: not chest in the room
//
//        String query = "UPDATE Room SET row=?, column=?, holygrail=?, currentRoom=?, " +
//                "visitedRoom=?, idGame=?, idMonster=?, healthMonster=?, goldMonster=?, idChest=?," +
//                " droppedItem=? WHERE idRoom=?";
//        jdbcTemplate.update(query,
//                room.getRow(),
//                room.getColumn(),
//                room.isContainsGrail(),
//                room.isCurrentRoom(),
//                room.isVisited(),
//                room.getGame().getId(),
//                if (room.getMonster() != null){
//                room.getMonster().getIdCharacter(),
//                room.getMonster().getHealthPoints(),
//                room.getMonster().getGold()},
//                if(room.getChest() != null)(
//                room.getChest().getId()),
//                room.getDroppedItem(),
//                room.getId());
//        return room;
//    }

//    private Room updateRoom(Room room) {
//        Monster monster = room.getMonster();
//        Chest chest = room.getChest();
//        int monsterId = (monster != null) ? monster.getIdCharacter() : 0; // Gebruik standaardwaarde als null
//        int chestId = (chest != null) ? chest.getId() : 0; // Gebruik standaardwaarde als null: geen kist in de kamer
//
//        String query = "UPDATE Room SET row=?, column=?, holygrail=?, currentRoom=?, " +
//                "visitedRoom=?, idGame=?, idMonster=?, healthMonster=?, goldMonster=?, idChest=?," +
//                " droppedItem=? WHERE idRoom=?";
//
//        // Voer de update-query uit met behulp van jdbcTemplate.update
//        jdbcTemplate.update(query,
//                room.getRow(),
//                room.getColumn(),
//                room.isContainsGrail(),
//                room.isCurrentRoom(),
//                room.isVisited(),
//                room.getGame().getId(),
//                monsterId, // Gebruik monsterId in plaats van room.getMonster().getIdCharacter()
//                (monster != null) ? monster.getHealthPoints() : 0, // Gebruik standaardwaarde als monster null is
//                (monster != null) ? monster.getGold() : 0, // Gebruik standaardwaarde als monster null is
//                chestId, // Gebruik chestId in plaats van room.getChest().getId()
//                (room.getChest() != null) ? room.getChest().getDroppedItem() : 0, // Gebruik standaardwaarde als kist null is
//                room.getId());
//
//        return room;
//    }

    private Room updateRoom(Room room) {
        Monster monster = room.getMonster();
        Chest chest = room.getChest();
        int monsterId = (monster != null) ? monster.getIdCharacter() : 0; // Gebruik standaardwaarde als null
        int chestId = (chest != null) ? chest.getId() : 0; // Gebruik standaardwaarde als null: geen kist in de kamer

        String query = "UPDATE Room SET `row`=?, `column`=?, holygrail=?, currentRoom=?, " +
                "visitedRoom=?, idGame=?, idMonster=?, healthMonster=?, goldMonster=?, idChest=?," +
                " droppedItem=? WHERE idRoom=?";

        // Voer de update-query uit met behulp van jdbcTemplate.update
        jdbcTemplate.update(query,
                room.getRow(),
                room.getColumn(),
                room.isContainsGrail(),
                room.isCurrentRoom(),
                room.isVisited(),
                room.getGame().getId(),
                monsterId, // Gebruik monsterId in plaats van room.getMonster().getIdCharacter()
                (monster != null) ? monster.getHealthPoints() : 0, // Gebruik standaardwaarde als monster null is
                (monster != null) ? monster.getGold() : 0, // Gebruik standaardwaarde als monster null is
                chestId, // Gebruik chestId in plaats van room.getChest().getId()
                room.getDroppedItem().getId(), // Update droppedItem direct
                room.getId());

        return room;
    }

    public int getHighestColumnByGameId(int idGame){
        String sql = "SELECT MAX(`column`) FROM Room WHERE idGame = ?";
        try {
            Integer column = jdbcTemplate.queryForObject(sql, new Object[]{idGame},
                    Integer.class);
            return column;
        } catch (Exception e) {
            logger.error("Error finding gameId by crawlerId", e);
            return -1;
        }
    }

    public int getHighestRowByGameId(int idGame){
        String sql = "SELECT MAX(`row`) FROM Room WHERE idGame = ?";
        try {
            Integer row = jdbcTemplate.queryForObject(sql, new Object[]{idGame},
                    Integer.class);
            return row;
        } catch (Exception e) {
            logger.error("Error finding gameId by crawlerId", e);
            return -1;
        }
    }

    public List<Room> findAllRoomsFromIdGame(int idGame) {
        String sql = "SELECT * FROM Room WHERE idGame = ? ORDER BY idRoom";
        return jdbcTemplate.query(sql, new Object[]{idGame}, new RoomRowMapper());
    }

    @Override
    public Optional<Room> findOneById(int id) {
        String query = "SELECT * FROM Room WHERE idRoom = ?";
        List<Room> rooms = jdbcTemplate.query(query, new RoomRowMapper(), id);
        return rooms.isEmpty() ? Optional.empty() : Optional.of(rooms.get(0));
    }

    public MonsterDTO getMonsterDataByRoomId(int idRoom) {
        String sql = "SELECT idMonster, healthMonster, goldMonster FROM Room WHERE idRoom = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{idRoom}, new MonsterRowMapper());
    }

    public int getIdChestByIdRoom(int idRoom) {
        String sql = "SELECT idChest FROM Room WHERE idRoom = ?";
        Integer idChest = jdbcTemplate.queryForObject(sql, Integer.class, idRoom);
        return idChest != null ? idChest : -1;
    }

    public int getIdDroppedItemByIdRoom(int idRoom) {
        String sql = "SELECT droppedItem FROM Room WHERE idRoom = ?";
        Integer droppedItem = jdbcTemplate.queryForObject(sql, Integer.class, idRoom);
        return droppedItem != null ? droppedItem : -1;
    }
    private static class MonsterRowMapper implements RowMapper<MonsterDTO> {
        @Override
        public MonsterDTO mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            MonsterDTO monster = new MonsterDTO();
            monster.setIdCharacter(resultSet.getInt("idMonster"));
            monster.setHealthPoints(resultSet.getInt("healthMonster"));
            monster.setGold(resultSet.getInt("goldMonster"));
            return monster;
        }
    }

    private class RoomRowMapper implements RowMapper<Room> {
        @Override
        public Room mapRow(ResultSet rs, int rowNum) throws SQLException {
            Room room = new Room();
            room.setId(rs.getInt("idRoom"));
            room.setRow(rs.getInt("row"));
            room.setColumn(rs.getInt("column"));
            room.setContainsGrail(rs.getBoolean("holygrail"));
            room.setCurrentRoom(rs.getBoolean("currentRoom"));
            room.setVisited(rs.getBoolean("visitedRoom"));
            return room;
        }
    }

}

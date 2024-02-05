package nl.hva.hvacrawler.persistence.dao;

import nl.hva.hvacrawler.business.domain.Highscore;
import nl.hva.hvacrawler.business.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;

@Repository
public class JdbcUserDao implements UserDao {
    private final Logger logger = LoggerFactory.getLogger(JdbcUserDao.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        logger.info("New UserDAO");
    }

    public JdbcUserDao() {
        this.jdbcTemplate = new JdbcTemplate();
    }

    @Override public User save(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> buildInsertCustomerStatement(user, connection), keyHolder);
        int newKey = keyHolder.getKey().intValue();
        user.setIdUser(newKey);
        return user;
    }

    @Override
    public User updateUser(User user) {
        jdbcTemplate.update("UPDATE User SET email = ?, password = ?, salt = ?, isVerified = ?, " +
                        "resetToken = ?, jwtToken = ?, highscore = ? WHERE idUser = ?",
                user.getEmail(), user.getPassword(), user.getSalt(), user.isVerified(),
                user.getResetToken(), user.getJwtToken(), user.getHighscore(), user.getIdUser());
        return user;
    }


    private PreparedStatement buildInsertCustomerStatement(User user, Connection connection) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO User(email, password, salt, isVerified, resetToken, jwtToken, highscore) " +
                        "VALUES (?,?,?,?,?,?,?)",
                Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, user.getEmail());
        ps.setString(2, user.getPassword());
        ps.setString(3, user.getSalt());
        ps.setBoolean(4, user.isVerified());
        ps.setString(5, user.getResetToken());
        ps.setString(6, user.getJwtToken());
        ps.setInt(7, user.getHighscore());
        return ps;
    }

    @Override
    public User findUserByEmail(String email) {
        String query = "SELECT * FROM User WHERE email = ?;";
        List<User> users = jdbcTemplate.query(query, new UserRowMapper(), email);
        if (users.size() == 1) {
            return users.get(0);
        }
        return null;
    }

    @Override public User findUserById(int idUser) {
        String query = "SELECT * FROM User WHERE idUser = ?;";
        List<User> users = jdbcTemplate.query(query, new UserRowMapper(), idUser);
        if (users.size() == 1) {
            return users.get(0);
        }
        return null;
    }

    @Override
    public User findUserByjwtToken(String jwtToken) {
        String query = "SELECT * FROM User WHERE jwtToken = ?;";
        List<User> users = jdbcTemplate.query(query, new UserRowMapper(), jwtToken);
        if (users.size() == 1) {
            return users.get(0);
        }
        return null;
    }

    private class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            int id = resultSet.getInt("idUser");
            String email = resultSet.getString("email");
            String password = resultSet.getString("password");
            String salt = resultSet.getString("salt");
            boolean isVerified = resultSet.getBoolean("isVerified");
            String resetToken = resultSet.getString("resetToken");
            String jwtToken = resultSet.getString("jwtToken");
            int highscore = resultSet.getInt("highscore");
            User user = new User(email, password, salt, isVerified, resetToken, jwtToken, highscore);
            user.setIdUser(id);
            return user;
        }
    }

    @Override
    public boolean updateResetTokenForUser(String email, String token) {
        String updateQuery = "UPDATE User SET resetToken = ? WHERE email = ?";

        int rowsAffected = jdbcTemplate.update(updateQuery, token, email);

        return rowsAffected > 0; // Returns true if at least one row was updated
    }

    @Override public boolean updateUserIdentificatieToken(User user) {
        String updateQuery = "UPDATE User SET jwtToken = ? WHERE email = ?";
        int rowsAffected = jdbcTemplate.update(updateQuery, user.getJwtToken(), user.getEmail());
        return rowsAffected > 0;
    }
    //geeft een lijst tot een gegeven limiet
    public List<Highscore> getHighScoreslist(int limit) {
        String sql = "SELECT email, highscore FROM User ORDER BY highscore DESC LIMIT ?";
        return jdbcTemplate.query(sql, new HighScoreRowMapper(), limit);
    }
    //geeft een highscore voor een specifieke naam
    public List<Highscore> getHighScoresAroundName(String name) {
        String sql = "SELECT email, highscore FROM User WHERE email LIKE ?";
        String namePattern = "%" + name + "%";  // Creating a pattern to match any emails containing the given name
        return jdbcTemplate.query(sql, new HighScoreRowMapper(), namePattern);
    }

    //geeft een highscore voor een specifieke rank
    public List<Highscore> getHighScoresAroundRank(int rank) {
        String sql = "WITH RankedScores AS (" +
                "SELECT email, highscore, RANK() OVER (ORDER BY highscore DESC) AS ranking FROM User" +
                ") " +
                "SELECT email, highscore FROM RankedScores WHERE ranking BETWEEN ? - 5 AND ? + 5";
        return jdbcTemplate.query(sql, new HighScoreRowMapper(), rank, rank);
    }


    public class HighScoreRowMapper implements RowMapper<Highscore> {
        @Override
        public Highscore mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            String email = resultSet.getString("email");
            int highscore = resultSet.getInt("highscore");
            return new Highscore(email, highscore);
        }
    }

}




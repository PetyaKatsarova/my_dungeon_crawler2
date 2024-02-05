package nl.hva.hvacrawler.persistence.dao;

import nl.hva.hvacrawler.business.domain.Gold;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
//@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JdbcGoldDaoTest {
    private final JdbcTemplate jdbcTemplate;
    private final GoldDao daoUnderTest;

    @Autowired
    public JdbcGoldDaoTest(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
        this.daoUnderTest = new JdbcGoldDao(jdbcTemplate);
    }

    @Test
    void getGoldById() {
        Gold expected = new Gold(26, "1_Goldpiece", 1);
        expected.setId(26);
        Optional<Gold> actual = daoUnderTest.getGoldById(26);
        assertThat(actual).isNotNull().isNotEmpty().contains(expected);
    }
}
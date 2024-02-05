package nl.hva.hvacrawler.persistence.dao;

import nl.hva.hvacrawler.business.domain.Monster;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JdbcMonsterDaoTest {

    @Autowired
    private MonsterDao monsterDao;

    @Test
    public void testSaveMonster() {
        // Arrange
        Monster monster = new Monster("TestMonster", 100, 0, 33);

        // Act
        monsterDao.save(monster);

        // Assert
        // Controleer of het monster correct is opgeslagen
        Optional<Monster> retrievedMonster = monsterDao.getMonsterById(monster.getIdCharacter());
        assertTrue(retrievedMonster.isPresent());
        assertEquals(monster.getName(), retrievedMonster.get().getName());
        assertEquals(monster.getHealthPoints(), retrievedMonster.get().getHealthPoints());

    }


    @Test
    public void testGetMonsterById() {
        // Arrange
        Monster monster = new Monster("TestMonster", 100, 0, 33);
        monsterDao.save(monster);

        // Act
        Optional<Monster> retrievedMonster = monsterDao.getMonsterById(monster.getIdCharacter());

        // Assert
        // Controleer of het monster correct kan worden opgehaald
        assertTrue(retrievedMonster.isPresent());
        assertEquals(monster.getName(), retrievedMonster.get().getName());
        assertEquals(monster.getHealthPoints(), retrievedMonster.get().getHealthPoints());

    }
}
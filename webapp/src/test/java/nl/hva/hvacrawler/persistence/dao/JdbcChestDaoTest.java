package nl.hva.hvacrawler.persistence.dao;

import nl.hva.hvacrawler.business.domain.Chest;
import nl.hva.hvacrawler.business.domain.Room;
import nl.hva.hvacrawler.business.service.ChestService;
import nl.hva.hvacrawler.persistence.repository.ChestRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JdbcChestDaoTest {
    private Chest testChest;
    private ChestDAO chestDaoUnderTest;
    private JdbcRoomDao roomDao;
    private List<Chest> ChestsUnderList;


    @BeforeEach
    void setUp() {
        testChest = new Chest();
        chestDaoUnderTest = Mockito.mock(ChestDAO.class);
        ChestRepository mockRepo = Mockito.mock(ChestRepository.class);
        Mockito.when(mockRepo.findOneById(1)).thenReturn(Optional.ofNullable(testChest));

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void saveOrUpdateOne() {
        chestDaoUnderTest.saveOrUpdateOne(testChest);


    }

    @Test
    void findOneById() {
        Optional actual = chestDaoUnderTest.findOneById(1);
        Optional expected = chestDaoUnderTest.findOneById(testChest.getId());
        assertEquals(actual, expected);
    }
}
package nl.hva.hvacrawler.business.service;

import nl.hva.hvacrawler.business.domain.Monster;
import nl.hva.hvacrawler.persistence.repository.MonsterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MonsterServiceTest {
    @Mock
    private MonsterRepository monsterRepository;
    private MonsterService monsterService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        monsterService = new MonsterService(monsterRepository);
    }
    @Test
    void save() {
        Monster saveNewMonster = new Monster("testMonster",100,3,1);
        monsterService.save(saveNewMonster);
        verify(monsterRepository, times(1)).save(saveNewMonster);
    }

    @Test
    void getMonsterById() {
        int monsterId = 1;
        Monster expectedMonster = new Monster("testMonster",100,3,1);
        when(monsterRepository.getMonsterById(monsterId)).thenReturn(Optional.of(expectedMonster));

        Optional<Monster> result = monsterService.getMonsterById(monsterId);

        assertTrue(result.isPresent());
        assertEquals(expectedMonster, result.get());
    }
    @Test
    void getNonExistingMonsterById() {
        int monsterId = 999; // Assuming this ID does not exist in your data
        when(monsterRepository.getMonsterById(monsterId)).thenReturn(Optional.empty());

        Optional<Monster> result = monsterService.getMonsterById(monsterId);

        assertFalse(result.isPresent());
    }



}
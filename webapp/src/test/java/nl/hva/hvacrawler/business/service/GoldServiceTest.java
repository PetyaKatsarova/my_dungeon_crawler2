package nl.hva.hvacrawler.business.service;

import nl.hva.hvacrawler.business.domain.Gold;
import nl.hva.hvacrawler.persistence.repository.GoldRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GoldServiceTest {

    @Mock
    private GoldRepository goldRepository;
    @Mock
    private GoldService goldService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        goldService = new GoldService(goldRepository);
    }


    @Test
    void getGoldById() {
        int goldId = 1;
        Gold expectedGold = new Gold(1, "15 goldPieces", 15);
        when(goldRepository.getGoldById(goldId)).thenReturn(Optional.of(expectedGold));

        Optional<Gold> result = goldService.getGoldById(goldId);

        assertTrue(result.isPresent());
        assertEquals(expectedGold, result.get());
    }
}


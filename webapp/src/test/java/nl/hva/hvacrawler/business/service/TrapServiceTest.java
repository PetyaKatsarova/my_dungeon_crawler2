package nl.hva.hvacrawler.business.service;

import nl.hva.hvacrawler.business.domain.Trap;
import nl.hva.hvacrawler.persistence.repository.TrapRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TrapServiceTest {
    @Mock
    private TrapRepository trapRepository;
    private TrapService trapService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        trapService = new TrapService(trapRepository);
    }

    @Test
    void getTrapById() {
        int trapId = 1;
        Trap expectedTrap = new Trap(1, "deathTrap", 100);
        when(trapRepository.getTrapById(trapId)).thenReturn(Optional.of(expectedTrap));

        Optional<Trap> result = trapService.getTrapById(trapId);

        assertTrue(result.isPresent());
        assertEquals(expectedTrap, result.get());
    }
}
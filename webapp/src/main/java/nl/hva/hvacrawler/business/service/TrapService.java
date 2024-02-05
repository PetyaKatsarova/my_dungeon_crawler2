package nl.hva.hvacrawler.business.service;

import jakarta.mail.search.SearchTerm;
import nl.hva.hvacrawler.business.domain.Trap;
import nl.hva.hvacrawler.persistence.repository.TrapRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

@Service
public class TrapService {
    private final Logger logger = LoggerFactory.getLogger(TrapRepository.class);
    private final TrapRepository trapRepository;
    private final Set<Integer> usedTrapIds = new HashSet<>();

    public TrapService(TrapRepository trapRepository) {
        this.trapRepository = trapRepository;
        logger.info("New TrapService");
    }

    public Optional<Trap> getTrapById(int id) {
        return trapRepository.getTrapById(id);
    }

    public Optional<Trap> getRandomTrap() {
        Random random = new Random();

        // Define the range of valid trap IDs, similar to how it's done in PotionService
        int minId = 41;
        int maxId = 43;

        // Check if all possible IDs have been used, reset the list if needed
        if (usedTrapIds.size() == (maxId - minId + 1)) {
            usedTrapIds.clear();
        }

        int randomId;
        // Choose a new trap ID that has not been used yet
        do {
            randomId = random.nextInt((maxId - minId) + 1) + minId;
        } while (usedTrapIds.contains(randomId));

        usedTrapIds.add(randomId); // Add the used ID to the list of used IDs

        return trapRepository.getTrapById(randomId); // Fetch the trap from the database based on the generated ID
    }
}

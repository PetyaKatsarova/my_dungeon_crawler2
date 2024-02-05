package nl.hva.hvacrawler.business.service;

import nl.hva.hvacrawler.business.domain.Gold;
import nl.hva.hvacrawler.business.domain.Trap;
import nl.hva.hvacrawler.persistence.repository.GoldRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

@Service
public class GoldService {
    private Logger logger = LoggerFactory.getLogger(GoldService.class);
    private final GoldRepository goldRepository;
    private final Set<Integer> usedGoldIds = new HashSet<>();
    private final int MIN_ID = 26;
    private final int MAX_ID = 35;

    public GoldService(GoldRepository goldRepository) {
        this.goldRepository = goldRepository;
        logger.info("New GoldService");
    }

    public Optional<Gold> getGoldById(int id) {
        return goldRepository.getGoldById(id);
    }

    public Optional<Gold> getRandomGold() {
        Random random = new Random();

        // Check if all possible IDs have been used, reset the list if needed
        if (usedGoldIds.size() == (MAX_ID - MIN_ID + 1)) {
            usedGoldIds.clear();
        }

        int randomId;
        // Choose a new trap ID that has not been used yet
        do {
            randomId = random.nextInt((MAX_ID - MIN_ID) + 1) + MIN_ID;
        } while (usedGoldIds.contains(randomId));

        usedGoldIds.add(randomId); // Add the used ID to the list of used IDs

        return goldRepository.getGoldById(randomId); // Fetch the goldPiece from the database based on the generated ID
    }
}

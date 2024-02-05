package nl.hva.hvacrawler.business.service;

import nl.hva.hvacrawler.business.domain.Potion;
import nl.hva.hvacrawler.persistence.repository.PotionRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

@Service
public class PotionService {
    private final PotionRepository potionRepository;
    private final Set<java.lang.Integer> usedPotionIds = new HashSet<>();

    public PotionService(PotionRepository potionRepository) {
        this.potionRepository = potionRepository;
    }

    public Optional<Potion> getPotionById(int id) {
        return potionRepository.getPotionById(id);
    }

    public Optional<Potion> getRandomPotion() {
        Random random = new Random();

        // Define the range of valid potion IDs
        int minId = 36;
        int maxId = 40;

        // Check if all possible IDs have been used, reset the list if needed
        if (usedPotionIds.size() == (maxId - minId + 1)) {
            usedPotionIds.clear();
        }

        int randomId;
        // Choose a new potion ID that has not been used yet
        do {
            randomId = random.nextInt((maxId - minId) + 1) + minId;
        } while (usedPotionIds.contains(randomId));

        usedPotionIds.add(randomId); // Add the used ID to the list of used IDs

        return potionRepository.getPotionById(randomId); // Fetch the potion from the database based on the generated ID
    }
}

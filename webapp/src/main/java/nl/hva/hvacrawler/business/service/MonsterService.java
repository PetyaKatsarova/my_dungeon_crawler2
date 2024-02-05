package nl.hva.hvacrawler.business.service;

import nl.hva.hvacrawler.business.domain.Monster;
import nl.hva.hvacrawler.persistence.repository.MonsterRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

@Service
public class MonsterService {

    private final MonsterRepository monsterRepository;
    private final Set<Integer> usedMonsterIds = new HashSet<>();

    public MonsterService(MonsterRepository monsterRepository) {
        this.monsterRepository = monsterRepository;
    }

    public void save(Monster monster){monsterRepository.save(monster);}

    public Optional<Monster> getMonsterById(int id){return monsterRepository.getMonsterById(id);}

    /**
     * maakt een random getal en slaat deze tegelijk op in een setlist voor een monsterID om die uit de database zonder herhaling van ID's.
     *
     * @return Een optioneel monsterobject als er een monster is gevonden, anders een lege optioneel.
     */
    public Optional<Monster> getRandomMonster() {
        Random random = new Random();
        int totalMonsters = 32; // Het totale aantal monsters dat we hebben in de Database
        if (usedMonsterIds.size() == totalMonsters) {
            usedMonsterIds.clear();   // Alle mogelijke ID's zijn gebruikt, reset de lijst
        }
        int randomId;
        do {
            randomId = random.nextInt(totalMonsters) + 1;
        } while (usedMonsterIds.contains(randomId)); // Kies een nieuw ID als het al gebruikt is
        usedMonsterIds.add(randomId); // Voeg het gebruikte ID toe aan de lijst
        return monsterRepository.getMonsterById(randomId);
    }
}

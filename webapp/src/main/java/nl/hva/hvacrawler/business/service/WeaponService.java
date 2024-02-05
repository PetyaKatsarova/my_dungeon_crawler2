package nl.hva.hvacrawler.business.service;

import nl.hva.hvacrawler.business.domain.Weapon;
import nl.hva.hvacrawler.persistence.repository.WeaponRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

@Service
public class WeaponService {
    private final WeaponRepository weaponRepository;
    private final Set<Integer> usedWeaponIds = new HashSet<>();

    public WeaponService(WeaponRepository weaponRepository) {
        this.weaponRepository = weaponRepository;
    }


    public Optional<Weapon> getWeaponById(int id){return weaponRepository.getWeaponById(id);}

    public Optional<Weapon> getRandomWeapon() {
        Random random = new Random();
        int totalWeapons = 25; // Het totale aantal wapens dat we hebben in de Database
        // Controleer of alle mogelijke ID's zijn gebruikt, reset de lijst indien nodig
        if (usedWeaponIds.size() == totalWeapons) {
            usedWeaponIds.clear();
        }
        int randomId;
        // Kies een nieuw wapen-ID dat nog niet is gebruikt
        do {
            randomId = random.nextInt(totalWeapons) + 1;
        } while (usedWeaponIds.contains(randomId));

        usedWeaponIds.add(randomId); // Voeg het gebruikte ID toe aan de lijst van gebruikte ID's

        return weaponRepository.getWeaponById(randomId); // Haal het wapen op uit de database op basis van het gegenereerde ID
    }
}

package nl.hva.hvacrawler.persistence.dao;

import nl.hva.hvacrawler.business.domain.Potion;

import java.util.Optional;

public interface PotionDao {
    Optional<Potion> getPotionById(int id);
    Optional<Potion> getRandomPotion();
}

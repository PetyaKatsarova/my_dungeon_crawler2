package nl.hva.hvacrawler.persistence.dao;

import nl.hva.hvacrawler.business.domain.Gold;

import java.util.Optional;

public interface GoldDao {
    Optional<Gold> getGoldById(int id);
    Optional<Gold> getRandomGold();
}

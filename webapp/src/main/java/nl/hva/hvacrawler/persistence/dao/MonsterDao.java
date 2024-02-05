package nl.hva.hvacrawler.persistence.dao;

import nl.hva.hvacrawler.business.domain.Monster;

import java.util.Optional;

public interface MonsterDao {
    void save(Monster monster);
    Optional<Monster> getMonsterById(int id);

}

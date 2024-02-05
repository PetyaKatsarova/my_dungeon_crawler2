package nl.hva.hvacrawler.persistence.dao;

import nl.hva.hvacrawler.business.domain.Trap;

import java.util.Optional;

public interface TrapDao {
    Optional<Trap> getTrapById(int id);
    Optional<Trap> getRandomTrap();

}

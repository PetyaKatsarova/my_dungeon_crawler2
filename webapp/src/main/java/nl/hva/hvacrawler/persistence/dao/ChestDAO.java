package nl.hva.hvacrawler.persistence.dao;

import nl.hva.hvacrawler.business.domain.Chest;

import java.util.Optional;

public interface ChestDAO<C> {
    Chest saveOrUpdateOne(Chest obj); // generic type T, replace with your class
    Optional<Chest> findOneById(int id);

    int getIdItemByIdChest(int idChest);
}

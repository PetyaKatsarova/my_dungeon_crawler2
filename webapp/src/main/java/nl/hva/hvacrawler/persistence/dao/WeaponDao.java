package nl.hva.hvacrawler.persistence.dao;

import nl.hva.hvacrawler.business.domain.Weapon;

import java.util.Optional;

public interface WeaponDao {
    Optional<Weapon> getWeaponById(int id);
    Optional<Weapon> getRandomWeapon();
}

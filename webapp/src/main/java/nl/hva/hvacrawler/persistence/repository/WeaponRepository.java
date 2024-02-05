package nl.hva.hvacrawler.persistence.repository;

import nl.hva.hvacrawler.business.domain.Weapon;
import nl.hva.hvacrawler.persistence.dao.WeaponDao;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class WeaponRepository {
    private final WeaponDao weaponDao;

    public WeaponRepository(WeaponDao weaponDao) {
        this.weaponDao = weaponDao;
    }
    public Optional<Weapon> getWeaponById(int id){return weaponDao.getWeaponById(id);}

    public Optional<Weapon> getRandomWeapon() {
        return weaponDao.getRandomWeapon();
    }
}

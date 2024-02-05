package nl.hva.hvacrawler.persistence.repository;

import nl.hva.hvacrawler.business.domain.Monster;
import nl.hva.hvacrawler.persistence.dao.MonsterDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public class MonsterRepository {
    private final MonsterDao monsterDao;

    @Autowired
    public MonsterRepository(MonsterDao monsterDao) {
        this.monsterDao = monsterDao;
    }

    public Optional<Monster> getMonsterById(int id) {
        return monsterDao.getMonsterById(id);
    }

    public void save(Monster monster) {monsterDao.save(monster);
    }


}

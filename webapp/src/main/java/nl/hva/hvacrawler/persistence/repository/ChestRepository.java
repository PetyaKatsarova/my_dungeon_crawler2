package nl.hva.hvacrawler.persistence.repository;

import nl.hva.hvacrawler.business.domain.Chest;
import nl.hva.hvacrawler.persistence.dao.ChestDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ChestRepository {
    private final ChestDAO chestDAO;

    @Autowired
    public ChestRepository(ChestDAO chestDAO) {
        this.chestDAO = chestDAO;
    }

    public Chest saveOrUpdateOne(Chest obj) {
        return chestDAO.saveOrUpdateOne(obj);
    }

    public Optional<Chest> findOneById(int id) {
        return chestDAO.findOneById(id);
    }
}

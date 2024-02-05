package nl.hva.hvacrawler.persistence.repository;

import nl.hva.hvacrawler.business.domain.Trap;
import nl.hva.hvacrawler.persistence.dao.TrapDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public class TrapRepository {
    private final Logger logger = LoggerFactory.getLogger(TrapRepository.class);
    private final TrapDao trapDao;

    @Autowired
    public TrapRepository(TrapDao trapDao) {
        this.trapDao = trapDao;
        logger.info("New TrapRepository");
    }

    public Optional<Trap> getTrapById(int id) {
        return trapDao.getTrapById(id);
    }

    public Optional<Trap> getRandomTrap() {
        return trapDao.getRandomTrap();
    }
}

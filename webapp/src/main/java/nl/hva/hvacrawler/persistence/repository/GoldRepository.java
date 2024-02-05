package nl.hva.hvacrawler.persistence.repository;

import nl.hva.hvacrawler.business.domain.Gold;
import nl.hva.hvacrawler.persistence.dao.GoldDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class GoldRepository {
    private final Logger logger = LoggerFactory.getLogger(GoldRepository.class);
    private final GoldDao goldDao;

    @Autowired
    public GoldRepository(GoldDao goldDao) {
        this.goldDao = goldDao;
        logger.info("New GoldRepository");
    }
    public Optional<Gold> getGoldById(int id) { return goldDao.getGoldById(id);}
    public Optional<Gold> getRandomGold() { return goldDao.getRandomGold();}
}

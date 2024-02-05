package nl.hva.hvacrawler.persistence.repository;

import nl.hva.hvacrawler.business.domain.Potion;
import nl.hva.hvacrawler.communication.dto.HasPotionDTO;
import nl.hva.hvacrawler.persistence.dao.JdbcCrawlerHasPotionDao;
import nl.hva.hvacrawler.persistence.dao.PotionDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PotionRepository {
    private final Logger logger = LoggerFactory.getLogger(PotionRepository.class);
    private final PotionDao potionDao;
    private final JdbcCrawlerHasPotionDao jdbcCrawlerHasPotionDao;

    public PotionRepository(PotionDao potionDao, JdbcCrawlerHasPotionDao jdbcCrawlerHasPotionDao) {
        this.potionDao = potionDao;
        this.jdbcCrawlerHasPotionDao = jdbcCrawlerHasPotionDao;
        logger.info("New PotionRepository");
    }

    public Optional<Potion> getPotionById(int id) {
        return potionDao.getPotionById(id);
    }

    public List<Potion> getAllPotionsFromUser(int crawlerId) {
        List<HasPotionDTO> potionIds = jdbcCrawlerHasPotionDao.findAllPotionInfoByCrawlerId(crawlerId);
        List<Potion> potionList = new ArrayList<>();
        if(potionIds != null) {
            for(HasPotionDTO potionDTO: potionIds) {
                for(int i = 0; i < potionDTO.getQuantity(); i++) {
                    Optional<Potion> potion = getPotionById(potionDTO.getIdItemPotion());
                    potion.ifPresent(potionList::add);
                }
            }
        }
        return potionList;
    }

}

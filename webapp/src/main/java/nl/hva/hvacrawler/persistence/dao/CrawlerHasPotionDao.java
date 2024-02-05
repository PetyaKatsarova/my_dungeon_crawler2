package nl.hva.hvacrawler.persistence.dao;

import nl.hva.hvacrawler.business.domain.Game;
import nl.hva.hvacrawler.communication.dto.HasPotionDTO;

import java.util.List;

public interface CrawlerHasPotionDao {


    List<HasPotionDTO> findAllPotionInfoByCrawlerId(int id);

    void saveOrUpdateOne(HasPotionDTO potion);

    boolean potionExists(int idCrawler, int idItemPotion);

    void deleteCrawlerHasPotionByCrawlerId(int idCrawler);
}

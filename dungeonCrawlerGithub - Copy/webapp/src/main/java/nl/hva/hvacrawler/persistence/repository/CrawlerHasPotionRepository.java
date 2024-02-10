package nl.hva.hvacrawler.persistence.repository;

import nl.hva.hvacrawler.business.domain.Crawler;
import nl.hva.hvacrawler.communication.dto.HasPotionDTO;
import nl.hva.hvacrawler.persistence.dao.JdbcCrawlerHasPotionDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

/**
 * Description:
 * Author: Petya Katsarova
 * Email: pskpetya@gmail.com
 * Created on: 10/02/2024 20:30
 */

@Repository
public class CrawlerHasPotionRepository {
    private final Logger                    logger = LoggerFactory.getLogger(CrawlerHasPotionRepository.class);
    private final JdbcCrawlerHasPotionDao   jdbcCrawlerHasPotionDao;

    public CrawlerHasPotionRepository(JdbcCrawlerHasPotionDao jdbcCrawlerHasPotionDao) {
        this.jdbcCrawlerHasPotionDao = jdbcCrawlerHasPotionDao;
    }
    public void saveOrUpdateOne(HasPotionDTO potion) {
        jdbcCrawlerHasPotionDao.saveOrUpdateOne(potion);
    }
}

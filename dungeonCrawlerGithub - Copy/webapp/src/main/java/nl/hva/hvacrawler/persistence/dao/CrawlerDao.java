package nl.hva.hvacrawler.persistence.dao;

import nl.hva.hvacrawler.business.domain.Crawler;

import java.util.Optional;

public interface CrawlerDao extends BaseDao<Crawler> {

    Optional<Crawler> findOneByIdUser(int idUser);

    int getWeaponIdByCrawlerId(int idCrawler);

    int getUserIdByCrawlerId(int idCrawler);

    boolean checkIfCrawlerAlreadyExists(String name);
}

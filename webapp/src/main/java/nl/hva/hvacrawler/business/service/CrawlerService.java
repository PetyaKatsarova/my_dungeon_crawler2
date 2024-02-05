package nl.hva.hvacrawler.business.service;

import nl.hva.hvacrawler.business.domain.Crawler;
import nl.hva.hvacrawler.persistence.repository.CrawlerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.OptionalInt;
@Service
public class CrawlerService {
    private final Logger logger = LoggerFactory.getLogger(CrawlerRepository.class);
    private final CrawlerRepository crawlerRepository;


    public CrawlerService(CrawlerRepository crawlerRepository) {
        this.crawlerRepository = crawlerRepository;
        logger.info("New CrawlerService");
    }
    public void save(Crawler crawler) {
        crawlerRepository.saveOrUpdateOne(crawler);}

    public Optional<Crawler> getCrawlerById(int id) {
        return crawlerRepository.findOneById(id);
    }

    public Crawler getCrawlerByUserId(int idUser){
        return crawlerRepository.findOneByUserId(idUser);
    }
}

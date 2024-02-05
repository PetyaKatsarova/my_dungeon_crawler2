package nl.hva.hvacrawler.persistence.repository;

import nl.hva.hvacrawler.business.domain.Crawler;
import nl.hva.hvacrawler.business.domain.Weapon;
import nl.hva.hvacrawler.persistence.dao.JdbcCrawlerDao;
import nl.hva.hvacrawler.persistence.dao.JdbcUserDao;
import nl.hva.hvacrawler.persistence.dao.JdbcWeaponDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class CrawlerRepository {

    private final Logger logger = LoggerFactory.getLogger(CrawlerRepository.class);

    private final JdbcCrawlerDao jdbcCrawlerDao;
    private final JdbcWeaponDao jdbcWeaponDao;
    private final PotionRepository potionRepository;
    private final JdbcUserDao jdbcUserDao;

    public CrawlerRepository(JdbcCrawlerDao jdbcCrawlerDao, JdbcWeaponDao jdbcWeaponDao,
                             PotionRepository potionRepository, JdbcUserDao jdbcUserDao) {
        this.jdbcCrawlerDao = jdbcCrawlerDao;
        this.jdbcWeaponDao = jdbcWeaponDao;
        this.potionRepository = potionRepository;
        this.jdbcUserDao = jdbcUserDao;
        logger.info("New CrawlerRepository");
    }

    public Crawler saveOrUpdateOne(Crawler crawler) {
        // implement potion repo save potions: crawerlhas potiosn
        // create potiondto from crawerl-> use potionrepo.save
        // tosave potions and crawler info
        return jdbcCrawlerDao.saveOrUpdateOne(crawler);
    }

    // do we use this method????
    public Optional<Crawler> findOneById(int idCrawler) {
        // implement staff from down -- getting crawler with all potions
        return jdbcCrawlerDao.findOneById(idCrawler);
    }

    public Crawler findOneByUserId(int idUser) {
        Optional<Crawler> crawler = jdbcCrawlerDao.findOneByIdUser(idUser);
        if (crawler.isPresent()) {
            int idWeapon = jdbcCrawlerDao.getWeaponIdByCrawlerId(crawler.get().getIdCharacter());
            Optional<Weapon> weapon = jdbcWeaponDao.getWeaponById(idWeapon);
            crawler.get().setWeapon(weapon.get());
            crawler.get().setHealthPotions(potionRepository.getAllPotionsFromUser(crawler.get().getIdCharacter()));
            crawler.get().setUser(jdbcUserDao.findUserById(idUser));
            return crawler.get();
        }else {
            return null;
        }
    }

    public int getUserIdByCrawlerId(int idCrawler) {
        return jdbcCrawlerDao.getUserIdByCrawlerId(idCrawler);
    }

    public boolean checkIfCrawlerAlreadyExists(String name) {
        return jdbcCrawlerDao.checkIfCrawlerAlreadyExists(name);
    }
}

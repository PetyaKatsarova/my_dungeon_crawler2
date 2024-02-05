package nl.hva.hvacrawler.communication.controller;

import nl.hva.hvacrawler.business.domain.Crawler;
import nl.hva.hvacrawler.business.service.CrawlerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/crawler")
public class CrawlerController {
    private final CrawlerService crawlerService;
    private final Logger logger = LoggerFactory.getLogger(CrawlerController.class);
    @Autowired
    public CrawlerController(CrawlerService crawlerService) {
        super();
        this.crawlerService = crawlerService;
        logger.info("New CrawlerController");
    }

    @GetMapping("/{id}")
    public Crawler getCrawlerByIdHandler(@PathVariable int id) {
        // Retrieve crawler from the database using CrawlerDAO
        return crawlerService.getCrawlerById(id).orElse(null);
    }
}

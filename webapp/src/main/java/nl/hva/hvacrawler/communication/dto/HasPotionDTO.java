package nl.hva.hvacrawler.communication.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

public class HasPotionDTO {

    private final Logger logger = LoggerFactory.getLogger(HasPotionDTO.class);

    private int idCrawler;
    private int idItemPotion;
    private int quantity;

    public HasPotionDTO(int idCrawler, int idItemPotion, int quantity) {
        this.idCrawler = idCrawler;
        this.idItemPotion = idItemPotion;
        this.quantity = quantity;
        logger.info("New HasPotionDTO");
    }

    public int getIdCrawler() {
        return idCrawler;
    }

    public void setIdCrawler(int idCrawler) {
        this.idCrawler = idCrawler;
    }

    public int getIdItemPotion() {
        return idItemPotion;
    }

    public void setIdItemPotion(int idItemPotion) {
        this.idItemPotion = idItemPotion;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

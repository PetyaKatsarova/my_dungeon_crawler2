package nl.hva.hvacrawler.exception;

public class CrawlerAlreadyExistsException extends RuntimeException {
    public CrawlerAlreadyExistsException(String message) {
        super(message);
    }
}

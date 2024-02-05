package nl.hva.hvacrawler.exception;

public class CrawlerNotFoundException extends RuntimeException {
    public CrawlerNotFoundException(String message) {
        super(message);
    }
}

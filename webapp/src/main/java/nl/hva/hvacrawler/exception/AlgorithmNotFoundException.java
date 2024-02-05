package nl.hva.hvacrawler.exception;

public class AlgorithmNotFoundException extends RuntimeException {

    public static final String ALGORITME_BESTAAT_NIET = "Algoritme bestaat niet: ";
    public AlgorithmNotFoundException(String algorithm, Throwable cause) {
        super(ALGORITME_BESTAAT_NIET + algorithm, cause);
    }
}

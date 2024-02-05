package nl.hva.hvacrawler.exception;

public class PasswordNotValidException extends IllegalArgumentException{
    public PasswordNotValidException(String message) {
        super(message);
    }
}

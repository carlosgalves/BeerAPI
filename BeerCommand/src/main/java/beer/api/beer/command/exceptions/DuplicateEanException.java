package beer.api.beer.command.exceptions;

public class DuplicateEanException extends RuntimeException {
    public DuplicateEanException(String message) {
        super(message);
    }
}

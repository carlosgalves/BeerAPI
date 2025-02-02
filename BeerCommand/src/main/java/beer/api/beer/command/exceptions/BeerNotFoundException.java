package beer.api.beer.command.exceptions;

import java.util.UUID;

public class BeerNotFoundException extends RuntimeException {
    public BeerNotFoundException(UUID id) {
        super("Couldn't find beer with ID: " + id);
    }
}

package pl.filmoteka.exception;

/**
 * Exception thrown when client requests an non-existing entity.
 */
public class InvalidResourceRequestedException extends Exception {

    public InvalidResourceRequestedException(String message) {
        super(message);
    }
}

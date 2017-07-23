package pl.filmoteka.exception;

/**
 * Exception thrown when response from external API was somewhat malformed and can't be properly processed.
 */
public class InvalidExternalApiResponseException extends Exception {

    public InvalidExternalApiResponseException(String message) {
        super(message);
    }

    public InvalidExternalApiResponseException(String message, Throwable cause) {
        super(message, cause);
    }
}

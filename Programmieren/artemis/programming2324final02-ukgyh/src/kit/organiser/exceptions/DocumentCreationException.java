package kit.organiser.exceptions;

/**
 * Exception thrown when a document cannot be created.
 * @author ukgyh
 */
public class DocumentCreationException extends Exception {
    /**
     * Creates a new DocumentCreationException with the given message.
     * @param message The message of the exception.
     */
    public DocumentCreationException(String message) {
        super(message);
    }
}

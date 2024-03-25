package kit.organiser.exceptions;

/**
 * Exception thrown when a Document is missing.
 * @author ukgyh
 */
public class MissingDocumentException extends Exception {
    /**
     * Creates a new MissingDocumentException with the given message.
     * @param message The message of the exception.
     */
    public MissingDocumentException(String message) {
        super(message);
    }
}

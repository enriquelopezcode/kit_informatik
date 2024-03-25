package kit.organiser.exceptions;

/**
 * Exception thrown when a tag value is invalid.
 * @author ukgyh
 */
public class InvalidTagValueException extends Exception {
    /**
     * Creates a new InvalidTagValueException with the given message.
     * @param message The message of the exception.
     */
    public InvalidTagValueException(String message) {
        super(message);
    }
}

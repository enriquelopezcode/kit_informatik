package kit.organiser.exceptions;

/**
 * Exception thrown when there is a problem changing the access amount of a document in a folder.
 * @author ukgyh
 */
public class AccessAmountChangeException extends Exception {
    /**
     * Creates a new AccessAmountChangeException with the given message.
     * @param message The message of the exception.
     */
    public AccessAmountChangeException(String message) {
        super(message);
    }
}

package kit.codefight.exceptions;

/**
 * This exception is thrown when there is a problem creating an Ai object.
 * @author ukgyh
 */
public class AiCreationException extends Exception {

    /**
     * Constructs a new exception with the specified detail message.
     * @param message the detail message
     */
    public AiCreationException(String message) {
        super(message);
    }
}

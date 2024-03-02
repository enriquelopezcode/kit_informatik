package kit.codefight.exceptions;

/**
 * this exception is thrown when an AI that does not exist is referenced.
 * @author ukgyh
 */
public class MissingAiException extends Exception {
    /**
     * Constructs a new exception with the specified detail message.
     * @param message the detail message
     */
    public MissingAiException(String message) {
        super(message);
    }

}

